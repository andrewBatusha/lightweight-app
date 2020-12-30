package example.api.routers

import akka.http.scaladsl.model.headers._
import ch.megard.akka.http.cors.CorsDirectives._
import ch.megard.akka.http.cors.CorsSettings
import example.api.protocol.ApiResponse
import example.services.{AccountService, AuthService, BeverageService, ReviewService}

class ApiRouter(
    accountService:  AccountService,
    authService:     AuthService,
    beverageService: BeverageService,
    reviewService:   ReviewService
) {

  val accountApiRouter  = new AccountApiRouter(accountService)
  val authApiRouter     = new AuthApiRouter(authService)
  val beverageApiRouter = new BeverageApiRouter(beverageService)
  val reviewApiRoute    = new ReviewApiRoute(reviewService)

  val corsSettings = CorsSettings.defaultSettings.copy(
    allowGenericHttpRequests = true,
    allowedOrigins = HttpOriginRange.*
  )
  import example.api.protocol.ApiJsonProtocol._

  // format: OFF
  val routes: Route = handleRejections(ValidationRejectionHandler) {
      sessionFromAuthorizationHeader { implicit optionalSession =>
        pathPrefix("api" / "v1") {
          cors(corsSettings) {
            healthRouter ~
            accountApiRouter.route ~
            authApiRouter.route ~
            beverageApiRouter.route ~
            reviewApiRoute.route
        }
      }
    }
  }
        // format: ON

  def healthRouter: Route = pathPrefix("health") {
    get {
      complete(ApiResponse.OK)
    }
  }
}
