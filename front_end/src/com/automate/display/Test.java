package com.automate.display;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

import java.io.File;
import java.io.IOException;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;

public class Test {
    public static void main(String args[]) throws IOException {
       Graph g = graph("test")
       .directed()
       .graphAttr()
       .with(Rank.dir(RankDir.LEFT_TO_RIGHT))
       .nodeAttr().with(Font.name("arial"))
       .linkAttr().with("class", "class-link")
       .with(
           node("a").with(Color.RED).link(node("b")),
           node("b").link(node("c"))
       );
        Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("./ex1.png"));
       //Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("example/ex1.png"));

    }
}
