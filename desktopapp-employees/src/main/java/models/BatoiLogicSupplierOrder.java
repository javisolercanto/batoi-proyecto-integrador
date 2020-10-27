package models;
// Generated 12 feb. 2020 0:21:54 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * BatoiLogicSupplierOrder generated by hbm2java
 */
public class BatoiLogicSupplierOrder  implements java.io.Serializable {


     private int id;
     private BatoiLogicProduct batoiLogicProduct;
     private BatoiLogicProvider batoiLogicProvider;
     private int quantity;
     private Date requestDate;
     private Integer createUid;
     private Date createDate;
     private Integer writeUid;
     private Date writeDate;
     private Set batoiLogicProviderBatoiLogicSupplierOrderRels = new HashSet(0);

    public BatoiLogicSupplierOrder() {
    }

	
    public BatoiLogicSupplierOrder(int id, BatoiLogicProduct batoiLogicProduct, BatoiLogicProvider batoiLogicProvider, int quantity, Date requestDate) {
        this.id = id;
        this.batoiLogicProduct = batoiLogicProduct;
        this.batoiLogicProvider = batoiLogicProvider;
        this.quantity = quantity;
        this.requestDate = requestDate;
    }
    public BatoiLogicSupplierOrder(int id, BatoiLogicProduct batoiLogicProduct, BatoiLogicProvider batoiLogicProvider, int quantity, Date requestDate, Integer createUid, Date createDate, Integer writeUid, Date writeDate, Set batoiLogicProviderBatoiLogicSupplierOrderRels) {
       this.id = id;
       this.batoiLogicProduct = batoiLogicProduct;
       this.batoiLogicProvider = batoiLogicProvider;
       this.quantity = quantity;
       this.requestDate = requestDate;
       this.createUid = createUid;
       this.createDate = createDate;
       this.writeUid = writeUid;
       this.writeDate = writeDate;
       this.batoiLogicProviderBatoiLogicSupplierOrderRels = batoiLogicProviderBatoiLogicSupplierOrderRels;
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
    public BatoiLogicProvider getBatoiLogicProvider() {
        return this.batoiLogicProvider;
    }
    
    public void setBatoiLogicProvider(BatoiLogicProvider batoiLogicProvider) {
        this.batoiLogicProvider = batoiLogicProvider;
    }
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Date getRequestDate() {
        return this.requestDate;
    }
    
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public Integer getCreateUid() {
        return this.createUid;
    }
    
    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Integer getWriteUid() {
        return this.writeUid;
    }
    
    public void setWriteUid(Integer writeUid) {
        this.writeUid = writeUid;
    }
    public Date getWriteDate() {
        return this.writeDate;
    }
    
    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }
    public Set getBatoiLogicProviderBatoiLogicSupplierOrderRels() {
        return this.batoiLogicProviderBatoiLogicSupplierOrderRels;
    }
    
    public void setBatoiLogicProviderBatoiLogicSupplierOrderRels(Set batoiLogicProviderBatoiLogicSupplierOrderRels) {
        this.batoiLogicProviderBatoiLogicSupplierOrderRels = batoiLogicProviderBatoiLogicSupplierOrderRels;
    }

    @Override
    public String toString()
    {
        return this.requestDate+" - "+this.batoiLogicProduct.getName()+ " - " + this.quantity + " (" + this.batoiLogicProvider.getName() + ")";
    }
}

