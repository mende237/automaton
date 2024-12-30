package com.automate.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.automate.structure.StateType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class PopupController extends Controller implements Initializable{
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnConfirm;



    @FXML
    private ComboBox<StateType> stateTypeComboBox;

    protected static final String ID = "popupController";

    private static PopupController popupController = null;

    private PopupController(Mediator mediator) {
        super(ID, mediator);
        //TODO Auto-generated constructor stub
    }

    public static PopupController getPopupController(Mediator mediator){
        if(PopupController.popupController != null)
            return PopupController.popupController;
        
        return new PopupController(mediator);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.stateTypeComboBox.getItems().addAll(StateType.NORMAL, StateType.INITIAL , StateType.FINAL, StateType.FINAL_INITIAL , StateType.WELL);
    }

    @FXML
    void handleCancelButtonClicked(ActionEvent event) {
        Message message = new Message(CreateAutomatonController.ID, null);
        this.sendMessage(message);
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleConfirmButtonClicked(ActionEvent event) {
        StateType stateTypeSelected = this.stateTypeComboBox.getValue();
        HashMap<String, Object> contentMessageToSend = new HashMap<>();
        contentMessageToSend.put("stateType", stateTypeSelected);
        Message message = new Message(CreateAutomatonController.ID, contentMessageToSend);
        this.sendMessage(message);
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(ID);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveMessage'");
    }
}
