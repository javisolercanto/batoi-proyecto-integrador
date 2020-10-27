package dam.android.sergic.app2.models;

public class DeliveryNote
{
    private String notes;
    private Order order;

    public DeliveryNote() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}