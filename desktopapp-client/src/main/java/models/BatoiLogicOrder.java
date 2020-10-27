package models;
import java.sql.Date;
import java.util.List;

public class BatoiLogicOrder implements java.io.Serializable
{
     private int id;
     private BatoiLogicAddress batoiLogicAddress;
     private BatoiLogicCustomer batoiLogicCustomer;
     private String name;
     private Date date;
     private String status;
     private String information;
//     private BatoiLogicDeliveryNote batoiLogicDeliveryNote;
     private List<Integer> lines_id;

    public BatoiLogicOrder() {
    }

    public BatoiLogicOrder(BatoiLogicAddress batoiLogicAddress, BatoiLogicCustomer batoiLogicCustomer, String name, Date date, String status, String information) {
        this.batoiLogicAddress = batoiLogicAddress;
        this.batoiLogicCustomer = batoiLogicCustomer;
        this.name = formatNumber(name);
        this.date = date;
        this.status = status;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BatoiLogicAddress getBatoiLogicAddress() {
        return batoiLogicAddress;
    }

    public void setBatoiLogicAddress(BatoiLogicAddress batoiLogicAddress) {
        this.batoiLogicAddress = batoiLogicAddress;
    }

    public BatoiLogicCustomer getBatoiLogicCustomer() {
        return batoiLogicCustomer;
    }

    public void setBatoiLogicCustomer(BatoiLogicCustomer batoiLogicCustomer) {
        this.batoiLogicCustomer = batoiLogicCustomer;
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

    public List<Integer> getLines_id() {
        return lines_id;
    }

    public void setLines_id(List<Integer> lines_id) {
        this.lines_id = lines_id;
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
    
    private String formatNumber(String name) {
        String number = "";
        for (int i = 0; i < 4 - name.length(); i++) {
            number += 0;
        }
        
        return number + name;
    }

    @Override
    public String toString() {
        return "BatoiLogicOrder{" +
                "id=" + id +
                ", batoiLogicAddress=" + batoiLogicAddress +
                ", batoiLogicCustomer=" + batoiLogicCustomer +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", information='" + information + '\'' +
                ", lines_id=" + lines_id +
                '}';
    }
}