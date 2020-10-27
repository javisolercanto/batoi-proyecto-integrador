package models;

public class Order implements java.io.Serializable
{
     private String orderNumber;
     private Customer customer;
     private Address address;
     private String date;
     private int status;
     private String about;
     private String notes;

     public static final int OUT_FOR_DELIVERY = 2;
     public static final int DELIVERED = 3;
     public static final int NOT_DELIVERED = 4;

    public Order() {
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//
//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderStatus() {
        String orderStatus;
        
        switch(this.status) {
            case 1:
                orderStatus = "READY";
                break;
                
            case 2:
                orderStatus = "OUT FOR DELIVERY";
                break;
                
            case 3:
                orderStatus = "DELIVERED";
                break;
                
            case 4:
                orderStatus = "NOT DELIVERED";
                break;
                
            case 0:
            default:
                orderStatus = "PENDING";
                break;
        }
        
        return orderStatus;
    }
}