package dao;

import java.util.HashSet;
import java.util.Set;

import models.BatoiLogicCity;
import models.BatoiLogicPostalCode;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author batoi
 */
public class PostalCodeDAO implements GenericDAO<BatoiLogicPostalCode> {

    private final Session connectionBD;

    public PostalCodeDAO() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicPostalCode findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicPostalCode.class, id);
    }

    @Override
    public Set<BatoiLogicPostalCode> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("from BatoiLogicPostalCode bill", BatoiLogicPostalCode.class).list());
    }

    public BatoiLogicPostalCode findByName(int zip)
    {
        String hql = "from BatoiLogicPostalCode where name = :name";
        Query query = connectionBD.createQuery(hql);
        query.setInteger("name", zip);

        return (BatoiLogicPostalCode) (query.list().size()>0? query.list().get(0): null);
    }

    @Override
    public boolean insert(BatoiLogicPostalCode object)
    {
        boolean exit = false;

        if (object != null)
        {
            connectionBD.beginTransaction();
            connectionBD.save(object);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }

    @Override
    public boolean update(BatoiLogicPostalCode object) throws Exception {
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
    public boolean delete(BatoiLogicPostalCode object) throws Exception {
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