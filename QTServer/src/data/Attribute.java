package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un attributo generico, caratterizzato da un nome simbolico
 * e un indice numerico. Gli attributi sono usati per descrivere le "colonne" o
 * le caratteristiche dei dati in un dataset (ad esempio, in una tupla).
 * <p>
 * Questa classe è la base per tipi di attributi più specifici, come
 * {@link DiscreteAttribute} e {@link ContinuousAttribute}.
 * <p>
 * Implementa {@link java.io.Serializable} per consentire la serializzazione
 * delle sue sottoclassi. Poiché i campi {@code name} (String) e {@code index} (int)
 * sono di tipi primitivi/serializzabili, la serializzazione di default è generalmente sufficiente.
 * </p>
 *
 * @see DiscreteAttribute
 * @see ContinuousAttribute
 * @see java.io.Serializable
 */
abstract class Attribute implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * Il nome simbolico dell'attributo (es. "Outlook", "Temperature").
     * Questo campo è incluso nella forma serializzata di default della classe.
     * @serial
     */
    private String name;

    /**
     * L'identificatore numerico (indice) dell'attributo.
     * Può rappresentare, ad esempio, la posizione della colonna dell'attributo
     * all'interno di un dataset o di una tupla.
     * Questo campo è incluso nella forma serializzata di default della classe.
     * @serial
     */
    private int index;

    /**
     * Costruttore protetto per la classe {@code Attribute}.
     * Inizializza il nome e l'indice dell'attributo.
     * Viene tipicamente invocato dai costruttori delle sottoclassi.
     *
     * @param name  Il nome simbolico da assegnare all'attributo. Non dovrebbe essere nullo.
     * @param index L'identificatore numerico (indice) da assegnare all'attributo.
     * Dovrebbe essere un valore non negativo.
     */
    protected Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome simbolico dell'attributo.
     *
     * @return Il nome dell'attributo (es. "Temperature").
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Restituisce l'identificatore numerico (indice) dell'attributo.
     *
     * @return L'indice dell'attributo.
     */
    protected int getIndex() {
        return this.index;
    }

    /**
     * Restituisce una rappresentazione testuale dell'attributo.
     * Per default, questa implementazione restituisce il nome dell'attributo.
     * Le sottoclassi possono sovrascrivere questo metodo per fornire una
     * rappresentazione più dettagliata (es. includendo il dominio dei valori).
     *
     * @return Il nome dell'attributo.
     */
    @Override
    public String toString() {
        return this.name;
    }
}

