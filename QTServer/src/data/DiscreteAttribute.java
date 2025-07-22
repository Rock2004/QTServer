package data;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Rappresenta un attributo discreto, i cui valori appartengono a un dominio finito di stringhe.
 * <p>
 * Questa classe estende {@link Attribute} per descrivere caratteristiche categoriche
 * (es. un attributo "Outlook" con valori {"Sunny", "Rainy", "Overcast"}).
 * Utilizza un {@link TreeSet} per memorizzare il dominio, garantendo che i valori
 * siano unici e ordinati alfabeticamente.
 * </p>
 * <p>
 * Implementa {@link Iterable<String>} per consentire l'iterazione diretta sui valori del dominio.
 * </p>
 * * @see Attribute
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insieme ordinato dei valori distinti (dominio) che l'attributo può assumere.
     */
    private TreeSet<String> values;

    /**
     * Costruisce un nuovo attributo discreto a partire da un insieme di valori possibili.
     * <p>
     * I valori duplicati o nulli presenti nell'array di input verranno ignorati.
     * </p>
     *
     * @param name   Il nome simbolico dell'attributo (es. "Outlook").
     * @param index  L'identificatore numerico (indice) dell'attributo.
     * @param values Un array di stringhe che definisce il dominio dell'attributo.
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
     *
     * @return La dimensione del dominio.
     */
    public int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Restituisce un iteratore per scorrere i valori del dominio dell'attributo.
     * <p>
     * I valori sono restituiti in ordine naturale (alfabetico), come garantito dal {@link TreeSet}.
     * </p>
     *
     * @return Un {@link Iterator} sui valori del dominio.
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}

