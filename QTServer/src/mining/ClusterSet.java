package mining;

import data.Data;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Rappresenta un insieme ordinato di {@link Cluster}.
 * <p>
 * Questa classe agisce come un contenitore per oggetti {@code Cluster},
 * utilizzando un {@link TreeSet} per garantire che l'insieme sia sempre
 * ordinato secondo l'ordine naturale definito in {@link Cluster#compareTo(Cluster)}.
 * È serializzabile e iterabile.
 * </p>
 *
 * @see Cluster
 * @see java.util.TreeSet
 */
public class ClusterSet implements Iterable<Cluster>, Serializable {

    /**
     * Identificativo univoco per la versione della classe, usato durante la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * L'insieme ordinato che contiene i cluster.
     * @serial
     */
    private final Set<Cluster> C = new TreeSet<>();

    /**
     * Costruisce un nuovo {@code ClusterSet} vuoto.
     */
    public ClusterSet() {
        // L'insieme C è già inizializzato nella dichiarazione del campo.
    }

    /**
     * Aggiunge un nuovo cluster all'insieme.
     * <p>
     * L'inserimento è gestito dal {@code TreeSet}, che mantiene l'ordinamento
     * e l'unicità degli elementi basandosi sull'implementazione di
     * {@link Cluster#compareTo(Cluster)}.
     * </p>
     *
     * @param c Il {@link Cluster} da aggiungere.
     * @throws IllegalArgumentException se il cluster fornito è nullo.
     */
    public void add(Cluster c) {
        if (c == null) {
            throw new IllegalArgumentException("Il cluster da aggiungere non può essere nullo.");
        }
        C.add(c);
    }

    /**
     * Restituisce una rappresentazione testuale dell'insieme di cluster.
     * <p>
     * Ogni cluster nell'insieme viene rappresentato dal suo centroide,
     * preceduto da un indice numerico e seguito da un a capo.
     * Esempio di output:
     * <pre>
     * 1:Centroid=(val1val2...)
     * 2:Centroid=(val3val4...)
     * </pre>
     * </p>
     *
     * @return Una stringa che elenca i centroidi dei cluster. Se l'insieme è vuoto, restituisce una stringa vuota.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Cluster c : C) {
            sb.append(i).append(":").append(c.toString()).append("\n");
            i++;
        }
        return sb.toString();
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata dell'intero insieme di cluster.
     * <p>
     * Per ogni cluster, viene generata la sua rappresentazione dettagliata
     * (che include esempi e distanze), preceduta da un indice numerico.
     * </p>
     *
     * @param data L'oggetto {@link Data} necessario per generare i dettagli di ogni cluster.
     * @return Una stringa formattata con i dettagli di tutti i cluster.
     * @throws NullPointerException se l'oggetto {@code data} fornito è nullo.
     */
    public String toString(Data data) {
        if (data == null) {
            throw new NullPointerException("L'oggetto Data non può essere nullo.");
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Cluster c : C) {
            sb.append(i).append(":").append(c.toString(data)).append("\n");
            i++;
        }
        return sb.toString();
    }

    /**
     * Restituisce un iteratore sull'insieme dei {@link Cluster}.
     * <p>
     * L'iterazione avviene secondo l'ordinamento naturale dei cluster
     * gestito dal {@link TreeSet}.
     * </p>
     *
     * @return Un {@link Iterator} per navigare attraverso i cluster.
     */
    @Override
    public Iterator<Cluster> iterator() {
        return C.iterator();
    }
}