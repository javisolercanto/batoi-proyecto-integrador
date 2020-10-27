package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import postgresql.ConnectionDB;

public class OrderDAO 
{

    public OrderDAO()
    {
    }
    
    public int findByName(String name) throws SQLException {
        int id = 0;
        
        Statement stmt = ConnectionDB.getConnection().createStatement();
			String sql = "SELECT id FROM batoi_logic_order WHERE name LIKE '" + name + "'";
			ResultSet rs = stmt.executeQuery(sql);
        
        if (rs.next()) {
            id = rs.getInt("id");
        }
                        
        return id;
    }

    public boolean update(int id, String status, String about) throws Exception {
        
        String information = about == null ? "" : about;
        
        Statement stmt = ConnectionDB.getConnection().createStatement();
			String sql = "UPDATE batoi_logic_order SET status = " + status + ", information = '"+ information + "' WHERE id = " + id + ";";
			stmt.executeUpdate(sql);
        
        return true;
    }
}