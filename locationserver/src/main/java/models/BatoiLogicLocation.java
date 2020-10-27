/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author javi
 */
public class BatoiLogicLocation {
    
    private int id;
    private int deliveryMan_id;
    private double lat;
    private double lon;

    public BatoiLogicLocation() {
    }

    public BatoiLogicLocation(int deliveryMan_id, double lat, double lon) {
        this.deliveryMan_id = deliveryMan_id;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeliveryMan_id() {
        return deliveryMan_id;
    }

    public void setDeliveryMan_id(int deliveryMan_id) {
        this.deliveryMan_id = deliveryMan_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    
    
    
}
