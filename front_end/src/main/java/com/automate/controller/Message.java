package com.automate.controller;

import java.util.HashMap;

public class Message {
    private String idDestinataire;
    private String idExpediteur;
    private HashMap<String, ? extends Object> content;

    public Message(String idDestinataire, HashMap<String, ? extends Object> content) {
        this.idDestinataire = idDestinataire;
        this.content = content;
    }


    public String getIdDestinataire() {
        return this.idDestinataire;
    }

    public void setIdDestinataire(String idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    public String getIdExpediteur() {
        return this.idExpediteur;
    }

    public void setIdExpediteur(String idExpediteur) {
        this.idExpediteur = idExpediteur;
    }

    public HashMap<String, ? extends Object> getContent() {
        return this.content;
    }

    public void setContent(HashMap<String, ? extends Object> content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return "l'expediteur est " + this.idExpediteur + "\nle contenu est " + this.content + "\nle destinataire est " + this.idDestinataire;
    }
}
