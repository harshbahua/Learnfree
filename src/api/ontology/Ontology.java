/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author Monil Gudhka
 */
public class Ontology {
    
    private File file;
    private ArrayList<Node> nodes;
    private Node root_node;
    private ArrayList<Property> properties;
    private ArrayList<Relation> relations;
    private Parser parser;
    private boolean parsed;
    
    public Ontology(String file){
        this(new File(file));
    }
    public Ontology(File file){
        this.file = file;
        nodes = new ArrayList<Node>();
        properties = new ArrayList<Property>();
        relations = new ArrayList<Relation>();
    }
    
    public File getFile(){
        return this.file;
    }
    public void setParser(Parser parser){
        this.parser = parser;
    }
    public void parse() throws FileNotFoundException, IOException, SAXException{
        if(parsed)
            return;
        if(this.parser == null)
            this.parser = Parser.createInstance(this);
        this.parser.parse();
        this.findRootNode(0);
        parsed = true;
    }
    
    
  
    void findRootNode(int from){
        Node n1 = getNode(from);
        Node n2 = null;
        while(n1 != null){
            n2 = n1;
            n1 = n1.getParent();
        }
        this.root_node = n2;
    }
    public Node getRootNode(){
        return this.root_node;
    }
    public void addNode(Node n){
        if(!this.nodes.contains(n)){
            this.nodes.add(n);
        }
    }
    public void removeNode(Node n){
        this.nodes.remove(n);
    }
    Node getNode(int index){
        try{
            return this.nodes.get(index);
        }catch(IndexOutOfBoundsException e){}
        return null;
    }
    
    
    public void addProperty(Property prop){
        if(!this.properties.contains(prop))
            this.properties.add(prop);
    }
    public void removeProperty(Property prop){
        this.properties.remove(prop);
    }
    Property getProperty(int index){
        try{
            return this.properties.get(index);
        }catch(IndexOutOfBoundsException e){}
        return null;
    }
    
    
    public void addRelation(Relation r){
        if(!this.relations.contains(r))
            this.relations.add(r);
    }
    public void removeRelation(Relation r){
        this.relations.remove(r);
    }
    Relation getRelation(int index){
        try{
            return this.relations.get(index);
        }catch(IndexOutOfBoundsException e){}
        return null;
    }
    
    
    
    
    
    
    
    
    public Node[] search(String... words){
        return SearchEngine.search(this, words);
    }
    public Node[] search(String sent){
        Node[] n = SearchEngine.search(this, sent);
        this.sent = SearchEngine.getResidue();
        return n;
    }
    private String sent;
    public String getResidue(){
        return sent;
    }
    
    Node searchNode(String word){
        Iterator<Node> itr = this.nodes.iterator();
        while(itr.hasNext()){
            Node n = itr.next();
            if(n.getName().equalsIgnoreCase(word))
                return n;
        }
        return null;
    }
    Property searchProperty(String... words){
        Iterator<Property> itr = this.properties.iterator();
        while(itr.hasNext()){
            Property prop = itr.next();
            if(prop.match(words))
                return prop;
        }
        return null;
    }
    
    
    
    
    public static void main(String... args){
        Ontology onto = new Ontology("Elearn.owl");
        try {
            onto.parse();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SAXException ex) {
            Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Ontology Name = "+onto.getFile()+"\n");
        
        for(int i=0; i< onto.nodes.size(); i++){
            System.out.println("Node["+i+"] = "+onto.nodes.get(i));
        }
        System.out.println();
        
        //System.out.println(onto.searchNode("#FormAttributes").getParent().getParent());
        
        
        System.out.println("Root Node = "+onto.root_node);
        System.out.println();
        for(int i=0; i<onto.properties.size(); i++){
            System.out.println("Property["+i+"] = "+onto.properties.get(i));
        }
        System.out.println();
        /*for(int i=0; i<onto.relations.size(); i++){
            System.out.println("Relation["+i+"] = "+onto.relations.get(i));
        }*/
        
        System.out.println("Basics in HTML'");
        Node[] n = onto.search("Forms Attributes");
        for (Node n1 : n) {
            System.out.print(n1+"("+n1.getUrl(0)+")");
            Node a = n1;
            do
            {
                a=a.getParent();
                System.out.print(" -> "+a+"("+a.getUrl(0)+")");
                
                
            } while(a != onto.root_node);
            System.out.println();
        }
        
    }
    
    
       
}   
