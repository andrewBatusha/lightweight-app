package example.services

import example.common.domain.beverages.MainBeverage
import example.common.domain.reviews.{Review, ReviewConverter, ReviewDto}
import example.persistence.ReviewRepository

import scala.concurrent.ExecutionContextExecutor

trait ReviewService {

  def create(dto: ReviewDto): AsyncServiceAction

  def getReviewByBeverage(id: Long): AsyncServiceResult[Seq[Review]]

  def update(dto: ReviewDto): AsyncServiceAction

  def delete(id: Long): AsyncServiceAction

  def getBeverageWithReview: AsyncServiceResult[Seq[MainBeverage]]

}

class ReviewServiceImpl(implicit ex: ExecutionContextExecutor) extends ReviewService {

  override def create(dto: ReviewDto): AsyncServiceAction = {
    val beverage = ReviewConverter.toEntity(dto)
    ReviewRepository.save(beverage).asAction
  }

  override def getReviewByBeverage(id: Long): AsyncServiceResult[Seq[Review]] = {
    ReviewRepository.findReviewByBeverageId(id).asResult
  }

  override def update(dto: ReviewDto): AsyncServiceAction = {
    ReviewRepository.update(dto).asAction
  }

  override def delete(id: Long): AsyncServiceAction = {
    ReviewRepository.delete(id).asAction
  }

  override def getBeverageWithReview: AsyncServiceResult[Seq[MainBeverage]] = {
    ReviewRepository.getAllBeverageWithReviews
      .map(
        _.groupBy(_._1)
          .mapValues(_ map (_._2))
          .map {
            case (beverage, maybeReviews) => MainBeverage(beverage, maybeReviews)
          }
          .toSeq
      )
      .asResult
  }
}
