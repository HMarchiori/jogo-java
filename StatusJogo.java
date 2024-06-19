import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class StatusJogo implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Jogador> jogadores;
    private List<Inimigo> inimigos;
    private Mapa mapa;
    private Jogo jogo;
    private int numJogadores = 1;
    private int numInimigos = 1;
    private boolean jogoEmAndamento = true;

    public StatusJogo(){
        this.jogadores = new ArrayList<>();
        this.inimigos = new ArrayList<>();
        this.numJogadores = jogadores.size();
        this.numInimigos = inimigos.size();
        this.jogoEmAndamento = true;
    }

    public List<Jogador> getJogadores(){
        return jogadores;
    }

    public List<Inimigo> getInimigos(){
        return inimigos;
    }

    public Mapa getMapa(){
        return mapa;
    }

    public Jogo getJogo(){
        return jogo;
    }

    public int getNumJogadores(){
        return numJogadores;
    }

    public int getNumInimigos(){
        return numInimigos;
    }

    public boolean isJogoEmAndamento(){
        return jogoEmAndamento;
    }

    public synchronized void adicionaJogador(Jogador jogador){
        jogadores.add(jogador);
        numJogadores = jogadores.size();
    }

    public synchronized void adicionaInimigo(Inimigo inimigo){
        inimigos.add(inimigo);
        numInimigos = inimigos.size();
    }

    public synchronized void atualizarPosicaoJogador(String id, int x, int y){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.setPosX(x);
                j.setPosY(y);
            }
        }
    }

    public synchronized void atualizarPosicaoInimigo(String id, int x, int y) {
        for (Inimigo i : inimigos) {
            if (i.getId().equals(id)) {
                i.setPosicao(x, y);
            }
        }
    }

    public synchronized void atualizarNumeroMoedas(String id){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.incrementaMoedas();
            }
        }
    }

}
