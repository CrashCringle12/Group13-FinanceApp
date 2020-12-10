/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Alertmodel;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Lamar Cooley
 */
public class AlertController implements Initializable {

    @FXML
    private Button button;

    @FXML
    private Text searchBy;

    @FXML
    private Button buttonCreate;

    @FXML
    private Button buttonUpdate;

    @FXML
    private TextField searchbar;

    @FXML
    private TableView<Alertmodel> alertTable;

    @FXML
    private TableColumn<Alertmodel, Integer> alertID;

    @FXML
    private TableColumn<Alertmodel, Boolean> severity;

    @FXML
    private TableColumn<Alertmodel, Date> date;

    @FXML
    private TableColumn<Alertmodel, String> description;

    @FXML
    private TableColumn<Alertmodel, String> accountName;

    @FXML
    private Button searchbutton;

    private ObservableList<Alertmodel> alertData;

    public void setTableData(List<Alertmodel> alertList) {

        alertData = FXCollections.observableArrayList();

        alertList.forEach(s -> {
            alertData.add(s);
        });

        alertTable.setItems(alertData);
        alertTable.refresh();
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
    void search(ActionEvent event) {
        System.out.println("Clicked");

        String name = searchbar.getText();
        System.out.println(name);
        List<Alertmodel> alerts = readByAccountname(name);

        if (alerts == null || alerts.isEmpty()) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog Box");
            alert.setHeaderText("Lamar's Finance App Header");
            alert.setContentText("No alert found");
            alert.showAndWait(); // line 5
        } else {

            setTableData(alerts);
        }

    }

    @FXML
    void showDetails(ActionEvent event) throws IOException {

        Alertmodel selected = alertTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AlertsDetailView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        DetailedAlertController detailedController = loader.getController();
        detailedController.initData(selected);

        Stage stage = new Stage();
        stage.setScene(tableViewScene);
        stage.show();

    }

    @FXML
    void showDetailsInPane(ActionEvent event) throws IOException {

        Alertmodel selectedAlert = alertTable.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AlertsDetailView.fxml"));

        Parent detailedModelView = loader.load();

        Scene tableViewScene = new Scene(detailedModelView);

        DetailedAlertController detailedController = loader.getController();

        detailedController.initData(selectedAlert);

        Scene currentScene = ((Node) event.getSource()).getScene();
        detailedController.setTheOleScene(currentScene);

        Stage stage = (Stage) currentScene.getWindow();

        stage.setScene(tableViewScene);
        stage.show();
    }

    @FXML
    void advancedSearch(ActionEvent event) {
        System.out.println("Clicked");

        String name = searchbar.getText();
        System.out.println(name);
        List<Alertmodel> alerts = readByAccountNameAdvanced(name);

        if (alerts == null || alerts.isEmpty()) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog Box");
            alert.setHeaderText("Lamar's Finance App Header");
            alert.setContentText("No alert found");
            alert.showAndWait();
        } else {

            setTableData(alerts);
        }

    }

    @FXML
    public void readAll(ActionEvent event) {

        Query query = manager.createNamedQuery("Alertmodel.findAll");
        List<Alertmodel> data = query.getResultList();

        for (Alertmodel s : data) {
            System.out.println(s.getId() + " " + s.getAccountname() + ": " + (s.getSeverity() ? "*Bad* " : "Moderate ") + s.getDate()
                    + "\n" + s.getDescription());
        }
    }

    @FXML
    void createAlert(ActionEvent event) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please Enter ID");
        int id = scnr.nextInt();

        System.out.println("Is this alert Severe? Y/N");
        boolean severe = scnr.next().toUpperCase().contains("Y");
        
        scnr.nextLine();
        System.out.println("Please enter a short desc of the alert");
        String desc = scnr.nextLine();

        System.out.println("Please enter the Account name");
        String name = scnr.next();

        Alertmodel alert = new Alertmodel();

        alert.setAccountname(name);

        alert.setSeverity(severe);

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

    public List<Alertmodel> readByAccountname(String name) {
        Query query = manager.createNamedQuery("Alertmodel.findByAccountname");
        query.setParameter("accountname", name);

        List<Alertmodel> alerts = query.getResultList();
        for (Alertmodel s : alerts) {
            if (s != null) {
                System.out.println(s.getId() + " " + s.getAccountname() + ": " + (s.getSeverity() ? "*Bad* " : "Moderate ") + s.getDate()
                        + "\n" + s.getDescription());
            }
        }
        return alerts;

    }

    public List<Alertmodel> readByAccountNameAdvanced(String name) {
        Query query = manager.createNamedQuery("Alertmodel.findByAccountnameAdvanced");

        query.setParameter("accountname", name);

        List<Alertmodel> alerts = query.getResultList();
        for (Alertmodel element : alerts) {
            System.out.println(element.getId() + " " + element.getAccountname() + " " + (element.getSeverity() ? "Bad" : "Moderate") + "\n"
                    + element.getDescription());
        }

        return alerts;
    }

    // Database manager
    EntityManager manager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // loading data from database
        manager = (EntityManager) Persistence.createEntityManagerFactory("LamarRussFXMLPU").createEntityManager();
        alertID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        severity.setCellValueFactory(new PropertyValueFactory<>("Severity"));
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        accountName.setCellValueFactory(new PropertyValueFactory<>("Accountname"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        alertTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

}
