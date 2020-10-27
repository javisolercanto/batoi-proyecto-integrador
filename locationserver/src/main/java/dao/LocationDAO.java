/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Statement;
import models.BatoiLogicLocation;
import postgresql.ConnectionDB;

/**
 *
 * @author javi
 */
public class LocationDAO
{
    public LocationDAO()
    {
    }

    public boolean insert(BatoiLogicLocation object) throws Exception {
        Statement stmt = ConnectionDB.getConnection().createStatement();
            String sql = "INSERT INTO public.batoi_logic_location(\n" +
                    "            latitude, longitude, delivery_man_id)\n" +
                    "    VALUES ("+object.getLat()+", "+object.getLon()+", "+object.getDeliveryMan_id()+");";
			stmt.executeUpdate(sql);
        
        return true;
    }
}
