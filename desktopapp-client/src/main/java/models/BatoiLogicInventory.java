package models;

import java.io.Serializable;

public class BatoiLogicInventory implements Serializable
{
     private int id;
     private BatoiLogicProduct batoiLogicProduct;
     private int stock;
     private String location;

    public BatoiLogicInventory()
    {
    }

    public BatoiLogicInventory(int id, BatoiLogicProduct batoiLogicProduct, int stock, String location) {
        this.id = id;
        this.batoiLogicProduct = batoiLogicProduct;
        this.stock = stock;
        this.location = location;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public BatoiLogicProduct getBatoiLogicProduct() {
        return this.batoiLogicProduct;
    }
    
    public void setBatoiLogicProduct(BatoiLogicProduct batoiLogicProduct) {
        this.batoiLogicProduct = batoiLogicProduct;
    }
    public int getStock() {
        return this.stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}