package models;

import javafx.scene.image.Image;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class BatoiLogicProduct  implements java.io.Serializable
{
    private int id;
    private String name;
    private double price;
    private double kg;
    private String description;
    private Image image;
    private List<Integer> batoiLogicProductBatoiLogicProviderRels;
    private Set batoiLogicInventories = new HashSet(0);
    private List<Integer> lines;                                                           // All lines which has this product in it.
    private Set batoiLogicSupplierOrders = new HashSet(0);

    public BatoiLogicProduct()
    {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price)
    {
        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        df.setRoundingMode(RoundingMode.FLOOR);
        this.price = new Double(df.format(price));
    }

    public double getKg() {
        return kg;
    }

    public void setKg(double kg) {
        this.kg = kg;
    }

    public String getDescription()
    {
        return description;
    }

    public String getFancyDescription()
    {
        int cont = 0;
        StringBuilder result = new StringBuilder();
        for(int i=0;i<description.length();i++)
        {
            cont++;
            if(cont > 40 && result.charAt(i-1)==' ')
            {
                result.append(System.lineSeparator());
                cont = 0;
            }

            result.append(description.charAt(i));
        }

        return result.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getBatoiLogicProductBatoiLogicProviderRels() {
        return batoiLogicProductBatoiLogicProviderRels;
    }

    public void setBatoiLogicProductBatoiLogicProviderRels(List<Integer> batoiLogicProductBatoiLogicProviderRels) {
        this.batoiLogicProductBatoiLogicProviderRels = batoiLogicProductBatoiLogicProviderRels;
    }

    public Set getBatoiLogicInventories() {
        return batoiLogicInventories;
    }

    public void setBatoiLogicInventories(Set batoiLogicInventories) {
        this.batoiLogicInventories = batoiLogicInventories;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public void setLines(List<Integer> lines) {
        this.lines = lines;
    }

    public Set getBatoiLogicSupplierOrders() {
        return batoiLogicSupplierOrders;
    }

    public void setBatoiLogicSupplierOrders(Set batoiLogicSupplierOrders) {
        this.batoiLogicSupplierOrders = batoiLogicSupplierOrders;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "BatoiLogicProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", kg=" + kg +
                ", description='" + description + '\'' +
                ", batoiLogicProductBatoiLogicProviderRels=" + batoiLogicProductBatoiLogicProviderRels +
                ", batoiLogicInventories=" + batoiLogicInventories +
                ", lines=" + lines +
                ", batoiLogicSupplierOrders=" + batoiLogicSupplierOrders +
                '}';
    }

    @Override
    public boolean equals(Object obj)
    {
        return ((BatoiLogicProduct) obj).getId() == this.getId();
    }
}