package data;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Classe pubblica che rappresenta un attributo discreto.
 * Il dominio di un attributo discreto è un insieme ordinato e univoco di stringhe.
 * <p>
 * Estende la classe astratta {@link Attribute} e implementa {@link Iterable<String>}
 * per consentire l'iterazione diretta sui valori del suo dominio.
 * Inoltre utilizza un contenitore di tipo {@link TreeSet} per memorizzare i valori.
 * </p>
 * 
 * @see Attribute
 * @see java.util.Iterator
 * @see java.util.TreeSet
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {
    
    private static final long serialVersionUID = 1L;
    
	/**
     * Contenitore di tipo {@link TreeSet} che memorizza i valori distinti (dominio) dell'attributo.
     * L'uso di TreeSet garantisce che i valori siano unici e mantenuti in ordine naturale (alfabetico per le stringhe).
     */
    private TreeSet<String> values;

    /**
     * Costruttore della classe {@code DiscreteAttribute}.
     * Mantiene la segnatura originale che accetta un array di stringhe.
     * I valori forniti vengono utilizzati per popolare un {@link TreeSet} interno,
     * garantendo così l'unicità e l'ordinamento dei valori del dominio.
     *
     * @param name  il nome simbolico dell'attributo (ereditato da {@link Attribute})
     * @param index l'identificatore numerico dell'attributo (ereditato da {@link Attribute})
     * @param values array di stringhe che rappresenta i possibili valori del dominio dell'attributo.
     *               Eventuali duplicati verranno ignorati e non inseriti nel {@code TreeSet}.
     *               Se l'array è {@code null} o vuoto, il dominio dell'attributo sarà vuoto.
     */

    public DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        
        this.values = new TreeSet<>(); 

        if (values != null) {
            for(int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    this.values.add(values[i]);
                }
            }
        }
    }

    /**
     * Restituisce il numero di valori distinti nel dominio dell'attributo.
     * Corrisponde alla dimensione del {@link TreeSet} interno.
     *
     * @return il numero di valori unici che questo attributo può assumere.
     */
    public int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Fornisce un {@link Iterator} per scorrere i valori del dominio dell'attributo.
     * I valori verranno restituiti secondo l'ordinamento naturale definito dal {@link TreeSet} (es. alfabetico).
     * Questo metodo è richiesto dall'implementazione dell'interfaccia {@link Iterable<String>}.
     *
     * @return un {@link Iterator<String>} sui valori del dominio.
     */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}

