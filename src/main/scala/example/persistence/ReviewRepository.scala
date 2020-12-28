package example.persistence

import example.common.domain.beverages.Beverage
import example.common.domain.reviews.{Review, ReviewDto}
import example.persistence.DatabaseConnection.DB
import example.persistence.DatabaseConnection.api._
import example.persistence.base.{BaseEntityRepository, BaseTable}
import slick.lifted.{ProvenShape, TableQuery, Tag}

import scala.concurrent.Future

object ReviewRepository extends BaseEntityRepository[Review, ReviewTable](TableQuery[ReviewTable]) {
  def findReviewByBeverageId(id: Long): Future[Seq[Review]] =
    DB.run {
      entities.filter(_.beverageId === id).result
    }

  def update(dto: ReviewDto): Future[Int] =
    DB.run {
      idFilter(dto.id.get)
        .map(a => (a.description, a.pros, a.cons, a.rating))
        .update(dto.description, dto.pros, dto.cons, dto.rating)
        .transactionally
    }

  def getAllBeverageWithReviews: Future[Seq[(Beverage, Option[Review])]] =
    DB.run {
      Beverages.all.joinLeft(entities).on(_.id === _.beverageId).to[Seq].result
    }

}

class ReviewTable(tag: Tag) extends BaseTable[Review](tag, "reviews") {

  // format: OFF
  def description    = column[String]("description")
  def pros           = column[Option[String]]("pros")
  def cons           = column[Option[String]]("cons")
  def rating         = column[Option[Long]]("rating")
  def beverageId     = column[Long]("beverage_id")
  override def * : ProvenShape[Review] =
    (
      id.?, createdAt, updatedAt, description,
      pros, cons, rating, beverageId
    ) <> (Review.tupled, Review.unapply)

  def account  = foreignKey("beverage_fk", beverageId, Beverages.all)(_.id)
  // format: ON

}

object Reviews {
  lazy val all = TableQuery[ReviewTable]
}
