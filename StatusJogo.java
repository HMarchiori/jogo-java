import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class StatusJogo implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Jogador> jogadores;
    private Mapa mapa;
    private int numJogadores = 1;
    private boolean jogoEmAndamento = true;

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

    public int getNumJogadores(){
        return numJogadores;
    }

    public boolean isJogoEmAndamento(){
        return jogoEmAndamento;
    }

    public void adicionaJogador(Jogador jogador){
        jogadores.add(jogador);
        numJogadores = jogadores.size();
    }

    public void atualizarPosicaoJogador(String id, int x, int y){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.setPosX(x);
                j.setPosY(y);
            }
        }
    }

    public void atualizarNumeroMoedas(String id){
        for(Jogador j : jogadores){
            if(j.getId().equals(id)){
                j.incrementaMoedas();
            }
        }
    }
    

}
