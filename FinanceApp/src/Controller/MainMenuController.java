/**
 * Sample Skeleton for 'mainview.fxml' Controller Class
 */

package Controller;

import Model.Users;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="username"
    private Text username; // Value injected by FXMLLoader

    @FXML // fx:id="username1"
    private Text username1; // Value injected by FXMLLoader

    @FXML // fx:id="alertButton"
    private Button alertButton; // Value injected by FXMLLoader

    @FXML // fx:id="balanceButton"
    private Button balanceButton; // Value injected by FXMLLoader

    @FXML // fx:id="userButton"
    private Button userButton; // Value injected by FXMLLoader
    
    private Users user;
        private Scene scene;

    @FXML
    void toAlerts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AlertsView.fxml"));

        Parent alertView = loader.load();

        Scene mainScene = new Scene(alertView);

        AlertController alertController = loader.getController();


        alertController.setTheOleScene(((Node) event.getSource()).getScene());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(mainScene);
        stage.show();
    }

    @FXML
    void toBalances(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BalanceView.fxml"));

        Parent alertView = loader.load();

        Scene mainScene = new Scene(alertView);

        AlertController alertController = loader.getController();


        alertController.setTheOleScene(((Node) event.getSource()).getScene());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(mainScene);
        stage.show();
    }

    @FXML

    void toDetails(ActionEvent event) throws IOException {
        scene = ((Node) event.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/UserDetailsView.fxml"));

        Parent userView = loader.load();

        Scene mainScene = new Scene(userView);

        UserDetailsController userController = loader.getController();
        
        userController.initData(user);


        userController.setTheOleScene(scene);
        Stage stage = (Stage) scene.getWindow();

        stage.setScene(mainScene);
        stage.show();
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert username != null : "fx:id=\"username\" was not injected: check your FXML file 'mainview.fxml'.";
        assert username1 != null : "fx:id=\"username1\" was not injected: check your FXML file 'mainview.fxml'.";
        assert alertButton != null : "fx:id=\"alertButton\" was not injected: check your FXML file 'mainview.fxml'.";
        assert balanceButton != null : "fx:id=\"balanceButton\" was not injected: check your FXML file 'mainview.fxml'.";
        assert userButton != null : "fx:id=\"userButton\" was not injected: check your FXML file 'mainview.fxml'.";
    }
    
    Scene previousScene;
    public void setTheOleScene(Scene scene) {
        previousScene = scene;

    }
    
    
    //Borrowed from source code
    @FXML
    void goBack(ActionEvent event) {
        // option 1: get current stage -- from event
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (previousScene != null) {
            stage.setScene(previousScene);
        }

    }

    void initData(Users user) {
        this.user = user;
        username.setText(user.getUsername());
        System.out.println(user.getUsername());
    }
}
