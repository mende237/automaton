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
            switch (state.getType()) {
                case INITIAL:
                    circle.setFill(Color.web("#b5fed9"));
                    break;
                case FINAL:
                    circle.setFill(Color.web("e2cbc1"));
                    break;
                case WELL:
                    circle.setFill(Color.GRAY);
                    break;
                case FINAL_INITIAL:
                    circle.setFill(Color.web("#b5fed9"));
                    break;
                default:
                    circle.setFill(Color.web("#87CEEB"));
                    break;
            }
            // System.out.println(circle + "==" + circle.getLayoutY());
        }
    }
        
    
}
