package com.automate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.utils.CircleTableCell;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CreateAutomataController implements Initializable{

    @FXML
    private HBox alphabetBox;

    @FXML
    private Pane automatonPane;

    @FXML
    private ComboBox<?> deleteStateComboBox;

    @FXML
    private ComboBox<?> deleteSymbolComboBox;

    @FXML
    private ComboBox<?> deleteTransitionComboBox;

    @FXML
    private TableColumn<?, ?> isFinalStateColumn;

    @FXML
    private TableColumn<?, ?> isInitialStateColumn;

    @FXML
    private TextField newStateNameTextField;

    @FXML
    private TextField newSymbolTextField;

    @FXML
    private ComboBox<?> newTransitionFromComboBox;

    @FXML
    private ComboBox<?> newTransitionInputComboBox;

    @FXML
    private ComboBox<?> newTransitionToComboBox;

    @FXML
    private TableColumn<String , String> stateNameColumn;

    @FXML
    private TableView<?> statesTableView;

    @FXML
    private TableColumn<?, ?> transitionFromColumn;

    @FXML
    private TableColumn<?, ?> transitionInputColumn;

    @FXML
    private TableColumn<?, ?> transitionToColumn;

    @FXML
    private TableView<?> transitionsTableView;

    @FXML
    void handleAddStateButtonClick(ActionEvent event) {

    }

    @FXML
    void handleAddSymbolButtonClick(ActionEvent event) {

    }

    @FXML
    void handleAddTransitionButtonClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteStateButtonClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteSymbolButtonClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteTransitionButtonClick(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stateNameColumn.setCellFactory(column -> new CircleTableCell<String>());
    }

}
