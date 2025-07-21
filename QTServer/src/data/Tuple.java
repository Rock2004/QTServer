package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe pubblica che rappresenta una tupla come sequenza ordinata di coppie attributo-valore ({@link Item}).
 * Una tupla è essenzialmente un record o una riga di un dataset.
 * <p>
 * Questa classe implementa {@link java.io.Serializable} per consentire la
 * serializzazione e deserializzazione delle sue istanze. 
 * È importante che anche la classe {@link Item} e le sue sottoclassi concrete
 * (come {@link DiscreteItem} e {@link ContinuousItem}) siano serializzabili
 * affinché la serializzazione di {@code Tuple} funzioni correttamente.
 * Java genererà automaticamente un serialVersionUID per questa classe se non
 * esplicitamente definito.
 * </p>
 *
 * @see Item
 * @see DiscreteItem
 * @see ContinuousItem
 * @see java.io.Serializable
 * @see java.util.Set
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;
    
	/**
     * Array di {@link Item} che costituiscono gli elementi della tupla.
     * Ogni item rappresenta una coppia attributo-valore.
     * Questo campo è incluso nella forma serializzata di default della classe.
     * @serial
     */
    private Item[] tuple;

    /**
     * Costruisce una nuova istanza di {@code Tuple} con una dimensione specificata.
     * L'array interno di {@link Item} viene inizializzato con questa dimensione,
     * pronto per essere popolato.
     *
     * @param size Il numero di item (elementi) che la tupla conterrà. Deve essere non negativo.
     */
    public Tuple(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("La dimensione della tupla non può essere negativa.");
        }
        tuple = new Item[size];
    }

    /**
     * Restituisce il numero di item (elementi) presenti in questa tupla.
     *
     * @return La lunghezza dell'array interno di item.
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'{@link Item} situato alla posizione (indice) specificata all'interno della tupla.
     *
     * @param i L'indice (basato su zero) dell'item da recuperare.
     * @return L'{@link Item} presente alla posizione {@code i}.
     * @throws IndexOutOfBoundsException se l'indice {@code i} è negativo o maggiore o uguale alla lunghezza della tupla.
     */
    public Item get(int i) {
        if (i < 0 || i >= tuple.length) {
            throw new IndexOutOfBoundsException("Indice non valido: " + i + ". La tupla ha lunghezza " + tuple.length);
        }
        return tuple[i];
    }

    /**
     * Inserisce o sostituisce un {@link Item} in una specifica posizione (indice) della tupla.
     *
     * @param c L'{@link Item} da aggiungere o sostituire.
     * @param i La posizione (basata su zero) in cui inserire o sostituire l'item {@code c}.
     * @throws IndexOutOfBoundsException se l'indice {@code i} è negativo o maggiore o uguale alla lunghezza della tupla.
     */
    public void add(Item c, int i) {
        if (i < 0 || i >= tuple.length) {
            throw new IndexOutOfBoundsException("Indice non valido: " + i + ". La tupla ha lunghezza " + tuple.length);
        }
        tuple[i] = c;
    }

    /**
     * Calcola la distanza euclidea aggregata tra questa tupla e un'altra tupla specificata.
     * La distanza totale è calcolata come la somma delle distanze tra gli {@link Item}
     * corrispondenti nelle due tuple. Si assume che la distanza per ciascun item
     * sia definita nel rispettivo metodo {@code item.distance()}.
     *
     * @param obj La tupla {@link Tuple} rispetto alla quale calcolare la distanza.
     * Deve avere la stessa lunghezza di questa tupla.
     * @return La somma delle distanze tra gli item corrispondenti.
     * @throws IllegalArgumentException se le due tuple non hanno la stessa lunghezza o se {@code obj} è nullo.
     */
    public double getDistance(Tuple obj) {
        if (obj == null || obj.getLength() != this.getLength()) {
            throw new IllegalArgumentException("Le tuple devono avere la stessa lunghezza e l'oggetto tupla non può essere nullo.");
        }

        double distance = 0.0;
        for (int i = 0; i < this.getLength(); i++) {
            // Il metodo distance dell'Item viene chiamato passando il valore dell'Item corrispondente nell'altra tupla.
            distance += this.get(i).distance(obj.get(i).getValue());
        }
        return distance;
    }
    
    /**
     * Calcola la distanza media tra questa tupla (tipicamente un centroide) e un insieme
     * di altre tuple specificate dai loro ID all'interno di un oggetto {@link Data}.
     * Questo metodo è utile per calcolare la distanza media di un centroide
     * dagli esempi appartenenti al suo cluster.
     *
     * @param data L'oggetto {@link Data} che contiene il dataset completo,
     * necessario per recuperare le tuple tramite i loro ID. Non può essere nullo.
     * @param clusteredData Un {@link Set} di {@link Integer} contenente gli ID (indici)
     * delle tuple nel dataset {@code data} rispetto alle quali
     * calcolare la distanza media. Non può essere nullo.
     * @return La distanza media calcolata. Se l'insieme {@code clusteredData} è vuoto,
     * restituisce 0.0 per evitare divisioni per zero.
     * @throws IllegalArgumentException se {@code data} o {@code clusteredData} sono nulli.
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        if (data == null) {
            throw new IllegalArgumentException("L'oggetto Data non può essere nullo.");
        }
        if (clusteredData == null) {
            throw new IllegalArgumentException("L'insieme clusteredData non può essere nullo.");
        }
        
        double sumD = 0.0;

        for (Integer id : clusteredData) {
            // Recupera la tupla dal dataset usando l'ID fornito
            Tuple exampleTuple = data.getItemSet(id); 
            double d = getDistance(exampleTuple); // Calcola la distanza da questa tupla (centroide)
            sumD += d;
        }

        if (clusteredData.isEmpty()) {
            return 0.0; // Evita la divisione per zero se non ci sono dati nel cluster
        }

        return sumD / clusteredData.size(); // Calcola la media
    }
}
