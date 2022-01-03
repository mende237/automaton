package com.automate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {
    @FXML
    private Button btnDeterminisation;
    @FXML
    private Button btnEpDeterminisation;
    @FXML
    private Button btnUnion;
    @FXML
    private Button btnIntersection;
    @FXML
    private Button btnMiroire;
    @FXML
    private Button btnComplementaire;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private SplitPane splitPane;

    private TreeItem<String> root, afd, afn, epAfn;

    @FXML
    private void handleDeterminisationView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
        // System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");
    }

    @FXML
    void handleComplementaireView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    void handleEpDeterminisationView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    void handleMinimisationView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    void handleMiroirView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    private void handleGlushkovView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/regToAfnView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }

    }

    @FXML
    private void handleThomsonView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/regToAfnView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image icon;
        ImageView imgView;
        this.splitPane.setDividerPositions(0.1);

        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-d.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        root = new TreeItem<String>();
        root.setExpanded(true);
        afd = new TreeItem<String>("AFD", imgView);
        afd.setExpanded(true);
        root.getChildren().add(afd);
        makeBranch("afd1", afd, 1);
        makeBranch("afd2", afd, 1);
        makeBranch("afd3", afd, 1);
        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-n.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        afn = new TreeItem<String>("AFN", imgView);
        afn.setExpanded(true);
        root.getChildren().add(afn);
        makeBranch("afn1", afn, 2);
        makeBranch("afn2", afn, 2);
        makeBranch("afn3", afn, 2);
        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-n.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        epAfn = new TreeItem<String>("\u03B5-AFN", imgView);
        epAfn.setExpanded(true);
        root.getChildren().add(epAfn);
        makeBranch("ep-afn1", epAfn, 2);
        makeBranch("ep-afn2", epAfn, 2);
        makeBranch("ep-afn3", epAfn, 2);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setVisible(true);
        System.out.println("enter");
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent, int type) {
        Image icon;
        ImageView imgView;
        if (type == 1) {
            icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/afd_icon.png"));
        } else {
            icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/afn_icon.png"));
        }
        
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        TreeItem<String> item = new TreeItem<String>(title , imgView);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

}
