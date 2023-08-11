package org.rodrigodiaz.db;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;


public class Conexion {
    private Connection conexion;
    
    private static Conexion instancia;
    
    private Conexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBTonysKinal2023?useSSL=false","root", "admin");
            //conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBTonysKinal2023?useSSL=false","kinal", "admin");
      
        } catch (ClassNotFoundException error) {
            error.printStackTrace();
        }catch(InstantiationException error){
            error.printStackTrace();
        }catch(IllegalAccessException error){
            error.printStackTrace();
        }catch(SQLException error){
            error.printStackTrace();    
        }catch(Exception error){
            error.printStackTrace();
        }
    }
    
    public  static Conexion getInstancia(){
        if (instancia == null) 
            instancia = new Conexion();
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    
  


    
    
    
}
