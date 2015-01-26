/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.util;

import api.gui.MessageBox;
import java.awt.Component;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 *
 * @author Monil Gudhka
 */
public class User {
    
    private String user_id;
    private String password;
    private InputStream input_stream;
    private Component comp;
    private Console console;
    private String MESSAGE = "Enter your Password";
    private boolean authorized;
    
    public User(String user_id, String password, InputStream input_stream) throws NullPointerException{
        this(user_id, password);
        if(input_stream==null)
            throw new NullPointerException("Incomplete Data");
        this.input_stream = input_stream;
    }
    public User(String user_id, String password, Console console) throws NullPointerException{
        this(user_id, password);
        if(console == null)
            throw new NullPointerException("Incomplete Data");
        this.console = console;
    }
    public User(String user_id, String password, Component comp) throws NullPointerException{
        this(user_id, password);
        this.comp = comp;
    }
    private User(String user_id, String password) throws NullPointerException{
        if(user_id!=null  && password!=null){
            this.user_id = user_id;
            this.password = password;
            this.authorized = false;
        }else{
            throw new NullPointerException("Incomplete Data");
        }
    }
    
    
    
    
    
    public String getUserID(){
        return this.user_id;
    }
    public String getMessage(){
        return this.MESSAGE;
    }
    public void setMessage(String message){
        if(message!=null)
            this.MESSAGE = message;
    }
    public void changePassword(String pwd){
        if(pwd!=null && !pwd.isEmpty()){
            this.password = pwd;
            this.authorized = false;
        }
    }
    
    
    boolean hasPassword(String pwd){
        return this.password.equals(pwd);
    }
    public boolean isAuthorized(){
        if(!authorized){
            this.authorized = this.hasPassword(this.getPassword());
        }
        return this.authorized;
    }
    public boolean authenticate(){
        this.authorized = false;
        return this.isAuthorized();
    }
    
    private String getPassword(){
        if(this.input_stream != null){
            return readStream();
        }
        if(this.console != null){
            return readConsole();
        }
        return readGUI();
    }
    
    private String readStream(){
        StringBuilder s = new StringBuilder();
        try{
            int d = -1;
            while((d = this.input_stream.read()) >= 0){
                s.append(d);
            }
        }catch(IOException e){
            return null;
        }
        return s.toString();
    }
    private String readConsole(){
        return new String(this.console.readPassword(this.MESSAGE+" = "));
    }
    private String readGUI(){
        return MessageBox.password(comp, this.MESSAGE, "Authentication");
    }
    
    
    
    
    
    
    @Override
    public boolean equals(Object object){
        if(object instanceof User){
            User another_user = (User)object;
            return this.user_id.equals(another_user.user_id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.user_id);
        return hash;
    }
}
