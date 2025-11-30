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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.util.Duration;

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

    // @FXML
    // private TextField txtSuggestion;

    @FXML
    private HBox hboxExpression;

    // private Label suggestionLabel;

    private BoxBlur blurEffect;

    @FXML
    private HBox labelHbox;

    // @FXML
    // private Label lblRegularExpression;

    // @FXML
    // private Label lblSuggestion;

    @FXML
    private ScrollPane automatonResultScrollPane;

    @FXML
    private ImageView automatonResultImageView;

    private String actualText = ""; // Stores the real user input without the proposed value

    private static final int PROPOSAL_TIMEOUT = 2000; // Time for proposal (in milliseconds)

    private Timeline proposalTimeline; // Timer for clearing the proposal

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
        // this.lblRegularExpression.textProperty().bind(this.txtRegularExpression.textProperty());
        this.saveVBox.setPickOnBounds(false);
        // this.labelHbox.setPickOnBounds(false);

        this.saveButton.setOnMouseEntered(event -> {
            this.saveVBox.setPickOnBounds(true);
            this.zoomResultVBox.setPickOnBounds(false);
        });

        this.saveButton.setOnMouseExited(event -> {
            this.zoomResultVBox.setPickOnBounds(true);
            this.saveVBox.setPickOnBounds(false);
        });

        // this.txtRegularExpression.setStyle("-fx-text-fill: transparent;");

        // txtRegularExpression.textProperty().addListener((observable, oldValue,
        // newValue) -> {
        // if (!newValue.isEmpty()) {
        // if (!newValue.endsWith("+") && !newValue.endsWith(".")) {
        // lblSuggestion.setStyle("-fx-opacity: 0.5;");
        // lblSuggestion.setText(".");
        // } else {
        // lblSuggestion.setText("");
        // }
        // System.out.println("*************************************************** new
        // value " + newValue);
        // } else {
        // lblSuggestion.setText("");
        // }
        // });

        // txtRegularExpression.setOnKeyPressed(event -> {
        // switch (event.getCode()) {
        // case TAB:
        // String currentText = txtRegularExpression.getText();
        // txtRegularExpression.setText(currentText + lblSuggestion.getText());
        // txtRegularExpression.positionCaret(txtRegularExpression.getText().length());
        // // Move caret to the
        // lblSuggestion.setText("");
        // event.consume();
        // break;
        // default:
        // break;
        // }
        // });

        // this.lblSuggestion.setText("ffffffffffffffffffff");

        // this.lblSuggestion.setPickOnBounds(true);
        // SimpleBooleanProperty proposalActive = new SimpleBooleanProperty(false);

        // this.txtRegularExpression.setOnMouseEntered(event -> {
        // this.txtRegularExpression.setPickOnBounds(true);
        // this.lblSuggestion.setPickOnBounds(false);
        // });

        // this.txtRegularExpression.setOnMouseExited(event -> {
        // if (!proposalActive.get()) {
        // this.txtRegularExpression.setPickOnBounds(false);
        // this.lblSuggestion.setPickOnBounds(true);
        // }
        // });

        // // Add a listener to track text changes and propose a "."
        // txtRegularExpression.textProperty().addListener((observable, oldValue,
        // newValue) -> {
        // // Check if the input length reaches the threshold
        // if (newValue.length() > 0 && !newValue.endsWith(".") &&
        // !newValue.endsWith("+")
        // && !newValue.endsWith("*")) {
        // System.out.println("******************************************///////////////////");
        // txtRegularExpression.setStyle("-fx-text-fill: gray; -fx-opacity: 0.6;");
        // this.actualText = newValue; // Store the actual user input
        // txtRegularExpression.setText(actualText + ".");
        // txtRegularExpression.positionCaret(txtRegularExpression.getText().length());
        // proposalActive.set(true);

        // // Start the timer to clear the proposal after the timeout
        // if (proposalTimeline != null) {
        // proposalTimeline.stop(); // Reset the timer if it's already running
        // }
        // proposalTimeline = new Timeline(new
        // KeyFrame(Duration.millis(PROPOSAL_TIMEOUT), event -> {
        // txtRegularExpression.setStyle("-fx-text-fill: black; -fx-opacity: 1;");
        // txtRegularExpression.setText(actualText);
        // proposalActive.set(false);
        // }));
        // proposalTimeline.setCycleCount(1);
        // proposalTimeline.play();
        // } else if (!proposalActive.get()) {
        // txtRegularExpression.setStyle("-fx-text-fill: black; -fx-opacity: 1;");
        // }
        // });

        // // Handle the Tab key to accept the proposed "."
        // txtRegularExpression.setOnKeyPressed(event -> {
        // switch (event.getCode()) {
        // case TAB: {
        // String text = txtRegularExpression.getText();
        // if (proposalActive.get()) {
        // txtRegularExpression.setText(text + "."); // Append the proposed "."
        // txtRegularExpression.positionCaret(txtRegularExpression.getText().length());
        // // Move cursor to
        // txtRegularExpression.setStyle("-fx-text-fill: black; -fx-opacity: 1;"); //
        // Reset visual feedback
        // proposalActive.set(false);
        // if (proposalTimeline != null) {
        // proposalTimeline.stop(); // Stop the timer if the proposal is accepted
        // }
        // }
        // event.consume(); // Prevent default Tab behavior
        // txtRegularExpression.requestFocus(); // Keeps the focus on the text field
        // txtRegularExpression.positionCaret(txtRegularExpression.getText().length());
        // // Moves the caret to
        // // the end without
        // // selecting text
        // break;
        // }
        // default: {
        // // if (proposalTimeline != null) {
        // // proposalTimeline.stop(); // Stop the timer if the proposal is accepted
        // // txtRegularExpression.setStyle("-fx-text-fill: black; -fx-opacity: 1;"); //
        // // Reset visual feedback
        // // }
        // if (!proposalActive.get()) {
        // // txtRegularExpression.setStyle("-fx-text-fill: black;"); // Reset style on
        // // other key presses
        // // lblSuggestion.setStyle("-fx-opacity: 0;"); // Make the suggestion label
        // // invisible
        // txtRegularExpression.setStyle("-fx-text-fill: black; -fx-opacity: 1;"); //
        // Reset visual feedback
        // }
        // break;
        // }
        // }
        // });
    }

    /**
     * Propose a dot (".") to the user and start the timeout timer.
     */
    private void proposeDot(TextField regexTextField) {
        regexTextField.setText(actualText + "."); // Visually append the proposed dot
        regexTextField.positionCaret(regexTextField.getText().length()); // Move cursor to the end

        // Start or restart the timeline for the proposal timeout
        if (proposalTimeline != null) {
            proposalTimeline.stop();
        }
        proposalTimeline = new Timeline(new KeyFrame(Duration.millis(PROPOSAL_TIMEOUT), event -> {
            cancelProposal(regexTextField);
        }));
        proposalTimeline.setCycleCount(1);
        proposalTimeline.play();
    }

    /**
     * Cancel the dot proposal and revert to the actual text.
     */
    private void cancelProposal(TextField regexTextField) {
        if (proposalTimeline != null) {
            proposalTimeline.stop();
        }
        regexTextField.setText(actualText); // Revert to the actual user input
        regexTextField.positionCaret(regexTextField.getText().length()); // Move cursor to the end
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
