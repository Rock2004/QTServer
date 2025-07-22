package server;

import data.Data;
import mining.QTMiner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Gestisce la sessione di comunicazione con un singolo client in un thread dedicato.
 * <p>
 * Questa classe è un "worker thread" istanziato da {@link MultiServer} per ogni
 * connessione client accettata. Ascolta le richieste del client in un ciclo,
 * le elabora e invia le risposte. La comunicazione si basa su un protocollo
 * a codici interi (request code) inviati dal client.
 * </p>
 *
 * @see MultiServer
 */
public class ServerOneClient extends Thread {

    /**
     * Il socket di comunicazione specifico per questo client.
     */
    private final Socket socket;
    /**
     * Lo stream per ricevere oggetti (richieste) dal client.
     */
    private final ObjectInputStream in;
    /**
     * Lo stream per inviare oggetti (risposte) al client.
     */
    private final ObjectOutputStream out;

    /**
     * Il dataset attualmente caricato in memoria (es. da una tabella di database).
     * Questo stato viene mantenuto per tutta la durata della sessione del client.
     */
    private Data data;
    /**
     * L'istanza del miner che contiene il risultato di un'operazione di clustering.
     * Viene riutilizzato per operazioni successive, come il salvataggio su file.
     */
    private QTMiner kmeans;

    /**
     * Costruisce e avvia un nuovo thread per gestire la comunicazione con un client.
     * <p>
     * Inizializza gli stream di input/output a partire dal socket fornito e avvia
     * immediatamente il thread chiamando {@code this.start()}.
     * </p>
     *
     * @param s Il socket che rappresenta la connessione al client,
     * tipicamente ottenuto da {@link java.net.ServerSocket#accept()}.
     * @throws IOException se la creazione degli stream di input o output fallisce.
     */
    public ServerOneClient(Socket s) throws IOException {
        this.socket = s;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
        System.out.println("[SERVER-THREAD] Stream per il client " + s.getInetAddress() + " inizializzati.");
        this.start();
    }

    /**
     * Esegue il ciclo di vita principale del thread, gestendo le richieste del client.
     * <p>
     * Si mette in un ciclo infinito in attesa di ricevere un codice intero dal client.
     * A seconda del codice ricevuto, esegue una delle seguenti operazioni:
     * <ul>
     * <li><b>0:</b> Carica un dataset da una tabella di un database.</li>
     * <li><b>1:</b> Esegue l'algoritmo di clustering (QTMiner) sul dataset in memoria.</li>
     * <li><b>2:</b> Salva il risultato del clustering su un file.</li>
     * <li><b>3:</b> Carica un risultato di clustering pre-calcolato da un file.</li>
     * </ul>
     * Il ciclo termina e il thread si arresta quando il client chiude la connessione,
     * causando una {@link java.net.SocketException}. Il blocco {@code finally} assicura che
     * il socket venga sempre chiuso correttamente.
     * </p>
     */
    @Override
    public void run() {
        try {
            while (true) {
                int requestCode = (Integer) in.readObject();
                System.out.println("Server: Ricevuta richiesta [" + requestCode + "]");

                switch (requestCode) {
                    case 0: // Richiesta di caricare dati da DB
                        try {
                            String tableName = (String) in.readObject();
                            System.out.println("Server: Caricamento dati da tabella '" + tableName + "'...");
                            this.data = new Data(tableName);
                            out.writeObject("OK");
                            System.out.println("Server: Dati caricati con successo.");
                        } catch (Exception e) {
                            out.writeObject("ERROR: " + e.getMessage());
                        }
                        break;

                    case 1: // Richiesta di eseguire clustering
                        try {
                            if (this.data == null) throw new IllegalStateException("Nessun dato caricato su cui eseguire il clustering.");
                            double radius = (Double) in.readObject();
                            System.out.println("Server: Esecuzione clustering con raggio " + radius + "...");
                            this.kmeans = new QTMiner(radius);
                            int numClusters = kmeans.compute(this.data);

                            out.writeObject("OK");
                            out.writeObject(numClusters);
                            out.writeObject(kmeans.getC().toString(this.data));
                            System.out.println("Server: Clustering completato.");
                        } catch (Exception e) {
                            out.writeObject("ERROR: " + e.getMessage());
                        }
                        break;

                    case 2: // Richiesta di salvare i risultati su file
                        try {
                            if (this.kmeans == null) throw new IllegalStateException("Nessun risultato di clustering da salvare.");
                            String fileName = (String) in.readObject();
                            System.out.println("Server: Salvataggio cluster su file '" + fileName + "'...");
                            this.kmeans.salva(fileName);
                            out.writeObject("OK");
                            System.out.println("Server: Salvataggio completato.");
                        } catch (Exception e) {
                            out.writeObject("ERROR: " + e.getMessage());
                        }
                        break;

                    case 3: // Richiesta di caricare risultati da file
                        try {
                            String fileNameToLoad = (String) in.readObject();
                            System.out.println("Server: Caricamento cluster da file '" + fileNameToLoad + "'...");
                            this.kmeans = new QTMiner(fileNameToLoad);

                            out.writeObject("OK");
                            // Invia la rappresentazione dei cluster caricati
                            out.writeObject(this.kmeans.getC().toString());
                             System.out.println("Server: Caricamento da file completato.");
                        } catch (FileNotFoundException e) {
                            out.writeObject("ERROR: File non trovato sul server. Dettagli: " + e.getMessage());
                        } catch (Exception e) {
                            out.writeObject("ERROR: " + e.getMessage());
                        }
                        break;

                    default:
                        out.writeObject("ERROR: Codice richiesta non valido.");
                        break;
                }
            }
        } catch (SocketException e) {
            // Eccezione normale che si verifica quando il client chiude la connessione.
            System.out.println("Server: Client " + socket.getInetAddress() + " si è disconnesso.");
        } catch (IOException | ClassNotFoundException e) {
            // Errore più grave (es. stream corrotto, classe non trovata)
            System.err.println("Server: Errore di comunicazione con il client " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            // Assicura che le risorse vengano sempre rilasciate
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del socket: " + e.getMessage());
            }
        }
    }
}