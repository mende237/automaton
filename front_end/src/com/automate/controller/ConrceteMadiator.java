package com.automate.controller;

public class ConrceteMadiator extends Mediator{
    private static ConrceteMadiator mediator = null;
    private ConrceteMadiator(){

    }

    public static ConrceteMadiator getConrceteMadiator(){
        if(ConrceteMadiator.mediator == null){
            ConrceteMadiator.mediator = new ConrceteMadiator();
        }

        return ConrceteMadiator.mediator;
    }

    @Override
    public void transmitMessage(Message message) {
       Controller receiver = super.controllers.get(message.getIdDestinataire());
       receiver.receiveMessage(message);
    }
    
}
