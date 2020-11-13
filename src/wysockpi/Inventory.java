package wysockpi;

import java.io.FileNotFoundException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * This program manages an inventory of cookies(CookieInventoryItem)
 * or any other item.
 * The program allows users to add items to the database 
 * and sell them as well.
 * 
 * @author Piotr Wysocki 8/8/20
 */
public class Inventory extends Application {

    /**
     * The main method 
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        stage.setTitle("Inventory");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
    
}
