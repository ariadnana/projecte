package com.example.app;

public class Concert {
    // Atributs
    public Integer Id;
    public String Nom;
    public Integer Dia;
    public String Mes;
    public String Hora;
    public String Preu;
    public String Lloc;

    public Concert() {
    }

    public Concert(Integer Id, String Nom, Integer Dia, String Mes, String Hora, String Lloc, String Preu) {
        this.Id = Id;
        this.Nom = Nom;
        this.Lloc = Lloc;
        this.Dia = Dia;
        this.Mes = Mes;
        this.Hora = Hora;
        this.Preu = Preu;
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

    public Integer getDia() {
        return Dia;
    }

    public void setDia(Integer Dia) {
        this.Dia = Dia;
    }

    public String getMes() {
        return Mes;
    }

    public void setMes(String Mes) {
        this.Mes = Mes;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String Hora) { this.Hora = Hora; }

    public String getPreu() {
        return Preu;
    }

    public void setPreu(String Preu) {
        this.Preu = Preu;
    }

    public String getLloc() {
        return Lloc;
    }

    public void setLloc(String Lloc) {
        this.Lloc = Lloc;
    }
}