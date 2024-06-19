import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusJogo implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Jogador> jogadores;
    private Mapa mapa;
    private Jogo jogo;
    private int numJogadores = 1;
    private boolean jogoEmAndamento = true;
    private Inimigo inimigo;

    public StatusJogo(){
        this.jogadores = new ArrayList<>();
        this.numJogadores = jogadores.size();
        this.jogoEmAndamento = true;
    }

    public List<Jogador> getJogadores(){
        return jogadores;
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

    public boolean isJogoEmAndamento(){
        return jogoEmAndamento;
    }

    public synchronized void adicionaJogador(Jogador jogador){
        jogadores.add(jogador);
        numJogadores = jogadores.size();
    }

    public synchronized void atualizarPosicaoJogador(String id, int x, int y){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.setPosX(x);
                j.setPosY(y);
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

    public Inimigo getInimigo() {
        return inimigo;
    }

    public void setInimigo(Inimigo inimigo) {
        this.inimigo = inimigo;
    }
}
