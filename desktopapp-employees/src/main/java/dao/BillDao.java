package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicBill;
import org.hibernate.Session;

public class BillDao implements GenericDAO<BatoiLogicBill> {

    private final Session connectionBD;

    public BillDao() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    public void close()
    {
        connectionBD.close();
    }

    @Override
    public BatoiLogicBill findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicBill.class, id);
    }

    @Override
    public Set<BatoiLogicBill> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicBill bill", BatoiLogicBill.class).list());
    }

    @Override
    public boolean insert(BatoiLogicBill bill) throws Exception {
        boolean exit = false;

        if (bill != null) {
            connectionBD.beginTransaction();
            connectionBD.save(bill);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }

    @Override
    public boolean update(BatoiLogicBill bill) throws Exception {
        boolean exit = false;
        
        if (bill != null) {
            connectionBD.beginTransaction();
            connectionBD.update(bill);
            connectionBD.getTransaction().commit();
            exit = true;
        }
        return exit;
    }

    @Override
    public boolean delete(BatoiLogicBill bill) throws Exception {
        boolean exit = false;

        if (bill != null) {
            connectionBD.beginTransaction();
            connectionBD.delete(bill);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }
}