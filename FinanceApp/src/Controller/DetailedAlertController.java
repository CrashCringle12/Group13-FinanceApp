package Controller;

import Model.Alertmodel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class DetailedAlertController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="alertID"
    private Text alertID; // Value injected by FXMLLoader

    @FXML // fx:id="description"
    private TextArea description; // Value injected by FXMLLoader

    @FXML // fx:id="date"
    private Text date; // Value injected by FXMLLoader

    @FXML // fx:id="severity"
    private Text severity; // Value injected by FXMLLoader

    @FXML // fx:id="accountName"
    private Text accountName; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    @FXML // fx:id="button"
    private Button button; // Value injected by FXMLLoader
    Alertmodel selectedModel;

    public void initData(Alertmodel model) {
        System.out.println("TEst");
        selectedModel = model;
        alertID.setText(model.getId().toString());
        accountName.setText("Account: " + model.getAccountname());
        severity.setText("Severity: " + (model.getSeverity() ? "Bad" : "Moderate"));
        date.setText("Datse: " + model.getDate().toString());
        date.setWrappingWidth(500);
        alertID.setWrappingWidth(200);
        accountName.setWrappingWidth(500);
        description.setText(model.getDescription());
        try {

            String imagename = "/resources/images/icon.png";
            Image profile = new Image(getClass().getResourceAsStream(imagename));
            image.setImage(profile);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
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

    @FXML
    void createAlert(ActionEvent event) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please Enter ID");
        int id = scnr.nextInt();

        System.out.println("Is this alert Severe? Y/N");
        boolean severity = scnr.next().toUpperCase().contains("Y");

        System.out.println("Please enter a short desc of the alert");
        String desc = scnr.nextLine();

        System.out.println("Please enter the Account name");
        String name = scnr.next();

        Alertmodel alert = new Alertmodel();

        alert.setAccountname(name);

        alert.setSeverity(severity);

        alert.setId(id);

        alert.setDescription(desc);
        create(alert);
    }

    @FXML
    void deleteAlert(ActionEvent event) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter ID");
        int id = scnr.nextInt();

        Alertmodel alert = readById(id);
        System.out.println("Deleting alert: " + id);
        deleteAlert(alert);

    }

    public Alertmodel readById(int id) {
        Query query = manager.createNamedQuery("Alertmodel.findById");
        query.setParameter("ID", id);

        Alertmodel s = (Alertmodel) query.getSingleResult();

        if (s != null) {
            System.out.println(s.getId() + " " + s.getAccountname() + ": " + (s.getSeverity() ? "*Bad* " : "Moderate ") + s.getDate()
                    + "\n" + s.getDescription());
        }
        return s;

    }

    @FXML
    void updateAlert(ActionEvent event) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please Enter ID");
        int id = scnr.nextInt();

        System.out.println("Is this alert Severe? Y/N");
        boolean severity = scnr.next().toUpperCase().contains("Y");

        System.out.println("Please enter a short desc of the alert");
        String desc = scnr.nextLine();

        System.out.println("Please enter the Account name");
        String name = scnr.next();

        Alertmodel alert = new Alertmodel();

        alert.setAccountname(name);

        alert.setSeverity(severity);

        alert.setId(id);

        alert.setDescription(desc);
        update(alert);
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

    //The below methods (delete, create, read, and modify were provided in demo
    @FXML
    void deleteAlert(Alertmodel alert) {
        try {
            Alertmodel existingAlert = manager.find(Alertmodel.class, alert.getId());

            // sanity check
            if (existingAlert != null) {

                // begin transaction
                manager.getTransaction().begin();

                //remove alert
                manager.remove(existingAlert);

                // end transaction
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    // Update operation
    public void update(Alertmodel model) {
        try {

            Alertmodel existingAlert = manager.find(Alertmodel.class, model.getId());

            if (existingAlert != null) {
                // begin transaction
                manager.getTransaction().begin();

                // update all atttributes
                existingAlert.setSeverity(model.getSeverity());
                existingAlert.setDescription(model.getDescription());
                existingAlert.setAccountname(model.getAccountname());

                // end transaction
                manager.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Create operation
    public void create(Alertmodel alert) {
        try {
            // begin transaction
            manager.getTransaction().begin();

            // sanity check
            if (alert != null) {

                // create student
                manager.persist(alert);

                // end transaction
                manager.getTransaction().commit();

                System.out.println(alert.toString() + " is created");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    // Database manager
    EntityManager manager;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        manager = (EntityManager) Persistence.createEntityManagerFactory("LamarRussFXMLPU").createEntityManager();
        assert alertID != null : "fx:id=\"alertID\" was not injected: check your FXML file 'mainmenu.fxml'.";
        assert description != null : "fx:id=\"description\" was not injected: check your FXML file 'mainmenu.fxml'.";
        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'mainmenu.fxml'.";
        assert severity != null : "fx:id=\"severity\" was not injected: check your FXML file 'mainmenu.fxml'.";
        assert accountName != null : "fx:id=\"accountName\" was not injected: check your FXML file 'mainmenu.fxml'.";
        assert image != null : "fx:id=\"image\" was not injected: check your FXML file 'mainmenu.fxml'.";

    }
}
