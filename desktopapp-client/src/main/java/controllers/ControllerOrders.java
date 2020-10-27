package controllers;

import com.dam.group2.desktopapp.client.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import dao.CustomersDAO;
import dao.OrderDAO;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.BatoiLogicOrder;
import tools.BackgroundLoad;
import tools.Tools;

public class ControllerOrders implements Initializable
{
    @FXML private ImageView btnTracking;
    @FXML private ImageView btnBack;
    @FXML private ImageView btnHistoryOrPending;

    @FXML private Text textHistoryOrPending;
    @FXML private TableView<BatoiLogicOrder> tvOrders;
    @FXML private TableColumn<BatoiLogicOrder, String> cOrderNumber;
    @FXML private TableColumn<BatoiLogicOrder, String> cDate;
    @FXML private TableColumn<BatoiLogicOrder, Integer> cLines;
    @FXML private TableColumn<BatoiLogicOrder, String> cStatus;
    @FXML private ProgressBar pbLoad;
    @FXML private Text header;

    private static final String PENDING = "PENDING";
    private static final String HISTORY = "HISTORY";
    private String mode;

    private static final Image ORDERS_IMAGE = new Image(new File("src/main/resources/com/dam/group2/desktopapp/client/images/orders-white.png").toURI().toString());
    private static final Image HISTORY_IMAGE = new Image(new File("src/main/resources/com/dam/group2/desktopapp/client/images/history-white.png").toURI().toString());

    private static String VIEW_ORDERS = "ViewOrders";
    private static String VIEW_MAIN = "ViewMain";
    private static String VIEW_ORDER_DETAIL = "ViewOrderDetail";
    
    private static String HEADER_PENDING = "MY ORDERS - PENDING";
    private static String HEADER_HISTORY = "MY ORDERS - HISTORY";

    private OrderDAO ordersDB;
    private ObservableList<BatoiLogicOrder> orders;
    private ObservableList<BatoiLogicOrder> history;
    private BackgroundLoad loader;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        try
        {
            mode = PENDING;
            ordersDB = new OrderDAO();
            orders = FXCollections.observableArrayList();
            history = FXCollections.observableArrayList();

            // THREAD!!!
            CurrentCustomer.setUser(new CustomersDAO().findByPk(CurrentCustomer.getCurrentCustomer().getId()));
            if(CurrentCustomer.getCurrentCustomer().getOrders_id().size()>0)
            {
                loader = new BackgroundLoad(pbLoad, orders, history,tvOrders, ordersDB);
                loader.start();
            }
            else
                pbLoad.setVisible(false);

            setupTable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupTable()
    {
        tvOrders.setItems(orders);

        cOrderNumber.setCellValueFactory(new PropertyValueFactory<>("name"));
        cDate.setCellValueFactory(o -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy")
                .format(o.getValue().getDate())));
        cLines.setCellValueFactory(o -> new SimpleIntegerProperty(o.getValue().getLines_id().size()).asObject());
        cStatus.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getOrderStatus()));
    }

    @FXML
    public void openTracking(MouseEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog("Tracking number");
        dialog.setTitle("Track your order");
        dialog.setHeaderText("Track your order");
        dialog.setContentText("Please enter your tracking number:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try {
                BatoiLogicOrder order = ordersDB.findByName(result.get());
                ControllerOrdersDetail.order = order;
                openView(VIEW_ORDER_DETAIL, event);
            } catch (Exception ex) {
                Tools.showSimpleError("Incorrect tracking number", "Tracking Error");
            }
        }
    }

    @FXML
    public void openHistoryOrPending()
    {
        if (mode.equals(HISTORY))
        {
            mode = PENDING;
            textHistoryOrPending.setText(PENDING);
            btnHistoryOrPending.setImage(ORDERS_IMAGE);
            header.setText(HEADER_PENDING);
            tvOrders.setItems(orders);
        }
        else
        {
            mode = HISTORY;
            textHistoryOrPending.setText(HISTORY);
            btnHistoryOrPending.setImage(HISTORY_IMAGE);
            header.setText(HEADER_HISTORY);
            tvOrders.setItems(history);
        }

        tvOrders.refresh();
    }

    @FXML
    public void onTableClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2 && (tvOrders.getSelectionModel().getSelectedItem() != null)) {
            ControllerOrdersDetail.order = tvOrders.getSelectionModel().getSelectedItem();
            openView(VIEW_ORDER_DETAIL, event);
        }
    }

    @FXML
    public void back(MouseEvent event) throws IOException
    {
        openView(VIEW_MAIN, event);
    }

    private void openView(String view, MouseEvent event) throws IOException
    {
        if(loader!=null)
            loader.interrupt();
        CurrentCustomer.setPreviousScreen(VIEW_ORDERS);

        Scene scene = new Scene(App.loadFXML(view));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}