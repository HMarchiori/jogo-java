import java.awt.Color;

public class Inimigo implements ElementoMapa {
    private int x;
    private int y;
    private final char simbolo = '☠';
    private final Color cor = Color.RED;

    public Inimigo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        return false;
    }

    @Override
    public boolean podeInteragir() {
        return false;
    }

    @Override
    public String interage() {
        return null;
    }

    public void move(Direcao direcao) {
        switch (direcao) {
            case CIMA:
                y -= 1;
                break;
            case BAIXO:
                y += 1;
                break;
            case ESQUERDA:
                x -= 1;
                break;
            case DIREITA:
                x += 1;
                break;
        }
    }
}
