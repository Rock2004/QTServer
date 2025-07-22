package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Gestisce un server multi-threaded in grado di accettare connessioni multiple.
 * <p>
 * Questa classe apre una {@link ServerSocket} su una porta specifica e si mette in
 * ascolto di connessioni client in un ciclo infinito. Per ogni connessione accettata,
 * istanzia e avvia un nuovo thread {@link ServerOneClient} che gestirà la
 * comunicazione con quel singolo client, permettendo al server principale di rimanere
 * disponibile per nuove connessioni.
 * </p>
 *
 * @see ServerOneClient
 */
public class MultiServer {

    /**
     * La porta di rete sulla quale il server rimane in ascolto.
     */
    private final int PORT;

    /**
     * Costruisce una nuova istanza di MultiServer.
     *
     * @param port La porta di rete su cui il server accetterà le connessioni.
     */
    public MultiServer(int port) {
        this.PORT = port;
    }

    /**
     * Avvia il ciclo di vita del server.
     * <p>
     * Apre la {@code ServerSocket} e si mette in un ciclo infinito di attesa ({@code accept()}).
     * Per ogni richiesta, delega la gestione del client a un nuovo {@code ServerOneClient}.
     * Gestisce internamente le {@code IOException} sia in fase di avvio del server
     * sia durante l'accettazione di una singola connessione, stampando messaggi di errore
     * senza interrompere il servizio.
     * </p>
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("MultiServer in ascolto sulla porta: " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connessione accettata da: " + clientSocket.getInetAddress());
                    // Avvia un nuovo gestore per il client appena connesso.
                    // Si assume che ServerOneClient estenda Thread o implementi Runnable
                    // e che il suo costruttore avvii il thread.
                    new ServerOneClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Errore: Impossibile accettare la connessione client. " + e.getMessage());
                    // Il ciclo continua per poter accettare i client successivi.
                }
            }
        } catch (IOException e) {
            System.err.println("Errore fatale: Impossibile avviare il server sulla porta " + PORT + ". " + e.getMessage());
            // Se la ServerSocket non può essere creata, l'applicazione termina.
        }
    }

    /**
     * Punto di ingresso principale per l'applicazione server.
     * <p>
     * Analizza gli argomenti della riga di comando per determinare la porta,
     * istanzia e avvia il {@code MultiServer}.
     * </p>
     *
     * @param args Argomenti della riga di comando. Se fornito, {@code args[0]} dovrebbe
     * contenere il numero di porta da utilizzare.
     */
    public static void main(String[] args) {
        // Imposta una porta di default nel caso non venga fornita.
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argomento porta '" + args[0] + "' non valido. Utilizzo della porta di default 8080.");
            }
        }
        new MultiServer(port).run();
    }
}