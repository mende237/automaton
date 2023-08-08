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
import java.util.LinkedList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import com.automate.inputOutput.Configuration;
import com.automate.inputOutput.Instruction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class REG_toAutomateController extends Controller implements Initializable {
    protected static final String ID = "REG_toAutomateController";
    private static REG_toAutomateController reg_toAutomateController = null;
    private Algorithm algorithmType;

    @FXML
    private TextField txtExpression;

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
        // TODO Auto-generated method stub

    }


    @FXML
    void handleBtnConvertClicked(ActionEvent event) {
        try {
            this.REG_toJson(txtExpression.getText(), ".");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Configuration config = Configuration.getConfiguration();
        System.out.println("***************************************************");
        switch (this.algorithmType) {
            case GLUSHKOV:
                // Instruction instruction = new Instruction("hopcroft minisation", this.dataPath);
                break;
            case THOMSON:
                
                break;
            default:

                break;
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
                if(temp.length() != 0){
                    list.add(temp);
                }
                String operator = "";
                operator += reg.charAt(i);
                list.add(operator);
                temp = "";
            }else{
                wasOperator = false;
                temp += reg.charAt(i);
            }
        }

        if(wasOperator == false){
            list.add(temp);
        }

        for (int i = 0; i < list.size() ; i++) {
            System.out.printf("    %s   " , list.get(i));
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
        raf.write(jsonText.getBytes());
        raf.close();
    }



   
    @Override
    public void sendMessage(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveMessage(Message message) {
        // TODO Auto-generated method stub

    }


    // public static void main(String[] args) {
    //     REG_toAutomateController.REG_toJson("a.(a+b)*.(a+a)*");
    // }


}
