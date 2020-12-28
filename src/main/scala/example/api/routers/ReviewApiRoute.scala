package example.api.routers

import example.api.protocol.ApiResponse
import example.common.domain.auth.UserSession
import example.common.domain.beverages.BeverageConverter
import example.common.domain.reviews.{ReviewConverter, ReviewDto}
import example.services.ReviewService

class ReviewApiRoute(reviewService: ReviewService) {
  import example.common.domain.beverages.BeveragesJsonProtocol._

  // format: OFF
  def route(implicit session: Option[UserSession]): Route = pathPrefix("reviews") {
    createRouter ~
    getReviewByBeverageRouter ~
    updateRouter ~
    deleteRouter ~
    getBeverageWithReview
  }

  private def createRouter(implicit session: Option[UserSession]): Route =
    postJson(as[ReviewDto]) { dto =>
      authorized(session) { _ =>
        onSuccess(reviewService.create(dto)) {
          result => complete(ApiResponse.convert(result))
        }
      }
    }

  private def getReviewByBeverageRouter: Route = path("beverage" / LongNumber) { id =>
    get {
      onSuccess(reviewService.getReviewByBeverage(id)) { result =>
        complete(ApiResponse.convert(result, ReviewConverter.toDtoList))
      }
    }
  }

  private def updateRouter(implicit session: Option[UserSession]): Route =
    putJson(as[ReviewDto]) { dto =>
      authorized(session) { _ =>
        onSuccess(reviewService.update(dto)) { result =>
          complete(ApiResponse.convert(result))
        }
      }
    }

  private def deleteRouter(implicit session: Option[UserSession]): Route = deletingIdPath { id =>
    authorized(session) { _ =>
      onSuccess(reviewService.delete(id)) { _ =>
        complete(ApiResponse.OK)
      }
    }
  }

  private def getBeverageWithReview: Route = path("full") {
    get {
      onSuccess(reviewService.getBeverageWithReview) { result =>
        complete(ApiResponse.convert(result, BeverageConverter.toMainDtoList))
      }
    }
  }
}
