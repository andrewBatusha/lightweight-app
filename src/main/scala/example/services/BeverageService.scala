package example.services

import example.common.domain.beverages.{Beverage, BeverageConverter, BeverageDto}
import example.persistence.BeverageRepository

import scala.concurrent.ExecutionContextExecutor

trait BeverageService {

  def create(dto: BeverageDto): AsyncServiceAction

  def update(dto: BeverageDto): AsyncServiceAction

  def getOne(id: Long): AsyncServiceResult[Beverage]

  def getAll: AsyncServiceResult[Seq[Beverage]]

  def getUsersBeverage(id: Long): AsyncServiceResult[Seq[Beverage]]

  def delete(id: Long): AsyncServiceAction

}
class BeverageServiceImpl(implicit ex: ExecutionContextExecutor) extends BeverageService {

  override def update(dto: BeverageDto): AsyncServiceAction = {
    BeverageRepository.update(dto).asAction
  }

  override def getOne(id: Long): AsyncServiceResult[Beverage] = {
    BeverageRepository
      .findOne(id)
      .flatMap(ServiceResult.orNotFound)
  }

  override def getAll: AsyncServiceResult[Seq[Beverage]] = BeverageRepository.findAll.asResult

  override def delete(id: Long): AsyncServiceAction = {
    BeverageRepository.delete(id).asAction
  }

  override def create(dto: BeverageDto): AsyncServiceAction = {
    val beverage = BeverageConverter.toEntity(dto)
    BeverageRepository.save(beverage).asAction
  }

  override def getUsersBeverage(id: Long): AsyncServiceResult[Seq[Beverage]] = {
    BeverageRepository.findBeverageByUserId(id).asResult
  }
}
