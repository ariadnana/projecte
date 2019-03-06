package com.example.app;

public class Concert {
    // Atributs
    public String Nom;
    public String Data;
    public String Lloc;

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