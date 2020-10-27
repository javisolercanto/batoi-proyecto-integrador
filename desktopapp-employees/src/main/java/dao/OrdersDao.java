package dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.BatoiLogicOrder;
import models.BatoiLogicOrderLine;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author silvia
 */
public class OrdersDao implements GenericDAO<BatoiLogicOrder> {

    private final Session connectionBD;

    public OrdersDao() {

        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    public void close()
    {
        connectionBD.close();
    }

    @Override
    public BatoiLogicOrder findByPk(int id) throws Exception {

        return connectionBD.get(BatoiLogicOrder.class, id);
    }

    @Override
    public Set<BatoiLogicOrder> findAll() throws Exception {

        HashSet<BatoiLogicOrder> ordersList = new HashSet<>();
        ordersList.addAll(connectionBD.createQuery("from BatoiLogicOrder ord",
                BatoiLogicOrder.class).list());
        return ordersList;
    }

    @Override
    public boolean insert(BatoiLogicOrder order) throws Exception {

        boolean isInserted = false;
        connectionBD.getTransaction().begin();

        if (order != null) {

            connectionBD.save(order);
            connectionBD.getTransaction().commit();
            isInserted = true;
        }

        return isInserted;
    }

    @Override
    public boolean update(BatoiLogicOrder order) throws Exception {

        boolean isUpdated = false;
        connectionBD.getTransaction().begin();

        if (order != null) {

            connectionBD.update(order);
            connectionBD.getTransaction().commit();
            isUpdated = true;
        }

        return isUpdated;
    }
    
    public int last_id() throws Exception {
        Query query = connectionBD.createQuery("select max(id) as max from BatoiLogicOrder cat");
         List<Object> order = query.list();
        return (int) order.get(0) + 1;
    }

    @Override
    public boolean delete(BatoiLogicOrder order) throws Exception {

        boolean isDeleted = false;

        connectionBD.getTransaction().begin();
        if (order != null) {

            connectionBD.delete(order);
            connectionBD.getTransaction().commit();
            isDeleted = true;
        }

        return isDeleted;
    }
}