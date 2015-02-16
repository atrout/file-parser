import play.api.GlobalSettings
import play.api.libs.json.Json
import play.api.mvc.RequestHeader
import play.api.mvc.Results.{BadRequest, InternalServerError}

import scala.concurrent.Future

object FileParserGlobal extends GlobalSettings {
  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(InternalServerError(
      Json.obj("status" -> "error", "message" -> ex.getMessage)
    ))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest(
      Json.obj("status" -> "error", "message" -> ("Bad Request: " + error))
    ))
  }
}

