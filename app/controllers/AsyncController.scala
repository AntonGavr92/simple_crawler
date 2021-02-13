package controllers

import javax.inject._
import play.api.mvc._
import services.CrawlService
import dto.Dto._
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import utils.JsonHelper.UrlFormat

@Singleton
class AsyncController @Inject()(crawlService: CrawlService, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def crawlTitles: Action[AnyContent] = Action.async { implicit req =>
    (for {
        body <- req.body.asJson
        urls <- body.validate[Seq[Url]].asOpt
      } yield {
        crawlService.crawlTitles(urls).map(result => Ok(Json.toJson(result)))
      }).getOrElse(Future.successful(BadRequest("Wrong url format")))
  }
}
