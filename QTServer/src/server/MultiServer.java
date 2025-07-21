package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server principale che accetta connessioni multiple da client.
 * Per ogni client, avvia un thread ServerOneClient dedicato.
 */
public class MultiServer {

    private final int PORT;

    public MultiServer(int port) {
        this.PORT = port;
    }

    /**
     * Avvia il server mettendolo in ascolto di connessioni client.
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("MultiServer in ascolto sulla porta: " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connessione accettata da: " + clientSocket.getInetAddress());
                    new ServerOneClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Errore: Impossibile accettare la connessione client. " + e.getMessage());
                    // Continua ad ascoltare per altri client
                }
            }
        } catch (IOException e) {
            System.err.println("Errore fatale: Impossibile avviare il server sulla porta " + PORT + ". " + e.getMessage());
        }
    }

    /**
     * Punto di ingresso per avviare il server.
     * @param args Argomenti da riga di comando (es. porta).
     */
    public static void main(String[] args) {
        int port = 8080; // Porta di default
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argomento porta non valido, uso la porta di default 8080.");
            }
        }
        new MultiServer(port).run();
    }
}