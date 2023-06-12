package com.automate.controller;

import static guru.nidi.graphviz.model.Factory.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.automate.inputOutput.Configuration;
import com.automate.inputOutput.Instruction;
import com.automate.inputOutput.Messenger;
import com.automate.structure.Automate;

import org.json.JSONArray;
import org.json.JSONObject;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
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
    private Automate automate;

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

    public Automate getAutomate() {
        return this.automate;
    }

    public void setAutomate(Automate automate) {
        this.automate = automate;
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

    @FXML
    public void handleDetect() throws IOException {
        if (this.automate != null) {
            Configuration config = Configuration.getConfiguration();
            String path = config.getDataRequestPath() + "/word.json";
            String word = txtWord.getText();
            this.wordToJson(word, path);
            Path source = Paths.get(this.automate.getPath());
            Path destination;
            Instruction instruction;

            switch (this.algorithmType) {
                case RECONNAISSANCE_AFD:
                    destination = Paths.get(config.getDataRequestPath() + File.separator + "afd.json");
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    instruction = new Instruction("reconnaissance AFD", config.getDataRequestPath());
                    this.handleReconnaissance(instruction);
                    break;
                case RECONNAISSANCE_AFN:
                    destination = Paths.get(config.getDataRequestPath() + File.separator + "afn.json");
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    instruction = new Instruction("reconnaissance AFN", config.getDataRequestPath());
                    this.handleReconnaissance(instruction);
                    break;
                default:

                    break;
            }
        }
    }

    private Node addState(String state) {
        String finalStateTab[] = this.automate.getFinalStateTab();
        for (int i = 0; i < finalStateTab.length; i++) {
            if (state.equalsIgnoreCase(finalStateTab[i]) == true) {
                return node(state).with(Style.FILLED, Color.BROWN.fill());
            }
        }
        return node(state).with(Style.FILLED, Color.DARKGREEN.fill());
    }

    private void handleReconnaissance(Instruction instruction) {
        Messenger messenger = Messenger.getMessenger();
        Configuration config = Configuration.getConfiguration();
        try {
            messenger.sendInstruction(instruction);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (messenger.isResponse() == true) {
            Graph g = this.automate.markeGraph();
            MutableGraph mg = g.toMutable();
            mg.linkAttrs().add(Style.DASHED);
            Collection<Link> edges = mg.edges();
            Random random = new Random();
            try {
                String tab[][] = this.jsonToTabPath(messenger.getDataPathResponse());
                for (int i = 0; i < tab.length; i++) {
                    int b = 0;
                    if (tab[i].length % 2 == 0) {
                        b = 1;
                    }

                    if (tab[i].length >= 3) {
                        int red = random.nextInt(256);
                        int green = random.nextInt(256);
                        int blue = random.nextInt(256);
                        Color linkColor = Color.rgb(red, green, blue);
                        this.drawPath(edges, tab[i], b, linkColor);
                    }
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String path = config.getImagePath() + File.separator + "automate.png";
            try {
                Graphviz.fromGraph(mg).width(1500).render(Format.PNG).toFile(new File(path));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            File file = new File(path);
            Image image = new Image(file.toURI().toString());
            this.imageView.setImage(image);
        }

        this.automate = null;
    }

    private void drawPath(Collection<Link> edges, String[] path, int b, Color linkColor) {
        int j = 0;
        do {
            for (Link link : edges) {
                if (link.from().name().value().equalsIgnoreCase(path[j]) &&
                        link.to().name().value().equalsIgnoreCase(path[j + 2]) &&
                        (((Label) link.get("label")).value().equalsIgnoreCase(path[j + 1])
                                || ((Label) link.get("label")).value().equalsIgnoreCase("\u03B5"))) {
                    link.add(Style.SOLID);
                    if (link.get("color") != null) {
                        String temp = (String) link.get("color");
                        String tab[] = temp.split(":");
                        Color color = Color.rgb(tab[0]);
                        for (int i = 1; i < tab.length; i++) {
                            color.and(Color.rgb(tab[i]));
                        }
                        link.add(color.and(linkColor));
                        System.out.println((String) link.get("color"));
                    }else{
                        link.add(linkColor);
                    }
                }
            }
            j += 2;
        } while (j < path.length - 2 - b);
        System.out.println("b " + b);
    }

    private void wordToJson(String word, String path) throws IOException {
        String temp = "";
        ArrayList<String> wordList = new ArrayList<String>();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != '.') {
                temp += word.charAt(i);
                if (i == word.length() - 1) {
                    wordList.add(temp);
                }
            } else {
                wordList.add(temp);
                temp = "";
            }
        }
        System.out.println("*************************************************");
        for (int i = 0; i < wordList.size(); i++) {
            System.out.println(wordList.get(i));
        }
        System.out.println("***********************************************");
        JSONObject obj = new JSONObject();
        JSONArray Jword = new JSONArray();
        for (int i = 0; i < wordList.size(); i++) {
            Jword.put(wordList.get(i));
        }
        obj.put("word", Jword);
        StringWriter strW = new StringWriter();
        obj.write(strW);
        String jsonText = strW.toString();

        RandomAccessFile raf = new RandomAccessFile(path, "rw");
        raf.write(jsonText.getBytes());
        raf.close();
    }

    private String[][] jsonToTabPath(String filePath) throws FileNotFoundException {
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
            File file = new File((String) message.getContent());
            Image image = new Image(file.toURI().toString());
            this.imageView.setImage(image);
        }
    }

}
