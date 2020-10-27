/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.BatoiLogicBill;
import models.BatoiLogicCity;
import models.BatoiLogicProduct;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author batoi
 */
public class CityDAO implements GenericDAO<BatoiLogicCity> {

    private final Session connectionBD;

    public CityDAO() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicCity findByPk(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BatoiLogicCity findByName(String name) throws Exception {

        String hql = "from BatoiLogicCity where name = :name";
        Query query = connectionBD.createQuery(hql);
        query.setString("name", name);
        

        return (BatoiLogicCity) (query.list().size()>0? query.list().get(0): null);
    }

    @Override
    public Set<BatoiLogicCity> findAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insert(BatoiLogicCity object) throws Exception {
        boolean isActive = false;

        if (object != null)
        {
            connectionBD.getTransaction().begin();
            connectionBD.save(object);
            connectionBD.getTransaction().commit();
            isActive = true;
        }

        return isActive;
    }

    @Override
    public boolean update(BatoiLogicCity object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(BatoiLogicCity object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
