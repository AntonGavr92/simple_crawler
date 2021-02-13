package dto

import play.api.libs.json.{Json, OFormat}
import utils.JsonHelper.UrlFormat

object Dto {

    case class Url(raw: String)
    object Url {
        implicit def toRaw(url: Url): String = url.raw
    }

    case class CrawlResult(url: Url, result: String)
    object CrawlResult {
        implicit val formatCrawlResult: OFormat[CrawlResult] = Json.format[CrawlResult]
    }

}
