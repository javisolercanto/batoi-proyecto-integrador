package dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import models.BatoiLogicDeliveryNote;
import models.BatoiLogicOrder;
import org.hibernate.Session;

public class DeliveryNoteDao implements GenericDAO<BatoiLogicDeliveryNote> {

    private final Session connectionBD;

    public DeliveryNoteDao()
    {
        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    public void close()
    {
        connectionBD.close();
    }

    // Works
    @Override
    public BatoiLogicDeliveryNote findByPk(int id) throws Exception {

        return connectionBD.getReference(BatoiLogicDeliveryNote.class, id);
    }

    // Works
    @Override
    public Set<BatoiLogicDeliveryNote> findAll() throws Exception {
        return new HashSet<>(connectionBD.createQuery("select a from BatoiLogicDeliveryNote a", BatoiLogicDeliveryNote.class).list());
    }
    
//    public List<BatoiLogicDeliveryNote> findDeliveryNotes() throws Exception {
//        List list = FXCollections.observableArrayList();
//
//        for (BatoiLogicDeliveryNote deliveryNote : findAll()) {
//            if ((deliveryNote.getBatoiLogicOrder().getStatus().equals("0") || deliveryNote.getBatoiLogicOrder().getStatus().equals("4")) && deliveryNote.getBatoiLogicRoute() == null) {
//                list.add(deliveryNote);
//            }
//        }
//
//        return list;
//    }

    @Override
    public boolean insert(BatoiLogicDeliveryNote deliveryNote) throws Exception
    {
        boolean exit = false;

        if (deliveryNote != null)
        {
            connectionBD.beginTransaction();
            connectionBD.save(deliveryNote);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }

    // Works
    @Override
    public boolean update(BatoiLogicDeliveryNote deliveryNote) throws Exception {
        boolean exit = false;

        if (deliveryNote != null) {
            
            connectionBD.beginTransaction();
            connectionBD.update(deliveryNote);
            connectionBD.getTransaction().commit();
            exit = true;
        }

        return exit;
    }

    // There is no need to implement this, it is required in BillDao
    @Override
    public boolean delete(BatoiLogicDeliveryNote object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public boolean order_created(BatoiLogicOrder object) throws Exception { 
        return new HashSet<>(connectionBD.createQuery("select a from BatoiLogicDeliveryNote a WHERE a.batoiLogicOrder.id = 1", BatoiLogicDeliveryNote.class).list()).size() == 0;
    }
}