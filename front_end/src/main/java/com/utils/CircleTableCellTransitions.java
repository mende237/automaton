package com.utils;

import com.automate.structure.State;
import com.automate.structure.Transition;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;



public class CircleTableCellTransitions extends TableCell<Transition, State> {

    public enum ColumnName{
        FROM , TO
    }

    private ColumnName columnName;

    public CircleTableCellTransitions(ColumnName columnName){
        this.columnName = columnName;
    }

    @Override
    protected void updateItem(State item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            Circle circle = new Circle();
            setTextFill(Color.BLACK);
            setContentDisplay(ContentDisplay.CENTER);
            setGraphic(circle);

            setText(item.getName());
            Transition transition = getTableView().getItems().get(getIndex());
            State state = null;
            if(columnName == ColumnName.FROM){
                state = transition.getBegin();
            }else{
                state = transition.getEnd();
            }
            
            // System.out.println("column " + columnName);
            double radius = Math.max(20, state.getName().length() * 5);
            circle.setRadius(radius);
            circle.setStroke(Color.BLACK);
            if (state.isInitial()) {
                circle.setFill(Color.web("#00cc66"));
            } else if (state.isFinalState()) {
                circle.setFill(Color.web("#ff3300"));
            } else {
                circle.setFill(Color.web("#4286f4"));
            }
        }
    }
    
}

