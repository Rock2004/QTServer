package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;

/**
 * Classe pubblica che rappresenta un cluster di dati, definito da un centroide (una {@link Tuple})
 * e un insieme di ID interi che identificano gli esempi (tuple del dataset)
 * appartenenti a questo cluster.
 * <p>
 * Questa classe implementa:
 * <ul>
 * <li>{@link Iterable<Integer>} per consentire l'iterazione diretta sugli ID degli esempi nel cluster.</li>
 * <li>{@link Comparable<Cluster>} per permettere il confronto tra cluster, basato principalmente sulla loro dimensione.</li>
 * <li>{@link java.io.Serializable} per consentire la serializzazione delle sue istanze.
 * È fondamentale che anche le classi dei suoi campi membri ({@link Tuple}, {@link Integer})
 * siano serializzabili. Java genererà automaticamente un serialVersionUID per questa classe.</li>
 * </ul>
 * </p>
 *
 * @see Tuple
 * @see Data
 * @see java.lang.Iterable
 * @see java.lang.Comparable
 * @see java.io.Serializable
 */
public class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable {

    private static final long serialVersionUID = 1L;

	/**
     * Il centroide di questo cluster, rappresentato da una {@link Tuple}.
     * Questo campo è incluso nella forma serializzata di default della classe.
     * @serial
     */
    private Tuple centroid;

    /**
     * Un insieme (Set) di ID interi. Ogni ID corrisponde all'indice di un esempio
     * nel dataset originale (gestito dalla classe {@link Data}) che è stato assegnato
     * a questo cluster. L'uso di {@link HashSet} garantisce l'unicità degli ID.
     * Questo campo è incluso nella forma serializzata di default della classe.
     * @serial
     */
    private Set<Integer> clusteredData; 
  
    /**
     * Costruisce un nuovo Cluster con la tupla specificata come centroide.
     * Inizializza un insieme vuoto (HashSet) per memorizzare gli ID degli esempi
     * che verranno assegnati a questo cluster.
     *
     * @param centroid La {@link Tuple} che funge da centroide iniziale per questo cluster.
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        this.clusteredData = new HashSet<Integer>();
    }
        
    /**
     * Restituisce il centroide di questo cluster.
     *
     * @return La {@link Tuple} che rappresenta il centroide.
     */
    Tuple getCentroid() {
        return centroid;
    }
    
    // return true if the tuple is changing cluster
    /**
     * Aggiunge l'ID di un esempio (tupla del dataset) a questo cluster.
     *
     * @param id L'ID (indice nel dataset originale) dell'esempio da aggiungere.
     * @return {@code true} se l'ID è stato aggiunto con successo (cioè non era già presente),
     * {@code false} altrimenti, come da contratto di {@link Set#add(Object)}.
     */
    boolean addData(int id) {
        return clusteredData.add(id);
    }
    
    /**
     * Verifica se un esempio, identificato dal suo ID, è contenuto in questo cluster.
     *
     * @param id L'ID (indice nel dataset originale) dell'esempio da verificare.
     * @return {@code true} se l'esempio con l'ID specificato appartiene a questo cluster,
     * {@code false} altrimenti.
     */
    boolean contain(int id) {
        return clusteredData.contains(id);
    }
    
    /**
     * Rimuove un esempio (identificato dal suo ID) da questo cluster.
     *
     * @param id L'ID (indice nel dataset originale) dell'esempio da rimuovere.
     * Se l'ID non è presente nel cluster, il metodo non ha effetto (come da {@link Set#remove(Object)}).
     */
    void removeTuple(int id) {
        clusteredData.remove(id);
    }
    
    /**
     * Restituisce il numero di esempi attualmente assegnati a questo cluster.
     *
     * @return La dimensione del cluster (numero di esempi contenuti).
     */
    int  getSize() { // Mantenuta la spaziatura originale "int  getSize()"
        return clusteredData.size();
    }
    
    /**
     * Restituisce un iteratore sugli ID ({@link Integer}) degli esempi contenuti in questo cluster.
     * Questo metodo è richiesto dall'implementazione dell'interfaccia {@link Iterable<Integer>}.
     * L'ordine di iterazione dipende dall'implementazione specifica del {@link Set} utilizzato
     * ({@link HashSet} in questo caso, che non garantisce un ordine specifico).
     *
     * @return Un {@link Iterator<Integer>} per gli ID degli esempi nel cluster.
     */
    @Override
    public Iterator<Integer> iterator() {
        return clusteredData.iterator();
    }

    /**
     * Restituisce una rappresentazione testuale concisa del cluster, mostrando solo il suo centroide.
     * Il formato è "Centroid=(item1item2...itemN)". La concatenazione degli item avviene
     * senza separatori aggiuntivi.
     *
     * @return Una stringa che rappresenta il centroide del cluster.
     */
    @Override
    public String toString() {
        String str = "Centroid=("; // Per performance, StringBuilder sarebbe preferibile per concatenazioni in un ciclo.
        for (int i = 0; i < centroid.getLength(); i++) {
            str += centroid.get(i); // Concatena la rappresentazione stringa di ciascun Item.
        }
        str += ")";
        return str;
    }
    
    /**
     * Restituisce una rappresentazione testuale dettagliata del cluster.
     * Include il centroide (con i suoi valori separati da spazi), gli esempi contenuti nel cluster
     * (con i loro valori e la distanza dal centroide), e la distanza media del cluster.
     * <p>
     * La formattazione del centroide è "Centroid=(item1 item2 ... itemN )".
     * Ogni esempio è formattato come "[val1 val2 ... valN ] dist=distanzaDalCentroide".
     * </p>
     *
     * @param data L'oggetto {@link Data} che contiene il dataset completo, necessario per
     * recuperare i valori degli esempi e calcolare le distanze.
     * @return Una stringa formattata che descrive dettagliatamente il cluster.
     * Se {@code data} è nullo, potrebbe verificarsi un {@link NullPointerException}
     * nei metodi chiamati.
     */
    public String toString(Data data) {
        String str = "Centroid=("; // Per performance, StringBuilder sarebbe preferibile.
        for (int i = 0; i < centroid.getLength(); i++) {
            str += centroid.get(i) + " "; // Valori del centroide separati da spazio.
        }
        str += ")\nExamples:\n"; // Spazio finale prima di ')' e newline.
        Iterator<Integer> iteratore = iterator();
        while (iteratore.hasNext()) {
            str += "[";
            Integer value = iteratore.next(); // ID dell'esempio corrente.
            for (int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++) {
                str += data.getAttributeValue(value, j) + " "; // Valori dell'esempio separati da spazio.
            }
            // Spazio finale prima di ']' è garantito dall'ultimo " " aggiunto nel ciclo.
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(value)) + "\n";
        }
        str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        return str;
    }

    /**
     * Confronta questo cluster con un altro cluster basandosi sulla loro dimensione (numero di esempi).
     * <p>
     * Se questo cluster ha meno esempi dell'altro, restituisce -1.
     * Se questo cluster ha più esempi dell'altro, restituisce 1.
     * Se i cluster hanno lo stesso numero di esempi, restituisce 1.
     * </p>
     * <p>
     * Nota sull'implementazione: restituire 1 quando le dimensioni sono uguali
     * (invece di 0, che indicherebbe uguaglianza per l'ordinamento) è una strategia
     * per permettere a strutture dati come {@link java.util.TreeSet} di contenere istanze multiple
     * di {@code Cluster} che hanno la stessa dimensione ma sono oggetti distinti.
     * Questo comportamento si discosta dal contratto generale di {@code compareTo}
     * che prevede {@code (x.compareTo(y)==0) == (x.equals(y))}.
     * Se si implementasse {@code equals} basato solo sulla dimensione, questo {@code compareTo}
     * non sarebbe coerente.
     * </p>
     *
     * @param other L'altro oggetto {@link Cluster} con cui confrontare.
     * @return -1 se questo cluster è considerato "minore" (ha meno esempi),
     * 1 se è considerato "maggiore" (ha più esempi o ha lo stesso numero di esempi).
     * @throws NullPointerException se {@code other} è nullo (comportamento standard di compareTo se non gestito).
     */
    @Override
    public int compareTo(Cluster other) {
        if (this.getSize() < other.getSize()) {
            return -1;
        } else if (this.getSize() > other.getSize()) {
            return 1;
        } else {
            return 1; //??? se ritorno 0 il treeSet li considera duplicati e ne inserisce solo 1
        }
    }
}