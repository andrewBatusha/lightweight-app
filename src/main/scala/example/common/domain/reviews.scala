package example.common.domain

import example.api.protocol.ApiJsonProtocol
import example.common.domain.beverages.{BeverageConverter, MainBeverage, MainBeverageDto}
import example.common.utils.DateHelper

object reviews {
  case class Review(
      id:          Option[Long]     = None,
      createdAt:   DateTime         = DateHelper.now,
      updatedAt:   Option[DateTime] = None,
      description: String,
      pros:        Option[String]   = None,
      cons:        Option[String]   = None,
      rating:      Option[Long]     = None,
      beverageId:  Long
  ) extends BaseEntity

  case class ReviewDto(
      id:          Option[Long]   = None,
      description: String,
      pros:        Option[String] = None,
      cons:        Option[String] = None,
      rating:      Option[Long]   = None,
      beverageId:  Long
  )

  trait ReviewsJsonProtocol extends ApiJsonProtocol {
    implicit val ReviewFormat = jsonFormat(ReviewDto, "id", "description", "pros", "cons", "rating", "beverage_id")

    implicit val ReviewDtoResponseFormat     = apiResponseFormat[ReviewDto]
    implicit val ReviewDtoListResponseFormat = apiResponseFormat[Seq[ReviewDto]]
  }

  object ReviewsJsonProtocol extends ReviewsJsonProtocol

  object ReviewConverter {

    def toDto(entity: Review): ReviewDto = {
      ReviewDto(
        id          = entity.id,
        description = entity.description,
        pros        = entity.pros,
        cons        = entity.cons,
        rating      = entity.rating,
        beverageId  = entity.beverageId
      )
    }

    def toDtoList(entity: Seq[Review]): Seq[ReviewDto] = {
      entity.map(toDto)
    }

    def toEntity(dto: ReviewDto): Review = {
      Review(
        id          = dto.id,
        description = dto.description,
        pros        = dto.pros,
        cons        = dto.cons,
        rating      = dto.rating,
        beverageId  = dto.beverageId
      )
    }
  }
}
