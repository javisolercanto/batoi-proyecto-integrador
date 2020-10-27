package tools;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools
{
    public static void showSimpleError(String title, String header)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();
    }

    public static void showExceptionMessage(Exception ex, String msg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de excepción");
        alert.setHeaderText(msg);
        alert.setContentText(ex.getMessage());

        StringBuilder exceptionText = new StringBuilder();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement ste : stackTrace) {
            exceptionText.append(ste.toString()).append(System.getProperty("line.separator"));
        }

        Label label = new Label("La traza de la excepción ha sido: ");

        TextArea textArea = new TextArea(exceptionText.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static void showSuperInfo(String title, String header, String content, String info)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        TextArea textArea = new TextArea(info);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        Label label = new Label("Details: ");
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public static String encryptThisSHA1(String password) throws NoSuchAlgorithmException
    {
        String result;

        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(password.getBytes(StandardCharsets.UTF_8));
        result = String.format("%040x", new BigInteger(1, digest.digest()));

        return result;
    }

    public static void showSucceed(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static Alert showInfo(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        return alert;
    }

    public static int confirmationDialog(String title)
    {
        String message = "Dou you want to disconnect or exit?";
        ButtonType btnExit = new ButtonType("Exit");
        ButtonType btnDisconnect = new ButtonType("Disconnect");
        Alert d = new Alert(Alert.AlertType.CONFIRMATION, message, btnDisconnect, btnExit, ButtonType.CANCEL);
        d.setTitle(title);
        d.showAndWait();

        int opcion;
        if (d.getResult() == btnDisconnect) {
            opcion = 0;

        } else if (d.getResult() == btnExit) {
            opcion = 1;

        } else {
            opcion = 2;
        }

        return opcion;
    }

    public static Object[] confirmationDialogCustom(String prompt, String title)
    {
        Object[] results = new Object[2];

        TextArea textArea = new TextArea();
        textArea.setPromptText(prompt);
        textArea.setEditable(true);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();

        results[0] = alert.getResult() == ButtonType.OK;
        results[1] = textArea.getText();

        return results;
    }

    public static boolean confirmationDialog(String header, String content, String title)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }
}