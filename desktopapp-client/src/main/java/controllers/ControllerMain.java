package controllers;

import com.dam.group2.desktopapp.client.App;
import dao.OrderDAO;
import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import dao.ProductDAO;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.BatoiLogicProduct;
import tools.BackgroundLoad;
import tools.Tools;

public class ControllerMain implements Initializable
{
    @FXML private ImageView btnCart;
    @FXML private ImageView btnDisconnect;
    @FXML private ImageView btnOrders;
    @FXML private Text tfNumItems;
    @FXML private TableView<BatoiLogicProduct> tvProducts;
    @FXML private TextField tfSearch;
    @FXML private ComboBox<String> cbFilters;
    @FXML private Text btnSignIn;
    @FXML private TableColumn<BatoiLogicProduct, ImageView> cImage;
    @FXML private TableColumn<BatoiLogicProduct, String> cName;
    @FXML private TableColumn<BatoiLogicProduct, String> cDescription;
    @FXML private TableColumn<BatoiLogicProduct, String> cPrice;
    @FXML private ProgressBar pbLoad;

    private ProductDAO productsDB;
    private ObservableList<BatoiLogicProduct> products;
    private BackgroundLoad loader;

    private static final String VIEW_LOGIN = "ViewLogin";
    private static final String VIEW_ORDERS = "ViewOrders";
    private static final String VIEW_CART = "ViewCart";
    private static final String VIEW_PROFILE = "ViewProfile";
    private static final String VIEW_MAIN = "ViewMain";
    private static final String VIEW_PRODUCTS = "ViewProduct";

    private static final String FILTER_PRICE_1 = "Low to High";
    private static final String FILTER_PRICE_2 = "High to Low";
    private static final String FILTER_ALPHABETICALLY = "Sorted alphabetically";
    private static final String NO_FILTERS = "No Filters";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            tfNumItems.setText(CurrentCustomer.getShoppingCart().size()+"");
        
            productsDB = new ProductDAO();
            products = FXCollections.observableArrayList();

            // THREAD!!!
            loader = new BackgroundLoad(pbLoad, products, tvProducts, productsDB);
            loader.start();

            setupTable();

            if (CurrentCustomer.getCurrentCustomer() != null)
                btnSignIn.setText(CurrentCustomer.getCurrentCustomer().getNickname());
            else
                btnSignIn.setText("SIGN IN");

            ObservableList<String> filters = FXCollections.observableArrayList();
            filters.add(FILTER_PRICE_1);
            filters.add(FILTER_PRICE_2);
//            filters.add("Available");
            filters.add(FILTER_ALPHABETICALLY);
            filters.add(NO_FILTERS);

            cbFilters.setItems(filters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupTable()
    {
        tvProducts.setItems(products);

        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        cPrice.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getPrice()+"€"));
        cImage.setCellValueFactory(o ->
        {
            ImageView iv = new ImageView();
            iv.setFitHeight(150);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            iv.setImage(o.getValue().getImage());

            return new ObservableValue<>()
            {
                @Override public void addListener(ChangeListener<? super ImageView> changeListener) {}
                @Override public void removeListener(ChangeListener<? super ImageView> changeListener) {}
                @Override public void addListener(InvalidationListener invalidationListener) {}
                @Override public void removeListener(InvalidationListener invalidationListener) {}

                @Override
                public ImageView getValue()
                {
                    return iv;
                }
            };
        });
    }

    @FXML
    public void openCart(MouseEvent event) throws IOException
    {
        CurrentCustomer.setPreviousScreen(VIEW_MAIN);
        openView(VIEW_CART, event);
    }

    @FXML
    public void openOrders(MouseEvent event) throws IOException
    {
        if (CurrentCustomer.getCurrentCustomer() != null)
            openView(VIEW_ORDERS, event);
        else
        {
            CurrentCustomer.setPreviousScreen(VIEW_ORDERS);
            openView(VIEW_LOGIN, event);
        }
    }

    @FXML
    public void openLogin(MouseEvent event) throws IOException
    {
        CurrentCustomer.setPreviousScreen(VIEW_MAIN);
        if (CurrentCustomer.getCurrentCustomer() != null)
        {
            openView(VIEW_PROFILE, event);
        }
        else
        {
            openView(VIEW_LOGIN, event);
        }
    }

    @FXML
    public void disconnect(MouseEvent event) throws IOException {
        if (CurrentCustomer.getCurrentCustomer() != null) {
            switch (Tools.confirmationDialog("DISCONNECT")) {
                case 0:
                    CurrentCustomer.setPreviousScreen(VIEW_MAIN);
                    openView(VIEW_LOGIN, event);
                    CurrentCustomer.setUser(null);
                    break;
                case 1:
                    System.exit(0);
                    break;
            }

        } else {
            System.exit(0);
        }
    }

    private void openView(String view, MouseEvent event) throws IOException
    {
        loader.interrupt();

        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void searchByName()
    {
        String toSearch = tfSearch.getText().trim();

        ObservableList<BatoiLogicProduct> copy = FXCollections.observableArrayList(products);
        if(toSearch.length()>0)
        {
            copy.removeIf(c -> !c.getName().toLowerCase().contains(toSearch.toLowerCase()));
            tvProducts.setItems(copy);
        }
        else
            tvProducts.setItems(products);
    }

    @FXML
    private void applyFilter()
    {
        switch (cbFilters.getValue())
        {
            case FILTER_PRICE_1:
                products.sort(Comparator.comparingDouble(BatoiLogicProduct::getPrice));
                break;
            case FILTER_PRICE_2:
                products.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
                break;
            case FILTER_ALPHABETICALLY:
                products.sort((o1, o2) -> CharSequence.compare(o1.getName(), o2.getName()));
                break;
            case NO_FILTERS:
                products.sort(Comparator.comparingInt(BatoiLogicProduct::getId));
                break;
        }
        tvProducts.refresh();
    }

    @FXML
    public void onTableClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && (tvProducts.getSelectionModel().getSelectedItem() != null)) {
            CurrentCustomer.setPreviousScreen(VIEW_MAIN);
            ControllerProduct.product = tvProducts.getSelectionModel().getSelectedItem();
            openView(VIEW_PRODUCTS, event);
        }
    }
}