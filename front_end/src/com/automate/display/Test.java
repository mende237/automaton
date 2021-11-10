package com.automate.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class Test implements Initializable{
    @FXML private TreeView<String> treeView;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        TreeItem<String> root , afd , afn , epAfn;
        root = new TreeItem<>();
        
    }

    private TreeItem<String> makeBranch(String title , TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }
}
