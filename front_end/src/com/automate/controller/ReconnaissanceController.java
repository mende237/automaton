package com.automate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.automate.inputOutput.Configuration;
import com.automate.inputOutput.Instruction;
import com.automate.inputOutput.Messenger;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ReconnaissanceController extends Controller implements Initializable {
    protected static final String ID = "reconnaissanceController";
    private static ReconnaissanceController reconnaissanceController = null;
    private Algorithm algorithmType;
    private String dataPath;

    @FXML
    private TextField txtWord;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane anchorPane;


    private ReconnaissanceController(Mediator mediator, Algorithm algorithmType) {
        super(ID, mediator);
        this.algorithmType = algorithmType;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.imageView.fitWidthProperty().bind(this.anchorPane.widthProperty());
        this.imageView.fitHeightProperty().bind(this.anchorPane.heightProperty());
        this.imageView.setPreserveRatio(true);
    }

    public static ReconnaissanceController getReconnaissanceController(Mediator mediator, Algorithm algorithmType) {
        if (ReconnaissanceController.reconnaissanceController == null) {
            ReconnaissanceController.reconnaissanceController = new ReconnaissanceController(mediator, algorithmType);
        } else {
            ReconnaissanceController.reconnaissanceController.algorithmType = algorithmType;
        }

        return ReconnaissanceController.reconnaissanceController;
    }

    public static ReconnaissanceController getReconnaissanceController() {
        return ReconnaissanceController.reconnaissanceController;
    }

    public void handleDetect() {
        Messenger messenger = Messenger.getMessenger();
        Configuration config = Configuration.getConfiguration();
        String path = config.getDataRequestPath()+"/word.json";

        switch (this.algorithmType) {
            case RECONNAISSANCE_AFD:
                Instruction instruction = new Instruction("reconnaissance AFD", path);

                break;
            case RECONNAISSANCE_AFN:

                break;
            // dans ce cas on applique la reconnaissance avec un afn
            default:

                break;
        }
    }

    private void handleReconnaissanceAFD(String dataPath , String dataResponsePath) {
        
    }

    private void handleReconnaissanceAFN(String dataPath , String dataResponsePath) {
        Instruction instruction = new Instruction("reconnaissance AFN", dataPath);

    }

    private void wordToJson(String word , String path){
        String temp = "";
        ArrayList<String> wordList = new ArrayList<String>();
        int j = 0;
        for (int i = 0; i < word.length(); i++) {
            if(word.charAt(i) != '.'){
                temp += word.charAt(i);
            }else{
                wordList.add(temp);
                temp = "";
            }
        }
        
    }

    private String[][] jsonToTabPath(String filePath) throws FileNotFoundException{
        try (Scanner reader = new Scanner(new File(filePath)).useDelimiter("\\Z")) {
            String content = reader.next();
            reader.close();
            JSONObject obj = new JSONObject(content);
            JSONArray JTabPath = obj.getJSONArray("path list");
            String tab[][] = new String[JTabPath.length()][];
            for (int i = 0; i < JTabPath.length(); i++) {
                JSONArray JPath = JTabPath.getJSONArray(i);
                tab[i] = new String[JPath.length()];
                for (int j = 0; j < tab[i].length; j++) {
                    tab[i][j] = JPath.getString(j);
                }
            }
            return tab;
        }
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public void receiveMessage(Message message) {
        System.out.println(message);
        if (message.getIdExpediteur().equals(MainController.ID)) {
            String tab[] = message.getContent().split(";");
            File file = new File(tab[0]);
            Image image = new Image(file.toURI().toString());
            this.imageView.setImage(image);
            this.dataPath = tab[1];
        }
    }

}
