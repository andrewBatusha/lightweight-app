package example.api.routers

import example.api.protocol.ApiResponse
import example.common.domain.auth.UserSession
import example.common.domain.beverages.{BeverageConverter, BeverageDto}
import example.services.BeverageService

class BeverageApiRouter(beverageService: BeverageService) {
  import example.common.domain.beverages.BeveragesJsonProtocol._

  // format: OFF
  def route(implicit session: Option[UserSession]): Route = pathPrefix("beverages") {
    getOneRouter ~
    getAllRouter ~
    getUsersBeverageRouter ~
    createRouter ~
    deleteRouter ~
    updateRouter
  }
  // format: ON
  private def createRouter(implicit session: Option[UserSession]): Route =
    postJson(as[BeverageDto]) { dto =>
      authorized(session) { session =>
        val validDto = dto.copy(creatorId = session.accountId)
        onSuccess(beverageService.create(validDto)) {
          result => complete(ApiResponse.convert(result))
        }
      }
    }

  private def getAllRouter: Route =
    get {
      onSuccess(beverageService.getAll) { result =>
        complete(ApiResponse.convert(result, BeverageConverter.toDtoList))
      }
    }

  private def getUsersBeverageRouter(implicit session: Option[UserSession]): Route = path("user") {
    post {
      authorized(session) { session =>
        onSuccess(beverageService.getUsersBeverage(session.accountId)) { result =>
          complete(ApiResponse.convert(result, BeverageConverter.toDtoList))
        }
      }
    }
  }

  private def getOneRouter: Route = idPath { id =>
    onSuccess(beverageService.getOne(id)) { result =>
      complete(ApiResponse.convert(result, BeverageConverter.toDto))
    }
  }

  private def updateRouter(implicit session: Option[UserSession]): Route =
    putJson(as[BeverageDto]) { dto =>
      authorized(session) { _ =>
        onSuccess(beverageService.update(dto)) { result =>
          complete(ApiResponse.convert(result))
        }
      }
    }

  private def deleteRouter(implicit session: Option[UserSession]): Route = deletingIdPath { id =>
    authorized(session) { _ =>
      onSuccess(beverageService.delete(id)) { _ =>
        complete(ApiResponse.OK)
      }
    }
  }
}
