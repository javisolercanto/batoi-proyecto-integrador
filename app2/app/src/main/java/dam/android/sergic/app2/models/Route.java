package dam.android.sergic.app2.models;

import java.util.List;

public class Route
{
    private List<Integer> deliveryNotesIds;

    public Route(List<Integer> deliveryNotesIds) {
        this.deliveryNotesIds = deliveryNotesIds;
    }

    public List<Integer> getDeliveryNotesIds() {
        return deliveryNotesIds;
    }
}