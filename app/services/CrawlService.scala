package services

import com.google.inject.Inject
import dto.Dto._
import play.api.libs.ws.{WSClient, WSResponse}
import utils.CrawlMatching.titleMatching
import utils.FutureUtils.FutureExtensions

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.{FiniteDuration, _}
import scala.xml.Node
import utils.HTMLParser

class CrawlService @Inject()(ws: WSClient) {
    private implicit lazy val exec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

    def crawlTitles(urls: Seq[Url]): Future[Seq[CrawlResult]] =
        crawl(urls)(titleMatching)

    private def crawl(urls: Seq[Url])(matching: (Url, Node) => CrawlResult): Future[Seq[CrawlResult]] =
        Future.sequence(
            urls.map(url =>
                sendGetRequest(url)
                    .map(response => matching(url, HTMLParser.loadString(response.body)))
                    .recover { case e => CrawlResult(url, s"Oops, getting error: ${e.getMessage}") }
            )
        )

    private def sendGetRequest(url: Url, timeout: FiniteDuration = 20.seconds): Future[WSResponse] =
        ws.url(url.raw)
            .get()
            .withTimeout(s"Timeout for get request. Url: ${url.raw} ", timeout)
}

