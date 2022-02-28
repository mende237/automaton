package com.automate.controller;

import java.util.HashMap;
import java.util.Map;

public abstract class Mediator {
    protected Map<String , Controller> controllers = new HashMap<>();

    public void addController(String id , Controller controller){
        this.controllers.put(id , controller);
    }

    public abstract void transmitMessage(Message message);
}
