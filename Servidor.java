import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.HashMap;


public class Servidor extends UnicastRemoteObject implements InterfaceServidor {
    private static final long serialVersionUID = 1L;
    private StatusJogo statusJogo;
    private Map<String, Integer> numerosSequentes;

    public Servidor() throws RemoteException {
        statusJogo = new StatusJogo();
        numerosSequentes = new HashMap<>();
    }

    public synchronized void registrarCliente(String clientId) throws RemoteException {
        if(!numerosSequentes.containsKey(clientId)) {
            numerosSequentes.put(clientId, 0);
            Jogador jogador = new Jogador(clientId);
            statusJogo.adicionaJogador(jogador);
        }
    }

    public synchronized void enviarComandoJogadores(String clientId, int numeroSequente, int posX, int posY) throws RemoteException {
        if(numerosSequentes.get(clientId) < numeroSequente) {
            numerosSequentes.put(clientId, numeroSequente);
            statusJogo.atualizarPosicaoJogador(clientId, posX, posY);
        }
    }

    public synchronized void enviarComandoInimigos(Inimigo inimigo, int numeroSequente, int posX, int posY) throws RemoteException {
        statusJogo.atualizarPosicaoInimigo(inimigo,posX, posY);
    }

    public synchronized void enviarComandoMoedas(Moeda moeda, int numeroSequente, Integer posX, Integer posY) throws RemoteException {
        if (posX == null || posY == null) {
            removerMoedas(moeda);
        } else {
            statusJogo.atualizarPosicaoMoeda(moeda, posX, posY);
        }
    }

    public synchronized void removerMoedas(Moeda moeda) throws RemoteException {
        statusJogo.removerMoeda(moeda);
    }

    public synchronized StatusJogo getStatusJogo() throws RemoteException {
        return statusJogo;
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.registry.LocateRegistry.getRegistry().rebind("servidor", servidor);
            System.out.println("Servidor pronto");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


