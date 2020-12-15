package Controller;

import static Controller.signinController.validate;
import Model.Users;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class UserDetailsController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="buttonUpdate"
    private Button buttonUpdate; // Value injected by FXMLLoader

    @FXML // fx:id="buttonDelete"
    private Button buttonDelete; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameText"
    private Text firstNameText; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameText"
    private Text lastNameText; // Value injected by FXMLLoader

    @FXML // fx:id="ageText"
    private Text ageText; // Value injected by FXMLLoader

    @FXML // fx:id="emailText"
    private Text emailText; // Value injected by FXMLLoader

    @FXML // fx:id="usernameText"
    private Text usernameText; // Value injected by FXMLLoader

    @FXML // fx:id="passwordText"
    private Text passwordText; // Value injected by FXMLLoader

    @FXML // fx:id="addressText"
    private Text addressText; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameBox"
    private TextField firstNameBox; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameBox"
    private TextField lastNameBox; // Value injected by FXMLLoader

    @FXML // fx:id="ageBox"
    private TextField ageBox; // Value injected by FXMLLoader

    @FXML // fx:id="addressBox"
    private TextField addressBox; // Value injected by FXMLLoader

    @FXML // fx:id="emailBox"
    private TextField emailBox; // Value injected by FXMLLoader

    @FXML // fx:id="usernameBox"
    private TextField usernameBox; // Value injected by FXMLLoader

    @FXML // fx:id="passwordBox"
    private PasswordField passwordBox; // Value injected by FXMLLoader

    @FXML // fx:id="buttonAlert"
    private Button buttonAlert; // Value injected by FXMLLoader

    @FXML // fx:id="button"
    private Button button; // Value injected by FXMLLoader

    @FXML // fx:id="backbutton"
    private Button backbutton; // Value injected by FXMLLoader

    Scene previousScene;

    private Users user;

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

    @FXML
    void logOut(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/startView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        signinController signInController = loader.getController();

        Scene currentScene = ((Node) event.getSource()).getScene();
        signInController.setTheOleScene(currentScene);

        Stage stage = (Stage) currentScene.getWindow();

        stage.setScene(tableViewScene);
        stage.show();

    }

    @FXML
    void toAlerts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AlertsView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        AlertController alertController = loader.getController();

        Scene currentScene = ((Node) event.getSource()).getScene();
        alertController.setTheOleScene(currentScene);

        Stage stage = (Stage) currentScene.getWindow();

        stage.setScene(tableViewScene);
        stage.show();
    }

    @FXML
    void updateDetails(ActionEvent event) {
        List<TextField> fields = Arrays.asList(firstNameBox, lastNameBox, ageBox, addressBox, emailBox, usernameBox, passwordBox);
        boolean done = false;
        for (TextField field : fields) {
            if (field.getText() == null) {
                done = true;
            }
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("User Details Update");
        if (!done && validate(passwordBox.getText())) {
            if (emailBox.getText().contains("@")) {
                Users usah = new Users(usernameBox.getText(), passwordBox.getText());
                usah.setEmail(emailBox.getText());
                usah.setFirstname(firstNameBox.getText());
                usah.setLastname(lastNameBox.getText());
                usah.setAge(Integer.parseInt(ageBox.getText()));
                usah.setAddress(addressBox.getText());
                usah.setPassword(passwordBox.getText());
                update(usah);
            } else {
                alert.setHeaderText("Email Validation Failed");
                alert.setContentText("Invalid Email");
                alert.showAndWait(); // line 5
            }
        } else {
            alert.setHeaderText("Invalid Entry");
            alert.setContentText("Not all Fields were filled in or invalid password");
            alert.showAndWait();
        }

    }

    @FXML
    void deleteAccount(ActionEvent event) {
        if (usernameBox.getText() != null) {
            try {
                Users existingUser = manager.find(Users.class, usernameBox.getText());

                // sanity check
                if (existingUser != null) {

                    // begin transaction
                    manager.getTransaction().begin();

                    //remove alert
                    manager.remove(existingUser);

                    // end transaction
                    manager.getTransaction().commit();
                    logOut(event);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    void initData(Users user) {
        this.user = user;
        firstNameText.setText(user.getFirstname());
        lastNameText.setText(user.getLastname());
        ageText.setText(user.getAge().toString());
        addressText.setText(user.getAddress());
        usernameText.setText(user.getUsername());
        passwordText.setText(user.getPassword());
        emailText.setText(user.getEmail());

    }

    // Database manager
    EntityManager manager;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        manager = (EntityManager) Persistence.createEntityManagerFactory("LamarRussFXMLPU").createEntityManager();
        assert buttonUpdate != null : "fx:id=\"buttonUpdate\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert buttonDelete != null : "fx:id=\"buttonDelete\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert firstNameText != null : "fx:id=\"firstNameText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert lastNameText != null : "fx:id=\"lastNameText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert ageText != null : "fx:id=\"ageText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert emailText != null : "fx:id=\"emailText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert usernameText != null : "fx:id=\"usernameText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert passwordText != null : "fx:id=\"passwordText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert addressText != null : "fx:id=\"addressText\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert firstNameBox != null : "fx:id=\"firstNameBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert lastNameBox != null : "fx:id=\"lastNameBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert ageBox != null : "fx:id=\"ageBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert addressBox != null : "fx:id=\"addressBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert emailBox != null : "fx:id=\"emailBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert usernameBox != null : "fx:id=\"usernameBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert passwordBox != null : "fx:id=\"passwordBox\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert buttonAlert != null : "fx:id=\"buttonAlert\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert button != null : "fx:id=\"button\" was not injected: check your FXML file 'UserDetailsView.fxml'.";
        assert backbutton != null : "fx:id=\"backbutton\" was not injected: check your FXML file 'UserDetailsView.fxml'.";

    }

    // Update operation
    public void update(Users model) {
        try {

            Users existingUser = manager.find(Users.class, model.getUsername());

            if (existingUser != null) {
                // begin transaction
                manager.getTransaction().begin();

                // update all atttributes
                existingUser.setFirstname(model.getFirstname());
                existingUser.setLastname(model.getLastname());
                existingUser.setAge(model.getAge());
                existingUser.setAddress(model.getAddress());
                existingUser.setEmail(model.getEmail());
                existingUser.setUsername(model.getUsername());
                existingUser.setPassword(model.getPassword());

                // end transaction
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Create operation
    public void create(Users user) {
        try {
            // begin transaction
            manager.getTransaction().begin();

            // sanity check
            if (user != null) {

                // create student
                manager.persist(user);

                // end transaction
                manager.getTransaction().commit();

                System.out.println(user.toString() + " is created");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
