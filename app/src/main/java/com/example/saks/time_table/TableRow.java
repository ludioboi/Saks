package com.example.saks.time_table;

public class TableRow {
    public String uhrzeit, fach, lehrer, raum, tag;
    public int klasse, doppelstunde;

    public TableRow(String uhrzeit, String fach, String lehrer, int klasse, String raum, String tag, int doppelstunde) {
        this.uhrzeit = uhrzeit;
        this.fach = fach;
        this.lehrer = lehrer;
        this.klasse = klasse;
        this.raum = raum;
        this.tag = tag;
        this.doppelstunde = doppelstunde;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public String getFach() {
        return fach;
    }

    public String getLehrer() {
        return lehrer;
    }

    public int getKlasse() {
        return klasse;
    }

    public String getRaum() {
        return raum;
    }

    public String getTag() {
        return tag;
    }

    public int getDoppelstunde() {
        return doppelstunde;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDoppelstunde(int doppelstunde) {
        this.doppelstunde = doppelstunde;
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "uhrzeit='" + uhrzeit + '\'' +
                ", fach='" + fach + '\'' +
                ", lehrer='" + lehrer + '\'' +
                '}';
    }
}