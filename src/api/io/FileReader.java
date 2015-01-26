/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;

/**
 *
 * @author Monil Gudhka
 */
public class FileReader extends BufferedReader{
    
    private File file;
    
    public FileReader(String file_name) throws FileNotFoundException{
        this(new File(file_name));
    }
    public FileReader(File file) throws FileNotFoundException{
        super(new java.io.FileReader(file));
        this.file = file;
    }
    public FileReader(String parent, String child) throws FileNotFoundException{
        this(new File(parent, child));
    }
    public FileReader(File parent, String child) throws FileNotFoundException{
        this(new File(parent, child));
    }
    public FileReader(URI uri) throws FileNotFoundException{
        this(new File(uri));
    }
    
    public String readFile() throws IOException{
        StringBuilder str = new StringBuilder();
        String b = null;
        while((b = this.readLine()) != null){
            str.append(b).append('\n');
        }
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }
    public Object readObject() throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        fis.close();
        return obj;
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        FileReader file = new FileReader("Test2.txt");
        System.out.println(file.readFile());
    }
}
