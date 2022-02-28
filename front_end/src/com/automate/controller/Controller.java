package com.automate.controller;

public abstract class Controller {
    protected Mediator mediator;
    protected String id;

    public Controller(String id , Mediator mediator){
        this.id = id;
        this.mediator = mediator;
        this.mediator.addController(this.id, this);
    }

    public abstract void sendMessage(Message message);
    public abstract void receiveMessage(Message message);


    public String getId(){
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

