package Gestione_auto;


public class Auto {
    private String colore;
    private String matricola;
    private int velocita;

    public Auto(String colore, String matricola, int velocita) {
        this.colore = colore;
        this.matricola = matricola;
        this.velocita = velocita;
        
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public int getVelocita() {
        return velocita;
    }

    public void setVelocita(int velocita) {
        this.velocita = velocita;
    }
}
