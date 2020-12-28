package example.persistence

import example.common.domain.beverages.{Beverage, BeverageDto}
import example.persistence.DatabaseConnection.DB
import example.persistence.DatabaseConnection.api._
import example.persistence.base.{BaseEntityRepository, BaseTable}
import slick.lifted.{ProvenShape, TableQuery, Tag}

import scala.concurrent.Future

object BeverageRepository extends BaseEntityRepository[Beverage, BeverageTable](TableQuery[BeverageTable]) {
  def update(dto: BeverageDto): Future[Int] =
    DB.run {
      idFilter(dto.id.get)
        .map(a => (a.name, a.description, a.recipe))
        .update(dto.name, dto.description, dto.recipe)
        .transactionally
    }

  def findBeverageByUserId(id: Long): Future[Seq[Beverage]] =
    DB.run {
      entities.filter(_.accountId === id).result
    }

  override def findOne(id: Long): Future[Option[Beverage]] =
    DB.run {
      idFilter(id).result.headOption
    }
}

class BeverageTable(tag: Tag) extends BaseTable[Beverage](tag, "beverages") {

  // format: OFF
  def name           = column[String]("name")
  def ingredients    = column[Option[String]]("ingredients")
  def imageUrl       = column[Option[String]]("image_url")
  def description    = column[Option[String]]("description")
  def recipe         = column[Option[String]]("recipe")
  def accountId      = column[Long]("account_id")
  override def * : ProvenShape[Beverage] =
    (
      id.?, createdAt, updatedAt, name, ingredients, imageUrl,
      description, recipe, accountId
    ) <> (Beverage.tupled, Beverage.unapply)

  def account  = foreignKey("account_fk", accountId, Accounts.all)(_.id)
  // format: ON

}
object Beverages {
  lazy val all = TableQuery[BeverageTable]
}