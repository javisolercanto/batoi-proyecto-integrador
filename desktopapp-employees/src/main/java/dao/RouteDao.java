package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicRoute;
import org.hibernate.Session;

public class RouteDao implements GenericDAO<BatoiLogicRoute> {

    private final Session connectionBD;

    public RouteDao() {

        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicRoute findByPk(int id) throws Exception {

        return connectionBD.get(BatoiLogicRoute.class, id);
    }

    @Override
    public Set<BatoiLogicRoute> findAll() throws Exception {

        HashSet<BatoiLogicRoute> ordersList = new HashSet<>();
        ordersList.addAll(connectionBD.createQuery("select route from BatoiLogicRoute route",
                BatoiLogicRoute.class).list());

        return ordersList;
    }

    @Override
    public boolean insert(BatoiLogicRoute route) throws Exception {

        boolean isInserted = false;

        if (route != null)
        {
            connectionBD.getTransaction().begin();
            connectionBD.save(route);
            connectionBD.getTransaction().commit();
            isInserted = true;
        }

        return isInserted;
    }

    @Override
    public boolean update(BatoiLogicRoute route) throws Exception {

        boolean isUpdated = false;
        connectionBD.getTransaction().begin();

        if (route != null) {

            connectionBD.update(route);
            connectionBD.getTransaction().commit();
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean delete(BatoiLogicRoute route) throws Exception {

        boolean isDeleted = false;

        connectionBD.getTransaction().begin();
        if (route != null) {

            connectionBD.delete(route);
            connectionBD.getTransaction().commit();
            isDeleted = true;
        }

        return isDeleted;
    }

    public void close()
    {
        connectionBD.close();
    }
}