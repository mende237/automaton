package com.automate.controller;

public class Message {
    private String idDestinataire;
    private String idExpediteur;
    private String content;

    public Message(String idDestinataire, String content) {
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return "l'expediteur est " + this.idExpediteur + "\nle contenu est " + this.content + "\nle destinataire est " + this.idDestinataire;
    }
}
