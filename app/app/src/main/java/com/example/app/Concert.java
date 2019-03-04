package com.example.app;

public class Concert {
    // Atributs
    private String Nom;
    private String Data;
    private String Lloc;

    public Concert() {
    }

    public Concert(String Nom, String Lloc, String Data) {
        this.Nom = Nom;
        this.Lloc = Lloc;
        this.Data = Data;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getLloc() {
        return Lloc;
    }

    public void setLloc(String Lloc) {
        this.Lloc = Lloc;
    }

    public String getData() {
        return Data;
    }


    public void setData(String Data) {
        this.Data = Data;
    }
}