package com.automate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ViewController implements Initializable{
    @FXML private Label name;
    @FXML private Label description;
    @FXML private ImageView imageView;
    @FXML private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("autre controlleur");
        // imageView.fitWidthProperty().bind(this.anchorPane.widthProperty());
        // imageView.fitHeightProperty().bind(this.anchorPane.heightProperty());
        // imageView.setPreserveRatio(true);

    }


}
