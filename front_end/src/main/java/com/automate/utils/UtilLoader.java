package com.automate.utils;

import java.io.IOException;

import com.automate.controller.ConrceteMadiator;
import com.automate.controller.PopupController;
import com.automate.controller.SavePopupController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UtilLoader {

    public static String dialogBoxViewPath = "/window/dialogBox.fxml";
    public static String savePopupViewPath = "/window/savePopup.fxml";
    public static String dialogCssPath = "/style/diaglogstyle.css";
    public static String savePopupCssPath = "/style/savePopup.css";


    public static void showDialog() throws IOException{
        FXMLLoader loader = new FXMLLoader(UtilLoader.class.getResource(dialogBoxViewPath));
        loader.setControllerFactory(c -> {
            return PopupController.getPopupController(ConrceteMadiator.getConrceteMadiator());
        });

        BorderPane popup = loader.load();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(popup);
        scene.getStylesheets().add(UtilLoader.class.getResource(dialogCssPath).toExternalForm());
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    public static void showPopupSave(String name , String description , String errorLabel, String callingControllerID) throws IOException{
        FXMLLoader loader = new FXMLLoader(UtilLoader.class.getResource(savePopupViewPath));
        SavePopupController savePopupController = SavePopupController.getSavePopupController(ConrceteMadiator.getConrceteMadiator(), callingControllerID);
        loader.setControllerFactory(c -> {
            return savePopupController;
        });

        AnchorPane popupSave = loader.load();
        savePopupController.setTextNameField(name);
        savePopupController.setTextDescriptionArea(description);
        savePopupController.setLblErrorMessage(errorLabel);

        Stage popupSaveStage = new Stage();
        popupSaveStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(popupSave);
        scene.getStylesheets().add(UtilLoader.class.getResource(savePopupCssPath).toExternalForm());
        popupSaveStage.setScene(scene);
        popupSaveStage.showAndWait();
    }
}
