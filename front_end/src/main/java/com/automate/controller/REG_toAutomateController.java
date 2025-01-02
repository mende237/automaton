package com.automate.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.automate.inputOutput.Configuration;
import com.automate.inputOutput.Instruction;
import com.automate.inputOutput.Messenger;
import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.AutomateType;
import com.automate.structure.Automaton;
import com.automate.utils.UtilFile;
import com.automate.utils.UtilLoader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

public class REG_toAutomateController extends Controller implements Initializable {
    private static final String dataFileName = "regularExpression.json";

    protected static final String ID = "REG_toAutomateController";
    private static REG_toAutomateController reg_toAutomateController = null;
    private Algorithm algorithmType;

    @FXML
    private VBox saveVBox;

    @FXML
    private Button saveButton;

    @FXML
    private VBox zoomResultVBox;

    @FXML
    private TextField txtRegularExpression;

    @FXML
    private HBox hboxExpression;

    // private Label suggestionLabel;

    private BoxBlur blurEffect;

    @FXML
    private HBox labelHbox;

    @FXML
    private Label lblRegularExpression;

    @FXML
    private Label lblSuggestion;

    @FXML
    private ScrollPane automatonResultScrollPane;

    @FXML
    private ImageView automatonResultImageView;

    private Message response = null;

    private Automaton automaton = null;

    // private DropShadow blurEffect;

    private REG_toAutomateController(Mediator mediator, Algorithm algorithmType) {
        super(REG_toAutomateController.ID, mediator);
        this.algorithmType = algorithmType;
        // TODO Auto-generated constructor stub
    }

    public static REG_toAutomateController getREG_toAutomateController(Mediator mediator, Algorithm algorithmType) {
        if (REG_toAutomateController.reg_toAutomateController == null) {
            REG_toAutomateController.reg_toAutomateController = new REG_toAutomateController(mediator, algorithmType);
        } else {
            REG_toAutomateController.reg_toAutomateController.algorithmType = algorithmType;
        }

        return REG_toAutomateController.reg_toAutomateController;
    }

    public static REG_toAutomateController getREG_toAutomateController() {
        return REG_toAutomateController.reg_toAutomateController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lblRegularExpression.textProperty().bind(this.txtRegularExpression.textProperty());
        this.saveVBox.setPickOnBounds(false);
        this.labelHbox.setPickOnBounds(false);

        this.saveButton.setOnMouseEntered(event -> {
            this.saveVBox.setPickOnBounds(true);
            this.zoomResultVBox.setPickOnBounds(false);
        });

        this.saveButton.setOnMouseExited(event -> {
            this.zoomResultVBox.setPickOnBounds(true);
            this.saveVBox.setPickOnBounds(false);
        });

        this.txtRegularExpression.setStyle("-fx-text-fill: transparent;");

        txtRegularExpression.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.endsWith("+") && !newValue.endsWith(".")) {
                    lblSuggestion.setStyle("-fx-opacity: 0.5;");
                    lblSuggestion.setText(".");
                } else{
                    lblSuggestion.setText("");
                }
                System.out.println("*************************************************** new value " + newValue);
            } else {
                lblSuggestion.setText("");
            }
        });

        txtRegularExpression.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case TAB:
                    String currentText = txtRegularExpression.getText();
                    txtRegularExpression.setText(currentText + lblSuggestion.getText());
                    txtRegularExpression.positionCaret(txtRegularExpression.getText().length()); // Move caret to the
                    lblSuggestion.setText("");
                    event.consume();
                    break;
                default:
                    break;
            }
        });
    }

    @FXML
    void handleBtnConvertClicked(ActionEvent event) {

        Configuration config = Configuration.getConfiguration();
        try {
            this.REG_toJson(txtRegularExpression.getText(), config.getDataRequestPath() + "/" + dataFileName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Messenger messenger = Messenger.getMessenger();
        // System.out.println("***************************************************");
        Instruction instruction = null;
        switch (this.algorithmType) {
            case GLUSHKOV:
                instruction = new Instruction("glushkov", config.getDataRequestPath() + "/" + dataFileName);
                break;
            case THOMSON:
                instruction = new Instruction("thomson", config.getDataRequestPath() + "/" + dataFileName);
                break;
            default:
                return;
        }

        try {
            messenger.sendInstruction(instruction);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (messenger.isResponse() == true) {
            try {
                Automaton automate = null;
                if (automate instanceof AFD) {
                    automate = AFD.jsonToAFD(messenger.getDataPathResponse(), false);
                } else {
                    automate = AFN.jsonToAFN(messenger.getDataPathResponse(), false);
                }
                this.automaton = automate;
                Image image = Automaton.makeImage(automate.markeGraph());
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

    private void REG_toJson(String reg, String filePath) throws FileNotFoundException, IOException {
        LinkedList<String> list = new LinkedList<>();
        String temp = "";
        boolean wasOperator = false;
        for (int i = 0; i < reg.length(); i++) {
            if (reg.charAt(i) == '+' || reg.charAt(i) == '.' || reg.charAt(i) == '*' || reg.charAt(i) == '('
                    || reg.charAt(i) == ')') {
                wasOperator = true;
                if (temp.length() != 0) {
                    list.add(temp);
                }
                String operator = "";
                operator += reg.charAt(i);
                list.add(operator);
                temp = "";
            } else {
                wasOperator = false;
                temp += reg.charAt(i);
            }
        }

        if (wasOperator == false) {
            list.add(temp);
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.printf("    %s   ", list.get(i));
        }

        JSONObject obj = new JSONObject();
        JSONArray Jword = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Jword.put(list.get(i));
        }

        obj.put("word", Jword);
        StringWriter strW = new StringWriter();
        obj.write(strW);
        String jsonText = strW.toString();
        RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
        raf.seek(0);
        raf.setLength(0);
        raf.write(jsonText.getBytes());
        raf.close();
    }

    @FXML
    private void handleZoomInResultButtonClick(ActionEvent event) {
        double scale = automatonResultImageView.getScaleX() * 1.1;
        automatonResultImageView.setScaleX(scale);
        automatonResultImageView.setScaleY(scale);
        automatonResultScrollPane.requestLayout();
    }

    @FXML
    private void handleSaveButtonClick(ActionEvent event) {
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        try {
            UtilLoader.showPopupSave("", "", "", REG_toAutomateController.ID);
            System.out.println("_____________________________________________________________________________");
            if (this.response != null && this.response.getContent() != null
                    && response.getIdExpediteur().equalsIgnoreCase(SavePopupController.ID)) {
                System.out.println(
                        "***************************-------------------" + response.getContent() + " $$$$$$$$$$$$$$$$");
                HashMap<String, ? extends Object> data = null;
                boolean fileExist = false;
                do {
                    data = response.getContent();
                    if (data == null)
                        break;
                    this.automaton.setName((String) data.get("name"));
                    this.automaton.setDescription((String) data.get("description"));
                    System.out.println("*************************** " + data.get("name") + " $$$$$$$$$$$$$$$$");
                    System.out.println("*************************** " + data.get("description") + " $$$$$$$$");
                    // // Automaton automaton = this.makeAutomata(data.get("name"),
                    // // data.get("description"));
                    AutomateType automateType = this.automaton instanceof AFD ? AutomateType.AFD
                            : ((AFN) this.automaton).isEpsiloneAFN() ? AutomateType.E_AFN : AutomateType.AFN;
                    System.out
                            .println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  ");
                    fileExist = UtilFile.isFileExist(automateType, (String) data.get("name"));
                    System.out.println("*************************** is file exist " + fileExist + " $$$$$$$$$$$$$$$$");
                    if (!fileExist || this.automaton != null) {
                        System.out.println(
                                "______________________________$$$$$$$$$$$$$$$$$$$$$$$_______________________________$$$$$$$$$$$$$$$$");
                        if (this.automaton != null) {
                            System.out.println(
                                    "nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
                            System.out.println();
                            HashMap<String, Object> content = new HashMap<>();
                            content.put("automaton", this.automaton);
                            content.put("type", automateType);
                            Message message = new Message(MainController.ID, content);
                            this.sendMessage(message);
                            // Stage stage = (Stage) saveButton.getScene().getWindow();
                            // stage.close();
                        }
                        System.out.println(
                                "______________________________$$$$$$$$$$$$$$$$$$$$$$$_______________________________$$$$$$$$$$$$$$$$");
                    } else {
                        String errortext = "An automaton with the same name\n alrealdy exist chose another name";
                        // this.showPopupSave(data.get("name"), data.get("description"), errortext);
                        UtilLoader.showPopupSave((String) data.get("name"), (String) data.get("description"), errortext,
                                ConvertController.ID);
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

    // @FXML
    // private void handleSaveButtonClick(ActionEvent event) {
    // // TODO Auto-generated method stub

    // }

    @FXML
    private void addEmptyWord(ActionEvent event) {

    }

    // @FXML
    // private void convertToAutomata(ActionEvent event){

    // }

    @FXML
    private void handleZoomOutResultButtonClick(ActionEvent event) {
        double scale = automatonResultImageView.getScaleX() / 1.1;
        automatonResultImageView.setScaleX(scale);
        automatonResultImageView.setScaleY(scale);
        automatonResultScrollPane.requestLayout();
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(super.id);
        super.mediator.transmitMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        this.response = message;
    }

    // public static void main(String[] args) {
    // REG_toAutomateController.REG_toJson("a.(a+b)*.(a+a)*");
    // }

}
