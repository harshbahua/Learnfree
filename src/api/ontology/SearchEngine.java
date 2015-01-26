/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.ontology;

import java.util.ArrayList;

/**
 *
 * @author Monil Gudhka
 */
public class SearchEngine {
    private static SearchEngine engine;
    public static Node[] search(Ontology onto, String sentence){
        if(engine == null){
            engine = new SearchEngine(onto);
        }
        return engine.search(sentence);
    }
    public static String getResidue(){
        String s = engine.sent.replaceAll("_", " ");
        engine.sent = null;
        return s;
    }
    public static Node[] search(Ontology onto, String... words){
        return search(onto, combine(words));
    }
    private static String combine(String... words){
        if(words==null)
            return null;
        StringBuilder sent = new StringBuilder();
        for(int i=0; i<words.length; i++){
            if(!words[i].isEmpty())
                sent.append(" ").append(words[i]);
        }
        sent.deleteCharAt(0);
        return sent.toString();
    }
    private static String format(String sent){
        StringBuilder s = new StringBuilder();
        s.append("_");
        for(char c : sent.trim().toCharArray()){
            if(Character.isLetterOrDigit(c) || c=='+' || c=='-'){
                s.append(Character.toLowerCase(c));
            }else
                s.append("_");
        }
        s.append("_");
        return s.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private Ontology onto;
    private ArrayList<Keyword> keyword;
    private SearchEngine(Ontology onto){
        if(onto==null ||  !onto.getFile().exists())
            throw new NullPointerException("Incomplete Data");
        this.onto = onto;
        keyword = new ArrayList<Keyword>();
        init();
    }
    private void init(){
        int index = 0;
        while(true){
            Node n = onto.getNode(index);
            if(n == null)
                break;
            int index2 = 0;
            while(true){
                String s = n.getLocalName(index2);
                if(s == null)
                    break;
                Keyword k = new Keyword(s, n);
                int i=0;
                for(; i<keyword.size(); i++){
                    if(keyword.get(i).local_name.length() <= s.length()){
                        break;
                    }
                }
                keyword.add(i, k);
                index2++;
            }
            index++;
        }
    }
    
    public Node[] search(String sent){
        this.sent = format(sent);
        ArrayList<Node> involve = new ArrayList<Node>();
        for(int i=0; i<keyword.size(); i++){
            if(match(keyword.get(i).local_name))
                involve.add(keyword.get(i).n);
        }
        int index = involve.size();
        if(involve==null || index<=0)
            return null;
        Node[] n = new Node[index];
        for(int i=0; i<index; i++){
            n[i] = involve.get(i);
        }
        return n;
    }
    private String sent;
    private boolean match(String s){
        if(this.sent.length() < s.length())
            return false;
        int length = this.sent.length();
        this.sent = this.sent.replaceAll("_"+s+"_", "__");
        return (length > this.sent.length());
    }
    
    
    
    
    
    
    class Keyword{
        private String local_name;
        private Node n;
        Keyword(String local_name, Node n){
            this.local_name = local_name;
            this.n = n;
        }
    }
}
