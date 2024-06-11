import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServidor extends Remote {
    void registrarCliente(String clientId) throws RemoteException;
    void enviarComando(String clientId, int numeroSequente, int posX, int posY) throws RemoteException;
    StatusJogo getStatusJogo() throws RemoteException;
}
