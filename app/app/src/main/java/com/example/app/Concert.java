package com.example.app;

public class Concert {
    // Atributs
    public Integer Id;
    public String Nom;
    public String Data;
    public String Lloc;

    public Concert() {
    }

    public Concert(Integer Id, String Nom, String Lloc, String Data) {
        this.Id = Id;
        this.Nom = Nom;
        this.Lloc = Lloc;
        this.Data = Data;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
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