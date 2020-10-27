package controllers;

import com.dam.group2.desktopapp.client.App;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import dao.InventoryDAO;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.BatoiLogicInventory;
import models.BatoiLogicProduct;
import tools.BackgroundLoad;
import tools.Tools;

public class ControllerProduct implements Initializable{

    @FXML private ImageView btnCart;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnAdd;

    @FXML private Text tfNumItems;
    @FXML private Text btnSignup;

    @FXML private ImageView image;
    @FXML private Text textName;
    @FXML private Text textPrice;
    @FXML private Text textStock;
    @FXML private TextArea textAreaDescription;
    
    @FXML private Pane toast;

    private static final String IN_STOCK = "In Stock";
    private static final String NOT_AVAILABLE = "Not Available";

    private static final String VIEW_CART = "ViewCart";
    private static final String VIEW_PRODUCTS = "ViewProduct";

    public static BatoiLogicProduct product;
    private InventoryDAO inventoriesDB;
    private BatoiLogicInventory stock;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        try
        {
            inventoriesDB = new InventoryDAO();
            new Thread(() ->
            {
                try
                {
                    stock = inventoriesDB.findByProductId(product.getId());
                    setStock(stock.getStock());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }).start();

            textName.setText(product.getName());
            textPrice.setText(product.getPrice() + "€");
            textAreaDescription.setText(product.getDescription());
            image.setImage(product.getImage());

            tfNumItems.setText(CurrentCustomer.getShoppingCart().size()+"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void addToCart()
    {
        if(stock!=null && stock.getStock()>0)
        {
            int quantity = CurrentCustomer.getShoppingCart().getOrDefault(product.getId(), -1);

            if(quantity != -1)
            {
                if(stock.getStock()-quantity > 0)
                {
                    CurrentCustomer.getShoppingCart().put(product.getId(),quantity+1);
                    showToast();
                }
                else
                    Tools.showSimpleError("Error","Not Enough Stock!");
            }
            else
                CurrentCustomer.getShoppingCart().put(product.getId(), 1);

            tfNumItems.setText(CurrentCustomer.getShoppingCart().size()+"");
        }
        else
            Tools.showSimpleError("Not Available","This product is out of stock");
    }
    
    @FXML
    public void openCart(MouseEvent event)
    {
        try
        {
            CurrentCustomer.setPreviousScreen(VIEW_PRODUCTS);
            openView(VIEW_CART, event);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void back(MouseEvent event) throws IOException
    {
        openView(CurrentCustomer.getPreviousScreen(), event);
        CurrentCustomer.setPreviousScreen(VIEW_PRODUCTS);
    }

    private void openView(String view, MouseEvent event) throws IOException
    {
        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setStock(Integer stock) {
        if (stock > 0) {
            textStock.setText(IN_STOCK);
            textStock.setFill(Paint.valueOf("#399a00"));

        } else {
            textStock.setText(NOT_AVAILABLE);
            textStock.setFill(Paint.valueOf("#F20000"));
        }
    }
    
    public void showToast() {
        toast.setVisible(true);
        
        FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1500), toast);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        fadeInTransition.setOnFinished(c -> {
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(1500), toast);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);

            fadeOutTransition.setOnFinished(o -> toast.setVisible(false));
            fadeOutTransition.play();
        });
        fadeInTransition.play();
    }
}