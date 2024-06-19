import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Servidor extends UnicastRemoteObject implements InterfaceServidor {
    private static final long serialVersionUID = 1L;
    private StatusJogo statusJogo;
    private Map<String, Integer> numerosSequentes;

    public Servidor() throws RemoteException {
        statusJogo = new StatusJogo();
        numerosSequentes = new HashMap<>();
        // Inicializando o inimigo na posição (5, 5)
        Inimigo inimigo = new Inimigo(5, 5);
        statusJogo.setInimigo(inimigo);
    }

    public synchronized void registrarCliente(String clientId) throws RemoteException {
        if (!numerosSequentes.containsKey(clientId)) {
            numerosSequentes.put(clientId, 0);
            Jogador jogador = new Jogador(clientId);
            statusJogo.adicionaJogador(jogador);
        }
    }

    public synchronized void enviarComando(String clientId, int numeroSequente, int posX, int posY) throws RemoteException {
        if (numerosSequentes.get(clientId) < numeroSequente) {
            numerosSequentes.put(clientId, numeroSequente);
            statusJogo.atualizarPosicaoJogador(clientId, posX, posY);
        }
    }

    public synchronized StatusJogo getStatusJogo() throws RemoteException {
        return statusJogo;
    }

    // Adicione lógica de movimentação do inimigo aqui
    public synchronized void movimentaInimigo() {
        // Obtém o inimigo do status do jogo
        Inimigo inimigo = statusJogo.getInimigo();
        if (inimigo != null) {
            // Lógica de movimentação do inimigo
            Random random = new Random();
            int direction = random.nextInt(4);
            switch (direction) {
                case 0:
                    inimigo.move(Direcao.CIMA);
                    break;
                case 1:
                    inimigo.move(Direcao.BAIXO);
                    break;
                case 2:
                    inimigo.move(Direcao.ESQUERDA);
                    break;
                case 3:
                    inimigo.move(Direcao.DIREITA);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.registry.LocateRegistry.getRegistry().rebind("servidor", servidor);
            System.out.println("Servidor pronto");

            // Inicie um thread para mover o inimigo periodicamente
            new Thread(() -> {
                while (true) {
                    try {
                        servidor.movimentaInimigo();
                        Thread.sleep(1000); // ajuste o tempo conforme necessário
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
