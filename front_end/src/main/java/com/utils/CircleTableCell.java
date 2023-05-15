package com.utils;


import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import com.automate.structure.State;

public class CircleTableCell extends TableCell<State, String> {
    private final StackPane stackPane = new StackPane();
    private final Circle circle = new Circle(10);
    private final Text text = new Text();

    public CircleTableCell() {
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().addAll(circle, text);
        setGraphic(stackPane);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            State state = getTableView().getItems().get(getIndex());
            double radius = 10;
            if (state.isInitial()) {
                circle.setFill(Color.RED);
            } else if (state.isFinalState()) {
                circle.setFill(Color.BLUE);
            } else {
                circle.setFill(Color.BROWN);
            }
            circle.setRadius(radius);
            text.setText(state.getName());
            text.setFont(Font.font(radius * 0.8));
            if (radius < 20) {
                text.setVisible(false);
            } else {
                text.setVisible(true);
            }
        }
    }
}
