package utils

import org.xml.sax.InputSource

import scala.xml.SAXParser
import scala.xml.parsing.NoBindingFactoryAdapter

object HTMLParser extends  NoBindingFactoryAdapter{
    override def loadXML(source : InputSource, _p: SAXParser) = {
        loadXML(source)
    }

    def loadXML(source : InputSource) = {
        import nu.validator.htmlparser.{sax,common}
        import sax.HtmlParser
        import common.XmlViolationPolicy

        val reader = new HtmlParser
        reader.setXmlPolicy(XmlViolationPolicy.ALLOW)
        reader.setContentHandler(this)
        reader.parse(source)
        rootElem
    }
}
