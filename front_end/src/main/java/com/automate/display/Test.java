package com.automate.display;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Factory.to;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.tree.MutableTreeNode;

import com.automate.structure.AFN;
import com.automate.structure.Automaton;
import com.automate.structure.State;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

public class Test {

   

    public static void main(String args[]) throws IOException {
        System.out.println(true && false);
        AFN afn = new AFN();
        Set<State> temp = new HashSet<>();
        State state1 = new State("gg");
        State state2 = new State("gg");

        temp.add(state1);
        temp.add(state2);
        
        Iterator<State> tempIterator = temp.iterator();
        int i = 0;
        while(tempIterator.hasNext()){
           System.out.println(tempIterator.next().getName());
        }
        // try {
        //     System.out.println(automaton.getClass());
        // } catch (NoSuchFieldException | SecurityException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // for(Method f: automaton.getClass().getDeclaredMethods()){
        //     System.out.println(f);
        // }
        // try {
        // config = Configuration.getConfiguration("../config.json");
        // System.out.println(config);
        // } catch (FileNotFoundException e) {
        // //TODO: handle exception
        // }

        // messenger.setSendingPath(config.getRequestPath());
        // messenger.setReceptionPath(config.getResponsePath());
        // App.reset();
        // launch();

        // Graphviz.useEngine(new GraphvizCmdLineEngine());
        // System.out.println("debut!!!!!!!!!!!!!!!!!!!!!!!!!");
        // Graph g =
        // graph("test").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT));
        // Graph g = graph("afn").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
        //         .nodeAttr().with(Shape.CIRCLE);

        // g = g.with(node("1")
        //         .link(to(node("2"))
        //                 .with(Label.of("a")),
        //                 to(node("3")).with(Label.of("b"), Style.DASHED, Color.rgb(150,52,80))));

        // MutableGraph mg = g.toMutable();
        // // mg.add(
        // // mutNode("a").add(Color.RED).addLink(mutNode("b")));

        // Collection<Link> edges = mg.edges();
        // Collection<MutableNode> nodes = mg.nodes();
        // for (Link link : edges) {
        //     if (link.get("color") != null) {
        //         // System.out.println(link.get("color"));
                
        //         Color color = Color.rgb((String) link.get("color"));
        //         link.add(color.and(Color.rgb(80,80,100) , Color.rgb(0, 80, 100)));
        //         System.out.println(link.get("color"));
        //         //System.out.println(link.);
        //         //link.add(Color.RED);
        //     }
        //     link.attrs();
        //     // System.out.printf("%s %s\n" , link.name().value() , link.get("label"));
        //     // Label l = (Label) link.get("label");
        //     // System.out.println(link.get("node"));
        //     // link.from().
        //     // System.out.println(link.);

        // }

        // for (MutableNode mn1 : nodes) {
        // // for (MutableNode mn2 : nodes) {
        // // mn1.
        // // }
        // System.out.println(mn1.linkTo());
        // }

        // System.out.println(g.name().value());
        // System.out.println(edges.size());

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

        // Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("./ex1.png"));

        // System.out.println("fin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
