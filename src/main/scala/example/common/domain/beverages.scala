package example.common.domain

import example.api.protocol.ApiJsonProtocol
import example.common.domain.reviews.{Review, ReviewConverter, ReviewDto, ReviewsJsonProtocol}
import example.common.utils.DateHelper

object beverages {

  case class MainBeverage(
      beverage: Beverage,
      review:   Seq[Option[Review]]
  )
  case class MainBeverageDto(
      beverageDTO: BeverageDto,
      reviewDTO:   Seq[ReviewDto]
  )

  case class Beverage(
      id:          Option[Long]     = None,
      createdAt:   DateTime         = DateHelper.now,
      updatedAt:   Option[DateTime] = None,
      name:        String,
      ingredients: Option[String]   = None,
      imageUrl:    Option[String]   = None,
      description: Option[String]   = None,
      recipe:      Option[String]   = None,
      creatorId:   Long
  ) extends BaseEntity

  case class BeverageDto(
      id:          Option[Long]   = None,
      name:        String,
      imageUrl:    Option[String] = None,
      ingredients: Option[String] = None,
      description: Option[String] = None,
      recipe:      Option[String] = None,
      creatorId:   Long
  )

  trait BeveragesJsonProtocol extends ApiJsonProtocol with ReviewsJsonProtocol {
    implicit val BeverageFormat                 = jsonFormat(BeverageDto, "id", "beverage_name", "ingredients", "image_url", "description", "recipe", "creator_id")
    implicit val mainBeverageFormat             = jsonFormat(MainBeverageDto, "beverage", "reviews")
    implicit val BeverageDtoResponseFormat      = apiResponseFormat[BeverageDto]
    implicit val BeverageDtoListResponseFormat  = apiResponseFormat[Seq[BeverageDto]]
    implicit val MainBeverageDtoResponseFormat  = apiResponseFormat[MainBeverageDto]
    implicit val MainBeverageDtosResponseFormat = apiResponseFormat[Seq[MainBeverageDto]]
  }

  object BeveragesJsonProtocol extends BeveragesJsonProtocol

  object BeverageConverter {

    def toMainDto(entity: MainBeverage): MainBeverageDto = {
      MainBeverageDto(
        beverageDTO = BeverageConverter.toDto(entity.beverage),
        reviewDTO   = ReviewConverter.toDtoList(entity.review.flatten)
      )
    }

    def toMainDtoList(entity: Seq[MainBeverage]): Seq[MainBeverageDto] = {
      entity.map(toMainDto)
    }

    def toDto(entity: Beverage): BeverageDto = {
      BeverageDto(
        id          = entity.id,
        name        = entity.name,
        ingredients = entity.ingredients,
        imageUrl    = entity.imageUrl,
        description = entity.description,
        recipe      = entity.recipe,
        creatorId   = entity.creatorId
      )
    }

    def toDtoList(entity: Seq[Beverage]): Seq[BeverageDto] = {
      entity.map(toDto)
    }

    def toEntity(dto: BeverageDto): Beverage = {
      Beverage(
        id          = dto.id,
        name        = dto.name,
        ingredients = dto.ingredients,
        imageUrl    = dto.imageUrl,
        description = dto.description,
        recipe      = dto.recipe,
        creatorId   = dto.creatorId
      )
    }
  }
}
