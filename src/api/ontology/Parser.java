/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import api.io.XMLHandler;
import api.io.XMLParser;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Monil Gudhka
 */
public abstract class Parser {
    private static final int ATTS = 0;
    
    private XMLHandler handler;
    private ArrayList<Element> element_list;
    private Ontology onto;
    
    public Parser(Ontology onto){
        this.onto = onto;
        element_list = new ArrayList<Element>();
        handler = Parser.getHandler(this);
    }
    
    public abstract void prepare();
    public void parse() throws FileNotFoundException, IOException, SAXException{
        prepare();
        XMLParser.parse(this.onto.getFile(), handler);
    }
    public void addElement(Element e){
        this.element_list.add(e);
    }
    public void removeElement(Element e){
        this.element_list.remove(e);
    }
    
    
    
    
    
    
    
    
    public static Parser createInstance(Ontology ontology){
        Parser parser = new Parser(ontology){
            @Override
            public void prepare(){
                prepareHelper(this);
            }
        };
        return parser;
    }
    public static void parse(Ontology ontology) throws FileNotFoundException, IOException, SAXException{
        Parser parser = createInstance(ontology);
        parser.parse();
    }
    private static void prepareHelper(final Parser p){
        final Element node = new Element("Class"){
            @Override
            public boolean isSingleElement(){
                return true;
            }
            @Override
            public void action() {
                p.onto.addNode(new Node(this.getAttributeValue(ATTS)));
            }
        };
        final Element prop = new Element("AnnotationProperty"){
            @Override
            public boolean isSingleElement(){
                return true;
            }
            @Override
            public void action() {
                p.onto.addProperty(new Property(this.getAttributeValue(ATTS)));
            }
        };
        
        Element declaration = new Element("Declaration"){
            @Override
            public void end() {
                p.removeElement(node);
                p.removeElement(prop);
            }
            @Override
            public void action() {
                p.addElement(node);
                p.addElement(prop);
            }
        };
        p.addElement(declaration);
        
        
        Element relation = new Element("AnnotationAssertion"){
            @Override
            public void action() {
                final Relation r = new Relation();
                
                Element prop = new Element("AnnotationProperty"){
                    @Override
                    public boolean isSingleElement(){
                        return true;
                    }
                    @Override
                    public void action() {
                        r.setProperty(p.onto.searchProperty(this.getAttributeValue(ATTS)));
                        p.removeElement(this);
                    }
                };
                p.addElement(prop);
                
                Element iri = new Element("IRI"){
                    @Override
                    public void action() {
                        r.addNode(p.onto.searchNode(this.getText()));
                        if(r.isComplete()){
                            p.onto.addRelation(r);
                            p.removeElement(this);
                        }
                    }
                };
                p.addElement(iri);
                
                Element literal = new Element("Literal"){
                    @Override
                    public void action() {
                        r.addValue(this.getText());
                        p.onto.addRelation(r);
                        p.removeElement(this);
                    }
                };
                p.addElement(literal);
            }
        };
        p.addElement(relation);
        
        
        Element sub_class = new Element("SubClassOf"){
            Property prop = new Property("#isSubClassOf");
            @Override
            public void action() {
                final Relation r = new Relation();
                r.setProperty(prop);
                p.onto.addProperty(prop);
                
                Element clas = new Element("Class"){
                    @Override
                    public boolean isSingleElement(){return true;}
                    @Override
                    public void action() {
                        r.addNode(p.onto.searchNode(this.getAttributeValue(ATTS)));
                        if(r.isComplete()){
                            p.onto.addRelation(r);
                            r.getRightNode().addChild(r.getLeftNode());
                        }
                    }
                };
                p.addElement(clas);
            }
        };
        p.addElement(sub_class);
    }
    
    
    
    
    
    public static XMLHandler getHandler(final Parser p){
        XMLHandler xml_handler = new XMLHandler(){
            Stack<Element> stack = new Stack();
            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                Element t = search(localName);
                if(t != null){
                    stack.add(t);
                    stack.peek().start(atts);
                }
            }
            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if(!stack.empty() && localName.equals(stack.peek().getName())){
                    stack.pop().end();
                }
            }
            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if(!stack.empty()){
                    stack.peek().characters(ch, start, length);
                }
            }
            Element search(String localName){
                int i=p.element_list.size()-1;
                for(; i>=0 ; i--){
                    if(localName.equals(p.element_list.get(i).getName()))
                        return p.element_list.get(i);
                }
                return null;
            }
        };
        return xml_handler;
    }
}
