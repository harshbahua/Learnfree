package api.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Monil Gudhka
 */
public class Database {
    private final String password;
    private final String username;
    private final String URL;
    private Statement statement;
    
    public Database(String URL, String username, String password) throws SQLException{
        this.URL = URL;
        this.username = username;
        this.password = password;
    }
    public boolean connect() throws SQLException{
        if(this.statement == null){
            Connection conn = DriverManager.getConnection(this.URL, this.username, this.password);
            statement = conn.createStatement();
        }
        return (this.statement != null);
    }
    public void close(){
        Connection conn = null;
        try {
            conn = statement.getConnection();
            statement.close();
        } catch (NullPointerException ex) {}
        catch (SQLException ex){}
        try {
            conn.commit();
            conn.close();
        } catch (NullPointerException ex) {}
        catch (SQLException ex){}
    }
    
    public List read(String query) throws SQLException{
        query = query.trim().toLowerCase();
        if(!query.startsWith("select")){
            throw new SQLException("Not a READ Query");
        }
        return readHelper(query);
    }
    public Object[][] select(String query) throws SQLException{
        return Database.toArray(read(query));
    }
    private synchronized List readHelper(String query) throws SQLException{
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        ResultSet result = statement.executeQuery(query);
        ResultSetMetaData meta = result.getMetaData();
        while(result.next()){
            Object[] row = new Object[meta.getColumnCount()];
            for(int i=0; i<row.length; i++) {
                row[i] = result.getObject(i+1);
            }
            list.add(row);
        }
        return list;
    }
    
    
    public int write(String query) throws SQLException{
        query = query.trim();
        return writeHelper(query);
    }
    private synchronized int writeHelper(String query) throws SQLException{
        return statement.executeUpdate(query);
    }
    
    
    public boolean create(String query) throws SQLException{
        query = query.trim().toLowerCase();
        if(!query.startsWith("create"))
            throw new SQLException("Not a Data Definition Language");
        return exec(query);
    }
    private synchronized boolean exec(String query) throws SQLException{
        return statement.execute(query);
    }
    
    
    public boolean execute(String query) throws SQLException{
        query = query.trim().toLowerCase();
        return exec(query);
    }
    
    
    
    @Override
    @SuppressWarnings("FinalizeDeclaration")
    public void finalize() throws Throwable{
        this.close();
        super.finalize();
    }
    @Override
    public boolean equals(Object obj){
        boolean flag = false;
        if(obj instanceof Database){
            Database db = (Database)obj;
            flag = this.URL.equals(db.URL) && this.username.equals(db.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.username);
        hash = 13 * hash + Objects.hashCode(this.URL);
        return hash;
    }
    
    
    
    public static Object[][] toArray(List list){
        int size = list.size();
        Object result[][] = new Object[size][];
        for(int i=0; i<size; i++){
            result[i] = (Object[]) list.get(i);
        }
        return result;
    }
}
