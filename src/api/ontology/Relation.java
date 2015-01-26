/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Monil Gudhka
 */
public class Relation {
    private Property prop;
    private Node left;
    private Node right;
    private ArrayList value;
    
    public Relation(){
        value = new ArrayList();
    }
    
    public void setProperty(Property prop){
        if(prop != null)
            this.prop = prop;
    }
    Property getProperty(){
        return this.prop;
    }
    
    public void addNode(Node node){
        if(node != null){
            if(left == null){
                left = node;
                //left.addRelation(this);
            }
            else{
                right = node;
            }
        }
    }
    Node getLeftNode(){
        return this.left;
    }
    Node getRightNode(){
        return this.right;
    }
    
    void addValue(Object value){
        if(value!=null){
            this.value.add(value);
            if(prop!=null  && left!=null){
                left.addRelation(this);
            }
        }
    }
    public Object getValue(int index){
        if(0<=index && index<this.value.size())
            return this.value.get(index);
        return null;
    }
    public boolean isComplete(){
        return (prop!=null) && (left!=null) && (right!=null);
    }
    
    
    
    @Override
    public String toString(){
        return left.getName()+" "+this.prop.toString()+" "+this.right.getName();
    }
    @Override
    public boolean equals(Object obj){
        try{
            if(obj instanceof Relation){
                Relation r = (Relation)obj;
                return this.prop.equals(r.prop) && this.left.equals(r.left) && this.right.equals(r.right);
            }
        }catch(NullPointerException e){}
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.prop);
        hash = 23 * hash + Objects.hashCode(this.left);
        hash = 23 * hash + Objects.hashCode(this.right);
        return hash;
    }
}
