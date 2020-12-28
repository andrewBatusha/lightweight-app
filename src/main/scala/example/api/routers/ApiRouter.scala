package example.api.routers

import example.api.protocol.{ApiJsonProtocol, ApiResponse}
import example.services.{AccountService, AuthService, BeverageService, ReviewService}

class ApiRouter(
    accountService:    AccountService,
    authService:       AuthService,
    beverageService:   BeverageService,
    reviewService:     ReviewService,
) {

  val accountApiRouter    = new AccountApiRouter(accountService)
  val authApiRouter       = new AuthApiRouter(authService)
  val beverageApiRouter   = new BeverageApiRouter(beverageService)
  val reviewApiRoute      = new ReviewApiRoute(reviewService)

  import ApiJsonProtocol._

  // format: OFF
  val routes: Route = handleRejections(ValidationRejectionHandler) {
    sessionFromAuthorizationHeader { implicit optionalSession =>
      pathPrefix("api" / "v1") {
        healthRouter ~
          accountApiRouter.route ~
          authApiRouter.route  ~
          beverageApiRouter.route ~
          reviewApiRoute.route
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
