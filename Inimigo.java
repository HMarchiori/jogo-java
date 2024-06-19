import java.awt.Color;
import java.util.UUID;

public class Inimigo implements ElementoMapa, Runnable {
    private String id;
    private Color cor;
    private Character simbolo;
    private int x = 1;
    private int y = 1;
    private Jogo jogo;
    private Thread thread;
    private boolean movendoDireita = true;
    private boolean moverParaBaixo = true;

    public Inimigo(Character simbolo, Color cor, Jogo jogo) {
        this.id = UUID.randomUUID().toString();
        this.simbolo = simbolo;
        this.cor = cor;
        this.jogo = jogo;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public String getId() {
        return id;
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
                Thread.sleep(300);
                moverInimigo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moverInimigo() {
        boolean movido = false;

        if (movendoDireita) {
            // Tentar mover para a direita
            if (jogo.getMapa().moveElemento(x, y, x + 1, y)) {
                setPosicao(x + 1, y);
                movido = true;
            } else {
                // Se não puder mover para a direita, mover para baixo ou para cima
                if (moverParaBaixo) {
                    if (jogo.getMapa().moveElemento(x, y, x, y + 1)) {
                        setPosicao(x, y + 1);
                        movendoDireita = false;
                        movido = true;
                    }
                } else {
                    if (jogo.getMapa().moveElemento(x, y, x, y - 1)) {
                        setPosicao(x, y - 1);
                        movendoDireita = false;
                        movido = true;
                    }
                }
            }
        } else {
            // Tentar mover para a esquerda
            if (jogo.getMapa().moveElemento(x, y, x - 1, y)) {
                setPosicao(x - 1, y);
                movido = true;
            } else {
                // Se não puder mover para a esquerda, mover para baixo ou para cima
                if (moverParaBaixo) {
                    if (jogo.getMapa().moveElemento(x, y, x, y + 1)) {
                        setPosicao(x, y + 1);
                        movendoDireita = true;
                        movido = true;
                    }
                } else {
                    if (jogo.getMapa().moveElemento(x, y, x, y - 1)) {
                        setPosicao(x, y - 1);
                        movendoDireita = true;
                        movido = true;
                    }
                }
            }
        }

        // Se estiver bloqueado (incapaz de se mover), alternar moverParaBaixo e tentar novamente
        if (!movido) {
            moverParaBaixo = !moverParaBaixo;
        }

        jogo.verificaProximidade(this);
        jogo.repaint();
    }
}
