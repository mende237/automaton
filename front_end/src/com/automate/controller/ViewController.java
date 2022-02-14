package com.automate.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ViewController extends Controller implements Initializable {

    @FXML
    private Label name;
    @FXML
    private Label description;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane anchorPane;

    public ViewController(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        imageView.fitWidthProperty().bind(this.anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(this.anchorPane.heightProperty());
        imageView.setPreserveRatio(true);

    }

    private void centerImage() {
        Image img = this.imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = this.imageView.getFitWidth() / img.getWidth();
            double ratioY = this.imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            this.imageView.setX((imageView.getFitWidth() - w) / 2);
            this.imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(super.id);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        System.out.println(message);
        String tab[] = message.getContent().split(";");
        this.name.setText(tab[0]);
        this.description.setText(tab[1]);

        String pathImage = tab[2];

        File file = new File(pathImage);
        Image image = new Image(file.toURI().toString());
        this.imageView.setImage(image);

        //this.centerImage();
    }

}
