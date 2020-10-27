package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicProduct;
import org.hibernate.Session;

public class ProductDao implements GenericDAO<BatoiLogicProduct> {

    private final Session connectionBD;

    public ProductDao() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    @Override
    public BatoiLogicProduct findByPk(int id) throws Exception
    {
        return connectionBD.getReference(BatoiLogicProduct.class, id);
    }

    @Override
    public Set<BatoiLogicProduct> findAll() throws Exception {
        HashSet<BatoiLogicProduct> productList = new HashSet<>();
        productList.addAll(connectionBD.createQuery("from BatoiLogicProduct prod",
                BatoiLogicProduct.class).list());

        return productList;
    }

    @Override
    public boolean insert(BatoiLogicProduct product) throws Exception {

        boolean isActive = false;
        connectionBD.getTransaction().begin();

        if (product != null) {

            connectionBD.save(product);
            connectionBD.getTransaction().commit();
            isActive = true;
        }

        return isActive;
    }

    @Override
    public boolean update(BatoiLogicProduct product)
    {
        boolean isActive = false;

        if (product != null) {

            connectionBD.getTransaction().begin();
            connectionBD.update(product);
            connectionBD.getTransaction().commit();
            isActive = true;
        }

        return isActive;
    }

    @Override
    public boolean delete(BatoiLogicProduct product) throws Exception {

        boolean isActive = false;
        connectionBD.getTransaction().begin();

        if (product != null) {

            connectionBD.delete(product);
            connectionBD.getTransaction().commit();
            isActive = true;
        }

        return isActive;
    }
}