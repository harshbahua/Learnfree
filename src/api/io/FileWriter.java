/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

/**
 *
 * @author Monil Gudhka
 */
public class FileWriter extends BufferedWriter{
    
    private File file;
    
    public FileWriter(File file) throws IOException{
        super(new java.io.FileWriter(file));
        this.file = file;
    }
    public FileWriter(String file_name) throws IOException{
        this(new File(file_name));
    }
    public FileWriter(String parent, String child) throws IOException{
        this(new File(parent, child));
    }
    public FileWriter(File parent, String child) throws IOException{
        this(new File(parent, child));
    }
    public FileWriter(URI uri) throws IOException{
        this(new File(uri));
    }
    
    
    public void writeFile(String... str) throws IOException{
        int i=0;
        for(; i<str.length-1; i++){
            this.write(str[i]);
            this.newLine();
        }
        this.write(str[i]);
    }
    public void writeObject(Object obj) throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        fos.flush();
        oos.close();
        fos.close();
    }
    
    public static void main(String args[]) throws IOException{
        FileWriter file = new FileWriter("Test2.txt");
        file.write("Hello");
        //file.writeFile("Hello", "How are you ?", "I m fine");
        file.flush();
        file.close();
    }
}
