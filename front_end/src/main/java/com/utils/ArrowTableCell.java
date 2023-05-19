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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.automate.structure.State;
import com.automate.structure.Transition;

public class ArrowTableCell extends TableCell<Transition , String> {


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            Line line = new Line();
            // Set the line's start and end points
            double x1 = 0;
            double y1 = getHeight() / 2;
            double x2 = getWidth();
            double y2 = getHeight() / 2;
            line.setStartX(x1);
            line.setStartY(y1);
            line.setEndX(x2);
            line.setEndY(y2);
            
            // Set the line's stroke width and color
            line.setStrokeWidth(2);
            line.setStroke(Color.BLACK);
            
            // Add the line to the cell's children
            // getChildren().setAll(line);
            setGraphic(line);
        }
    }

}
