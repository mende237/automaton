package com.utils;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleTableCell<T> extends TableCell<T, String> {
    private final Circle circle;
    private final Label label;

    public CircleTableCell() {
        this.circle = new Circle(10);
        this.label = new Label();
        setGraphic(new HBox(circle, label));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            circle.setFill(Color.TRANSPARENT);
            label.setText("");
        } else {
            circle.setFill(Color.GREEN);
            label.setText(item);
        }
    }

}
