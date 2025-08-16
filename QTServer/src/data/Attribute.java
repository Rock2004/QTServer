package data;

import java.io.Serializable;

/**
 * Rappresenta un attributo generico, caratterizzato da un nome e un indice.
 * <p>
 * Questa classe astratta funge da base per tipi di attributi più specifici,
 * come {@link DiscreteAttribute} e {@link ContinuousAttribute}. La sua visibilità
 * è volutamente {@code package-private} per controllare come viene estesa
 * all'interno del suo package.
 * </p>
 * <p>
 * Implementa {@link Serializable} per consentire la persistenza delle sue sottoclassi.
 * </p>
 *
 * @see DiscreteAttribute
 * @see ContinuousAttribute
 */
public abstract class Attribute implements Serializable {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Il nome simbolico dell'attributo (es. "Outlook", "Temperature").
     * @serial
     */
    private String name;

    /**
     * L'identificatore numerico (indice) dell'attributo, che ne rappresenta la posizione.
     * @serial
     */
    private int index;

    /**
     * Costruisce un nuovo attributo con il nome e l'indice specificati.
     * <p>
     * Essendo un costruttore {@code protected}, è destinato a essere chiamato
     * dalle sottoclassi.
     * </p>
     *
     * @param name  Il nome simbolico dell'attributo (non nullo).
     * @param index L'indice numerico dell'attributo (non negativo).
     */
    protected Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome simbolico dell'attributo.
     *
     * @return Il nome dell'attributo.
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Restituisce l'indice numerico dell'attributo.
     *
     * @return L'indice dell'attributo.
     */
    protected int getIndex() {
        return this.index;
    }

    /**
     * Restituisce una rappresentazione testuale dell'attributo, che per default
     * corrisponde al suo nome.
     * <p>
     * Le sottoclassi possono sovrascrivere questo metodo per fornire una
     * rappresentazione più dettagliata (es. includendo il dominio dei valori).
     * </p>
     *
     * @return Il nome dell'attributo.
     */
    @Override
    public String toString() {
        return this.name;
    }
}

