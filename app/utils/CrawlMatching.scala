package utils

import dto.Dto._

import scala.xml.Node

object CrawlMatching {

    def titleMatching(url: Url, xml: Node): CrawlResult = {
        Option(xml \ "head" \ "title")
            .filter(_.nonEmpty)
            .map(node => CrawlResult(url, node.text))
            .getOrElse(CrawlResult(url, "Tag title not found"))
    }
}
