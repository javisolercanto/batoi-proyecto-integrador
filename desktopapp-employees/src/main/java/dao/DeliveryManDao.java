package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicDeliveryMan;
import org.hibernate.Session;

public class DeliveryManDao implements GenericDAO<BatoiLogicDeliveryMan> {

    private Session connectionBD;

    public DeliveryManDao() {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    public Set<BatoiLogicDeliveryMan> findAll() throws Exception {

        return new HashSet<>(connectionBD.createQuery("select a from BatoiLogicDeliveryMan a order by id",
                BatoiLogicDeliveryMan.class).list());
    }

    @Override
    public BatoiLogicDeliveryMan findByPk(int id) throws Exception {
        return connectionBD.get(BatoiLogicDeliveryMan.class, id);
    }

    // Not necessary to implement this
    @Override
    public boolean insert(BatoiLogicDeliveryMan object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean update(BatoiLogicDeliveryMan deliveryMan) throws Exception {
        
        boolean updated = false;

        if (deliveryMan != null) {
            
            connectionBD.beginTransaction();
            connectionBD.update(deliveryMan);
            connectionBD.getTransaction().commit();
            updated = true;
        }

        return updated;
    }

    // Not necessary to implement this
    @Override
    public boolean delete(BatoiLogicDeliveryMan object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void close()
    {
        connectionBD.close();
    }
}