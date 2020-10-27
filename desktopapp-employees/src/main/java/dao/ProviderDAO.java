package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicProvider;
import org.hibernate.Session;

/**
 *
 * @author batoi
 */
public class ProviderDAO implements GenericDAO<BatoiLogicProvider> {
    
    private final Session connectionBD;
    
    public ProviderDAO() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicProvider findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicProvider.class, id);
    }

    @Override
    public Set<BatoiLogicProvider> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicProvider bill", BatoiLogicProvider.class).list());
    }

    @Override
    public boolean insert(BatoiLogicProvider object)
    {
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
    public boolean update(BatoiLogicProvider object) throws Exception {
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
    public boolean delete(BatoiLogicProvider object) throws Exception {
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