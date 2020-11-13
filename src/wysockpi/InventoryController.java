package wysockpi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/**
 * FXML Controller class
 *
 * @author Piotr Wysocki 8/8/20
 */
public class InventoryController implements Initializable {

    @FXML
    TextField txtSell, txtAdd;
    @FXML
    ComboBox CookieBox;
    @FXML
    Button btnAdd, btnSell, btnExit;

    File file = new File("cookies.txt");

    Cookies cookie;

    CookieInventoryFile cookieFile;

    CookieInventoryItem cookieItem = null;

    ObservableList<Cookies> CookieO = FXCollections.observableArrayList(Cookies.values()); 
    

    /**
     * Initializes the controller class.
     * populates the combobox(CookieBox) with cookie enums
     * Contains the event handlers for the:
     * ComboBox - instantiates the cookie object
         * with the respective selection, given that the cookieItem is contained
         * within the CookieInventoryFile(cookieFile) object.
     * Add button - Adds quantity to the cookieItem
     * Sell button - Subtracts quantity from the cookieItem
     * Exit button - Receives confirmation input from the user
         * Writes the data in the CookieInventoryFile(cookieFile) object to
         * a text file(cookies.txt)
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Data Entry Error");
        a.setHeaderText(null);
                
        CookieBox.setItems(CookieO);

        if (file.exists()) {
            cookieFile = new CookieInventoryFile(file);
        } else {
            cookieFile = new CookieInventoryFile();
        }
        
        CookieBox.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                cookie = (Cookies) CookieBox.getSelectionModel().getSelectedItem();
                cookieItem = cookieFile.find(cookie.getId());
            }
        });
        
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (cookie != null) {
                    if (!txtAdd.getText().isBlank()) {
                        if (isNumeric(txtAdd.getText())) {
                            int n = Integer.parseInt(txtAdd.getText());
                            if (n > 0) {
                                if (cookieItem == null) {
                                    cookieItem = new CookieInventoryItem(cookie, n);
                                    cookieFile.add(cookieItem);
                                    txtAdd.clear();
                                }else{                         
                                cookieItem.setQuantity(cookieItem.getQuantity() + n);                           
                                txtAdd.clear();
                                }
                            }else{
                                txtAdd.selectAll();
                                txtAdd.requestFocus();
                                a.setContentText("You must enter a quantity "
                                        + "that is greater than 0.");
                                a.show();
                            }
                        }else{
                            txtAdd.selectAll();
                            txtAdd.requestFocus();
                            a.setContentText("You must enter a valid numeric value.");                            
                            a.show();
                        }    
                    } else {
                        txtAdd.requestFocus();
                        a.setContentText("Please enter the number of cookies added.");
                        a.show();
                    }
                } else {
                    CookieBox.requestFocus();
                    a.setContentText("Please select a cookie from the drop-down list.");
                    a.show();
                }    
            }
        });
        
        btnSell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {                             
                if (cookie != null) {
                    if (!txtSell.getText().isBlank()) {
                        if (cookieItem != null) {
                            if (isNumeric(txtSell.getText())) {
                                int n = Integer.parseInt(txtSell.getText());
                                if (n > 0) {
                                    if (n <= cookieItem.getQuantity()) {
                                        if (cookieItem.getQuantity() == n) {
                                            cookieFile.remove(cookieFile.find(cookie.getId())); 
                                            txtSell.clear();
                                        } else {
                                            cookieItem.setQuantity(cookieItem.getQuantity()
                                                    - Integer.parseInt(txtSell.getText()));
                                            txtSell.clear();
                                        }
                                    } else {
                                        txtSell.selectAll();
                                        txtSell.requestFocus();
                                        a.setContentText("Sorry, not enough "
                                                + cookie.getName() + " to sell."
                                                + "\nYou only have "
                                                + cookieItem.getQuantity()
                                                + " left.");
                                        a.show();
                                    }
                                } else {
                                    txtSell.selectAll();
                                    txtSell.requestFocus();
                                    a.setContentText("You must enter a quantity "
                                            + "that is greater than 0.");
                                    a.show();
                                }
                            } else {
                                txtSell.selectAll();
                                txtSell.requestFocus();
                                a.setContentText("You must enter a numeric "
                                        + "value.");
                                a.show();
                            }
                        }else{
                            txtSell.selectAll();
                            txtSell.requestFocus();
                            a.setContentText("Sorry, there are no "
                                    + cookie.getName() 
                                    + " cookies avilable to sell.");
                            a.show();
                        }
                    } else {
                        txtSell.requestFocus();
                        a.setContentText("Please enter the number of cookies sold.");
                        a.show();
                    }
                } else {
                    CookieBox.requestFocus();
                    a.setContentText("Please select a cookie from the drop-down list.");
                    a.show();
                }
            }
        });
        
        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you wish to exit?", ButtonType.YES,
                        ButtonType.NO);
                alert.setTitle("Exit Program");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.YES) {
                    try {
                        cookieFile.writeToFile(file);
                        System.exit(0);
                    } catch (IOException ex) {
                        System.out.println("File Not Found");
                    }
                }
            }
        });

    }

    /**
     * Method which checks whether a String contains a numeric value
     * 
     * @param s
     * @return true if the string contains a number, false if it does not
     */
    public static boolean isNumeric(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

}
