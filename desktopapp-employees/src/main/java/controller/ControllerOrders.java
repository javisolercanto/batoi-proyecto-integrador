package controller;

import com.dam.group2.desktopapp.employees.App;
import static com.dam.group2.desktopapp.employees.App.loadFXML;
import dao.BillDao;
import dao.DeliveryNoteDao;
import dao.OrdersDao;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import tools.Tools;

public class ControllerOrders
{
    @FXML private ImageView btnGenerateDelivery;
    @FXML private ImageView btnViewDelivery;
    @FXML private ImageView btnGenerateBills;
    @FXML private ImageView btnViewOrders;

    @FXML private TableView<BatoiLogicOrder> tableViewOrders;
    @FXML private TableColumn<BatoiLogicOrder, String> cDate;
    @FXML private TableColumn<BatoiLogicOrder, String> cAddress;
    @FXML private TableColumn<BatoiLogicOrder, String> cCustomer;
    @FXML private TableColumn<BatoiLogicOrder, String> cStatus;

    @FXML private TableView<BatoiLogicDeliveryNote> tableViewDeliveryNote;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cdDate;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cdAddress;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cdCustomer;
    @FXML private TableColumn<BatoiLogicDeliveryNote, String> cdStatus;

    @FXML private TableView<BatoiLogicBill> tableViewBills;
    @FXML private TableColumn<BatoiLogicBill, String> cbDate;
    @FXML private TableColumn<BatoiLogicBill, String> cbAddress;
    @FXML private TableColumn<BatoiLogicBill, String> cbCustomer;
    @FXML private TableColumn<BatoiLogicBill, String> cbStatus;

    @FXML private Text textOrders;
    @FXML private Text textDelivery;
    @FXML private Text txtChangeMode;
    @FXML private Text txtGenerate;
    @FXML private Text textBills;
    
    @FXML private CheckBox checkPending;

    static final int CODE_KEY_EVENT = 69;
    static final int SIZE_SELECTED = 1;
    public static BatoiLogicOrder order_to_show;

    private static OrdersDao connectionOrders;
    private static DeliveryNoteDao connectionDeliveryNote;
    private static BillDao connectionBill;

    private static ObservableList<BatoiLogicOrder> ordersList;
    private static ObservableList<BatoiLogicDeliveryNote> deliveryNotesList;
    private static ObservableList<BatoiLogicBill> billsList;

    private static final String ORDERS = "ORDERS";
    private static final String DELIVERY = "DELIVERY";
    private static String MODE = ORDERS;

    @FXML
    public void initialize() {

        try
        {
            connectionOrders = new OrdersDao();
            connectionDeliveryNote = new DeliveryNoteDao();
            connectionBill = new BillDao();

            // Observable list of tableViewOrder and tableViewDeliveryNote
            ordersList = FXCollections.observableArrayList();
            deliveryNotesList = FXCollections.observableArrayList();
            billsList = FXCollections.observableArrayList();

            btnGenerateDelivery.setDisable(true);
            btnGenerateBills.setDisable(true);

            // Add data to the tableViews
            addDataToObservableList();

            // Creates both tableViews
            setupTableViews();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error while showing orders " + ex.getMessage());
        }
    }

    private void setupTableViews()
    {
        // TableView Orders -->
        tableViewOrders.setItems(ordersList);

        cDate.setCellValueFactory(o -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy")
                .format(o.getValue().getDate())));
        cAddress.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicAddress().getName()));
        cCustomer.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicCustomer().getName()));
        cStatus.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getOrderStatus()));

        tableViewOrders.setOnMouseClicked(e -> {

            BatoiLogicOrder selected = tableViewOrders.getSelectionModel().getSelectedItem();
            if(selected!=null && selected.getBatoiLogicDeliveryNotes().isEmpty())
                btnGenerateDelivery.setDisable(false);
            else
                btnGenerateDelivery.setDisable(true);
        });

        // TableView DeliveryNotes -->
        tableViewDeliveryNote.setItems(deliveryNotesList);

        cdDate.setCellValueFactory(o -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy")
                .format(o.getValue().getBatoiLogicOrder().getDate())));
        cdAddress.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getBatoiLogicAddress().getName()));
        cdCustomer.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getBatoiLogicCustomer().getName()));
        cdStatus.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicOrder().getOrderStatus()));

        tableViewDeliveryNote.setOnMouseClicked(e -> {

            BatoiLogicDeliveryNote selected = tableViewDeliveryNote.getSelectionModel().getSelectedItem();
            if(selected!=null && selected.getBatoiLogicBills().isEmpty())
                btnGenerateBills.setDisable(false);
            else
                btnGenerateBills.setDisable(true);
        });

        // TableView Bills -->
        tableViewBills.setItems(billsList);

        cbDate.setCellValueFactory(o -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy")
                .format(o.getValue().getDate())));
        cbAddress.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicDeliveryNote()
                .getBatoiLogicOrder().getBatoiLogicAddress().getName()));
        cbCustomer.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicDeliveryNote()
                .getBatoiLogicOrder().getBatoiLogicCustomer().getName()));
        cbStatus.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getBatoiLogicDeliveryNote()
                .getBatoiLogicOrder().getOrderStatus()));

        tableViewBills.setOnMouseClicked(e -> {

            BatoiLogicBill selected = tableViewBills.getSelectionModel().getSelectedItem();
            if(selected!=null)
            {
                tableViewDeliveryNote.getSelectionModel().select(selected.getBatoiLogicDeliveryNote());
            }
        });
    }

    public void addDataToObservableList() throws Exception
    {
        connectionDeliveryNote.close();
        connectionOrders.close();
        connectionBill.close();

        connectionDeliveryNote = new DeliveryNoteDao();
        connectionBill = new BillDao();
        connectionOrders = new OrdersDao();

        ordersList.clear();
        deliveryNotesList.clear();
        billsList.clear();

        ordersList.addAll(connectionOrders.findAll());
        deliveryNotesList.addAll(connectionDeliveryNote.findAll());
        billsList.addAll(connectionBill.findAll());

        this.clickCheckPending();
    }

    @FXML
    public void generateDeliveryNote()
    {
        try
        {
            BatoiLogicOrder order = tableViewOrders.getSelectionModel().getSelectedItem();
            if(order!=null && Tools.confirmationDialog("Create a new Delivery Note",
                    "Are you sure?","CONFIRMATION"))
            {
                TextInputDialog dialog = Tools.confirmationDialog("INSERTING",
                        "This step is optional");
                Optional<String> result = dialog.showAndWait();

                BatoiLogicDeliveryNote deliveryNote = new BatoiLogicDeliveryNote();
                deliveryNote.setBatoiLogicOrder(order);
                deliveryNote.setNotes(result.orElse(""));

                if(connectionDeliveryNote.insert(deliveryNote))
                {
                    HashSet d = new HashSet();
                    d.add(deliveryNote);
                    order.setBatoiLogicDeliveryNotes(d);
                    Tools.showSucceed("Done","New Delivery Note!");

                    btnGenerateDelivery.setDisable(true);

                    this.addDataToObservableList();
                    tableViewDeliveryNote.refresh();
                    tableViewBills.refresh();
                }
                else
                    Tools.showSimpleError("Error","Delivery Note almost generated but failed");
            }
        }
        catch (Exception ex)
        {
            Tools.showSimpleError("Error","Delivery Note impossible to generate");
        }
    }

    @FXML
    private void generateBills()
    {
        try
        {
            BatoiLogicDeliveryNote deliveryNote = tableViewDeliveryNote.getSelectionModel().getSelectedItem();

            if(deliveryNote!=null && Tools.confirmationDialog("Are you sure?",
                    "You are about to generate a new BILL","CONFIRMATION"))
            {
                BatoiLogicBill bill = new BatoiLogicBill();
                bill.setBatoiLogicDeliveryNote(deliveryNote);
                bill.setBatoiLogicAddress(deliveryNote.getBatoiLogicOrder().getBatoiLogicAddress());
                bill.setDate(Date.valueOf(LocalDate.now()));

                if(connectionBill.insert(bill))
                    Tools.showSucceed("INSERTED","The new Bill was generated successfully");
                else
                    Tools.showSimpleError("ERROR", "Something went wrong");

                btnGenerateBills.setDisable(true);

                this.addDataToObservableList();
                tableViewDeliveryNote.refresh();
                tableViewBills.refresh();
            }
        }
        catch (Exception ex)
        {
            Tools.showExceptionMessage(ex, "Error inserting the new bill");
        }
    }
    
    @FXML
    public void clickCheckPending()
    {
        try
        {
           if (checkPending.isSelected())
           {
               if(MODE.equals(ORDERS))
                   ordersList.removeIf(order -> !order.getBatoiLogicDeliveryNotes().isEmpty());
               else
               {
                   if(MODE.equals(DELIVERY))
                       deliveryNotesList.removeIf(deliveryNote -> !deliveryNote.getBatoiLogicBills().isEmpty());
               }
           }
           else
           {
                ordersList.clear();
                ordersList.setAll(connectionOrders.findAll());
                deliveryNotesList.clear();
                deliveryNotesList.setAll(connectionDeliveryNote.findAll());
           }
        } catch (Exception ex) {
            Tools.showExceptionMessage(ex, "Error loading information");
        } 
        
    }

    public void showOrders(KeyEvent key_pressed)
    {
        if ((((int) key_pressed.getCode().getName().charAt(0)) == CODE_KEY_EVENT) && (tableViewOrders.getSelectionModel().getSelectedItems().size() == SIZE_SELECTED)) {
            order_to_show = tableViewOrders.getSelectionModel().getSelectedItems().get(0);
            try {
                order_to_show = tableViewOrders.getSelectionModel().getSelectedItems().get(0);
                openView("ViewOrderDetail");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showdeliveryNote(KeyEvent key_pressed) {
        if ((((int) key_pressed.getCode().getName().charAt(0)) == CODE_KEY_EVENT) && (tableViewDeliveryNote.getSelectionModel().getSelectedItems().size() == SIZE_SELECTED)) {
            order_to_show = tableViewDeliveryNote.getSelectionModel().getSelectedItems().get(0).getBatoiLogicOrder();
            try {
                order_to_show = tableViewDeliveryNote.getSelectionModel().getSelectedItems().get(0).getBatoiLogicOrder();
                openView("ViewOrderDetail");
            } catch (IOException ex) {

            }
        }
    }

    private void openView(String view) throws IOException {
        App.scene = new Scene(loadFXML(view));
        App.stage.setScene(App.scene);
        App.stage.show();

    }

    @FXML
    public void back(MouseEvent event) throws IOException {
        openView("ViewMain", event);
    }

    @FXML
    void changeView(MouseEvent event)
    {
        MODE = MODE.equals(ORDERS) ? DELIVERY : ORDERS;
        try
        {
            changeMode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void changeMode() throws Exception
    {

        if (MODE.equals(ORDERS)) {

            tableViewDeliveryNote.setVisible(false);
            tableViewOrders.setVisible(true);
            textOrders.setVisible(true);
            btnGenerateDelivery.setVisible(true);
            btnViewDelivery.setVisible(true);
            textDelivery.setVisible(false);
            btnGenerateBills.setVisible(false);
            btnViewOrders.setVisible(false);
            txtChangeMode.setText("VIEW DELIVERY NOTES");
            txtGenerate.setText("GENERATE DELIVERY NOTES");

            textBills.setVisible(false);
            tableViewBills.setVisible(false);
        } else {

            tableViewDeliveryNote.setVisible(true);
            tableViewOrders.setVisible(false);
            textDelivery.setVisible(true);
            btnGenerateBills.setVisible(true);
            btnViewOrders.setVisible(true);
            textOrders.setVisible(false);
            btnGenerateDelivery.setVisible(false);
            btnViewDelivery.setVisible(false);
            txtChangeMode.setText("\tVIEW ORDERS");
            txtGenerate.setText("\t  GENERATE BILLS");

            textBills.setVisible(true);
            tableViewBills.setVisible(true);
        }

        this.addDataToObservableList();
    }

    private void openView(String view, MouseEvent event) throws IOException {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(App.loadFXML(view));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }
}
