package com.automate.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;
// import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.automate.inputOutput.Configuration;
import com.automate.structure.Automate;
import com.automate.structure.State;
import com.automate.structure.StateType;
import com.automate.structure.Transition;
import com.utils.CircleTableCellTransitions;
import com.utils.CircleTableCellTransitions.ColumnName;

import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

// import com.utils.ArrowTableCell;
// import com.utils.ArrowTableCell;
import com.utils.CircleTableCell;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
// import javafx.print.Collation;
// import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CreateAutomataController implements Initializable{

    @FXML
    private SplitPane splitPane;

    
    @FXML
    private Label alphabetLabel;
    
    @FXML
    private HBox alphabetBox;

    @FXML
    private Pane automatonPane;

    @FXML
    private ComboBox<State> deleteStateComboBox;

    @FXML
    private ComboBox<String> deleteSymbolComboBox;


    @FXML
    private ImageView automateImageView;

    @FXML
    private ComboBox<Transition> deleteTransitionComboBox;

    @FXML
    private TableColumn<State, Boolean> isFinalStateColumn;

    @FXML
    private TableColumn<State, Boolean> isInitialStateColumn;

    @FXML
    private TextField newStateNameTextField;

    @FXML
    private TextField newSymbolTextField;

    @FXML
    private ComboBox<State> newTransitionFromComboBox;

    @FXML
    private ComboBox<String> newTransitionInputComboBox;

    @FXML
    private ComboBox<State> newTransitionToComboBox;

    @FXML
    private TableColumn<State , String> stateNameColumn;

    @FXML
    private TableView<State> statesTableView;

    @FXML
    private TableColumn<Transition, State> transitionFromColumn;

    @FXML
    private TableColumn<Transition, String> transitionInputColumn;

    @FXML
    private TableColumn<Transition, State> transitionToColumn;

    @FXML
    private TableView<Transition> transitionsTableView;


    // Une liste observable d'états qui sera affichée dans le TableView
    private ObservableList<State> statesList = FXCollections.observableArrayList();
    private ObservableList<Transition> transitionsList = FXCollections.observableArrayList();
    private ObservableList<String> alphabetList = FXCollections.observableArrayList();
    private boolean isAFD = true;
    private String epsilone = "ep";

    // private Map<State, Node> stateNodes = new HashMap<>();

    // private List<String> alphabet = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        transitionsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurer les colonnes du TableView des états
        stateNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        isInitialStateColumn.setCellValueFactory(new PropertyValueFactory<>("initial"));
        isFinalStateColumn.setCellValueFactory(new PropertyValueFactory<>("final"));

        // Configurer les colonnes du TableView des transitions
        transitionFromColumn.setCellValueFactory(new PropertyValueFactory<>("begin"));
        transitionInputColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        transitionToColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        transitionFromColumn.setCellFactory(colum -> new CircleTableCellTransitions(ColumnName.FROM));
        // transitionInputColumn.setCellFactory(colum -> new ArrowTableCell());
        transitionToColumn.setCellFactory(colum -> new CircleTableCellTransitions(ColumnName.TO));

        // Lier la liste des transitions au TableView des transitions
        transitionsTableView.setItems(transitionsList);
        
        stateNameColumn.setCellFactory(column -> new CircleTableCell());
        isInitialStateColumn.setCellFactory(column -> new TableCell<State, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    if (item.booleanValue()) {
                        Image checkmarkImage = new Image(getClass().getResource("/icon/checkmark.png").toString());
                        imageView.setImage(checkmarkImage);
                    } else {
                        Image crossImage = new Image(getClass().getResource("/icon/cross.png").toString());
                        imageView.setImage(crossImage);
                    }
                    setGraphic(imageView);
                }
            }
        });

        
        isFinalStateColumn.setCellFactory(column -> new TableCell<State, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                    if (item.booleanValue()) {
                        Image checkmarkImage = new Image(getClass().getResource("/icon/checkmark.png").toString());
                        imageView.setImage(checkmarkImage);
                    } else {
                        Image crossImage = new Image(getClass().getResource("/icon/cross.png").toString());
                        imageView.setImage(crossImage);
                    }
                    setGraphic(imageView);
                }
            }
        });
        
        // Ajouter des états par défaut dans le TableView
        State state1 = new State("State 1", StateType.FINAL);
        State state2 = new State("State 2", StateType.INITIAL);
        State state3 = new State("State 2", StateType.INITIAL);
        statesList.add(state1);
        statesList.add(state2);
        statesList.add(state3);

        transitionsList.add(new Transition(state1, "a", state2));

        // Lier la liste d'états au TableView
        statesTableView.setItems(statesList);
        deleteStateComboBox.setItems(statesList);
        transitionsTableView.setItems(transitionsList);
        deleteTransitionComboBox.setItems(transitionsList);
        deleteSymbolComboBox.setItems(alphabetList);

        newTransitionFromComboBox.setItems(FXCollections.observableArrayList(statesList));
        newTransitionToComboBox.setItems(FXCollections.observableArrayList(statesList));
        // deleteStateComboBox.setItems(FXCollections.observableArrayList(statesList));

        newTransitionInputComboBox.setItems(FXCollections.observableArrayList(alphabetList));
        
        // alphabetList = FXCollections.observableArrayList();


        // updateTransitionComboBoxes(); // Mettre à jour les ComboBox au démarrage
        statesTableView.getItems().addListener((ListChangeListener<State>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateTransitionComboBoxes(); // Mettre à jour les ComboBox à chaque modification de la liste des états
                    updateInputComboBox(); // Mettre à jour la ComboBox à chaque modification de la liste des symboles de l'alphabet
                }
            }
        });


        // updateInputComboBox(); // Mettre à jour la ComboBox au démarrage
        updateAlphabetLabel();
        alphabetList.addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    updateInputComboBox(); // Mettre à jour la ComboBox à chaque modification de la liste des symboles de l'alphabet
                    updateAlphabetLabel();
                }
            }
        });   
        
        addSplitPaneListener();
    }


    private void addSplitPaneListener() {
        splitPane.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            statesTableView.setPrefWidth(newWidth.doubleValue() * 0.4);
            transitionsTableView.setPrefWidth(newWidth.doubleValue() * 0.4);
        });
    }

    private void updateAlphabetLabel() {
        String alphabet = String.join(", ", alphabetList);
        alphabetLabel.setText("Alphabet: " + alphabet);
    }

    private void updateInputComboBox() {
        newTransitionInputComboBox.setItems(FXCollections.observableArrayList(alphabetList));
        deleteSymbolComboBox.setItems(FXCollections.observableArrayList(alphabetList));
    }

    private void updateTransitionComboBoxes() {
        List<State> states = statesTableView.getItems().stream().collect(Collectors.toList());
        newTransitionFromComboBox.setItems(FXCollections.observableArrayList(states));
        newTransitionToComboBox.setItems(FXCollections.observableArrayList(states));
        deleteStateComboBox.setItems(FXCollections.observableArrayList(states));
    }
    

    @FXML
    private void handleAddStateButtonClick(ActionEvent event) {
        // Récupérer le nom du nouvel état à partir du TextField
        String stateName = newStateNameTextField.getText();

        // Créer un nouvel état avec le nom spécifié et les valeurs par défaut
        State newState = new State(stateName, StateType.NORMAL);

        if(isValidState(newState)){
            // Ajouter le nouvel état à la liste d'états
            statesList.add(newState);
        }else{
            System.out.println("**************** state ***************");
        }

        // Effacer le contenu du TextField
        newStateNameTextField.setText("");
    }

    @FXML
    private void handleDeleteStateButtonClick(ActionEvent event) {
        // Récupérer l'état sélectionné dans le ComboBox de suppression
        State stateToDelete = deleteStateComboBox.getValue();

        // Supprimer l'état de la liste d'états
        statesList.remove(stateToDelete);
        for (int i = 0; i < transitionsList.size(); i++) {
            Transition transition = transitionsList.get(i);
            if(transition.getBegin().equalState(stateToDelete) 
            || transition.getEnd().equalState(stateToDelete)){
                transitionsList.remove(transition);
            }
        }
        // for (Transition transition : transitionsList) {
            
        // }
    }



    @FXML
    private void handleAddSymbolButtonClick() {
        String newSymbol = newSymbolTextField.getText().trim();
        if (!newSymbol.isEmpty() && this.isValidLabel(newSymbol)) {
            alphabetList.add(newSymbol);
            newSymbolTextField.clear();
            // deleteSymbolComboBox.getItems().add(newSymbol); // Ajouter le nouveau symbole à la ComboBox
            // updateAlphabetLabel();
        }else{
            System.out.println("***************** bad label ********************");
        }
    }


    @FXML
    private void handleAddTransitionButtonClick(ActionEvent event) {
        State fromState = newTransitionFromComboBox.getValue();
        State toState = newTransitionToComboBox.getValue();
        String inputSymbol = newTransitionInputComboBox.getValue();

        
        if (fromState != null && toState != null && inputSymbol != null) {
            Transition newTransition = new Transition(fromState, inputSymbol, toState);
            if(isValidTransition(newTransition)){
                transitionsList.add(newTransition);
                System.out.println(newTransition);
                this.makeImage();
            }else{
                System.out.println("******************* bad transition ***********************");
            }

            
            // automaton.addTransition(newTransition);
            // transitionsTableView.getItems().add(newTransition);
        }
    }



    @FXML
    private void handleDeleteSymbolButtonClick(ActionEvent event) {
        String selectedSymbol = deleteSymbolComboBox.getValue();
        if (selectedSymbol != null) {
            alphabetList.remove(selectedSymbol);
            deleteSymbolComboBox.getSelectionModel().clearSelection();
            // for (Transition transition : transitionsList) {
            //     if(transition.getLabel().equals(selectedSymbol)){
            //         transitionsList.remove(transition);
            //     }
            // }
            for (int i = 0; i < transitionsList.size(); i++) {
                Transition transition = transitionsList.get(i);
                if(transition.getLabel().equals(selectedSymbol)){
                    transitionsList.remove(transition);
                }
            }
            updateAlphabetLabel(); // Mettre à jour le texte du Label "alphabetLabel"
        }
    }

    @FXML
    void handleDeleteTransitionButtonClick(ActionEvent event) {
        Transition transition = deleteTransitionComboBox.getValue();
        transitionsList.remove(transition);
    }

    private boolean isValidLabel(String label){
        int i = 0;
        boolean goodLabel = true;
        while (goodLabel && i < this.alphabetList.size()) {
            goodLabel = !label.equals(label);
            i++;
        }

        return goodLabel;
    }

    private boolean isValidState(State state){
        int i = 0;
        boolean goodState = true;
        while(goodState && i < this.statesList.size()){
            goodState = !this.statesList.get(i).equalState(state);
            i++;
        }

        return goodState;
    }

    private boolean isValidTransition(Transition transition){
        int i = 0;
        boolean goodTransition = true;
        while(goodTransition && i < this.transitionsList.size()){
            Transition trans = this.transitionsList.get(i);
            goodTransition = !trans.equalTransition(transition);
            if(isAFD && goodTransition){
                goodTransition = !trans.getBegin().equalState(transition.getBegin()) | !trans.getLabel().equals(transition.getLabel());
            }
            i++;
        }
        return goodTransition;
    }

    

    public void makeImage(){
        ArrayList<Transition> matTrans = new ArrayList<>(transitionsList);
        Graph g = isAFD ? Automate.markeGraph(matTrans) : Automate.markeGraph(matTrans, this.epsilone);
        try {
            Image image = Automate.makeImage(g);
            WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
            this.automateImageView.setImage(writableImage);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
}
