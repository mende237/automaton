package com.automate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.automate.inputOutput.Configuration;

import com.automate.inputOutput.Instruction;
import com.automate.inputOutput.Messenger;
import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.Automaton;

import guru.nidi.graphviz.model.Graph;

import org.json.JSONException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;



public class ConvertController extends Controller implements Initializable {
    private static ConvertController convertController = null;
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
 
    protected static final String ID = "convertController";
    private Algorithm algorithmType;
    private String dataPath;

    
    private ConvertController(Mediator mediator , Algorithm algorithmType) {
        super(ID , mediator);
        this.algorithmType = algorithmType;
        System.out.println("************convert controller*******************");
    }

    public static ConvertController getConvertController(Mediator mediator , Algorithm algorithmType) {
        if (ConvertController.convertController == null) {
            ConvertController.convertController = new ConvertController(mediator , algorithmType);
        }else{
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
        //System.out.println("la fenetre convert a ete lance!!!!!");
        // this.imageViewData.fitWidthProperty().bind(this.anchorPaneData.widthProperty());
        // this.imageViewData.fitHeightProperty().bind(this.anchorPaneData.heightProperty());
        // this.imageViewData.setPreserveRatio(true);

        // this.imageViewResult.fitWidthProperty().bind(this.anchorPaneResult.widthProperty());
        // this.imageViewResult.fitHeightProperty().bind(this.anchorPaneResult.heightProperty());
        // this.imageViewResult.setPreserveRatio(true);

        // this.splitPane.setDividerPositions(0.45);
    }


    @FXML
    private void handleZoomInButtonClick(ActionEvent event) {
       
    }

    @FXML
    private void handleZoomOutButtonClick(ActionEvent event) {
        
    }

    @FXML
    private void handleSaveButtonClick(ActionEvent event){
    
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
        // System.out.println(message);
        if(message.getIdExpediteur().equals(MainController.ID)){
            Automaton automaton = (Automaton) message.getContent();
            Graph g = automaton.markeGraph();
            Image image;
            try {
                image = Automaton.makeImage(g);
                WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
                this.imageViewData.setImage(writableImage);
                this.dataPath = automaton.getPath();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void handleBtnConvertClicked(ActionEvent event){
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

    private void handleConvertion(Instruction instruction , boolean isReturnAFD){
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
                if(isReturnAFD  == true){
                    automaton = AFD.jsonToAFD(messenger.getDataPathResponse(), false);
                }else{
                    automaton = AFN.jsonToAFN(messenger.getDataPathResponse(), false);
                }

                Image image = Automaton.makeImage(automaton.markeGraph());
                this.imageViewResult.setImage(image);
            } catch (FileNotFoundException | JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void handleDeterminisation(){
        Instruction instruction = new Instruction("determinisation", this.dataPath);
        this.handleConvertion(instruction , true);
    }

    private void handleEpDeterminisation() {
        Instruction instruction = new Instruction("epsilone determinisation", this.dataPath);
        this.handleConvertion(instruction , true);
    }


    private void handleMinisation(String type){
        Instruction instruction;
        if(type.equalsIgnoreCase("h")){
            instruction = new Instruction("hopcroft minisation", this.dataPath);
        }else{
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
            this.handleConvertion(instruction , false);
        }
    }
}