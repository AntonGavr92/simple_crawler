package utils

import dto.Dto.Url
import play.api.libs.json.{Format, JsError, JsResult, JsString, JsSuccess, JsValue}

import scala.util.matching.Regex

object JsonHelper {

    implicit object UrlFormat extends Format[Url] {

        val httpRegexp: Regex = "^https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_,\\+.~#?&\\/=]*)$".r

        def reads(json: JsValue): JsResult[Url] = {
            json.asOpt[String]
                .filter(value => httpRegexp.findFirstIn(value.trim).isDefined)
                .map(value => JsSuccess(Url(value)))
                .getOrElse(JsError("Not valid url"))
        }

        def writes(url: Url): JsValue = JsString(url.raw)
    }
}
