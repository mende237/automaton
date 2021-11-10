import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String args[]) throws IOException {
        launch();
        // Graph g = graph("test")
        // .directed()
        // .graphAttr()
        // .with(Rank.dir(RankDir.LEFT_TO_RIGHT))
        // .nodeAttr().with(name("arial"))
        // .linkAttr().with("class", "class-link")
        // .with(
        // node("a").with(Color.RED).link(node("b")),
        // node("b").link(node("c"))
        // );
        // Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new
        // File("./ex1.png"));
        // //Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new
        // File("example/ex1.png"));

    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/home/dimitri/automate_manip/front_end/src/ressource/window/mainView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}