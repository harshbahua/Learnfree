/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import java.util.Objects;

/**
 *
 * @author Monil Gudhka
 */
public class Property {
    
    private String name;
    
    public Property(String name) throws NullPointerException{
        if(name == null)
            throw new NullPointerException("Incomplete Data");
        this.name = name;    
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean match(String... words){
        for(String w: words){
            if(w.equals(this.name))
                return true;
        }
        return false;
    }
    
    
    
    @Override
    public String toString(){
        return this.name;
    }
    @Override
    public boolean equals(Object obj){
        try{
            if(obj instanceof Property){
                Property prop = (Property)obj;
                return this.name.equals(prop.name);
            }
        }catch(NullPointerException e){}
        return false;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
