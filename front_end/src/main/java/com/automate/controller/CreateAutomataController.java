package com.automate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.automate.structure.State;
import com.automate.structure.StateType;
import com.utils.CircleTableCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CreateAutomataController implements Initializable{

    @FXML
    private HBox alphabetBox;

    @FXML
    private Pane automatonPane;

    @FXML
    private ComboBox<State> deleteStateComboBox;

    @FXML
    private ComboBox<?> deleteSymbolComboBox;

    @FXML
    private ComboBox<?> deleteTransitionComboBox;

    @FXML
    private TableColumn<State, Boolean> isFinalStateColumn;

    @FXML
    private TableColumn<State, Boolean> isInitialStateColumn;

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
    private TableView<State> statesTableView;

    @FXML
    private TableColumn<?, ?> transitionFromColumn;

    @FXML
    private TableColumn<?, ?> transitionInputColumn;

    @FXML
    private TableColumn<?, ?> transitionToColumn;

    @FXML
    private TableView<?> transitionsTableView;


    // Une liste observable d'états qui sera affichée dans le TableView
    private ObservableList<State> statesList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // stateNameColumn.setCellFactory(column -> new CircleTableCell<String>());


        // Configurer les colonnes du TableView
        stateNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        isInitialStateColumn.setCellValueFactory(new PropertyValueFactory<>("initial"));
        isFinalStateColumn.setCellValueFactory(new PropertyValueFactory<>("final"));

        // isInitialStateColumn = new TableColumn<>("Is Initial State");

        
        isInitialStateColumn.setCellFactory(column -> new TableCell<State, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    if (item.booleanValue()) {
                        Image checkmarkImage = new Image(getClass().getResource("/icon/checkmark.png").toString());
                        imageView.setImage(checkmarkImage);
                    } else {
                        Image crossImage = new Image(getClass().getResource("/icon/cross.png").toString());
                        imageView.setImage(crossImage);
                    }
                    setGraphic(imageView);
                }
            }
        });
        
        isFinalStateColumn.setCellFactory(column -> new TableCell<State, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    if (item.booleanValue()) {
                        Image checkmarkImage = new Image(getClass().getResource("/icon/checkmark.png").toString());
                        imageView.setImage(checkmarkImage);
                    } else {
                        Image crossImage = new Image(getClass().getResource("/icon/cross.png").toString());
                        imageView.setImage(crossImage);
                    }
                    setGraphic(imageView);
                }
            }
        });
        
        // Ajouter des états par défaut dans le TableView
        statesList.add(new State("State 1", StateType.NORMAL));
        statesList.add( new State("State 2", StateType.NORMAL));
        statesList.add( new State("State 2", StateType.NORMAL));

        // Lier la liste d'états au TableView
        statesTableView.setItems(statesList);
        deleteStateComboBox.setItems(statesList);
    }

    @FXML
    private void handleAddStateButtonClick(ActionEvent event) {
        // Récupérer le nom du nouvel état à partir du TextField
        String stateName = newStateNameTextField.getText();

        // Créer un nouvel état avec le nom spécifié et les valeurs par défaut
        State newState = new State(stateName, StateType.NORMAL);

        // Ajouter le nouvel état à la liste d'états
        statesList.add(newState);

        // Effacer le contenu du TextField
        newStateNameTextField.setText("");
    }

    @FXML
    private void handleDeleteStateButtonClick(ActionEvent event) {
        // Récupérer l'état sélectionné dans le ComboBox de suppression
        State stateToDelete = deleteStateComboBox.getValue();

        // Supprimer l'état de la liste d'états
        statesList.remove(stateToDelete);
    }



    @FXML
    void handleAddSymbolButtonClick(ActionEvent event) {

    }

    @FXML
    void handleAddTransitionButtonClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteSymbolButtonClick(ActionEvent event) {

    }

    @FXML
    void handleDeleteTransitionButtonClick(ActionEvent event) {

    }


    
}
