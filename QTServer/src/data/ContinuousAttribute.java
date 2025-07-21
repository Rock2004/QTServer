package data;

/**
 * Classe pubblica che rappresenta un attributo continuo.
 * Un attributo continuo è caratterizzato da un intervallo numerico (minimo e massimo).
 * <p>
 * Estende la classe astratta {@link Attribute}.
 * </p>
 * 
 * @see Attribute
 */
public class ContinuousAttribute extends Attribute {

    private static final long serialVersionUID = 1L;

	/**
     * Valore massimo dell'attributo continuo.
     */
    private double max;

    /**
     * Valore minimo dell'attributo continuo.
     */
    private double min;

    /**
     * Costruttore della classe ContinuousAttribute.
     *
     * @param name  il nome simbolico dell'attributo
     * @param index l'identificatore numerico dell'attributo
     * @param min   il valore minimo che l'attributo può assumere
     * @param max   il valore massimo che l'attributo può assumere
     */
    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Restituisce il valore normalizzato (scalato) dell'attributo continuo rispetto al suo intervallo [min, max].
     *
     * @param v il valore da normalizzare
     * @return il valore scalato nell'intervallo [0, 1]
     */
    public double getScaledValue(double v) {
        return (v - this.min) / (this.max - this.min);
    }
}

