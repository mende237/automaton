package com.automate.controller;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class REG_toAutomateController extends Controller implements Initializable {
    protected static final String ID = "REG_toAutomateController";
    private static REG_toAutomateController reg_toAutomateController = null;
    Algorithm algorithmType;

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

    private static void REG_toJson(String reg) {
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
            System.out.printf("%s" , list.get(i));
        }   
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendMessage(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void receiveMessage(Message message) {
        // TODO Auto-generated method stub

    }


    public static void main(String[] args) {
        REG_toAutomateController.REG_toJson("a.(a+b)*.(a+a)*");
    }

}
