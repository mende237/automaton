package com.automate.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ConvertController extends Controller implements Initializable {
    public static ConvertController convertController = null;
    @FXML
    private Button btnConvert;
    @FXML
    private Button btnSave;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ImageView imageViewData;
    @FXML
    private ImageView imageViewResult;

    @FXML
    private AnchorPane anchorPaneData;
    @FXML
    private AnchorPane anchorPaneResult;

    private ConvertController(Mediator mediator) {
        super(mediator);
    }

    public static ConvertController getConvertController(Mediator mediator) {
        if (ConvertController.convertController == null) {
            ConvertController.convertController = new ConvertController(mediator);
        }

        return ConvertController.convertController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("la fenetre convert a ete lance!!!!!");
        this.imageViewData.fitWidthProperty().bind(this.anchorPaneData.widthProperty());
        this.imageViewData.fitHeightProperty().bind(this.anchorPaneData.heightProperty());
        this.imageViewData.setPreserveRatio(true);

        this.imageViewResult.fitWidthProperty().bind(this.anchorPaneResult.widthProperty());
        this.imageViewResult.fitHeightProperty().bind(this.anchorPaneResult.heightProperty());
        this.imageViewResult.setPreserveRatio(true);

        // this.splitPane.setDividerPositions(0.45);
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(super.id);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        System.out.println(message);
        File file = new File(message.getContent());
        Image image = new Image(file.toURI().toString());
        this.imageViewData.setImage(image);
    }

}
