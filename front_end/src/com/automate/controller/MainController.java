package com.automate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.Automate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private int nbrAFD;
    private int nbrAFN;
    private int nbrEpAFN;
    private TreeItem<String> root, afd, afn, epAfn;

    private String path = "/home/dimitri/Documents/beep-beep/save";
    private ArrayList<AFD> tabAFD;
    private ArrayList<AFN> tabAFN;
    private ArrayList<AFN> tabEpAFN;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.nbrAFD = 0;
        this.nbrAFN = 0;
        this.nbrEpAFN = 0;

        Image icon;
        ImageView imgView;
        this.splitPane.setDividerPositions(0.1);

        try {
            this.tabAFD = new ArrayList<>();
            this.tabAFN = new ArrayList<>();
            this.tabEpAFN = new ArrayList<>();
            ArrayList<Automate> tempTab;
            // chargement des afd
            tempTab = loadData(new File(this.path + "/afd"), true);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabAFD.add((AFD) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabAFD.size(); i++) {
            //     System.out.println(this.tabAFD.get(i));
            // }

            // chargement des afn
            tempTab = loadData(new File(this.path + "/afn"), false);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabAFN.add((AFN) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabAFN.size(); i++) {
            //     System.out.println(this.tabAFN.get(i));
            // }

            // chargement des epAFN
            tempTab = loadData(new File(this.path + "/ep-afn"), false);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabEpAFN.add((AFN) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabEpAFN.size(); i++) {
            //     System.out.println(this.tabEpAFN.get(i));
            // }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                handleTreeItemMouseClicked(mouseEvent);
            }

        });

        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-d.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(16);
        imgView.setFitWidth(16);
        root = new TreeItem<String>();
        root.setExpanded(true);

        afd = new TreeItem<String>("AFD", imgView);
        afd.setExpanded(true);
        root.getChildren().add(afd);
        for (int i = 0; i < this.tabAFD.size(); i++) {
            makeBranch(this.tabAFD.get(i).getName(), afd, 1);
        }
        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-n.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        afn = new TreeItem<String>("AFN", imgView);
        afn.setExpanded(true);
        root.getChildren().add(afn);
        for (int i = 0; i < this.tabAFN.size(); i++) {
            makeBranch(this.tabAFN.get(i).getName(), afn, 2);
        }
        icon = new Image(getClass().getResourceAsStream("../../../ressource/icon/lettre-n.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        epAfn = new TreeItem<String>("\u03B5-AFN", imgView);
        epAfn.setExpanded(true);
        root.getChildren().add(epAfn);

        for (int i = 0; i < this.tabEpAFN.size(); i++) {
            makeBranch(this.tabEpAFN.get(i).getName(), epAfn, 2);
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setVisible(true);
        System.out.println("enter");
    }

    private void handleTreeItemMouseClicked(MouseEvent mouseEvent) {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item == null || item == this.root || item == afn || item == epAfn || item == afd){
            System.out.println("enter!!!!!!!!!!!!!!!!!");
        }else{

            
            if(mouseEvent.getClickCount() == 2){
                int index = getTreeItemIndex(item);
                int indexParent = getTreeItemIndex(item.getParent());
                
                //System.out.println(indexParent + " " + index);
            }
        }
    }

    private int getTreeItemIndex(TreeItem<String> item){
        return item.getParent().getChildren().indexOf(item);
    }

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
        TreeItem<String> item = new TreeItem<String>(title, imgView);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    public ArrayList<Automate> loadData(File folder, boolean isAFD) throws FileNotFoundException {
        ArrayList<Automate> list = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (isAFD == true) {
                    AFD afd = AFD.jsonToAFD(file.getPath(), true);
                    list.add(afd);
                } else {
                    AFN afn = AFN.jsonToAFN(file.getPath(), true);
                    list.add(afn);
                }
            }
        }
        return list;
    }

}
