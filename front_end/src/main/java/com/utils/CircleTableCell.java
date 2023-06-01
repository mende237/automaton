package com.utils;


import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.automate.structure.State;

public class CircleTableCell extends TableCell<State, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            Circle circle = new Circle();
            setTextFill(Color.BLACK);
            setContentDisplay(ContentDisplay.CENTER);
            setGraphic(circle);

            setText(item);
            State state = getTableView().getItems().get(getIndex());
            double radius = Math.max(20, item.length() * 5);
            circle.setRadius(radius);
            circle.setStroke(Color.BLACK);
            if (state.isInitial()) {
                circle.setFill(Color.web("#00cc66"));
            } else if (state.isFinalState()) {
                circle.setFill(Color.web("#ff3300"));
            } else {
                circle.setFill(Color.web("#4286f4"));
            }
            // System.out.println(circle + "==" + circle.getLayoutY());
        }
    }
        
    
}
