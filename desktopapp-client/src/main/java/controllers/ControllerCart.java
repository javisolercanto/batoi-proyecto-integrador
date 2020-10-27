package controllers;

import com.dam.group2.desktopapp.client.App;
import dao.*;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import java.sql.Date;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import org.apache.xmlrpc.XmlRpcException;
import tools.Tools;

public class ControllerCart
{
    @FXML private ImageView btnRemoveCart;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnCheckout;
    @FXML private TableView<BatoiLogicProduct> tvCartTable;
    @FXML private Text btnSignIn;
    @FXML private Text tfTotal;

    @FXML private TableColumn<BatoiLogicProduct, ImageView> cImage;
    @FXML private TableColumn<BatoiLogicProduct, String> cName;
    @FXML private TableColumn<BatoiLogicProduct, Integer> cQuantity;
    @FXML private TableColumn<BatoiLogicProduct, String> cPrice;
    
    private ProductDAO productsDB;
    private AddressDAO addressDB;
    private OrderDAO orderDB;
    private OrderLineDAO orderLineDB;
    private InventoryDAO inventoryDB;
    private ObservableList<BatoiLogicProduct> products;    
    
    private static final String VIEW_LOGIN = "ViewLogin";
    private static final String VIEW_CART = "ViewCart";
    private static final String VIEW_PROFILE = "ViewProfile";
    private static final String VIEW_MAIN = "ViewMain";
    private static final String VIEW_PRODUCTS = "ViewProduct";
    private static final int MIN_STOCK = 10;

    private static final String PENDING = "0";
    private static final String WORKING = "We are working on your order";
    private static final String WAITING = "We haven't stock for your order now";
    
    @FXML
    public void initialize()
    {
        try
        {
            BatoiLogicCustomer cus = CurrentCustomer.getCurrentCustomer();
            if(cus !=null)
                btnSignIn.setText(cus.getNickname());

            productsDB = new ProductDAO();
            products = FXCollections.observableArrayList();
            
            addressDB = new AddressDAO();
            orderDB = new OrderDAO();
            orderLineDB = new OrderLineDAO();
            inventoryDB = new InventoryDAO();

            HashMap<Integer, Integer> cart = CurrentCustomer.getShoppingCart();
            if(cart.size()>0)
            {
                List<Integer> ids = new ArrayList<>(cart.keySet());
                products.addAll(productsDB.findAllByPks(ids));

                setupTable();
            }
            else
            {
                btnCheckout.setDisable(true);
                btnRemoveCart.setDisable(true);
            }
            
            refreshTotal();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void setupTable()
    {
        tvCartTable.setItems(products);
        cName.setCellValueFactory(new PropertyValueFactory<>("name"));

        cQuantity.setCellValueFactory(v -> new ObservableValue<>()
        {
            @Override public void addListener(ChangeListener<? super Integer> changeListener) {}
            @Override public void removeListener(ChangeListener<? super Integer> changeListener) {}
            @Override public void addListener(InvalidationListener invalidationListener) {}
            @Override public void removeListener(InvalidationListener invalidationListener) {}
            @Override
            public Integer getValue()
            {
                return v.getValue().getId();
            }
        });
        cQuantity.setCellFactory(o -> new TableCell<>()
        {
            @Override
            protected void updateItem(Integer id, boolean b)
            {
                super.updateItem(id, b);

                if(!b)
                {
                    try
                    {
                        int stock = new InventoryDAO().findByProductId(id).getStock();
                        int quantity = CurrentCustomer.getShoppingCart().get(id);

                        Spinner<Integer> spinner = new Spinner<>();
                        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stock, quantity));

                        spinner.valueProperty().addListener((observable, oldValue, newValue) ->
                        {
                            CurrentCustomer.getShoppingCart().put(id,newValue);
                            refreshTotal();
                        });

                        // Showing the spinner
                        setGraphic(spinner);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

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
    public void removeCart()
    {
        BatoiLogicProduct product = tvCartTable.getSelectionModel().getSelectedItem();
        if(product!=null)
        {
            CurrentCustomer.getShoppingCart().remove(product.getId());
            products.remove(product);
            tvCartTable.refresh();

            if(products.size()==0)
            {
                btnCheckout.setDisable(true);
                btnRemoveCart.setDisable(true);
            }
            
        } else {
            CurrentCustomer.getShoppingCart().clear();
            products = FXCollections.observableArrayList();
            tvCartTable.getItems().clear();
            tvCartTable.refresh();
            btnCheckout.setDisable(true);
            btnRemoveCart.setDisable(true);
        }
        
        refreshTotal();
    }
    
    @FXML
    public void openCheckOut(MouseEvent event) throws IOException, XmlRpcException, Exception
    {
        if (CurrentCustomer.getCurrentCustomer() != null)
        {
            if (CurrentCustomer.getCurrentCustomer().getAddresses_id().size() > 0)
            {
                BatoiLogicAddress choosenAddress = showCheckoutAlert();

                if (choosenAddress != null) {
                    BatoiLogicOrder order = new BatoiLogicOrder(choosenAddress, CurrentCustomer.getCurrentCustomer(), 
                            String.valueOf(orderDB.getMaxOrderNumber()+1), new Date(Calendar.getInstance().getTime().getTime()), PENDING, WORKING);

                    int id = orderDB.insertNoLines(order);

                    order.setId(id);
                    updateLines(order);

                    if (orderDB.update(order)) {
                            Tools.showSucceed("Order", "Order confirmed succesfuly\nOrder Nº: " + order.getName());
                            CurrentCustomer.removeAllCart();
                            openView(VIEW_MAIN, event);
                    } else
                        Tools.showSimpleError("Order", "Order can't be proceeded");
                }
            }
            else
            {
                Tools.showSimpleError("Addresses", "You don't have any address");
                CurrentCustomer.setPreviousScreen(VIEW_CART);
                openView(VIEW_PROFILE, event);
            }
        }
        else
        {
            CurrentCustomer.setPreviousScreen(VIEW_CART);
            openView(VIEW_LOGIN, event);
        }
    }

    @FXML
    public void openLogin(MouseEvent event) throws IOException
    {
        CurrentCustomer.setPreviousScreen(VIEW_CART);
        if (CurrentCustomer.getCurrentCustomer() != null)
            openView(VIEW_PROFILE, event);
        else
            openView(VIEW_LOGIN, event);
    }
    
    @FXML
    public void back(MouseEvent event) throws IOException
    {
        openView(CurrentCustomer.getPreviousScreen(), event);
        CurrentCustomer.setPreviousScreen(VIEW_MAIN);
    }
    
    @FXML
    public void onTableClicked(MouseEvent event) throws IOException 
    {
        if (event.getClickCount() == 2 && (tvCartTable.getSelectionModel().getSelectedItem() != null)) 
        {
            CurrentCustomer.setPreviousScreen(VIEW_MAIN);
            ControllerProduct.product = tvCartTable.getSelectionModel().getSelectedItem();
            openView(VIEW_PRODUCTS, event);
        }
    }
    
    private void refreshTotal() 
    {
        double price = 0.0;
        for (int i = 0; i < products.size(); i++) {
            price += products.get(i).getPrice() * CurrentCustomer.getShoppingCart().get(products.get(i).getId());
        }

        DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        df.setRoundingMode(RoundingMode.FLOOR);
        price = new Double(df.format(price));

        tfTotal.setText(price + "€");
    }
    
    private void openView(String view, MouseEvent event) throws IOException
    {
        CurrentCustomer.setPreviousScreen(VIEW_CART);
        
        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private BatoiLogicAddress showCheckoutAlert() throws MalformedURLException, XmlRpcException, Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Checkout the order");

        Label label = new Label("Choose the shipping and billing address: ");

        ComboBox<BatoiLogicAddress> cbAddress = new ComboBox<>();
        
        if (addressDB.findAllByPks(CurrentCustomer.getCurrentCustomer().getAddresses_id()) == null) return null;
        
        cbAddress.getItems().setAll(addressDB.findAllByPks(CurrentCustomer.getCurrentCustomer().getAddresses_id()));
        GridPane.setVgrow(cbAddress, Priority.ALWAYS);
        GridPane.setHgrow(cbAddress, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(new Label(), 0, 1);
        expContent.add(cbAddress, 0, 2);

        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
        
        if (alert.getResult() == ButtonType.OK)
            return cbAddress.getSelectionModel().getSelectedItem();
            
        else
            return null;
    }

    private void updateLines(BatoiLogicOrder order) throws Exception {
        List<Integer> ids = new ArrayList<>();
        for (BatoiLogicProduct product : products)
        {
            BatoiLogicOrderLine line = new BatoiLogicOrderLine(order, product, CurrentCustomer.getShoppingCart().get(product.getId()));
            
            int id = orderLineDB.insertRecoverId(line);
            ids.add(id);
            
            BatoiLogicInventory inventory = inventoryDB.findByProductId(product.getId());
            
            int newStock = inventory.getStock() - CurrentCustomer.getShoppingCart().get(product.getId());
            if (newStock < 0) {
                order.setInformation(WAITING);
                Tools.showSimpleError("Order", "Order has been confirmed but we haven't stock now");
                
                // DO ORDER SUPPLIER
                doOrderToProvider(product, CurrentCustomer.getShoppingCart().get(product.getId()));
            }
            else
                inventory.setStock(newStock);

            if(newStock==0)
                doOrderToProvider(product, MIN_STOCK);

            inventoryDB.update(inventory);
            order.setLines_id(ids);
        }
    }

    private void doOrderToProvider(BatoiLogicProduct product, int stock) throws Exception
    {
        BatoiLogicSupplierOrder orderToProvider = new BatoiLogicSupplierOrder();
        orderToProvider.setBatoiLogicProduct(product);
        orderToProvider.setQuantity(stock);
        orderToProvider.setRequestDate(Date.valueOf(LocalDate.now().toString()));

        BatoiLogicProvider provider = new ProviderDAO().findByPk(product.getBatoiLogicProductBatoiLogicProviderRels().get(0));
        orderToProvider.setBatoiLogicProvider(provider);

        new SupplierOrderDAO().insert(orderToProvider);
    }
}