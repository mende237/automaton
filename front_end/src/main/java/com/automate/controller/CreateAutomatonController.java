package com.automate.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;

// import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import com.automate.inputOutput.Configuration;
import com.automate.structure.AFD;
import com.automate.structure.AFN;
import com.automate.structure.Automaton;
import com.automate.structure.State;
import com.automate.structure.StateType;
import com.automate.structure.Transition;
import com.automate.utils.CircleTableCell;
import com.automate.utils.CircleTableCellTransitions;
import com.automate.utils.CircleTableCellTransitions.ColumnName;

import guru.nidi.graphviz.model.Graph;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreateAutomatonController extends Controller implements Initializable{
    public enum AutomateType{
        AFD , AFN , E_AFN
    }

    protected static final String ID = "createAutomataController";

    @FXML
    private Button saveButton;

    @FXML
    private VBox zoomVBox;

    @FXML
    private SplitPane splitPane;

    
    @FXML
    private Label alphabetLabel;
    

    @FXML
    private ComboBox<State> deleteStateComboBox;

    @FXML
    private ComboBox<String> deleteSymbolComboBox;

    @FXML
    private ScrollPane automatonScrollPane;

    @FXML
    private ImageView automatonImageView;

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
    private Message response = null;
    private AutomateType automateType = AutomateType.AFD;
    private static Automaton automaton;

    private static CreateAutomatonController createAutomataController;

    // private Map<State, Node> stateNodes = new HashMap<>();

    // private List<String> alphabet = new ArrayList<>();

    private CreateAutomatonController(Mediator mediator) {
        super(ID, mediator);
    }

    private CreateAutomatonController(Mediator mediator,  Automaton automaton) {
        super(ID, mediator);
        CreateAutomatonController.automaton = automaton;
    }

    public static CreateAutomatonController getCreateAutomataController(Mediator mediator){
        if(CreateAutomatonController.createAutomataController != null)
            return CreateAutomatonController.createAutomataController;
        
        return new CreateAutomatonController(mediator);
    }

    public static CreateAutomatonController getCreateAutomataController(Mediator mediator , Automaton automaton){
        if(CreateAutomatonController.createAutomataController != null){
            CreateAutomatonController.automaton = automaton;
            return CreateAutomatonController.createAutomataController;
        }
        
        
        return new CreateAutomatonController(mediator, automaton);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Désactiver la consommation d'événements de la VBox
        zoomVBox.setPickOnBounds(false);

        // Ajouter un écouteur d'événements pour la barre de défilement de la ScrollPane
        automatonScrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0 || newValue.doubleValue() == 1.0) {
                // Activer la consommation d'événements de la VBox lorsque la barre de défilement est en haut ou en bas
                zoomVBox.setPickOnBounds(true);
            } else {
                // Désactiver la consommation d'événements de la VBox lorsque la barre de défilement est en mouvement
                zoomVBox.setPickOnBounds(false);
            }
        });


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
        // State state1 = new State("State 1", StateType.FINAL);
        // State state2 = new State("State 2", StateType.INITIAL);
        // State state3 = new State("State 2", StateType.NORMAL);
        // statesList.add(state1);
        // statesList.add(state2);
        // statesList.add(state3);

        // transitionsList.add(new Transition(state1, "a", state2));

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
        // alphabetList.add("\\u03B5");
        newTransitionInputComboBox.getItems().add("\u03B5");
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
        // this.makeImage();
        if(CreateAutomatonController.automaton != null){
            this.initializeAutomaton(automaton);
            this.makeImage();
            // CreateAutomatonController.automaton = null;
        }
    }

    private void initializeAutomaton(Automaton automaton){
        for (String symbol : automaton.getTabLabel()) {
            alphabetList.add(symbol);
        }

        Set<State> stateSet = new HashSet<>();

        if(automaton instanceof AFN){
            AFN afn = (AFN) automaton;
            for (Transition transition : afn.getMatTrans()) {
                transitionsList.add(transition);
                stateSet.add(transition.getBegin());
                stateSet.add(transition.getEnd());
            }
        }else{
            AFD afd = (AFD) automaton;
            System.out.println("****************************** " + afd.getMatTrans().length);
            for (Transition transition : afd.getMatTrans()) {
                if(transition == null)
                    continue;

                transitionsList.add(transition);
                System.out.println("******************************************************************");
                System.out.println(transition);
                stateSet.add(transition.getBegin());
                stateSet.add(transition.getEnd());
            }
        }

        Iterator<State> stateSetIterator = stateSet.iterator();
        while (stateSetIterator.hasNext()) {
            statesList.add(stateSetIterator.next());
        }
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
        newTransitionInputComboBox.getItems().add(0, "\u03B5");
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
        
        // Créer un nouvel état avec le nom spécifié et les valeurs par défaut
        try {
            this.showDialog();
            System.out.println("***************" + response);
            if(this.response != null ){
                String stateName = newStateNameTextField.getText();
                State newState = new State(stateName, (StateType) response.getContent());
                if(isValidState(newState))
                    // Ajouter le nouvel état à la liste d'états
                    statesList.add(newState);
                else{
                    System.out.println("**************** state ***************");
                }
                this.response = null;
            }
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        boolean update = false;
        for (int i = 0; i < transitionsList.size(); i++) {
            Transition transition = transitionsList.get(i);
            if(transition.getBegin().equalState(stateToDelete) 
            || transition.getEnd().equalState(stateToDelete)){
                transitionsList.remove(transition);
                i--;
                update = true;
            }
        }

        if(update)
            this.makeImage();

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
            
            Transition newTransition = new Transition(fromState, inputSymbol.equalsIgnoreCase("\\u03B5") ? this.epsilone : inputSymbol, toState);
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
            boolean update = false;
            for (int i = 0; i < transitionsList.size(); i++) {
                Transition transition = transitionsList.get(i);
                if(transition.getLabel().equals(selectedSymbol)){
                    transitionsList.remove(transition);
                    i--;
                    update = true;
                }
            }
            updateAlphabetLabel(); // Mettre à jour le texte du Label "alphabetLabel"
            if(update)
                this.makeImage();
        }
    }

    @FXML
    private void handleDeleteTransitionButtonClick(ActionEvent event) {
        Transition transition = deleteTransitionComboBox.getValue();
        transitionsList.remove(transition);
        this.makeImage();
    }


    @FXML
    private void handleZoomInButtonClick(ActionEvent event) {
        double scale = automatonImageView.getScaleX() * 1.1;
        automatonImageView.setScaleX(scale);
        automatonImageView.setScaleY(scale);
        automatonScrollPane.requestLayout();
    }

    @FXML
    private void handleZoomOutButtonClick(ActionEvent event) {
        double scale = automatonImageView.getScaleX() / 1.1;
        automatonImageView.setScaleX(scale);
        automatonImageView.setScaleY(scale);
        automatonScrollPane.requestLayout();
    }

    @FXML
    private void handleSaveButtonClick(ActionEvent event){
        try {
            this.showPopupSave("" , "" , "");
            boolean editMode = CreateAutomatonController.automaton != null ? true : false;
            if(this.response != null  && this.response.getContent() != null && response.getIdExpediteur().equalsIgnoreCase(SavePopupController.ID)){
                HashMap<String, String> data = null;
                boolean fileExist = true;
                do{
                    data = (HashMap<String, String>) response.getContent();
                    if(data == null)
                        break;
                        
                    System.out.println("*************************** " + data.get("name") + " $$$$$$$$$$$$$$$$");
                    System.out.println("*************************** " + data.get("description") + " $$$$$$$$");
                    Automaton automaton = this.makeAutomata(data.get("name"), data.get("description"));
                    fileExist = isFileExist(data.get("name"));
                    if(!fileExist || CreateAutomatonController.automaton != null){
                        if(automaton != null){
                            HashMap<String, Object> content = new HashMap<>();
                            content.put("automaton", automaton);
                            content.put("type", this.automateType); 
                            Message message = new Message("mainController", content);
                            this.sendMessage(message);
                            Stage stage = (Stage) saveButton.getScene().getWindow();
                            stage.close();
                        }
                    }else{
                        String errortext = "An automaton with the same name\n alrealdy exist chose another name";
                        this.showPopupSave(data.get("name") , data.get("description"), errortext);                        
                    }
                }while(fileExist && !editMode);

                // this.response = null;
                this.automateType = AutomateType.AFD;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CreateAutomatonController.automaton = null;
    }

    private boolean isValidLabel(String label){
        int i = 0;
        boolean goodLabel = true;
        while (goodLabel && i < this.alphabetList.size()) {
            goodLabel = !label.equals(alphabetList.get(i));
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
        boolean isConnexe = false;
        boolean firstTransition = true;
        while(goodTransition && i < this.transitionsList.size()){
            firstTransition = false;
            Transition trans = this.transitionsList.get(i);
            goodTransition = !trans.equalTransition(transition);
            // if(goodTransition){
            //     goodTransition = !trans.getBegin().equalState(transition.getBegin()) | !trans.getLabel().equals(transition.getLabel());
            // }

            isConnexe |= transition.getBegin().equalState(trans.getBegin()) | transition.getBegin().equalState(trans.getEnd())
                         | transition.getEnd().equalState(trans.getBegin()) | transition.getEnd().equalState(trans.getEnd());
            i++;
        }
        return goodTransition & (isConnexe | firstTransition);
    }



    public void makeImage(){
        ArrayList<Transition> matTrans = new ArrayList<>(transitionsList);
        Graph g = isAFD ? Automaton.markeGraph(matTrans) : Automaton.markeGraph(matTrans, this.epsilone);
        try {
            if(g!=null){
                Image image = Automaton.makeImage(g);
                WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
                this.automatonImageView.setImage(writableImage);
            }else{
                this.automatonImageView.setImage(null);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void showDialog() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/dialogBox.fxml"));
        loader.setControllerFactory(c -> {
            return PopupController.getPopupController(ConrceteMadiator.getConrceteMadiator());
        });

        BorderPane popup = loader.load();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(popup);
        scene.getStylesheets().add(getClass().getResource("/style/diaglogstyle.css").toExternalForm());
        // scene.getStylesheets().add(css);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private void showPopupSave(String name , String description , String errorLabel) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/window/savePopup.fxml"));
        SavePopupController savePopupController = SavePopupController.getSavePopupController(ConrceteMadiator.getConrceteMadiator());
        loader.setControllerFactory(c -> {
            return savePopupController;
        });

        AnchorPane popupSave = loader.load();
        savePopupController.setTextNameField(name);
        savePopupController.setTextDescriptionArea(description);
        savePopupController.setLblErrorMessage(errorLabel);

        Stage popupSaveStage = new Stage();
        popupSaveStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(popupSave);
        scene.getStylesheets().add(getClass().getResource("/style/savePopup.css").toExternalForm());
        // scene.getStylesheets().add(css);
        popupSaveStage.setScene(scene);
        popupSaveStage.showAndWait();
    }

    private Automaton makeAutomata(String name, String description){
        Set<State> initialStateList = new HashSet<>();
        Set<State> finalStateList = new HashSet<>();
       
        for(int i=0; i<this.transitionsList.size(); i++){
            Transition transition = transitionsList.get(i);
            if(transition.getBegin().getType() == StateType.INITIAL)
                initialStateList.add(transition.getBegin());
            else if(transition.getBegin().getType() == StateType.FINAL)
                finalStateList.add(transition.getBegin());
            else if(transition.getBegin().getType() == StateType.FINAL_INITIAL){
                initialStateList.add(transition.getBegin());
                finalStateList.add(transition.getBegin());
            }

            if(transition.getEnd().getType() == StateType.INITIAL)
                initialStateList.add(transition.getEnd());
            else if(transition.getEnd().getType() == StateType.FINAL)
                finalStateList.add(transition.getEnd());
            else if(transition.getEnd().getType() == StateType.FINAL_INITIAL){
                initialStateList.add(transition.getEnd());
                finalStateList.add(transition.getEnd());
            }

            if(initialStateList.size() >= 2 && this.automateType != AutomateType.E_AFN)
                this.automateType = AutomateType.AFN;
            
            if(transition.getLabel().equalsIgnoreCase("\u03B5"))
                this.automateType = AutomateType.E_AFN;

            if(this.automateType == AutomateType.AFD){
                int j = i + 1;
                while (j < this.transitionsList.size() && this.automateType == AutomateType.AFD) {
                    Transition transition2 = this.transitionsList.get(j);
                    if(transition.semiEqualTransition(transition2))
                        this.automateType = AutomateType.AFN;
                    j++;
                }
            }
        }
       
        if(initialStateList.size() <= 0 || finalStateList.size() <= 0 || alphabetList.size() <= 0){
            ////////                ::::::::
            return null;
        }

        int nbrState = statesList.size();
        String labelTab[] = new String[alphabetList.size()];

        for(int i = 0; i < alphabetList.size(); i++)
            labelTab[i] = alphabetList.get(i);

        String finalStateTab[] = new String[finalStateList.size()];

        {
            Iterator<State> finalStateListIterator = finalStateList.iterator();
            int i = 0;
            while(finalStateListIterator.hasNext()){
                finalStateTab[i] = finalStateListIterator.next().getName();
                i++;
            }
        }
            

        // for(int i = 0; i < finalStateList.size(); i++)
        //     finalStateTab[i] = finalStateList.get(i).getName();

        Automaton automata = null;
        if(this.automateType == AutomateType.AFD){
            String initialState = null;
            {
                Iterator<State> initialStateListIterator = initialStateList.iterator();
                while(initialStateListIterator.hasNext()){
                    initialState = initialStateListIterator.next().getName();
                }
            }
            automata = new AFD(labelTab, nbrState, finalStateTab, initialState, name, description);

             for (Transition transition : this.transitionsList) 
                automata.addTransition(transition.getBegin(), transition.getLabel(), transition.getEnd());
        }else{
            String initialStateTab[] = new String[initialStateList.size()];

            {
                Iterator<State> initialStateListIterator = initialStateList.iterator();
                int i = 0;
                while(initialStateListIterator.hasNext()){
                    initialStateTab[i] = initialStateListIterator.next().getName();
                    i++;
                }
            }
            
            automata = new AFN(labelTab, this.epsilone , nbrState , finalStateTab, initialStateTab, name , description);
            for (Transition transition : this.transitionsList) 
                automata.addTransition(transition.getBegin(), transition.getLabel(), transition.getEnd());
            
        }
        return automata;
    }


    private boolean isFileExist(String fileName){
        String folderToSearch = null;
        switch (this.automateType) {
            case AFD:
                folderToSearch = Configuration.getConfiguration().getAfdFolderName();
                break;    
            case AFN:
                folderToSearch = Configuration.getConfiguration().getAfnFolderName();
                break;
            default:
                folderToSearch = Configuration.getConfiguration().getEp_afnFolderName();
                break;
        }
        // System.out.println("------------------ automate type === "+ this.automateType + "------ foldename ==" + folderToSearch + "-----------------------------");
        File file = new File(Configuration.getConfiguration().getSavePath() + "/" + folderToSearch+ "/" + fileName + ".json");
        return file.exists();
    }

    @Override
    public void sendMessage(Message message) {
        message.setIdExpediteur(ID);
        super.mediator.transmitMessage(message);
    }


    @Override
    public void receiveMessage(Message message) {
        response = message;
    }


    
}
