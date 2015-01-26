/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 *
 * @author Monil Gudhka
 */
public class XMLParser {
    private static final String vendor = "com.sun.org.apache.xerces.internal.parsers.SAXParser";
    
    public static void parse(File file, XMLHandler handler) throws FileNotFoundException, IOException, SAXException{
        if(!isExists(file))
            throw new FileNotFoundException();
        XMLParser parser = new XMLParser(file);
        parser.setHandler(handler);
        parser.parse();
    }
    public static void parse(String file_name, XMLHandler handler) throws FileNotFoundException, IOException, SAXException{
        parse(new File(file_name), handler);
    }
    private static boolean isExists(File f){
        return f.isFile() && f.exists();
    }
    
    
    
    private File file;
    private XMLReader reader;
    public XMLParser(File file) throws SAXException{
        this.file = file;
        init();
    }
    public XMLParser(String file_name) throws SAXException{
        this(new File(file_name));
    }
    private void init() throws SAXException{
        this.setReader(XMLReaderFactory.createXMLReader(vendor));
    }
    
    
    private void setReader(XMLReader reader){
        if(reader != null)
            this.reader = reader;
    }
    
    
    public void setHandler(XMLHandler handler){
        if(handler == null)
            return;
        reader.setContentHandler(handler);
        reader.setDTDHandler(handler);
        reader.setEntityResolver(handler);
        reader.setErrorHandler(handler);
    }
    public XMLHandler getHandler(){
        return (XMLHandler)reader.getContentHandler();
    }
    
    
    public void parse() throws IOException, SAXException{
        try{
            reader.parse(file.getPath());
        } catch (NullPointerException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void parse(InputSource src) throws IOException, SAXException{
        try{
            reader.parse(src);
        } catch (NullPointerException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
