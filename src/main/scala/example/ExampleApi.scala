package example

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import example.api.WebServer
import example.api.routers.ApiRouter
import example.common.config.ConfigKeeper
import example.persistence.migration.DatabaseSchemeMigration
import example.services.{AccountServiceImpl, AuthServiceImpl, BeverageServiceImpl, ReviewServiceImpl}

import scala.concurrent.ExecutionContextExecutor
import scala.util.Properties

object ExampleApi extends App {

  val config = ConfigKeeper.appConfig

  implicit val actorSystem = ActorSystem("ExampleSystem")
  implicit val dispatcher:   ExecutionContextExecutor = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer        = ActorMaterializer()

  DatabaseSchemeMigration.migrate(onFailure = () => sys.exit)

  val accountService    = wire[AccountServiceImpl]
  val authService       = wire[AuthServiceImpl]
  val beverageService   = wire[BeverageServiceImpl]
  val reviewService     = wire[ReviewServiceImpl]

  val apiRouter = wire[ApiRouter]

  val myPort = Properties.envOrElse("PORT", "8080").toInt // for Heroku compatibility

  WebServer(apiRouter.routes)
    .startServer(config.server.host, myPort, ServerSettings(actorSystem), actorSystem)

}
