package com.automate.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.automate.inputOutput.Instruction;
import com.automate.inputOutput.Messenger;
import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.AutomateType;
import com.automate.structure.Automaton;
import com.automate.utils.UtilFile;
import com.automate.utils.UtilLoader;

import guru.nidi.graphviz.model.Graph;

import org.json.JSONException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConvertController extends Controller implements Initializable {
    private static ConvertController convertController = null;
    @FXML
    private Button btnConvert;

    @FXML
    private Button saveButton;

    @FXML
    private Button btnSave;

    @FXML
    private SplitPane splitPane;

    @FXML
    private ScrollPane automatonDataScrollPane;

    @FXML
    private ScrollPane automatonResultScrollPane;

    @FXML
    private ImageView automatonDataImageView;

    @FXML
    private VBox zoomDataVBox;

    @FXML
    private VBox zoomResultVBox;

    @FXML
    private VBox saveVBox;

    @FXML
    private ImageView automatonResultImageView;

    @FXML
    private SplitPane splitPaneContainer;

    @FXML
    private StackPane stackPaneData;

    @FXML
    private StackPane stackPaneResult;

    @FXML
    private VBox vBoxBotton;

    @FXML
    private HBox hBoxButtonContainer;

    protected static final String ID = "convertController";

    private Algorithm algorithmType;
    private String dataPath;
    // private AutomateType automateType;
    private Automaton automaton;
    private Message response = null;

    private ConvertController(Mediator mediator, Algorithm algorithmType) {
        super(ID, mediator);
        this.algorithmType = algorithmType;
        System.out.println("************convert controller*******************");
    }

    public static ConvertController getConvertController(Mediator mediator, Algorithm algorithmType) {
        if (ConvertController.convertController == null) {
            ConvertController.convertController = new ConvertController(mediator, algorithmType);
        } else {
            ConvertController.convertController.algorithmType = algorithmType;
            System.out.println("deja lance");
        }

        return ConvertController.convertController;
    }

    public static ConvertController getConvertController() {
        return ConvertController.convertController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        switch (algorithmType) {
            case DERTIMINISATION:
                this.btnConvert.setText("deternine");
                break;
            case EP_DERTIMINISATION:
                this.btnConvert.setText("deternine");
                break;
            case MINIMISATION_B:
                this.btnConvert.setText("minimisation");
                break;
            case MINIMISATION_H:
                this.btnConvert.setText("minimisation");
                break;
            case COMPLEMENTAIRE:
                this.btnConvert.setText("complementaire");
                break;
            case COMPLETION:
                this.btnConvert.setText("completion");
                break;
            case MIROIR_AFD:
                this.btnConvert.setText("miroir");
                break;
            case MIROIR_AFN:
                this.btnConvert.setText("miroir");
                break;
            default:
                break;
        }

        this.zoomDataVBox.setPickOnBounds(false);

        // Ajouter un écouteur d'événements pour la barre de défilement de la ScrollPane
        this.automatonDataScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0 || newValue.doubleValue() == 1.0) {
                System.out.println("Scroll bar is at top or bottom ******");
                // Activer la consommation d'événements de la VBox lorsque la barre de
                // défilement est en haut ou en bas
                this.zoomDataVBox.setPickOnBounds(true);
            } else {
                // Désactiver la consommation d'événements de la VBox lorsque la barre de
                // défilement est en mouvement
                this.zoomDataVBox.setPickOnBounds(false);
            }
        });

        this.zoomResultVBox.setPickOnBounds(false);

        // Ajouter un écouteur d'événements pour la barre de défilement de la ScrollPane
        this.automatonResultScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0 || newValue.doubleValue() == 1.0) {
                System.out.println("Scroll bar is at top or bottom");
                // Activer la consommation d'événements de la VBox lorsque la barre de
                // défilement est en haut ou en bas
                this.zoomResultVBox.setPickOnBounds(true);
            } else {
                // Désactiver la consommation d'événements de la VBox lorsque la barre de
                // défilement est en mouvement
                this.zoomResultVBox.setPickOnBounds(false);
            }
        });

        hBoxButtonContainer.setPickOnBounds(false);
        this.saveVBox.setPickOnBounds(false);

        this.saveButton.setOnMouseEntered(event -> {
            // this.splitPaneContainer.setPickOnBounds(false);
            this.saveVBox.setPickOnBounds(true);
            this.zoomResultVBox.setPickOnBounds(false);
            System.out.println("Mouse entered the button area");
        });

        this.saveButton.setOnMouseExited(event -> {
            this.zoomResultVBox.setPickOnBounds(true);
            this.saveVBox.setPickOnBounds(false);
            System.out.println("Mouse exited the button area !!!!!!");
        });

        this.btnConvert.setOnMouseEntered(event -> {
            this.splitPaneContainer.setPickOnBounds(false);
            System.out.println("Mouse entered the button area");
        });

        this.btnConvert.setOnMouseExited(event -> {
            this.splitPaneContainer.setPickOnBounds(true);
            this.hBoxButtonContainer.setPickOnBounds(false);
            System.out.println("Mouse exited the button area");
        });

        // Bind the position of btnConvert with the divider bar of splitPaneContainer
        splitPaneContainer.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
            double position = newVal.doubleValue();
            double totalWidth = splitPaneContainer.getWidth();
            double translationValue = (totalWidth * position) - (totalWidth / 2);
            hBoxButtonContainer.setTranslateX(translationValue);
        });
    }

    @FXML
    private void handleZoomInDataButtonClick(ActionEvent event) {
        double scale = automatonDataImageView.getScaleX() * 1.1;
        automatonDataImageView.setScaleX(scale);
        automatonDataImageView.setScaleY(scale);
        automatonDataScrollPane.requestLayout();
    }

    @FXML
    private void handleZoomOutDataButtonClick(ActionEvent event) {
        double scale = automatonDataImageView.getScaleX() / 1.1;
        automatonDataImageView.setScaleX(scale);
        automatonDataImageView.setScaleY(scale);
        automatonDataScrollPane.requestLayout();
    }

    @FXML
    private void handleZoomInResultButtonClick(ActionEvent event) {
        double scale = automatonResultImageView.getScaleX() * 1.1;
        automatonResultImageView.setScaleX(scale);
        automatonResultImageView.setScaleY(scale);
        automatonResultScrollPane.requestLayout();
    }

    @FXML
    private void handleZoomOutResultButtonClick(ActionEvent event) {
        double scale = automatonResultImageView.getScaleX() / 1.1;
        automatonResultImageView.setScaleX(scale);
        automatonResultImageView.setScaleY(scale);
        automatonResultScrollPane.requestLayout();
    }

    @FXML
    private void handleSaveButtonClick(ActionEvent event) {
        try {
            System.out.println("*************************** " + ConvertController.ID + " $$$$$$$$$$$$$$$$");
            // this.showPopupSave("", "", "");
            UtilLoader.showPopupSave("", "", "", ConvertController.ID);
            // boolean editMode = CreateAutomatonController.automaton != null ? true :
            System.out.println("***************************++++++++++++++++++++ " + response.getIdExpediteur() + " $$$$$$$$$$$$$$$$");
            // false;
            if (this.response != null && this.response.getContent() != null
                    && response.getIdExpediteur().equalsIgnoreCase(SavePopupController.ID)) {
                System.out.println("***************************-------------------" + response.getContent() + " $$$$$$$$$$$$$$$$");
                HashMap<String, ? extends Object> data = null;
                boolean fileExist = false;
                do {
                    data = response.getContent();
                    if (data == null)
                        break;
                    
                    System.out.println("*************************** " + data.get("name") + " $$$$$$$$$$$$$$$$");
                    System.out.println("*************************** " + data.get("description") + " $$$$$$$$");
        //             // Automaton automaton = this.makeAutomata(data.get("name"),
        //             // data.get("description"));
                    AutomateType automateType = this.automaton instanceof AFD ? AutomateType.AFD
                            : ((AFN) this.automaton).isEpsiloneAFN() ? AutomateType.E_AFN : AutomateType.AFN;
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  ");
                    fileExist = UtilFile.isFileExist(automateType, (String) data.get("name"));
                    System.out.println("*************************** is file exist " + fileExist + " $$$$$$$$$$$$$$$$");
                    if (!fileExist || this.automaton != null) {
                        System.out.println("______________________________$$$$$$$$$$$$$$$$$$$$$$$_______________________________$$$$$$$$$$$$$$$$");
                        if (automaton != null) {
                            HashMap<String, Object> content = new HashMap<>();
                            content.put("automaton", automaton);
                            content.put("type", automateType);
                            Message message = new Message(MainController.ID, content);
                            this.sendMessage(message);
                            // Stage stage = (Stage) saveButton.getScene().getWindow();
                            // stage.close();
                        }
                        System.out.println("______________________________$$$$$$$$$$$$$$$$$$$$$$$_______________________________$$$$$$$$$$$$$$$$");
                    } else {
                        String errortext = "An automaton with the same name\n alrealdy exist chose another name";
                        // this.showPopupSave(data.get("name"), data.get("description"), errortext);
                        UtilLoader.showPopupSave((String) data.get("name"), (String) data.get("description"), errortext, ConvertController.ID);
                    }
                } while (fileExist);

                this.response = null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // this.automaton = null;
    }

    public Algorithm getAlgorithmType() {
        return this.algorithmType;
    }

    public void setAlgorithmType(Algorithm algorithmType) {
        this.algorithmType = algorithmType;
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(super.id);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        System.out.println("******************* receive message expediteur " + message.getIdExpediteur() + " *******************");
        // System.out.println(message);
        if (message.getIdExpediteur().equals(MainController.ID)) {
            this.automaton = (Automaton) message.getContent().get("automaton");
            Graph g = this.automaton.markeGraph();
            Image image;
            try {
                image = Automaton.makeImage(g);
                WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(),
                        (int) image.getHeight());
                this.automatonDataImageView.setImage(writableImage);
                this.dataPath = this.automaton.getPath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if (message.getIdExpediteur().equals(SavePopupController.ID)) {
            System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
            this.response = message;
        }
    }

    @FXML
    public void handleBtnConvertClicked(ActionEvent event) {
        switch (this.algorithmType) {
            case DERTIMINISATION:
                this.btnConvert.setText("deternine");
                this.handleDeterminisation();
                break;
            case EP_DERTIMINISATION:
                this.btnConvert.setText("deternine");
                this.handleEpDeterminisation();
                break;
            case MINIMISATION_B:
                this.btnConvert.setText("minimisation");
                this.handleMinisation("b");
                break;
            case MINIMISATION_H:
                this.btnConvert.setText("minimisation");
                this.handleMinisation("h");
                break;
            case COMPLEMENTAIRE:
                this.btnConvert.setText("complementaire");
                this.handleComplementaire();
                break;
            case COMPLETION:
                this.btnConvert.setText("completion");
                this.handleCompletion();
                break;
            case MIROIR_AFD:
                this.btnConvert.setText("miroir");
                this.handleMiroir("afd");
                break;
            case MIROIR_AFN:
                this.btnConvert.setText("miroir");
                this.handleMiroir("afn");
                break;
            default:
                break;
        }
    }

    private void handleConvertion(Instruction instruction, boolean isReturnAFD) {
        Messenger messenger = Messenger.getMessenger();
        // Configuration config = Configuration.getConfiguration();
        try {
            messenger.sendInstruction(instruction);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (messenger.isResponse() == true) {
            try {
                Automaton automaton = null;
                if (isReturnAFD == true) {
                    automaton = AFD.jsonToAFD(messenger.getDataPathResponse(), false);
                } else {
                    automaton = AFN.jsonToAFN(messenger.getDataPathResponse(), false);
                }
                this.automaton = automaton;
                Image image = Automaton.makeImage(automaton.markeGraph());
                this.automatonResultImageView.setImage(image);
            } catch (FileNotFoundException | JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void handleDeterminisation() {
        Instruction instruction = new Instruction("determinisation", this.dataPath);
        this.handleConvertion(instruction, true);
    }

    private void handleEpDeterminisation() {
        Instruction instruction = new Instruction("epsilone determinisation", this.dataPath);
        this.handleConvertion(instruction, true);
    }

    private void handleMinisation(String type) {
        Instruction instruction;
        if (type.equalsIgnoreCase("h")) {
            instruction = new Instruction("hopcroft minisation", this.dataPath);
        } else {
            instruction = new Instruction("brzozowski minisation", this.dataPath);
        }
        this.handleConvertion(instruction, true);
    }

    private void handleComplementaire() {
        Instruction instruction = new Instruction("complementaire", this.dataPath);
        this.handleConvertion(instruction, true);
    }

    private void handleCompletion() {
        Instruction instruction = new Instruction("completion", this.dataPath);
        this.handleConvertion(instruction, true);
    }

    private void handleMiroir(String type) {
        Instruction instruction;
        if (type.equalsIgnoreCase("afd")) {
            instruction = new Instruction("miroir AFD", this.dataPath);
            this.handleConvertion(instruction, false);
        } else {
            instruction = new Instruction("miroir AFN", this.dataPath);
            this.handleConvertion(instruction, false);
        }
    }
}