package controllers;

import com.dam.group2.desktopapp.client.App;
import dao.InventoryDAO;
import dao.OrderDAO;
import dao.OrderLineDAO;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
import models.BatoiLogicOrder;
import models.BatoiLogicOrderLine;
import org.apache.xmlrpc.XmlRpcException;
import tools.CancelOrderThread;
import tools.Tools;

public class ControllerOrdersDetail implements Initializable, CancelOrderThread.OnCancelled {

    @FXML private ImageView btnPrint;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnCancel;

    @FXML private Text textHistoryOrPending;
    @FXML private Text textOrderNumber;
    @FXML private Text textDate;
    @FXML private Text textStatus;

    @FXML private TableView<BatoiLogicOrderLine> tvOrderLines;
    @FXML private TableColumn<BatoiLogicOrderLine, Integer> cLine;
    @FXML private TableColumn<BatoiLogicOrderLine, ImageView> cImage;
    @FXML private TableColumn<BatoiLogicOrderLine, String> cProductName;
    @FXML private TableColumn<BatoiLogicOrderLine, Integer> cQuantity;
    @FXML private TableColumn<BatoiLogicOrderLine, String> cPrice;

    @FXML private TextArea textAreaObservations;
    @FXML private Text textTotal;
    @FXML private ProgressBar pbLoad;
    
    private static String VIEW_ORDERS = "ViewOrders";
    private static String VIEW_PRODUCT = "ViewProduct";
    private static String VIEW_MAIN = "ViewMain";
    private static String VIEW_ORDER_DETAIL = "ViewOrderDetail";
    public static final String WORKING = "We are working on your order";
    private static final String WAITING = "We haven't stock for yout order now";

    private OrderDAO ordersDB;
    private OrderLineDAO orderLineDAO;
    private InventoryDAO inventoryDAO;
    private ObservableList<BatoiLogicOrderLine> orderLines;
    private Thread loader;

    public static BatoiLogicOrder order;

    private Alert waitDialog;
    private boolean close;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        textDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(order.getDate()));
        textOrderNumber.setText(order.getName());
        textStatus.setText(order.getOrderStatus());
        textAreaObservations.setText(order.getInformation() != null ? order.getInformation() : "");

        try
        {
            ordersDB = new OrderDAO();
            orderLineDAO = new OrderLineDAO();
            inventoryDAO = new InventoryDAO();
            orderLines = FXCollections.observableArrayList();

            // Simple Thread.
            loader = new Thread(() -> {

                try
                {
                    orderLines.addAll(orderLineDAO.findAllByPks(order.getLines_id(), pbLoad));

                    double price = 0.0;
                    for(int i=0;i<orderLines.size();i++)
                    {
                        BatoiLogicOrderLine line = orderLines.get(i);
                        price += line.getBatoiLogicProduct().getPrice() * line.getQuantity();
                    }

                    // Formatting double with only two decimals.
                    DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
                    df.setRoundingMode(RoundingMode.FLOOR);
                    price = new Double(df.format(price));
                    textTotal.setText(price+"€");

                    pbLoad.setVisible(false);
                }
                catch (Exception e)
                {
//                    System.out.println("Load Interrupted");
                }
            });
            loader.start();

            setupTable();

            if(order.getStatus().equals("3"))
                btnCancel.setDisable(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(MouseEvent event)
    {
        if(Tools.confirmationDialog("Are your sure about this?","If you press OK, your order will be deleted"
                ,"Canceling ORDER"))
        {
            try
            {
                waitDialog = Tools.showInfo("Processing","Please wait a moment...");
                close = false;
                waitDialog.setOnCloseRequest(c -> {
                    if(!close)
                        c.consume();
                });                                           // Do Nothing on Close Request.
                new CancelOrderThread(this, order, event).start();
                waitDialog.show();
            }
            catch (MalformedURLException | XmlRpcException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void cancelled(boolean result, MouseEvent event)
    {
        // Java FX Thread to interact with the GUI from an external thread
        Platform.runLater(() ->
        {
            if(result)
            {
                try
                {
                    Tools.showSucceed("Cancelled","This order has been cancelled successfully");
                    close = true;
                    waitDialog.close();
                    back(event);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
                Tools.showSimpleError("Error","Some error occur, order not cancelled");
        });
    }

    @FXML
    void print(MouseEvent event)
    {
        // TODO: JasperSoft !!
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        openView(VIEW_ORDERS, event);
    }
    
    @FXML
    public void onTableClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && (tvOrderLines.getSelectionModel().getSelectedItem() != null)) {
            ControllerProduct.product = tvOrderLines.getSelectionModel().getSelectedItem().getBatoiLogicProduct();
            openView(VIEW_PRODUCT, event);
        }
    }

    private void openView(String view, MouseEvent event) throws IOException
    {
        if(loader!=null)
            loader.interrupt();

        CurrentCustomer.setPreviousScreen(VIEW_ORDER_DETAIL);
        
        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setupTable() {
        tvOrderLines.setItems(orderLines);

        cLine.setCellValueFactory(new PropertyValueFactory<>("id"));
        cImage.setCellValueFactory(o ->
        {
            ImageView iv = new ImageView();

            iv.setFitHeight(150);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            iv.setImage(o.getValue().getBatoiLogicProduct().getImage());

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

        cProductName.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicProduct().getName()));
        cQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cPrice.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicProduct().getPrice() + "€"));
    }
}