package com.automate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class TestController {

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
    private Region leftRegion;

    @FXML
    private TextField newStateNameTextField;

    @FXML
    private TextField newSymbolTextField;

    @FXML
    private TextField newTransitionFromTextField;

    @FXML
    private ComboBox<?> newTransitionInputComboBox;

    @FXML
    private TextField newTransitionToTextField;

    @FXML
    private Region rightRegion;

    @FXML
    private TableColumn<?, ?> stateNameColumn;

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


    private double leftRegionStartX;
    private double leftRegionStartWidth;
    private double rightRegionStartX;
    private double rightRegionStartWidth;

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

    @FXML
    private void handleLeftRegionDragged(MouseEvent event) {
        double newWidth = leftRegionStartWidth - (event.getX() - leftRegionStartX);
        double minWidth = automatonPane.getMinWidth();
        double maxWidth = automatonPane.getMaxWidth();
        newWidth = Math.max(newWidth, minWidth);
        newWidth = Math.min(newWidth, maxWidth);
        leftRegion.setPrefWidth(newWidth);
        automatonPane.setPrefWidth(automatonPane.getWidth() - (newWidth - leftRegionStartWidth));
        System.out.println("******************* drag left enter **************************");
    }

    @FXML
    private void handleLeftRegionPressed(MouseEvent event) {
        leftRegionStartX = event.getX();
        leftRegionStartWidth = leftRegion.getWidth();
        System.out.println("******************* press left enter **************************");
    }

   

    @FXML
    private void handleRightRegionPressed(MouseEvent event) {
        rightRegionStartX = event.getX();
        rightRegionStartWidth = rightRegion.getWidth();
        System.out.println("******************* press right enter **************************");
    }

    @FXML
    private void handleRightRegionDragged(MouseEvent event) {
        double newWidth = rightRegionStartWidth + (event.getX() - rightRegionStartX);
        double minWidth = automatonPane.getMinWidth();
        double maxWidth = automatonPane.getMaxWidth();
        newWidth = Math.max(newWidth, minWidth);
        newWidth = Math.min(newWidth, maxWidth);
        rightRegion.setPrefWidth(newWidth);
        automatonPane.setPrefWidth(automatonPane.getWidth() + (newWidth - rightRegionStartWidth));
        System.out.println("******************* drag right enter **************************");

    }


}
