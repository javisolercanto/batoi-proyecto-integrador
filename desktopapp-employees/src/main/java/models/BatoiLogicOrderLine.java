package models;
// Generated 12 feb. 2020 0:21:54 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * BatoiLogicOrderLine generated by hbm2java
 */
public class BatoiLogicOrderLine  implements java.io.Serializable {


     private int id;
     private BatoiLogicOrder batoiLogicOrder;
     private BatoiLogicProduct batoiLogicProduct;
     private int quantity;
     private Integer createUid;
     private Date createDate;
     private Integer writeUid;
     private Date writeDate;

    public BatoiLogicOrderLine() {
        
    }

	
    public BatoiLogicOrderLine(int id, BatoiLogicOrder batoiLogicOrder, BatoiLogicProduct batoiLogicProduct, int quantity) {
        this.id = id;
        this.batoiLogicOrder = batoiLogicOrder;
        this.batoiLogicProduct = batoiLogicProduct;
        this.quantity = quantity;
    }
    public BatoiLogicOrderLine(int id, BatoiLogicOrder batoiLogicOrder, BatoiLogicProduct batoiLogicProduct, int quantity, Integer createUid, Date createDate, Integer writeUid, Date writeDate) {
       this.id = id;
       this.batoiLogicOrder = batoiLogicOrder;
       this.batoiLogicProduct = batoiLogicProduct;
       this.quantity = quantity;
       this.createUid = createUid;
       this.createDate = createDate;
       this.writeUid = writeUid;
       this.writeDate = writeDate;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public BatoiLogicOrder getBatoiLogicOrder() {
        return this.batoiLogicOrder;
    }
    
    public void setBatoiLogicOrder(BatoiLogicOrder batoiLogicOrder) {
        this.batoiLogicOrder = batoiLogicOrder;
    }
    public BatoiLogicProduct getBatoiLogicProduct() {
        return this.batoiLogicProduct;
    }
    
    public void setBatoiLogicProduct(BatoiLogicProduct batoiLogicProduct) {
        this.batoiLogicProduct = batoiLogicProduct;
    }
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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




}

