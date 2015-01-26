/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import org.xml.sax.Attributes;

/**
 *
 * @author Monil Gudhka
 */
public abstract class Element {
    
    private String localName;
    private Attributes atts;
    private String text;
    
    public Element(String local_name){
        this.localName = local_name;
    }
    
    
    public String getName(){
        return this.localName;
    }
    public String getAttributeValue(int index){
        return atts.getValue(index);
    }
    public String getAttributeValue(String qName){
        return atts.getValue(qName);
    }
    public String getText(){
        return this.text;
    }
    
    
    
    private boolean started;
    public void start(Attributes atts){
        this.atts = atts;
        this.started = true;
        if(this.isSingleElement()){
            action();
            this.started = false;
        }
    }
    public void characters(char[] ch, int start, int length){
        String s = new String(ch, start, length);
        this.text = s.trim();
        if(started){
            action();
            this.started = false;
        }
    }
    public void end(){}
    public abstract void action();
    public boolean isSingleElement(){
        return false;
    }
    
    
    @Override
    public String toString(){
        return this.getName();
    }
}
