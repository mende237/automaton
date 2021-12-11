package com.automate.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable{
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

    private TreeItem<String> root , afd , afn ,epAfn;

    
    @FXML
    void handleDeterminisationView(ActionEvent event) {
        try{
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("../../../ressource/window/convertView.fxml"));
            //this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);
            
            // root.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            // root.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

        }catch(Exception e){
            e.printStackTrace();
            //System.out.println("eror");
        }
        //System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root = new TreeItem<String>();
        root.setExpanded(true);
        afd = new TreeItem<String>("AFD");
        afd.setExpanded(true);
        root.getChildren().add(afd);
        makeBranch("afd1", afd);
        makeBranch("afd2", afd);
        makeBranch("afd3", afd);
        afn = new TreeItem<String>("AFD");
        afn.setExpanded(true);
        root.getChildren().add(afn);
        makeBranch("afn1", afn);
        makeBranch("afn2", afn);
        makeBranch("afn3", afn);
        epAfn = new TreeItem<String>("\u03B5-AFN");
        epAfn.setExpanded(true);
        root.getChildren().add(epAfn);
        makeBranch("ep-afn1", epAfn);
        makeBranch("ep-afn2", epAfn);
        makeBranch("ep-afn3", epAfn);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setVisible(true);
        System.out.println("enter");
    }


    public  TreeItem<String> makeBranch(String title ,  TreeItem<String> parent){
         TreeItem<String> item = new  TreeItem<String>(title);
         item.setExpanded(true);
         parent.getChildren().add(item);
         return item;
    }

}
