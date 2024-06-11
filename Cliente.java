import java.rmi.registry.Registry;

public class Cliente {
    private InterfaceServidor servidor;
    private StatusJogo statusJogo;
    private String clientId;

    private Cliente(String enderecoServidor) {
        try {
            Registry registry = java.rmi.registry.LocateRegistry.getRegistry(enderecoServidor);
            servidor = (InterfaceServidor) registry.lookup("servidor");
            this.clientId = java.util.UUID.randomUUID().toString();
            servidor.registrarCliente(clientId);
            new Thread(this::atualizarStatusJogo).start();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarStatusJogo() {
        while (true) {
            try {
                statusJogo = servidor.getStatusJogo();
                System.out.println("Status do jogo: " + statusJogo);
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Cliente <endereco_servidor>");
            System.exit(1);
        }
        new Cliente(args[0]);
    }
}
