package models;
// Generated 5 feb. 2020 1:10:54 by Hibernate Tools 4.3.1

/**
 * BatoiLogicProviderBatoiLogicSupplierOrderRelId generated by hbm2java
 */
public class BatoiLogicProviderBatoiLogicSupplierOrderRelId  implements java.io.Serializable {


     private int batoiLogicProviderId;
     private int batoiLogicSupplierOrderId;

    public BatoiLogicProviderBatoiLogicSupplierOrderRelId() {
    }

    public BatoiLogicProviderBatoiLogicSupplierOrderRelId(int batoiLogicProviderId, int batoiLogicSupplierOrderId) {
       this.batoiLogicProviderId = batoiLogicProviderId;
       this.batoiLogicSupplierOrderId = batoiLogicSupplierOrderId;
    }
   
    public int getBatoiLogicProviderId() {
        return this.batoiLogicProviderId;
    }
    
    public void setBatoiLogicProviderId(int batoiLogicProviderId) {
        this.batoiLogicProviderId = batoiLogicProviderId;
    }
    public int getBatoiLogicSupplierOrderId() {
        return this.batoiLogicSupplierOrderId;
    }
    
    public void setBatoiLogicSupplierOrderId(int batoiLogicSupplierOrderId) {
        this.batoiLogicSupplierOrderId = batoiLogicSupplierOrderId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof BatoiLogicProviderBatoiLogicSupplierOrderRelId) ) return false;
		 BatoiLogicProviderBatoiLogicSupplierOrderRelId castOther = ( BatoiLogicProviderBatoiLogicSupplierOrderRelId ) other; 
         
		 return (this.getBatoiLogicProviderId()==castOther.getBatoiLogicProviderId())
 && (this.getBatoiLogicSupplierOrderId()==castOther.getBatoiLogicSupplierOrderId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getBatoiLogicProviderId();
         result = 37 * result + this.getBatoiLogicSupplierOrderId();
         return result;
   }   


}


