package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Rappresenta un cluster, ovvero un raggruppamento di dati.
 * <p>
 * Un cluster è definito da un centroide ({@link Tuple}) e da un insieme di
 * indici che identificano le tuple del dataset appartenenti al cluster stesso.
 * Questa classe è serializzabile e iterabile sugli indici delle tuple contenute.
 * </p>
 *
 * @see Tuple
 * @see Data
 */
public class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable {

    /**
     * Identificativo univoco per la versione della classe, usato durante la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Il centroide di questo cluster.
     * @serial
     */
    private final Tuple centroid;

    /**
     * Insieme degli indici delle tuple appartenenti a questo cluster.
     * Ogni intero corrisponde a un indice nell'oggetto {@link Data} originale.
     * @serial
     */
    private final Set<Integer> clusteredData;

    /**
     * Costruisce un nuovo Cluster con la tupla specificata come centroide iniziale.
     *
     * @param centroid La {@link Tuple} che funge da centroide per questo cluster.
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        this.clusteredData = new HashSet<>();
    }

    /**
     * Restituisce il centroide di questo cluster.
     *
     * @return La {@link Tuple} che rappresenta il centroide.
     */
    Tuple getCentroid() {
        return centroid;
    }

    /**
     * Aggiunge l'indice di una tupla a questo cluster.
     *
     * @param id L'indice della tupla da aggiungere.
     * @return {@code true} se l'indice non era già presente e viene aggiunto,
     * {@code false} altrimenti.
     */
    boolean addData(int id) {
        return clusteredData.add(id);
    }

    /**
     * Verifica se una tupla, identificata dal suo indice, appartiene a questo cluster.
     *
     * @param id L'indice della tupla da verificare.
     * @return {@code true} se la tupla appartiene a questo cluster, {@code false} altrimenti.
     */
    boolean contain(int id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove una tupla, identificata dal suo indice, da questo cluster.
     *
     * @param id L'indice della tupla da rimuovere.
     */
    void removeTuple(int id) {
        clusteredData.remove(id);
    }

    /**
     * Restituisce il numero di tuple assegnate a questo cluster.
     *
     * @return La dimensione del cluster.
     */
    int getSize() {
        return clusteredData.size();
    }

    /**
     * Restituisce un iteratore sugli indici delle tuple contenute nel cluster.
     * <p>
     * Questo metodo è richiesto dall'interfaccia {@link Iterable}. L'ordine di
     * iterazione non è garantito, poiché si basa su un {@link HashSet}.
     *
     * @return Un {@link Iterator} per gli indici nel cluster.
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Restituisce una rappresentazione testuale del centroide del cluster.
     *
     * @return Una stringa nel formato {@code "Centroid=(item1item2...)"}.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Centroid=(");
        for (int i = 0; i < centroid.getLength(); i++) {
            str.append(centroid.get(i));
        }
        str.append(")");
        return str.toString();
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata del cluster,
     * includendo il centroide, gli esempi contenuti e le loro distanze.
     *
     * @param data L'oggetto {@link Data} che contiene il dataset completo,
     * necessario per recuperare i valori delle tuple.
     * @return Una stringa formattata che descrive il cluster in dettaglio.
     * @throws NullPointerException se {@code data} è nullo.
     */
    public String toString(Data data) {
        if (data == null) {
            throw new NullPointerException("Il dataset non può essere nullo per generare la stringa dettagliata.");
        }
        StringBuilder str = new StringBuilder("Centroid=(");
        for (int i = 0; i < centroid.getLength(); i++) {
            str.append(centroid.get(i)).append(" ");
        }
        str.append(")\nExamples:\n");

        for (Integer id : clusteredData) {
            str.append("[");
            for (int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++) {
                str.append(data.getAttributeValue(id, j)).append(" ");
            }
            str.append("] dist=").append(getCentroid().getDistance(data.getItemSet(id))).append("\n");
        }
        str.append("\nAvgDistance=").append(getCentroid().avgDistance(data, clusteredData));
        return str.toString();
    }

    /**
     * Confronta questo cluster con un altro basandosi sulla loro dimensione.
     * <p>
     * <b>Nota sull'implementazione:</b> Questo metodo è deliberatamente reso
     * **incoerente con equals**. Restituisce {@code 1} (invece di {@code 0}) quando
     * le dimensioni sono uguali. Questa scelta strategica permette a strutture
     * dati ordinate come {@link java.util.TreeSet} di contenere cluster distinti
     * che hanno la stessa dimensione, evitando che vengano scartati come duplicati.
     * </p>
     *
     * @param other L'altro {@link Cluster} con cui confrontare.
     * @return -1 se questo cluster ha meno elementi, 1 altrimenti.
     */
    @Override
    public int compareTo(Cluster other) {
        if (this.getSize() < other.getSize()) {
            return -1;
        } else {
            return 1; // Ritorna 1 anche se le dimensioni sono uguali.
        }
    }
}