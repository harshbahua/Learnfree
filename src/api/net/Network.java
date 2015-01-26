/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.net;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 *
 * @author Monil Gudhka
 */
public class Network {
    
    private Socket client;
    private ServerSocket server;
    private ObjectInputStream read;
    private ObjectOutputStream write;
    private boolean encrypt;
    
    public Network(){
        this.encrypt = false;
    }
    public Network(int port) throws IOException{
        this();
        server = new ServerSocket(port);
    }
    
    
    public void connectTo(String ip_address, int port) throws UnknownHostException, IOException{
        client = new Socket(ip_address, port);
    }
    public void accept() throws IOException{
        if(this.server == null)
            throw new IOException("Not a Server");
        client = server.accept();
    }
    public void close(){
        try {
            if(read != null)
                read.close();
        } catch (IOException ex) {}
        
        try {
            if(write != null){
                write.flush();
                write.close();
            }
        } catch (IOException ex) {}
        
        try {
            client.close();
        } catch (IOException ex) {}
        
        if(this.isServer()){
            try {
                server.close();
            } catch (IOException ex) {}
        }
    }
    
    
    
    private void initWrite() throws IOException{
        if(read == null){
            if(write == null)
                write = new ObjectOutputStream(client.getOutputStream());
        }else
            throw new IOException("OutputStream is not Available");
    }
    public void write(Object data) throws IOException, NotSerializableException{
        initWrite();
        if(data instanceof Serializable){
            if(this.isEncryptionEnable()){
                Capsule big_data = new Capsule(data);
                write.writeObject(big_data);
            }else
                write.writeObject(data);
        }else{
            throw new NotSerializableException();
        }
    }
    
    private void initRead() throws IOException{
        if(write == null){
            if(read == null)
                read = new ObjectInputStream(client.getInputStream());
        }else
            throw new IOException("InputStream is not Available");
            
    }
    public Object read() throws IOException, ClassNotFoundException{
        initRead();
        Object data = read.readObject();
        if(this.isEncryptionEnable()){
            if(data instanceof Capsule){
                Capsule big_data = (Capsule)data;
                return big_data.getData();
            }
        }else
            return data;
        return null;
    }
    
    
    public void setEncryption(boolean enable){
        this.encrypt = enable;
    }
    public boolean isEncryptionEnable(){
        return this.encrypt;
    }
    public boolean isServer(){
        return (server != null);
    }
    
}

class Capsule implements Serializable{
    private Object data;
    private int hash;
    private int length;
    Capsule(Object data){
        this.data = data;
        this.hash = data.hashCode();
        this.length = data.toString().length();
    }
    Object getData(){
        boolean flag = (this.data.hashCode() == this.hash) && (this.data.toString().length() == this.length);
        return (flag)? this.data : null;
    }
}