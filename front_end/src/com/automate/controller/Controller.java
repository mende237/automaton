package com.automate.controller;

public abstract class Controller {
    protected Mediator mediator;
    private static int cmpt = 0;
    protected int id;

    public Controller(Mediator mediator){
        this.id = Controller.cmpt;
        this.mediator = mediator;
        Controller.cmpt++;
    }

    public abstract void sendMessage(Message message);
    public abstract void receiveMessage(Message message);


    public int getId(){
        return this.id;
    }

    public void setMediator(Mediator mediator){
        this.mediator = mediator;
        this.mediator.addController(this.id, this);
    }

    public Mediator getMediator(){
        return this.mediator;
    }
}

