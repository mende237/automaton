// import static guru.nidi.graphviz.model.Factory.*;

// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import com.automate.controller.ConrceteMadiator;
import com.automate.controller.MainController;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.model.Graph;
// import javafx.application.Application;
// import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String args[]) throws IOException {
        launch();
        // Graphviz.useEngine(new GraphvizCmdLineEngine());
        // System.out.println("debut!!!!!!!!!!!!!!!!!!!!!!!!!");
        // Graph g =
        // graph("test").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT));

        // g = g.with(node("a").with(Color.RED).link(node("b")),
        // node("b").link(to(node("c")).with(Label.of("test"))),
        // node("a").link(node("c")),
        // node("b").link(node("a"))
        // );

        // Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new
        // File("./ex1.png"));
        // System.out.println("test");
        // Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new
        // File("example/ex1.png"));

        // Node main = node("main").with(Label.html("<b>main</b><br/>start"),
        // Color.rgb("1020d0").font()),
        // init = node(Label.markdown("**_init_**")), execute = node("execute"),
        // compare = node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7,
        // .3, 1.0)),
        // mkString = node("mkString").with(Label.lines(
        // Justification.LEFT, "make", "a", "multi-line")), printf = node("printf");

        // Graph g = graph("example2").directed()
        // .with(main.link(to(node("parse").link(execute)).with(LinkAttr.weight(8)),
        // to(init).with(Style.DOTTED),
        // node("cleanup"), to(printf).with(Style.BOLD, Label.of("100 times"),
        // Color.RED)),
        // execute.link(graph().with(mkString, printf), to(compare).with(Color.RED)),
        // init.link(mkString));

        // Graphviz viz = Graphviz.fromGraph(g);

        // String json = viz.engine(Engine.NEATO).render(Format.JSON).toString();

        // BufferedWriter writter = new BufferedWriter(new FileWriter(new
        // File("./image.json")));
        // writter.write(json);

        // Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new
        // File("./ex1.png"));

        // System.out.println("fin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        // System.out.println(App.class.getResource("App.class"));
        // System.out.println(App.class.getResource("test1.txt"));
        // System.out.println("traverser");
        String css = this.getClass().getResource("ressource/style/caspian.css").toExternalForm();
        ConrceteMadiator m = ConrceteMadiator.getConrceteMadiator();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ressource/window/mainView.fxml"));

        loader.setControllerFactory(c -> {
            return new MainController(m);
        });
        AnchorPane root = loader.load();

        //AnchorPane root = FXMLLoader.load(getClass().getResource("ressource/window/mainView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

}