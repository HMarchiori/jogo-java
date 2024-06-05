import java.awt.Color;

public class Inimigo implements ElementoMapa, Runnable {
    private Color cor;
    private Character simbolo;
    private int x = 1;
    private int y = 1;
    private Jogo jogo;
    private Thread thread;

    public Inimigo(Character simbolo, Color cor, Jogo jogo) {
        this.simbolo = simbolo;
        this.cor = cor;
        this.jogo = jogo;
        this.thread = new Thread(this);
        this.thread.start();
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

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500); // Move every second
                moveInimigo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveInimigo() {
        int dx = x + 1;
        int dy = y;
        if (!jogo.getMapa().moveElemento(x, y, dx, dy)) {
            dx = x - 1;
            dy = y;
            if (!jogo.getMapa().moveElemento(x, y, dx, dy)) {
                dx = x;
                dy = y - 1;
                if (!jogo.getMapa().moveElemento(x, y, dx, dy)) {
                    dx = x;
                    dy = y + 1;
                    if (!jogo.getMapa().moveElemento(x, y, dx, dy)) {
                        return;
                    }
                }
            }
        }
        setPosicao(dx, dy);
        jogo.repaint();
    }
}
