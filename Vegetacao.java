import java.awt.Color;
import java.io.Serializable;

public class Vegetacao implements ElementoMapa , Serializable {
    private Color cor;
    private Character simbolo;

    public Vegetacao(Character simbolo, Color cor) {
        this.simbolo = simbolo;
        this.cor = cor;
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
}