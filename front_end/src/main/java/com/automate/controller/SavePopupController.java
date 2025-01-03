package com.automate.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SavePopupController extends Controller implements Initializable{
    protected static final String ID = "savePopupController";

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField nameField;

    private String callingControllerID;


    // private Message response = null;


    private SavePopupController(Mediator mediator, String callingControllerID) {
        super(ID, mediator);
        this.callingControllerID = callingControllerID;
    }

    private static SavePopupController savePopupController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public static SavePopupController getSavePopupController(Mediator mediator, String callingControllerID){
        if(SavePopupController.savePopupController != null){
            SavePopupController.savePopupController.callingControllerID = callingControllerID;
            return SavePopupController.savePopupController;
        }
        
        return new SavePopupController(mediator, callingControllerID);
    }

    @FXML
    private void handleCancelButtonClicked(ActionEvent event) {
        Message message = new Message(callingControllerID, null);
        this.sendMessage(message);
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleConfirmButtonClicked(ActionEvent event) {
        System.out.println("******************* enter calling " + callingControllerID + " *********************");
        if(nameField.getText().length() > 0){
            System.out.println("******************* enter " + nameField.getText() +" *********************");
            HashMap<String , String> content = new HashMap<>();
            
            content.put("name", nameField.getText());
            content.put("description", descriptionArea.getText());

            Message message = new Message(callingControllerID, content);
            this.sendMessage(message);
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
            this.nameField.setText("");
            this.descriptionArea.setText("");
        }else{
            this.lblErrorMessage.setText("Name field can't be empty");
        }
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(ID);
        super.mediator.transmitMessage(message);    
    }

    @Override
    public void receiveMessage(Message message) {
       
    }

    public void setTextNameField(String name){
        this.nameField.setText(name);
    }
    
    public void setTextDescriptionArea(String description){
        this.descriptionArea.setText(description);
    }

    public void setLblErrorMessage(String errorMessage){
        this.lblErrorMessage.setText(errorMessage);
    }
}
