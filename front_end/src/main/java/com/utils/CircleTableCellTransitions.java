package com.utils;

import com.automate.structure.State;
import com.automate.structure.Transition;


import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;




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

            // if (state.isInitial()) {
            //     circle.setFill(Color.web("#00cc66"));
            // } else if (state.isFinalState()) {
            //     circle.setFill(Color.web("#ff3300"));
            // } else {
            //     circle.setFill(Color.web("#4286f4"));
            // }
        }
    }
    
}

