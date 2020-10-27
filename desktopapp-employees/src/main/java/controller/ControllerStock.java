package controller;

import com.dam.group2.desktopapp.employees.App;
import dao.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import models.*;
import odooAPI.SimpleProductDAO;
import org.apache.xmlrpc.XmlRpcException;
import tools.SearchThread;
import tools.Tools;

public class ControllerStock implements Initializable, SearchThread.OnSearchComplete {

    @FXML private ImageView btnImport;
    @FXML private ImageView btnBack;
    @FXML private TextField text_search;
    @FXML private ImageView btnSearch;
    @FXML private TableView<BatoiLogicInventory> tvStockTable;
    @FXML private TableColumn<BatoiLogicInventory, ImageView> cImage;
    @FXML private TableColumn<BatoiLogicInventory, String> cName;
    @FXML private TableColumn<BatoiLogicInventory, String> cDescription;
    @FXML private TableColumn<BatoiLogicInventory, Integer> cStock;

    private SimpleProductDAO imagesDB;
    private static InventoryDao connectionInventory;
    private static ProviderDAO connectionProvider;
    private static ProductDao connectionProducts;
    private static CityDAO connectionCity;
    private static SupplierOrderDAO supplierOrderDAO;
    
    private static ObservableList<BatoiLogicInventory> listInventory;
    private static ObservableList<BatoiLogicInventory> orders;

    private PostalCodeDAO connectionPostalCode;
    private RelSupplierOrdersProductsDAO connectionRelProductsSupplierOrders;

    private SearchThread searchThread;
    private static final int DEFAULT_STOCK = 10;
    private static final double PERCENT = 0.30;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            imagesDB = new SimpleProductDAO();
            connectionInventory = new InventoryDao();
            connectionProducts = new ProductDao();
            connectionCity = new CityDAO();
            connectionProvider = new ProviderDAO();
            connectionPostalCode = new PostalCodeDAO();
            supplierOrderDAO = new SupplierOrderDAO();
            connectionRelProductsSupplierOrders = new RelSupplierOrdersProductsDAO();
            listInventory = FXCollections.observableArrayList();
            listInventory.addAll(connectionInventory.findAll());

            fullTable();
            viewTable();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    public void back(MouseEvent event) throws IOException
    {
        if(searchThread!=null)
            searchThread.interrupt();

        openView("ViewMain", event);
    }
    
    @FXML
    public void receiveOrder(MouseEvent event)
    {
        try {
            if (supplierOrderDAO.findAll().size() > 0)
            {
                BatoiLogicSupplierOrder supOrder = chooseSupplierOrder();
                if (supOrder != null)
                {
                    connectionInventory.close();
                    connectionInventory = new InventoryDao();
                    BatoiLogicInventory inventory =
                            connectionInventory.findByProductId(supOrder.getBatoiLogicProduct().getId());
                    inventory.setStock(inventory.getStock() + supOrder.getQuantity());

                    supplierOrderDAO.delete(supOrder);
                    if (connectionInventory.update(inventory)) Tools.showSucceed("Order received", "Order received succesful");
                    fullTable();
                }
            }
            else {
                Tools.showSimpleError("Provider Order", "There aren't provider orders");
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void importProducts(MouseEvent event) throws IOException
    {
        File fileToImport = App.fileChooser("Select the CSV file you want to import");

        if(fileToImport!=null)
        {
            String[] lines = readFile(fileToImport).split(System.lineSeparator());
            String name_file = fileToImport.getName().toLowerCase();

            if (name_file.contains("codigo")) importPostalCodes(lines);
            else if (name_file.contains("provedores")) importProviders(lines);
            else if (name_file.contains("proveer")) importProviderInfo(lines);
            else if (name_file.contains("tarifa"))
            {
                try
                {
                    BatoiLogicProvider provider = alertWithComboBox();
                    if(provider!=null)
                        importNewTaxes(lines, provider);
                    else
                        Tools.showSimpleError("ERROR","Operation Cancelled");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                if (name_file.contains("product"))
                    importNewProducts(lines);
                else
                    Tools.showSimpleError("Error","Unknown file");
            }
        }
    }

    private BatoiLogicProvider alertWithComboBox() throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("FIND");
        alert.setHeaderText("Choose the provider");

        Label label = new Label("List of Providers: ");

        ComboBox<BatoiLogicProvider> cbProviders = new ComboBox<>();

        cbProviders.getItems().setAll(connectionProvider.findAll());
        GridPane.setVgrow(cbProviders, Priority.ALWAYS);
        GridPane.setHgrow(cbProviders, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(new Label(), 0, 1);
        expContent.add(cbProviders, 0, 2);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK)
            return cbProviders.getSelectionModel().getSelectedItem();
        else
            return null;
    }

    private void importNewTaxes(String[] lines, BatoiLogicProvider provider) throws MalformedURLException, XmlRpcException
    {
        SimpleProductDAO simpleDao = new SimpleProductDAO();
        StringBuilder errors = new StringBuilder();
        String line_product;
        for (int i=1;i<lines.length;i++)
        {
            line_product = lines[i];
            try
            {
                String[] l = line_product.split(",");
                System.out.println(Arrays.toString(l));
                BatoiLogicProduct product = connectionProducts.findByPk(Integer.parseInt(l[0]));
                if(product!=null)
                {
                    List<Integer> ids = simpleDao.findProvidersByPk(product.getId());
                    if(ids.size()>0)
                    {
                        if(ids.contains(provider.getId()))
                        {
                            double price = Double.parseDouble(l[1]);
                            price = price + (price * PERCENT);
                            product.setPrice(BigDecimal.valueOf(price));
                            connectionProducts.update(product);
                        }
                        else
                            throw new Exception(provider.getName()+" --> Doesn't provide us "+product.getName());
                    }
                    else
                        throw new Exception("No Providers available for "+product.getName());
                }
                else
                    throw new Exception("This product doesn't exists "+l[0]);
            }
            catch (Exception e)
            {
                String error = e.toString();
                if(error.toLowerCase().contains("null"))
                    error = "Product with ID: "+line_product.split(",")[0]+" doesn't exists!";

                errors.append(error).append(System.lineSeparator())
                        .append(" ----->>> (").append(line_product).append(")");
            }
        }
        Tools.showSuperInfo("DONE","Importation new Taxes of "+provider.getName()
                ,"Errors bellow:",errors.toString());
    }

    private void importProviderInfo(String[] lines)
    {
        StringBuilder errors = new StringBuilder();
        for (String line_product : lines)
        {
            try
            {
                String[] l = line_product.split(",");

                BatoiLogicProductBatoiLogicProviderRel rel = new BatoiLogicProductBatoiLogicProviderRel();

                BatoiLogicProduct product = connectionProducts.findByPk(Integer.parseInt(l[0]));
                BatoiLogicProvider provider = connectionProvider.findByPk(Integer.parseInt(l[1]));

                BatoiLogicProductBatoiLogicProviderRelId id = new BatoiLogicProductBatoiLogicProviderRelId();
                id.setBatoiLogicProductId(product.getId());
                id.setBatoiLogicProviderId(provider.getId());

                rel.setId(id);
                rel.setBatoiLogicProduct(product);
                rel.setBatoiLogicProvider(provider);

                Set providerOfProduct = new HashSet<>();
                providerOfProduct.add(rel);
                product.setBatoiLogicProductBatoiLogicProviderRels(providerOfProduct);

                double price = Double.parseDouble(l[2]);
                price = price + (price * PERCENT);
                product.setPrice(BigDecimal.valueOf(price));

                connectionRelProductsSupplierOrders.insert(rel);
                connectionProducts.update(product);
            }
            catch (Exception e)
            {
                errors.append(line_product).append(System.lineSeparator()).append(" ").append(e.getMessage());
            }
        }

        Tools.showSuperInfo("DONE","Importation Providers Info","Errors bellow:",errors.toString());
    }

    private void importNewProducts(String[] lines)
    {
        StringBuilder errors = new StringBuilder();
        for (String line_product : lines)
        {
            try
            {
                BatoiLogicProduct product = new BatoiLogicProduct();
                product.setId(Integer.parseInt(line_product.split(",")[0]));
                product.setName(line_product.split(",")[1]);
                product.setPrice(BigDecimal.ONE);
                connectionProducts.insert(product);

                BatoiLogicInventory inventory = new BatoiLogicInventory();
                inventory.setBatoiLogicProduct(product);
                inventory.setLocation("In factory");
                inventory.setStock(DEFAULT_STOCK);
                connectionInventory.insert(inventory);
            }
            catch (Exception e)
            {
                errors.append(line_product).append(System.lineSeparator()).append(" ").append(e.getMessage());
            }
        }

        Tools.showSuperInfo("DONE","Importation Products","Errors bellow:",errors.toString());
        fullTable();
    }

    private void importPostalCodes(String[] lines)
    {
        StringBuilder errors = new StringBuilder();
        for (String line_product : lines)
        {
            try
            {
                String[] l = line_product.split("\";\"");
                int zip = Integer.parseInt(l[2].replace("\"", "").substring(0, 5));
                BatoiLogicPostalCode postalCode = new BatoiLogicPostalCode();
                postalCode.setName(zip);

                BatoiLogicCity city = connectionCity.findByName(l[1]);
                if(city==null)
                {
                    city = new BatoiLogicCity();
                    city.setName(l[1]);
                    city.setProvince(l[0]);
                    connectionCity.insert(city);
                }

                postalCode.setBatoiLogicCity(city);
                connectionPostalCode.insert(postalCode);
            }
            catch (Exception e)
            {
                errors.append(line_product).append(System.lineSeparator()).append(" ").append(e.getMessage());
            }
        }

        Tools.showSuperInfo("DONE","Importation Postal Codes","Errors bellow:",errors.toString());
    }

    private void importProviders(String[] lines)
    {
        StringBuilder errors = new StringBuilder();
        for (String line_product : lines)
        {
            try
            {
                String[] l = line_product.split(",");
                BatoiLogicProvider provider = new BatoiLogicProvider();
                provider.setId(Integer.parseInt(l[0]));
                provider.setCif("EMP"+l[0]);
                provider.setName(l[1]);
                provider.setTelephone(Integer.parseInt(l[7]
                        .replace("(", "")
                        .replace(")", "")
                        .replace("-", "")
                        .replace(" ", "")
                        .substring(3)));
                provider.setEmail("null".equals(l[9].toLowerCase())
                        ? "" : l[9]);

                connectionProvider.insert(provider);
            }
            catch (Exception e)
            {
                errors.append(line_product).append(System.lineSeparator()).append(" ").append(e.getMessage());
            }
        }

        Tools.showSuperInfo("DONE","Importation Completed","Errors bellow:",errors.toString());
    }

    private String readFile(File fileToImport) throws IOException
    {
        StringBuilder contentFile = new StringBuilder();
        try (BufferedReader readFile = new BufferedReader(new FileReader(fileToImport)))
        {
            String line;
            while ((line = readFile.readLine()) != null)
            {
                contentFile.append(line).append(System.lineSeparator());
            }
        }
        return contentFile.toString();
    }

    @FXML
    public void searchProduct_text()
    {
        if(searchThread!=null)
            searchThread.interrupt();

        searchThread = new SearchThread(this, listInventory,text_search.getText());
        searchThread.start();
    }

    @Override
    public synchronized void searchCompleted(ObservableList<BatoiLogicInventory> result)
    {
        // Java FX thread to update the GUI
        Platform.runLater(() -> {
            tvStockTable.setItems(result);
            tvStockTable.refresh();
        });
    }

    private void openView(String view, MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(App.loadFXML(view));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }

    private void fullTable()
    {
        try
        {
            listInventory.clear();
            listInventory.addAll(connectionInventory.findAll());

            SimpleProductDAO dao = new SimpleProductDAO();
            listInventory.forEach(i ->{
                try
                {
                    i.getBatoiLogicProduct().setRealImage(dao.findImageByPk(i.getBatoiLogicProduct().getId()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            orders = FXCollections.observableArrayList();
            orders.addAll(listInventory);
            tvStockTable.refresh();
            searchProduct_text();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void viewTable() {

        cImage.setCellValueFactory(o
                -> {
            ImageView iv = new ImageView();
            iv.setFitHeight(150);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            try
            {
                iv.setImage(o.getValue().getBatoiLogicProduct().getRealImage());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return new ObservableValue<>() {
                @Override
                public void addListener(ChangeListener<? super ImageView> changeListener) {
                }
                @Override
                public void removeListener(ChangeListener<? super ImageView> changeListener) {
                }
                @Override
                public void addListener(InvalidationListener invalidationListener) {
                }
                @Override
                public void removeListener(InvalidationListener invalidationListener) {
                }
                @Override
                public ImageView getValue() {
                    return iv;
                }

            };
        });

        cName.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicProduct().getName()));
        cDescription.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicProduct().getDescription()));
        cStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tvStockTable.setItems(orders);
    }
    
    private BatoiLogicSupplierOrder chooseSupplierOrder() throws Exception {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order");
        alert.setHeaderText("Receiving Supplier Order");

        Label label = new Label("Choose the order: ");

        ComboBox<BatoiLogicSupplierOrder> cbOrders = new ComboBox<>();
        cbOrders.getItems().setAll(supplierOrderDAO.findAll());
        GridPane.setVgrow(cbOrders, Priority.ALWAYS);
        GridPane.setHgrow(cbOrders, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(new Label(), 0, 1);
        expContent.add(cbOrders, 0, 2);

        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
        
        if (alert.getResult() == ButtonType.OK)
            return cbOrders.getSelectionModel().getSelectedItem();
        else
            return null;
    }
}