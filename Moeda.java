import java.awt.Color;
import java.io.Serializable;

public class Moeda implements ElementoMapa, Serializable {
    private Color cor;
    private Character simbolo;
    private int x;
    private int y;

    public Moeda(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
        this.x = 1;
        this.y = 1;
    }
    
    @Override
    public Character getSimbolo() {
        return simbolo;
    }

    @Override
    public Color getCor() {
        return cor;
    }

    @Override
    public boolean podeSerAtravessado() {
        return true;
    }

    @Override
    public boolean podeInteragir() {
        return false;
    }

    @Override
    public String interage() {
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosicao(int x, int y) {
        this.x = x;
        this.y = y;
    }
}