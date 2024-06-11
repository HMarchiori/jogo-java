import java.io.Serializable;

public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private int posX;
    private int posY;
    private int vida;
    private int moedas;

    public Jogador(String id) {
        this.id = id;
        this.posX = 50;
        this.posY = 50;
        this.vida = 3;
        this.moedas = 0;
    }

    public String getId() {
        return id;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void incrementaMoedas() {
        moedas++;
    }

    public void decrementaVida() {
        vida--;
    }
    
}
