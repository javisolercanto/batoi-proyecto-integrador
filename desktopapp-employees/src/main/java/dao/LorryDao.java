package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicLorry;
import org.hibernate.Session;

public class LorryDao implements GenericDAO<BatoiLogicLorry>{

    private Session connectionBD;

    public LorryDao() {

        connectionBD = connectionBD.getSessionFactory().openSession();
    }
    
    public Set<BatoiLogicLorry> findAll() throws Exception {

        HashSet<BatoiLogicLorry> lorriesList = new HashSet<>();
        lorriesList.addAll(connectionBD.createQuery("select lorries from BatoiLogicLorries lorries", 
                BatoiLogicLorry.class).list());
        return lorriesList;
    }

    @Override
    public BatoiLogicLorry findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicLorry.class, id);
    }

    // Not necessary to implement this
    @Override
    public boolean insert(BatoiLogicLorry object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Not necessary to implement this
    @Override
    public boolean update(BatoiLogicLorry object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Not necessary to implement this
    @Override
    public boolean delete(BatoiLogicLorry object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}