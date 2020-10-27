package models;

import java.util.List;

public class BatoiLogicAddress  implements java.io.Serializable
{
     private int id;
     private BatoiLogicCustomer batoiLogicCustomer;
     private BatoiLogicPostalCode batoiLogicPostalCode;
     private String name;
     private List<Integer> orders;                                          // All the orders associated with this direction
//     private Set<BatoiLogicBill> batoiLogicBills = new HashSet(0);

    public BatoiLogicAddress() {
    }
    
    public BatoiLogicAddress(BatoiLogicCustomer customer, BatoiLogicPostalCode code, String name) {
        this.batoiLogicCustomer = customer;
        this.batoiLogicPostalCode = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BatoiLogicCustomer getBatoiLogicCustomer() {
        return batoiLogicCustomer;
    }

    public void setBatoiLogicCustomer(BatoiLogicCustomer batoiLogicCustomer) {
        this.batoiLogicCustomer = batoiLogicCustomer;
    }

    public BatoiLogicPostalCode getBatoiLogicPostalCode() {
        return batoiLogicPostalCode;
    }

    public void setBatoiLogicPostalCode(BatoiLogicPostalCode batoiLogicPostalCode) {
        this.batoiLogicPostalCode = batoiLogicPostalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.batoiLogicPostalCode.getBatoiLogicCity().getName() + ")";
    }
}