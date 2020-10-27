/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Set;
import models.BatoiLogicOrderLine;
import org.hibernate.Session;

/**
 *
 * @author batoi
 */
public class Orders_LinesDAO implements GenericDAO<BatoiLogicOrderLine> {
    
    private final Session connectionBD;
    
    public Orders_LinesDAO() {

        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicOrderLine findByPk(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<BatoiLogicOrderLine> findAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insert(BatoiLogicOrderLine object) throws Exception {
        boolean isInserted = false;
        connectionBD.getTransaction().begin();

        if (object != null) {

            connectionBD.save(object);
            connectionBD.getTransaction().commit();
            isInserted = true;
        }

        return isInserted;
    }

    @Override
    public boolean update(BatoiLogicOrderLine object) throws Exception {
        boolean isUpdated = false;
        connectionBD.getTransaction().begin();

        if (object != null) {

            connectionBD.update(object);
            connectionBD.getTransaction().commit();
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean delete(BatoiLogicOrderLine object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
