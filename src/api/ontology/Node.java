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
public class Node {
    private static final String ALTERNATIVE = "#foundUsing";
    
    private String name;
    private ArrayList<String> local_name;
    private ArrayList<String> url;
    private Node parent;
    private ArrayList<Node> children;
    private ArrayList<Relation> relation;
    
    public Node(String name) throws NullPointerException{
        if(name == null)
            throw new NullPointerException("Incomplete Data");
        this.name = name;
        this.local_name = new ArrayList<String>();
        this.url = new ArrayList<String>();
        format(name, local_name);
        this.parent = null;
        this.children = new ArrayList<Node>();
        this.relation = new ArrayList<Relation>();
    }
    
    
    private void setParent(Node n){
        if(n != null)
            this.parent = n;
    }
    public Node getParent(){
        return this.parent;
    }
    void addChild(Node n){
        if(n != null){
            n.setParent(this);
            this.children.add(n);
        }
    }
    public Node getChild(int index){
        if(0<=index && index<this.children.size())
            return this.children.get(index);
        return null;
    }
    public Node[] getChildren(){
        Node[] n = new Node[this.children.size()];
        for(int i=0; i<this.children.size(); i++){
            n[i] = getChild(i);
        }
        return n;
    }
    
    void addRelation(Relation r){
        if(r != null){
            if(ALTERNATIVE.equals(r.getProperty().getName()))
                this.addUrl((String)r.getValue(0));
            else
                this.relation.add(r);
        }
    }
    public Relation getRelation(String property){
        for(int i=0; i<this.relation.size(); i++){
            if(property.equals(this.relation.get(i).getProperty().getName()))
                return this.relation.get(i);
        }
        return null;
    }
    public Relation getRelation(int index){
        if(0<=index && index<this.relation.size())
            return this.relation.get(index);
        return null;
    }
    
    
    public String getName(){
        return this.name;
    }
    void addLocalName(String name){
        if(name!=null && !name.isEmpty()){
            format(name, this.local_name);
        }
    }
    void addUrl(String u){
        this.url.add(u);
    }
    String getUrl(int index){
        if(0<=index && index<this.url.size())
            return this.url.get(index);
        return "";
    }
    String getLocalName(int index){
        if(0<=index && index<this.local_name.size())
            return this.local_name.get(index);
        return null;
    }
    
    
    
    
    @Override
    public String toString(){
        return this.name;
    }
    @Override
    public boolean equals(Object object){
        try{
            if(object instanceof Node){
                Node n = (Node)object;
                return n.name.equals(this.name);
            }
        }catch(NullPointerException e){}
        return false;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    
    
    
    private static void format(String s, ArrayList<String> list){
        if(isAbbrevation(s)){
            String t = s.replaceFirst("#", "").toLowerCase();
            list.add(t);
            return;
        }
        StringBuilder w = new StringBuilder();
        for(char c : s.toCharArray()){
            switch(c){
                case '#' : continue;
                case '+' : w.append("\\");
                case '-' : w.append(c); break;
                default : if(Character.isLetterOrDigit(c)){
                                if(Character.isUpperCase(c)){
                                    w.append('_');
                                    c = Character.toLowerCase(c);
                                }
                                w.append(c);
                           }else
                                w.append('_');
            }
        }
        w.deleteCharAt(0);
        int i=0;
        for(; i<list.size(); i++){
            if(list.get(i).length() < w.length())
                break;
        }
        list.add(i, w.toString());
    }
    private static boolean isAbbrevation(String s){
        for(char c : s.toCharArray()){
            if(Character.isLowerCase(c))
                return false;
        }
        return true;
    }
    public static void main(String rags[]){
        format("Kruskal'sAlgorithm", null);
    }
}
