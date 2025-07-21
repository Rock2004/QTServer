package mining;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * La classe ClusterSet rappresenta un insieme di cluster.
 * Utilizza un {@link java.util.TreeSet} per memorizzare i cluster,
 * garantendo l'ordinamento basato sull'implementazione di {@code Comparable}
 * nella classe {@link Cluster}. Implementa {@link Iterable<Cluster>} e
 * {@link Serializable}.
 */
public class ClusterSet implements Iterable<Cluster>, Serializable {

    private static final long serialVersionUID = 1L;
    
	/**
     * Insieme ordinato (TreeSet) di oggetti Cluster che rappresenta l'insieme dei cluster.
     * Inizialmente è vuoto.
     * Questo campo è serializzabile.
     * @serial
     */
    private Set<Cluster> C = new TreeSet<Cluster>();


    /**
     * Costruttore predefinito della classe ClusterSet.
     * Inizializza un'istanza di ClusterSet con un TreeSet di Cluster vuoto.
     * (Il campo C è già inizializzato nella sua dichiarazione).
     */
    public ClusterSet() {
        // L'insieme C è già inizializzato come new TreeSet<Cluster>() nella dichiarazione del campo.
    }

    /**
     * Aggiunge un cluster all'insieme (TreeSet).
     * Il TreeSet gestirà automaticamente l'inserimento mantenendo l'ordinamento
     * e l'unicità (basata sul metodo compareTo di Cluster).
     *
     * @param c Il {@link Cluster} da aggiungere all'insieme. Non può essere nullo.
     * @throws IllegalArgumentException se si tenta di aggiungere un cluster nullo.
     */
    public void add(Cluster c) {
        // MODIFICA APPLICATA QUI:
        if (c != null) {
            C.add(c); // Approccio diretto e più efficiente
        } else {
            throw new IllegalArgumentException("Il cluster da aggiungere non può essere nullo.");
        }
    }

    /**
     * Restituisce una rappresentazione in stringa dell'insieme di cluster.
     * La stringa contiene i centroidi di ciascun cluster, separati da virgole.
     * (Commento originale dell'utente mantenuto per questa sezione)
     *
     * @return Una stringa che rappresenta l'insieme dei cluster.
     * Se l'insieme è vuoto, restituisce una stringa vuota.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Cluster> iteratore = iterator();
        int i = 1;
        while (iteratore.hasNext()) {
            sb.append(i).append(":").append(iteratore.next().toString()).append("\n");
            i++;
        }
        return sb.toString();
    }


    /**
     * Restituisce una rappresentazione in stringa dell'insieme di cluster,
     * formattata in modo specifico per la visualizzazione dei dati.
     * Include l'indice del cluster e la sua rappresentazione specifica (ottenuta da Cluster.toString(data)).
     * (Commento originale dell'utente mantenuto per questa sezione)
     *
     * @param data Un oggetto Data contenente i dati utilizzati per la rappresentazione del cluster.
     * @return Una stringa formattata che rappresenta l'insieme dei cluster.
     */
    public String toString(Data data) {
        String str = "";
        int i=0;
        Iterator<Cluster> iteratore=iterator();
        while(iteratore.hasNext()) {
            Cluster ci=iteratore.next();
            if(ci != null) { // Verifica mantenuta dal codice originale
                str+= i + ":" + ci.toString(data) + "\n";
            }
            i++;
        }
        return str;
    }

    /**
     * Restituisce un iteratore sull'insieme dei {@link Cluster}.
     * Questo metodo è richiesto dall'implementazione dell'interfaccia {@link Iterable<Cluster>}.
     * L'iterazione avverrà secondo l'ordinamento definito dal {@link TreeSet}.
     *
     * @return Un {@link Iterator<Cluster>} per navigare attraverso i cluster.
     */
    @Override
    public Iterator<Cluster> iterator() {
        return C.iterator();
    }
}
