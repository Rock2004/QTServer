package mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import data.Data;
import data.Tuple;
import data.EmptyDatasetException;

/**
 * La classe QTMiner implementa l'algoritmo Quality Threshold (QT) per il clustering di dati.
 * Permette di calcolare i cluster da un dataset, di salvare l'insieme di cluster
 * risultante su file e di caricare un insieme di cluster da file.
 */
public class QTMiner {

    /**
     * L'insieme di cluster ({@link ClusterSet}) scoperti o caricati.
     * Questo campo viene serializzato quando si salva l'istanza di QTMiner
     * (anche se qui si serializza direttamente C).
     */
    private ClusterSet C;

    /**
     * Il raggio utilizzato per definire il vicinato sferico durante il clustering.
     * Questo campo non viene serializzato direttamente con i metodi salva/carica forniti,
     * i quali si concentrano solo su {@code C}.
     */
    private double radius;

    /**
     * Costruisce una nuova istanza di QTMiner con un raggio specificato.
     * Inizializza un nuovo {@link ClusterSet} vuoto e imposta il raggio
     * per l'algoritmo di clustering.
     *
     * @param radius Il raggio da utilizzare per il calcolo dei cluster.
     * Un valore positivo è atteso.
     */
    public QTMiner(double radius) {
        C = new ClusterSet();
        this.radius = radius;
    }

    /**
     * Costruisce una nuova istanza di QTMiner caricando un {@link ClusterSet} serializzato
     * da un file.
     *
     * @param fileName Il nome del file da cui caricare l'insieme di cluster serializzato.
     * @throws FileNotFoundException Se il file specificato non viene trovato.
     * @throws IOException Se si verifica un errore di I/O durante la lettura dal file.
     * @throws ClassNotFoundException Se la classe dell'oggetto serializzato
     * non può essere trovata.
     */
    public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream inFile = new FileInputStream(fileName);
        ObjectInputStream inStream = new ObjectInputStream(inFile);
        C = (ClusterSet) inStream.readObject();
        radius=0;
        inStream.close();
    }

    /**
     * Salva l'insieme di cluster corrente ({@code C}) in un file specificato
     * utilizzando la serializzazione Java.
     *
     * @param fileName Il nome del file in cui salvare l'insieme di cluster.
     * @throws FileNotFoundException Se il file non può essere creato o aperto per la scrittura.
     * @throws IOException Se si verifica un errore di I/O durante la scrittura sul file.
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream outFile = new FileOutputStream(fileName);
        ObjectOutputStream outStream = new ObjectOutputStream(outFile);
        outStream.writeObject(C); // Serializza l'oggetto ClusterSet C
        outStream.close();
    }
    
    /**
     * Restituisce l'insieme di cluster ({@link ClusterSet}) attualmente gestito da questa istanza di QTMiner.
     * Potrebbe essere il risultato di un'operazione di {@code compute} o caricato da file.
     *
     * @return L'oggetto {@link ClusterSet} contenente i cluster.
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo di clustering QT sul dataset fornito.
     * L'algoritmo identifica iterativamente i cluster basati sul raggio specificato
     * e popola l'insieme {@code C} con i cluster trovati.
     *
     * @param data Il dataset (oggetto {@link Data}) da clusterizzare. Non può essere nullo.
     * @return Il numero totale di cluster scoperti.
     * @throws EmptyDatasetException Se il dataset fornito è vuoto (non contiene esempi).
     * @throws ClusteringRadiusException Se tutti gli esempi vengono raggruppati in un unico cluster
     * (e il dataset contiene più di un esempio), indicando
     * un raggio potenzialmente troppo ampio.
     */
    public int compute(Data data) throws EmptyDatasetException, ClusteringRadiusException {
        if (data == null || data.getNumberOfExamples() == 0) {
            throw new EmptyDatasetException("Il dataset non può essere nullo o vuoto per l'algoritmo QTMiner.");
        }
        
        int numclusters = 0;
        boolean[] isClustered = new boolean[data.getNumberOfExamples()];

        // Inizializza l'array isClustered a false
        for (int i = 0; i < isClustered.length; i++) {
            isClustered[i] = false;
        }

        int countClustered = 0; // Contatore degli esempi già assegnati a un cluster

        // Continua finché tutti gli esempi non sono stati clusterizzati
        while (countClustered != data.getNumberOfExamples()) {
            Cluster c = buildCandidateCluster(data, isClustered);

            C.add(c); // Aggiunge il miglior cluster candidato all'insieme C
            numclusters++;

            // Aggiorna lo stato di clusterizzazione per gli esempi nel nuovo cluster
            Iterator<Integer> iteratore = c.iterator();
            while (iteratore.hasNext()) {
                isClustered[iteratore.next()] = true;
            }
            
            countClustered += c.getSize(); // Aggiorna il conteggio degli esempi clusterizzati
        }
        
        // Lancia un'eccezione se si forma un solo cluster e ci sono più esempi
        if (numclusters == 1 && data.getNumberOfExamples() > 1) {
            throw new ClusteringRadiusException("Tutti gli esempi sono stati raggruppati in un unico cluster con il raggio: " + this.radius);
        }

        return numclusters;
    }

    /**
     * Costruisce e restituisce il miglior cluster candidato possibile dai dati non ancora clusterizzati.
     * Un cluster candidato viene costruito attorno a ciascun esempio non ancora clusterizzato,
     * includendo tutti gli altri esempi non clusterizzati che cadono entro il {@code radius} specificato.
     * Viene restituito il candidato con il maggior numero di esempi.
     *
     * @param data L'oggetto {@link Data} contenente l'intero dataset.
     * @param isClustered Un array booleano che tiene traccia dello stato di clusterizzazione
     * di ciascun esempio. {@code isClustered[i]} è {@code true} se l'esempio
     * all'indice {@code i} è già stato assegnato a un cluster, {@code false} altrimenti.
     * @return Il {@link Cluster} candidato più popoloso. Restituisce {@code null} se non è possibile
     * formare alcun cluster (es. tutti i punti sono già clusterizzati o nessun punto
     * può formare un cluster con altri).
     */
    private Cluster buildCandidateCluster(Data data, boolean[] isClustered) {
        Cluster bestCluster = null;
        int maxClusterSize = 0;

        // Itera su tutti gli esempi del dataset per considerarli come potenziali centroidi
        for (int i = 0; i < data.getNumberOfExamples(); i++) {
            // Considera solo gli esempi non ancora clusterizzati come potenziali centroidi
            if (!isClustered[i]) {
                Tuple centroid = data.getItemSet(i); // La tupla i-esima diventa il centroide candidato
                Cluster currentCluster = new Cluster(centroid); // Crea un nuovo cluster con questo centroide
                currentCluster.addData(i); // Aggiunge l'ID del centroide stesso al cluster

                // Itera sugli altri esempi per vedere quali includere nel cluster candidato corrente
                for (int j = 0; j < data.getNumberOfExamples(); j++) {
                    // Salta se l'esempio j è lo stesso del centroide i (già aggiunto)
                    // o se l'esempio j è già stato assegnato a un cluster precedente.
                    if (i == j || isClustered[j]) {
                        continue;
                    }

                    // Calcola la distanza tra il centroide candidato e l'esempio j
                    double distance = centroid.getDistance(data.getItemSet(j));

                    // Se l'esempio j è sufficientemente vicino (entro il raggio), aggiungilo al cluster corrente
                    if (distance <= radius) {
                        currentCluster.addData(j);
                    }
                }

                // Se il cluster candidato corrente è più grande del miglior cluster trovato finora,
                // aggiorna bestCluster e maxClusterSize.
                if (currentCluster.getSize() > maxClusterSize) {
                    maxClusterSize = currentCluster.getSize();
                    bestCluster = currentCluster;
                }
            }
        }
        // Restituisce il miglior cluster candidato trovato.
        // Potrebbe essere null se nessun punto non clusterizzato è stato trovato per iniziare,
        // o se nessun cluster candidato valido (con dimensione > 0) è stato formato.
        return bestCluster;
    }
}
