package com.automate.controller;

public class Message {
    private int idDestinataire;
    private int idExpediteur;
    private String content;

    public Message(int idDestinataire, String content) {
        this.idDestinataire = idDestinataire;
        this.content = content;
    }


    public int getIdDestinataire() {
        return this.idDestinataire;
    }

    public void setIdDestinataire(int idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    public int getIdExpediteur() {
        return this.idExpediteur;
    }

    public void setIdExpediteur(int idExpediteur) {
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
