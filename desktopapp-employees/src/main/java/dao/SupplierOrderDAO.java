package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicSupplierOrder;
import org.hibernate.Session;

/**
 *
 * @author batoi
 */
public class SupplierOrderDAO implements GenericDAO<BatoiLogicSupplierOrder> {

    private final Session connectionBD;

    public SupplierOrderDAO() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicSupplierOrder findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicSupplierOrder.class, id);
    }

    @Override
    public Set<BatoiLogicSupplierOrder> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicSupplierOrder bill", BatoiLogicSupplierOrder.class).list());
    }

    @Override
    public boolean insert(BatoiLogicSupplierOrder object) throws Exception {
        boolean exit = false;

        if (object != null) {
            connectionBD.beginTransaction();
            connectionBD.save(object);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }

    @Override
    public boolean update(BatoiLogicSupplierOrder object) throws Exception {
        boolean exit = false;
        if (object != null) {
            connectionBD.beginTransaction();
            connectionBD.update(object);
            connectionBD.getTransaction().commit();
            exit = true;
        }
        return exit;
    }

    @Override
    public boolean delete(BatoiLogicSupplierOrder object) throws Exception {
        boolean exit = false;

        if (object != null) {
            connectionBD.beginTransaction();
            connectionBD.delete(object);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }
}