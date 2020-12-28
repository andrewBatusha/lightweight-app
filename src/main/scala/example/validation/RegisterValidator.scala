package example.validation

import example.common.domain.accounts.{Account, RegisterDto}

object RegisterValidator {

  def validate(data: RegisterDto): ValidationResult[RegisterDto] = {
    (
      NickValidator.validate(data.nickName),
      EmailValidator.validate(data.email),
      PasswordValidator.validate(data.password),
      passwordMatch(data.password, data.passwordRepeat)
    ).mapN(RegisterDto)
  }

  def passwordMatch(password: String, passwordRepeat: String): ValidationResult[String] = {
    if (password == passwordRepeat) {
      passwordRepeat.validNel
    } else {
      ("password_repeat", "password doens't match").invalidNel
    }
  }

  def checkUnique(data: RegisterDto)(foundedAccounts: Seq[Account]): ValidationResult[RegisterDto] = {
    if (foundedAccounts.isEmpty) {
      data.validNel
    } else {
      ("registration error",s"$data.nickName or $data.email doesn't unique").invalidNel
    }
  }

}
