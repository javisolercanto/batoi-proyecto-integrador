package models;
import java.sql.Date;
import java.util.List;

public class BatoiLogicOrder implements java.io.Serializable
{
     private int id;
     private String name;
     private Date date;
     private String status;
     private String information;

    public BatoiLogicOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    
    public String getOrderStatus() {
        String orderStatus;
        
        switch(Integer.parseInt(this.status)) {
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