package com.example.app;

import android.widget.ListView;

import java.util.List;

public class ConcertComplet {
    // Atributs
    public String Nom;
    public String Data;
    public String Desc;
    public String Localitzacio;
    public String Poblacio;
    public String Web;
    public String Preu;
    public List<String> Artistes;

    public ConcertComplet() {
    }

    public ConcertComplet(String Nom, String Data, String Desc, String Localitzacio, String Poblacio, String Web,
                   String Preu, List<String> Artistes) {
        this.Nom = Nom;
        this.Data = Data;
        this.Desc = Desc;
        this.Localitzacio = Localitzacio;
        this.Poblacio = Poblacio;
        this.Web = Web;
        this.Preu = Preu;
        this.Artistes = Artistes;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public String getData() { return Data; }

    public void setData(String Data) { this.Data = Data; }

    public String getDesc() { return Desc; }

    public void seDesc(String Desc) { this.Desc = Desc; }

    public String getLocalitzacio() { return Localitzacio; }

    public void setLocalitzacio(String Localitzacio) { this.Localitzacio = Localitzacio; }

    public String getPoblacio() {
        return Poblacio;
    }

    public void setPoblacio(String Nom) {
        this.Nom = Nom;
    }

    public String getWeb() { return Web; }

    public void setWeb(String Web) { this.Web = Web; }

    public String getPreu() { return Preu; }

    public void sePreu(String Preu) { this.Preu = Preu; }

    public List<String> getArtistes() {
        return Artistes;
    }

    public void setArtistes(List<String> Artistes) { this.Artistes = Artistes; }
}
