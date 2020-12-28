package example.services

import cats.data.Validated.{Invalid, Valid}
import example.common.domain.accounts.{Account, AccountConverter, AccountUpdateDto, RegisterDto}
import example.common.domain.auth.UserSession
import example.common.utils.DateHelper
import example.persistence.AccountRepository
import example.validation.{NickValidator, RegisterValidator}

import scala.concurrent.ExecutionContextExecutor

trait AccountService {

  def register(dto: RegisterDto): AsyncServiceAction

  def update(id: Long, dto: AccountUpdateDto, session: UserSession): AsyncServiceAction

  def getOne(id: Long, session: Option[UserSession]): AsyncServiceResult[Account]

}

class AccountServiceImpl(implicit ex: ExecutionContextExecutor) extends AccountService {

  def register(dto: RegisterDto): AsyncServiceAction = {
    AccountRepository
      .findByNickOrEmail(dto.nickName, dto.email)
      .onValid(RegisterValidator.checkUnique(dto)) { validDto =>
        val account = AccountConverter.toEntity(validDto)
        AccountRepository.save(account).asAction
      }
  }

  def update(id: Long, dto: AccountUpdateDto, session: UserSession): AsyncServiceAction = {
    if (session.hasAccess(id)) {
      AccountRepository
        .findByNick(dto.nickName)
        .map(NickValidator.checkUnique(id, dto.nickName))
        .flatMap {
          case Valid(nick) =>
            AccountRepository.update(id, nick, DateHelper.now).asAction

          case Invalid(errors) =>
            ServiceResult.validationError(errors)
        }
    } else {
      ServiceResult.error(403, "{action_forbidden}")
    }
  }

  def getOne(id: Long, session: Option[UserSession]): AsyncServiceResult[Account] = {
    AccountRepository
      .findOneActive(id)
      .flatMap(ServiceResult.orNotFound)
  }

}
