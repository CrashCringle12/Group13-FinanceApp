/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Alertmodel;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import Model.Money;

/**
 *
 * @author chees
 */
public class MainController implements Initializable
{

    @FXML
    private Label label;

    @FXML
    private Button buttonUpdate;

    @FXML
    void updateMoneyModel(ActionEvent event)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter id: ");
        int id = input.nextInt();
        System.out.println("Enter money added: ");
        double added = input.nextDouble();
        System.out.println("Enter money spent: ");
        double spent = input.nextDouble();
        
        Money moneymodel = new Money();
        
        moneymodel.setId(id);
        moneymodel.setAdded(added);
        moneymodel.setSpent(spent);
        update(moneymodel);
    }

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        
        Query query = manager.createNamedQuery("Money.findAll");
        List<Money> data = query.getResultList();
        for (Money m : data)
        {
            System.out.println(m.getId() + " " + m.getAdded() + " " + m.getSpent());
        }
    }

    private void update(Money moneymodel)
    {
        try
        {
        manager.getTransaction().begin();
        if(moneymodel.getId() !=null)
        {
            manager.persist(moneymodel);
            manager.getTransaction().commit();
            System.out.println(moneymodel.toString());
        }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
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
    void createAlertmodel(ActionEvent event) {
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
    void deleteAlertmodel(ActionEvent event) {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter ID");
        int id = scnr.nextInt();
        
        Alertmodel alert = readById(id);
        System.out.println("Deleting alert: " + id);
        deleteAlertmodel(alert);
        
    }

    @FXML
    void updateAlertmodel(ActionEvent event) {
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
    public void deleteAlertmodel(Alertmodel alert) {
        try {
            Alertmodel existingAlertmodel = manager.find(Alertmodel.class, alert.getId());

            // sanity check
            if (existingAlertmodel != null) {
                
                // begin transaction
                manager.getTransaction().begin();
                
                //remove alert
                manager.remove(existingAlertmodel);
                
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

            Alertmodel existingAlertmodel = manager.find(Alertmodel.class, model.getId());

            if (existingAlertmodel != null) {
                // begin transaction
                manager.getTransaction().begin();
                
                // update all atttributes
                existingAlertmodel.setSeverity(model.getSeverity());
                existingAlertmodel.setDescription(model.getDescription());
                existingAlertmodel.setAccountname(model.getAccountname());
                
                
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
        
        if (s != null)
            System.out.println(s.getId() + " " + s.getAccountname() + ": " + (s.getSeverity() ? "*Bad* " : "Moderate ") + s.getDate()
                    + "\n" + s.getDescription()); 
        return s;
        
    }

    EntityManager manager;
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        manager = (EntityManager) Persistence.createEntityManagerFactory("ModelsPU").createEntityManager();
    }
}
