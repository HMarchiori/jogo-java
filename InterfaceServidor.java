import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServidor extends Remote {
    void registrarCliente(String clientId) throws RemoteException;
    void enviarComandoJogadores(String clientId, int numeroSequente, int posX, int posY) throws RemoteException;
    void enviarComandoInimigos(Inimigo inimigo, int numeroSequente, int posX, int posY) throws RemoteException;
    void enviarComandoMoedas(Moeda moeda, int numeroSequente, Integer posX, Integer posY) throws RemoteException;
    void removerMoedas(Moeda moeda) throws RemoteException;
    StatusJogo getStatusJogo() throws RemoteException;
}
