package com.automate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


import com.automate.inputOutput.Configuration;
import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.Automaton;
import com.automate.controller.CreateAutomatonController.AutomateType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController extends Controller implements Initializable {
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

    //private String path = "/home/dimitri/Documents/beep-beep/save";
    //private String imagePath = "/home/dimitri/Documents/beep-beep/.communication/.images";
    private ArrayList<AFD> tabAFD;
    private ArrayList<AFN> tabAFN;
    private ArrayList<AFN> tabEpAFN;
    private Object response;

    private enum viewType {
        AUTOMATE_VIEW, CONVERT_VIEW , RECONNAISSANCE_VIEW
    }

    protected static final String ID = "mainController";
    private viewType vType;

    public MainController(Mediator mediator) {
        super(ID, mediator);
        this.vType = viewType.AUTOMATE_VIEW;
        System.out.println("enter main controller");
    }

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
            ArrayList<Automaton> tempTab;
            // chargement des afd
            tempTab = loadData(new File(Configuration.getConfiguration().getSavePath() + File.separator + "afd"), true);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabAFD.add((AFD) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabAFD.size(); i++) {
            // System.out.println(this.tabAFD.get(i));
            // }

            // chargement des afn
            tempTab = loadData(new File(Configuration.getConfiguration().getSavePath() + File.separator + "afn"), false);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabAFN.add((AFN) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabAFN.size(); i++) {
            // System.out.println(this.tabAFN.get(i));
            // }

            // chargement des epAFN
            tempTab = loadData(new File(Configuration.getConfiguration().getSavePath() + File.separator + "ep-afn"), false);
            for (int i = 0; i < tempTab.size(); i++) {
                this.tabEpAFN.add((AFN) tempTab.get(i));
            }

            // for (int i = 0; i < this.tabEpAFN.size(); i++) {
            // System.out.println(this.tabEpAFN.get(i));
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

        icon = new Image(getClass().getResourceAsStream("/icon/lettre-d.png"));
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
        icon = new Image(getClass().getResourceAsStream("/icon/lettre-n.png"));
        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        afn = new TreeItem<String>("AFN", imgView);
        afn.setExpanded(true);
        root.getChildren().add(afn);
        for (int i = 0; i < this.tabAFN.size(); i++) {
            makeBranch(this.tabAFN.get(i).getName(), afn, 2);
        }
        icon = new Image(getClass().getResourceAsStream("/icon/lettre-n.png"));
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
    }

    private void handleTreeItemMouseClicked(MouseEvent mouseEvent) {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item == null || item == this.root || item == afn || item == epAfn || item == afd) {
            System.out.println("enter!!!!!!!!!!!!!!!!!");
        } else {
            if (mouseEvent.getClickCount() == 2) {
                int itemIndex = getTreeItemIndex(item);
                int indexParent = getTreeItemIndex(item.getParent());

                // Scheduler.DOWNS2();// on bloque tout autre instruction d'affichage
                String pathCurrentImage = Configuration.getConfiguration().getImagePath();
                Automaton automate = null;
                if (indexParent == 0) {// dans ce cas on doit afficher un afd
                    automate = this.tabAFD.get(itemIndex);
                    pathCurrentImage += File.separator +  "afd.png";
                    automate.makeImage(pathCurrentImage);
                    System.out.println("superrrrrrrrrrrrrrrrrrrrrrrr");
                    System.out.println(pathCurrentImage);
                } else if (indexParent == 1) {// dans ce cas on doit afficher un afn
                    automate = this.tabAFN.get(itemIndex);
                    pathCurrentImage += File.separator + "afn.png";
                    automate.makeImage(pathCurrentImage);
                    System.out.println("superrrrrrrrrrrrrrrrrrrrrrrr");
                } else {//
                    automate = this.tabEpAFN.get(itemIndex);
                    pathCurrentImage += File.separator + "ep-afn.png";
                    automate.makeImage(pathCurrentImage);
                    System.out.println("superrrrrrrrrrrrrrrrrrrrrrrr");
                }
                System.out.println("chemin   " + automate.getPath());
                this.automateVisualisation(pathCurrentImage, automate);
                // Scheduler.UPS2();
                // System.out.println(indexParent + " " + index);
            }
        }
    }

    private void automateVisualisation(String path, Automaton automate) {
        try {
            Message message = null;
            switch (this.vType) {
                case AUTOMATE_VIEW:
                    ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
                    FXMLLoader loader = null;
                    loader = new FXMLLoader(getClass().getResource("/window/view.fxml"));
                    loader.setControllerFactory(c -> {
                        return ViewController.getViewController(mediator);
                    });

                    AnchorPane anchor = loader.load();
                    this.mainContainer.getChildren().clear();
                    AnchorPane.setTopAnchor(anchor, 0.0);
                    AnchorPane.setRightAnchor(anchor, 0.0);
                    AnchorPane.setLeftAnchor(anchor, 0.0);
                    AnchorPane.setBottomAnchor(anchor, 0.0);
                    this.mainContainer.getChildren().setAll(anchor);

                    anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
                    anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());

                    ViewController viewController = loader.getController();
                    message = new Message(viewController.getId(),
                            automate.getName() + ";" + automate.getDescription() + ";" + path);

                    System.out.println(message);
                    this.sendMessage(message);
                    break;
                case CONVERT_VIEW:
                    message = new Message(ConvertController.getConvertController().getId(),
                            path + ";" + automate.getPath());
                    this.sendMessage(message);
                    break;
                case RECONNAISSANCE_VIEW:
                    message = new Message(ReconnaissanceController.getReconnaissanceController().getId(),
                            path);
                    ReconnaissanceController.getReconnaissanceController().setAutomate(automate);
                    this.sendMessage(message);
                    break;
            }

            System.out.println("                  visualisation          ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // this.mainContainer = root;

    }

    private int getTreeItemIndex(TreeItem<String> item) {
        return item.getParent().getChildren().indexOf(item);
    }

    @FXML
    private void handleDeterminisationView(ActionEvent event) throws IOException {
        this.vType = viewType.CONVERT_VIEW;
        // Path path = Paths.get("src/ressource/test/convertView.fxml");
        // System.out.println(path.toRealPath());
        ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/convertView.fxml"));

        loader.setControllerFactory(c -> {
            return ConvertController.getConvertController(mediator, Algorithm.DERTIMINISATION);
        });
        AnchorPane anchor = loader.load();
        //ConvertController convertController = loader.getController();
        // this.mainContainer = anchor;

        this.mainContainer.getChildren().clear();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        this.mainContainer.getChildren().setAll(anchor);

        anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
        anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");
    }

    @FXML
    void handleComplementaireView(ActionEvent event) throws IOException {
        this.vType = viewType.CONVERT_VIEW;
        ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/convertView.fxml"));

        loader.setControllerFactory(c -> {
            return ConvertController.getConvertController(mediator, Algorithm.COMPLEMENTAIRE);
        });
        AnchorPane anchor = loader.load();
        //ConvertController convertController = loader.getController();
        // this.mainContainer = anchor;

        this.mainContainer.getChildren().clear();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        this.mainContainer.getChildren().setAll(anchor);

        anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
        anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");

    }

    @FXML
    void handleEpDeterminisationView(ActionEvent event) throws IOException {
        this.vType = viewType.CONVERT_VIEW;
        ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/convertView.fxml"));

        loader.setControllerFactory(c -> {
            return ConvertController.getConvertController(mediator, Algorithm.EP_DERTIMINISATION);
        });
        AnchorPane anchor = loader.load();
        //ConvertController convertController = loader.getController();
        // this.mainContainer = anchor;

        this.mainContainer.getChildren().clear();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        this.mainContainer.getChildren().setAll(anchor);

        anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
        anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");

    }

    @FXML
    void handleMinimisationView(ActionEvent event) throws IOException {
        this.vType = viewType.CONVERT_VIEW;
        ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/convertView.fxml"));

        loader.setControllerFactory(c -> {
            return ConvertController.getConvertController(mediator, Algorithm.MINIMISATION_B);
        });
        AnchorPane anchor = loader.load();
        //ConvertController convertController = loader.getController();
        // this.mainContainer = anchor;

        this.mainContainer.getChildren().clear();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        this.mainContainer.getChildren().setAll(anchor);

        anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
        anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");

    }

    @FXML
    void handleMiroirView(ActionEvent event) throws IOException {
        this.vType = viewType.CONVERT_VIEW;
        ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/convertView.fxml"));

        loader.setControllerFactory(c -> {
            return ConvertController.getConvertController(mediator, Algorithm.MIROIR_AFD);
        });
        AnchorPane anchor = loader.load();
        //ConvertController convertController = loader.getController();
        // this.mainContainer = anchor;

        this.mainContainer.getChildren().clear();
        AnchorPane.setTopAnchor(anchor, 0.0);
        AnchorPane.setRightAnchor(anchor, 0.0);
        AnchorPane.setLeftAnchor(anchor, 0.0);
        AnchorPane.setBottomAnchor(anchor, 0.0);
        this.mainContainer.getChildren().setAll(anchor);

        anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
        anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr!");

    }

    @FXML
    private void handleGlushkovView(ActionEvent event) {
        try {
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("/window/regToAfnView.fxml"));
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
            AnchorPane anchor = FXMLLoader.load(getClass().getResource("/window/regToAfnView.fxml"));
            // this.mainContainer = root;
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    public void handleReconnaissanceAFD_View() {
        try {
            this.vType = viewType.RECONNAISSANCE_VIEW;
            ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/reconnaissance.fxml"));

            loader.setControllerFactory(c -> {
                return ReconnaissanceController.getReconnaissanceController(mediator , Algorithm.RECONNAISSANCE_AFD);
            });

            AnchorPane anchor = loader.load();
            this.mainContainer.getChildren().clear();
            AnchorPane.setTopAnchor(anchor, 0.0);
            AnchorPane.setRightAnchor(anchor, 0.0);
            AnchorPane.setLeftAnchor(anchor, 0.0);
            AnchorPane.setBottomAnchor(anchor, 0.0);
            this.mainContainer.getChildren().setAll(anchor);

            anchor.prefWidthProperty().bind(this.mainContainer.prefWidthProperty());
            anchor.prefHeightProperty().bind(this.mainContainer.prefHeightProperty());
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println("eror");
        }
    }

    @FXML
    public void handleReconnaissanceAFN_View() {
        try {
            this.vType = viewType.RECONNAISSANCE_VIEW;
            ConrceteMadiator mediator = ConrceteMadiator.getConrceteMadiator();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/reconnaissance.fxml"));

            loader.setControllerFactory(c -> {
                return ReconnaissanceController.getReconnaissanceController(mediator, Algorithm.RECONNAISSANCE_AFN);
            });
            AnchorPane anchor = loader.load();
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
            icon = new Image(getClass().getResourceAsStream("/icon/afd_icon.png"));
        } else {
            icon = new Image(getClass().getResourceAsStream("/icon/afn_icon.png"));
        }

        imgView = new ImageView(icon);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        TreeItem<String> item = new TreeItem<String>(title, imgView);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    public ArrayList<Automaton> loadData(File folder, boolean isAFD) throws FileNotFoundException {
        ArrayList<Automaton> list = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (isAFD == true) {
                    AFD afd = AFD.jsonToAFD(file.getPath(), true);
                    afd.setPath(file.getPath());
                    list.add(afd);
                } else {
                    AFN afn = AFN.jsonToAFN(file.getPath(), true);
                    afn.setPath(file.getPath());
                    list.add(afn);
                }
            }
        }
        return list;
    }

    @FXML
    private void handleOpenCreateAutomateInterface(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/createAutomatonView.fxml"));
        loader.setControllerFactory(c -> {
            return CreateAutomatonController.getCreateAutomataController(ConrceteMadiator.getConrceteMadiator());
        });

        BorderPane createAutomateView;
        try {

            createAutomateView = loader.load();
            Stage createStage = new Stage();
            // createStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(createAutomateView);
            scene.getStylesheets().add(getClass().getResource("/style/style2.css").toExternalForm());
            // scene.getStylesheets().add(css);
            createStage.setScene(scene);
            createStage.showAndWait();
            if(response != null){
                HashMap<String, Object> data = (HashMap<String, Object>) response;
                String saveFolderName = null;
                Automaton automaton = (Automaton) data.get("automata");
                switch ((AutomateType) data.get("type")) {
                    case AFD:
                        saveFolderName = Configuration.getConfiguration().getAfdFolderName();
                        this.makeBranch(automaton.getName(), afd, 1);
                        break;
                    case AFN:
                        saveFolderName = Configuration.getConfiguration().getAfnFolderName();
                        this.makeBranch(automaton.getName(), afn, 2);
                        break;
                    default:
                        saveFolderName = Configuration.getConfiguration().getEp_afnFolderName();
                        this.makeBranch(automaton.getName(), epAfn, 2);
                        break;
                }
                System.out.println("*********** B *********************");
                System.out.println(automaton);
                System.out.println("************ E ********************");
                automaton.AutomatonToJson(Configuration.getConfiguration().getSavePath() + "/" + saveFolderName + "/" + automaton.getName() + ".json");
                System.out.println(data.get("type"));
                if((AutomateType) data.get("type") == AutomateType.AFD){
                    System.out.println("********************* AFD *********************");
                }else if((AutomateType) data.get("type") == AutomateType.AFN){
                    System.out.println("********************* AFN *********************");
                }else{
                    System.out.println("********************* E-AFN *********************");
                }
            }else
                System.out.println("*************** nulll *******************");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    // @FXML
    // public void exitApplication(ActionEvent event) {
    // System.out.println("Stage is closing");
    // Platform.exit();
    // }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(super.id);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        this.response = message.getContent();
    }

}
