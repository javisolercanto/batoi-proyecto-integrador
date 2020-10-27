package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicCustomer;
import org.hibernate.Session;

public class CustomerDao implements GenericDAO<BatoiLogicCustomer> {

    private final Session connectionBD;

    public CustomerDao() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicCustomer findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicCustomer.class, id);
    }

    @Override
    public Set<BatoiLogicCustomer> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicCustomer cust", BatoiLogicCustomer.class).list());
    }

    // Is not necessary to implement this
    @Override
    public boolean insert(BatoiLogicCustomer object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Is not necessary to implement this
    @Override
    public boolean update(BatoiLogicCustomer object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Is not necessary to implement this
    @Override
    public boolean delete(BatoiLogicCustomer object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}