package controller;

import com.dam.group2.desktopapp.employees.App;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.BatoiLogicOrder;
import models.BatoiLogicOrderLine;
import models.BatoiLogicProduct;

public class ControllerOrdersDetail {

    @FXML
    private ImageView btnPrint;

    @FXML
    private ImageView btnBack;

    @FXML
    private Text textHistoryOrPending;

    @FXML
    private ImageView btnGenerate;

    @FXML
    private Text textOrderNumber;

    @FXML
    private Text textDate;

    @FXML
    private Text textStatus;

    @FXML
    private TableView<BatoiLogicOrderLine> tvOrderLines;

    @FXML
    private TableColumn<BatoiLogicOrderLine, Integer> cLine;

    @FXML
    private TableColumn<BatoiLogicOrderLine, ImageView> cImage;

    @FXML
    private TableColumn<BatoiLogicOrderLine, BatoiLogicProduct> cProductName;

    @FXML
    private TableColumn<BatoiLogicOrderLine, Integer> cQuantity;

    @FXML
    private TableColumn<BatoiLogicOrderLine, Double> cPrice;

    @FXML
    private TextArea textAreaObservations;

    @FXML
    private Text textTotal;

    @FXML
    public void initialize() {

        BatoiLogicOrder order = ControllerOrders.order_to_show;
        textAreaObservations.setText(order.getInformation());

        try {
            textStatus.setText(order.getOrderStatus());
            textDate.setText(order.getDate().getDate() + "-" + (order.getDate().getMonth() + 1) + "-" + (order.getDate().getYear() + 1900));
            textOrderNumber.setText(order.getName());
            double total_order = 0.0;
            for (Iterator it = order.getBatoiLogicOrderLines().iterator(); it.hasNext();) {
                BatoiLogicOrderLine order_line = (BatoiLogicOrderLine) it.next();
                total_order += ((double) order_line.getQuantity() * order_line.getBatoiLogicProduct().getPrice().doubleValue());
            }
            textTotal.setText(total_order + "€");
            full_table(order.getBatoiLogicOrderLines());

        } catch (Exception exc) {

            textStatus.setText("NO STATUS");
        }
    }

    @FXML
    public void back(MouseEvent event) throws IOException {
        openView("ViewOrders", event);
    }

    private void openView(String view, MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(App.loadFXML(view));
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    void onTableClicked(MouseEvent event) {

    }

    @FXML
    void print(MouseEvent event) {

    }

    private void full_table(Set batoiLogicOrderLines) {
        tvOrderLines.getColumns().clear();
        tvOrderLines.getItems().clear();

        cLine = new TableColumn<>("Line of order");
        cLine.setCellValueFactory(new PropertyValueFactory<>("id"));
        cLine.setMinWidth(50);

        cProductName = new TableColumn<>("Product");
        cProductName.setCellValueFactory(new PropertyValueFactory<>("batoiLogicProduct"));
        cProductName.setMinWidth(50);

        tvOrderLines.getColumns().addAll(cLine, cProductName);
        tvOrderLines.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<BatoiLogicOrderLine> orderToShow = FXCollections.observableArrayList();
        orderToShow.addAll(batoiLogicOrderLines);
        tvOrderLines.setItems(orderToShow);
    }
}