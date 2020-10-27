package dam.android.sergic.app2.models;

public class PostalCode implements java.io.Serializable {
    private int zip;
    private City city;

    public PostalCode() {
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}