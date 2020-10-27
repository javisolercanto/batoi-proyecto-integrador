package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicAddress;
import org.hibernate.Session;

public class AddressDao implements GenericDAO<BatoiLogicAddress> {

    private final Session connectionBD;

    public AddressDao() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicAddress findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicAddress.class, id);
    }

    @Override
    public Set<BatoiLogicAddress> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicAddress address", BatoiLogicAddress.class).list());
    }

    // Is not necessary to implement this
    @Override
    public boolean insert(BatoiLogicAddress object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Is not necessary to implement this
    @Override
    public boolean update(BatoiLogicAddress object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Is not necessary to implement this
    @Override
    public boolean delete(BatoiLogicAddress object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}