/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.io;

import java.io.File;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Monil Gudhka
 */
public class XMLWriter {
    private File file;
    private Document doc;
    private Stack<Element> stack;
    
    public XMLWriter(String file_name) throws ParserConfigurationException{
        this(new File(file_name));
    }
    public XMLWriter(File file) throws ParserConfigurationException{
        this.file = file;
        this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        stack = new Stack<Element>();
    }
    
    public void setXMLVersion(String xml_version){
        this.doc.setXmlVersion(xml_version);
    }
    public void setXMLStandalone(boolean xml_standalone){
        this.doc.setXmlStandalone(xml_standalone);
    }
    
    public void write() throws TransformerConfigurationException, TransformerException{
        while(!this.stack.isEmpty()){
            endNode();
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	DOMSource source = new DOMSource(this.doc);
        StreamResult result = new StreamResult(this.file);
        transformer.transform(source, result);
    }
    
    
    
    public void startNode(String node_name){
        startNode(node_name, null);
    }
    public void startNode(String node_name, String namespace){
        Element node = this.doc.createElementNS(null, node_name);
        stack.push(node);
    }
    public void endNode(){
        this.append(this.stack.pop());
    }
    public void leafNode(String node_name, String context){
        this.leafNode(node_name, null, context);
    }
    public void leafNode(String node_name, String namespace, String context){
        this.startNode(node_name, namespace);
        this.textNode(context);
        this.endNode();
    }
    
    public void textNode(String context){
        this.append(this.doc.createTextNode(context));
    }
    public void comment(String comment){
        this.append(this.doc.createComment(comment));
    }
    
    public void setAttribute(String name, String value){
        if(!this.stack.isEmpty())
            this.stack.peek().setAttribute(name, value);
    }
    public void setAttributes(String[] name, String[] value){
        try{
            for(int i=0; i<name.length && i<value.length; i++){
                this.setAttribute(name[i], value[i]);
            }
        } catch(Exception e){}
    }
    
    private void append(Node node){
        this.peek().appendChild(node);
    }
    private Node peek(){
        if(this.stack.isEmpty())
            return this.doc;
        return this.stack.peek();
    }
    
    
    
    
    
    public static void main(String args[]){
        try {
            XMLWriter r = new XMLWriter("test.xml");
            String attr_list[] = {"private", "city"};
            String attr_value[] = {"yes", "MUM"};
            r.startNode("Company");
            r.startNode("Employee");
            r.leafNode("name", "Monil");
            r.leafNode("salary", "1000");
            r.startNode("Single");
            r.setAttributes(attr_list, attr_value);
            try {
                r.write();
            } catch (TransformerConfigurationException ex) {
                Logger.getLogger(XMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(XMLWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}