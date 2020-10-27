/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author javi
 */
public class ConnectionDB {
    
    private static Connection connection;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://grup2.cipfpbatoi.es:5432/batoi_logic_db", "postgres", "ser");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                connection.close();
            }
        }
        
        return connection;
    }
}
