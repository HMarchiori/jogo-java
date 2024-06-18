import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class StatusJogo implements Serializable {
    private static StatusJogo instancia;
    private static final long serialVersionUID = 1L;
    private List<Inimigo> inimigos;
    private List<Jogador> jogadores;
    private List<Moeda> moedas;
    private int numeroMoedas = 0;
    private Mapa mapa;
    private Jogo jogo;
    private int numJogadores = 1;
    private int numInimigos = 1;
    private boolean jogoEmAndamento = true;

    public StatusJogo(){
        this.jogadores = new ArrayList<>();
        this.inimigos = new ArrayList<>();
        this.moedas = new ArrayList<>();
        this.numeroMoedas = moedas.size();
        this.numJogadores = jogadores.size();
        this.numInimigos = inimigos.size();
        this.jogoEmAndamento = true;
    }

    // Método público estático para obter a instância
     public static StatusJogo getInstancia() {
        if (instancia == null) {
            instancia = new StatusJogo();
        }
        return instancia;
    }

    public List<Jogador> getJogadores(){
        return jogadores;
    }

    public List<Inimigo> getInimigos(){
        return inimigos;
    }

    public List<Moeda> getMoedas(){
        return moedas;
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

    public int getNumeroMoedas(){
        return numeroMoedas;
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

    public synchronized void atualizarPosicaoMoeda(Moeda moeda, int x, int y){
        moeda.setPosicao(x, y);
    }

    public synchronized void atualizarPosicaoInimigo(Inimigo inimigo, int x, int y){
        inimigo.setPosicao(x, y);
    }

    public synchronized void removerMoeda(Moeda moeda){
        moedas.remove(moeda);
        numeroMoedas = moedas.size();
    }

    public synchronized void atualizarNumeroMoedas(String id){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.incrementaMoedas();
            }
        }
    }

}
