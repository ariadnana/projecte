package com.example.app;

import android.widget.ListView;

import java.util.List;

public class ConcertComplet {
    // Atributs
    public String Nom;
    public String Dia;
    public String Mes;
    public String Hora;
    public String Desc;
    public String Localitzacio;
    public String Poblacio;
    public String Web;
    public String Preu;
    public List<String> Artistes;

    public ConcertComplet() {
    }

    public ConcertComplet(String Nom, String Dia, String Mes, String Hora, String Desc, String Localitzacio, String Poblacio, String Web,
                   String Preu, List<String> Artistes) {
        this.Nom = Nom;
        this.Dia = Dia;
        this.Mes = Mes;
        this.Hora = Hora;
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

    public String getDia() { return Dia; }

    public void setDia(String Dia) { this.Dia = Dia; }

    public String getMes() { return Mes; }

    public void setMes(String Mes) { this.Mes = Mes; }

    public String getHora() { return Hora; }

    public void setHora(String Hora) { this.Hora = Hora; }

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
