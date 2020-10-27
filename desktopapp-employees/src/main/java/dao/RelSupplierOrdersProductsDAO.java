package dao;
import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicProductBatoiLogicProviderRel;
import org.hibernate.Session;

/**
 *
 * @author batoi
 */
public class RelSupplierOrdersProductsDAO {
    private final Session connectionBD;
    
     public RelSupplierOrdersProductsDAO() {
         connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }
    public BatoiLogicProductBatoiLogicProviderRel findByPK(int id_product, int id_supplier_order){
        Set<BatoiLogicProductBatoiLogicProviderRel> list = new HashSet<>(connectionBD.createQuery("from BatoiLogicSupplierOrder supplierorder, BatoiLogicProduct product WHERE product.id=" + id_product + " AND supplierorder.id =" + id_supplier_order, BatoiLogicProductBatoiLogicProviderRel.class).list());
        return list.iterator().next();
    }
    
    public boolean insert(BatoiLogicProductBatoiLogicProviderRel obj){
        boolean isInserted = false;

        if (obj != null) {

            connectionBD.getTransaction().begin();
            connectionBD.save(obj);
            connectionBD.getTransaction().commit();
            isInserted = true;
        }

        return isInserted;
    }
    
    public boolean update(BatoiLogicProductBatoiLogicProviderRel object){
        boolean exit = false;
        if (object != null) {
            connectionBD.beginTransaction();
            connectionBD.update(object);
            connectionBD.getTransaction().commit();
            exit = true;
        }
        return exit;
    }
    
}