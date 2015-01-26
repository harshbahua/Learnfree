/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.io;

import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Monil Gudhka
 */
public abstract class XMLHandler implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler{

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return null;
    }
    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {}
    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {}
    @Override
    public void setDocumentLocator(Locator locator) {}
    @Override
    public void startDocument() throws SAXException {}
    @Override
    public void endDocument() throws SAXException {}
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {}
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {}
    /*@Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {}
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {}
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {}*/
    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
    @Override
    public void processingInstruction(String target, String data) throws SAXException {}
    @Override
    public void skippedEntity(String name) throws SAXException {}
    @Override
    public void warning(SAXParseException exception) throws SAXException {}
    @Override
    public void error(SAXParseException exception) throws SAXException {}
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {}
}
