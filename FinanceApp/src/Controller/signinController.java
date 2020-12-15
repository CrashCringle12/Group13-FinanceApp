/**
 * Sample Skeleton for 'startView.fxml' Controller Class
 */
package Controller;

import Model.Alertmodel;
import Model.Users;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.eclipse.persistence.exceptions.DatabaseException;

public class signinController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="searchBy"
    private Text searchBy; // Value injected by FXMLLoader

    @FXML // fx:id="RegisterButton"
    private Button RegisterButton; // Value injected by FXMLLoader

    @FXML // fx:id="usernamebar"
    private TextField usernamebar; // Value injected by FXMLLoader

    @FXML // fx:id="passwordbar"
    private PasswordField passwordbar; // Value injected by FXMLLoader

    @FXML // fx:id="LoginButton"
    private Button LoginButton; // Value injected by FXMLLoader

    @FXML // fx:id="password2bar"
    private PasswordField password2bar; // Value injected by FXMLLoader

    @FXML // fx:id="emailbar"
    private TextField emailbar; // Value injected by FXMLLoader

    @FXML // fx:id="policyBox"
    private Pane policyBox; // Value injected by FXMLLoader
    
    public static String userName;
    
    private Scene scene;
    @FXML
    void login(ActionEvent event) {
        policyBox.setVisible(false);
        Users user = new Users(usernamebar.getText(), passwordbar.getText());
        scene = ((Node) event.getSource()).getScene();
        login(user);

    }
    
    void login(Users user) {
        Query query = manager.createNamedQuery("Users.findByUsername");
        query.setParameter("username", user.getUsername());
        Users s = null; 
        try {
           s = (Users) query.getSingleResult();
        } catch (NoResultException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Invalid Username or Password");
            alert.showAndWait(); // line 5
        } 

        if (s != null) {
            if (s.getPassword().equals(user.getPassword())) {
                try {
                    nextScreen(s);
                } catch (IOException ex) {
                    Logger.getLogger(signinController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    @FXML
    void nextScreen(Users user) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mainview.fxml"));

        Parent alertView = loader.load();

        Scene mainScene = new Scene(alertView);

        MainMenuController mainController = loader.getController();
        
        mainController.initData(user);


        mainController.setTheOleScene(scene);
        Stage stage = (Stage) scene.getWindow();

        stage.setScene(mainScene);
        stage.show();
    }
    
    Scene previousScene;
    public void setTheOleScene(Scene scene) {
        previousScene = scene;

    }
    @FXML
    void register(ActionEvent event) {
        if (policyBox.isVisible()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            if (passwordbar.getText().equals(password2bar.getText())) {
                if (validate(passwordbar.getText())) {
                    if (emailbar.getText().contains("@")) {
                        Users user = new Users(usernamebar.getText(), passwordbar.getText());
                        user.setEmail(emailbar.getText());
                        newRegistration(user);
                    } else {
                        alert.setTitle("Registration");
                        alert.setHeaderText("Email Validation Failed");
                        alert.setContentText("Invalid Email");
                        alert.showAndWait(); // line 5
                    }
                } else {
                    alert.setTitle("Registration");
                    alert.setHeaderText("Password Validation Failed");
                    alert.setContentText("Password does not conform to policy");
                    alert.showAndWait(); // line 5
                }
            } else {
                alert.setTitle("Registration");
                alert.setHeaderText("Password Validation Failed");
                alert.setContentText("Passwords Do Not Match");
                alert.showAndWait(); // line 5
            }
        }
        policyBox.setVisible(true);
 
    }

    // Create operation
    public void newRegistration(Users user) {
        try {
            // begin transaction
            manager.getTransaction().begin();

            // sanity check
            if (user != null) {

                // create User
                manager.persist(user);

                // end transaction
                manager.getTransaction().commit();

                System.out.println(user.getUsername() + " is created");
                login(user);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // Database manager
    EntityManager manager;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert searchBy != null : "fx:id=\"searchBy\" was not injected: check your FXML file 'startView.fxml'.";
        assert RegisterButton != null : "fx:id=\"RegisterButton\" was not injected: check your FXML file 'startView.fxml'.";
        assert usernamebar != null : "fx:id=\"usernamebar\" was not injected: check your FXML file 'startView.fxml'.";
        assert passwordbar != null : "fx:id=\"passwordbar\" was not injected: check your FXML file 'startView.fxml'.";
        assert LoginButton != null : "fx:id=\"LoginButton\" was not injected: check your FXML file 'startView.fxml'.";
        assert password2bar != null : "fx:id=\"password2bar\" was not injected: check your FXML file 'startView.fxml'.";
        assert emailbar != null : "fx:id=\"emailbar\" was not injected: check your FXML file 'startView.fxml'.";
        assert policyBox != null : "fx:id=\"policyBox\" was not injected: check your FXML file 'startView.fxml'.";
        manager = (EntityManager) Persistence.createEntityManagerFactory("LamarRussFXMLPU").createEntityManager();
        policyBox.setVisible(false);
    }

    public static boolean validate(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean foundUpper = false;
        boolean foundLower = false;
        boolean foundDigit = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (isLowerCase(ch)) {
                foundLower = true;
            }
            if (isUpperCase(ch)) {
                foundUpper = true;
            }
            if (isDigit(ch)) {
                foundDigit = true;
            }
        }
        return foundUpper && foundLower && foundDigit;
    }

    public static boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    public static boolean isLowerCase(char ch) {
        return 'a' <= ch && ch <= 'z';
    }

    public static boolean isUpperCase(char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

}
