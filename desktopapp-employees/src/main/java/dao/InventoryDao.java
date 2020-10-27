package dao;

import java.util.HashSet;
import java.util.Set;
import models.BatoiLogicInventory;
import org.hibernate.Session;

public class InventoryDao implements GenericDAO<BatoiLogicInventory> {

    private Session connectionBD;

    public InventoryDao() {

        connectionBD = ConnectionHibernate.getSessionFactory().openSession();
    }

    /* Returns the object of the master table Inventory with the same id. 
    Throws NullPointerException if the product doesn't exists*/
    @Override
    public BatoiLogicInventory findByPk(int id) throws Exception {

        return connectionBD.get(BatoiLogicInventory.class, id);
    }

    /* Throws NullPointerException if the inventory master table is empty, 
    or the products cannot be finded*/
    @Override
    public Set<BatoiLogicInventory> findAll() throws Exception {

        HashSet<BatoiLogicInventory> inventoryList = new HashSet<>();
        inventoryList.addAll(connectionBD.createQuery("select inv from BatoiLogicInventory inv",
                BatoiLogicInventory.class).list());

        return inventoryList;
    }
    
    public BatoiLogicInventory findByProductId(int id) throws Exception {
        
        for (BatoiLogicInventory inventory : findAll()) {
            if (inventory.getBatoiLogicProduct().getId() == id) {
                return inventory;
            }
        }
        
        return null;
    }
    // Works
    @Override
    public boolean insert(BatoiLogicInventory inventory) throws Exception {

        boolean isInserted = false;

        if (inventory != null) {

            connectionBD.getTransaction().begin();
            connectionBD.save(inventory);
            connectionBD.getTransaction().commit();
            isInserted = true;
        }

        return isInserted;
    }

    /* Returns true if the product in inventory was updated correctly false if
    not*/
    @Override
    public boolean update(BatoiLogicInventory inventory) throws Exception {

        boolean isActive = false;

        if (inventory != null) {

            connectionBD.getTransaction().begin();
            connectionBD.update(inventory);
            connectionBD.getTransaction().commit();
            isActive = true;
        }

        return isActive;
    }

    /* Returns true if the product in inventory was deleted correctly false if
    not*/
    @Override
    public boolean delete(BatoiLogicInventory inventory) throws Exception {

        boolean isDeleted = false;

        if (inventory != null) {

            connectionBD.getTransaction().begin();
            connectionBD.delete(inventory);
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