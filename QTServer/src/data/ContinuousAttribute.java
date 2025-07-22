package data;

/**
 * Rappresenta un attributo i cui valori sono numeri reali all'interno di un intervallo.
 * <p>
 * Questa classe estende {@link Attribute} per descrivere caratteristiche continue
 * (es. temperatura, età, prezzo), definite da un valore minimo e uno massimo.
 * Fornisce un metodo per normalizzare i valori in un intervallo standard [0, 1],
 * operazione fondamentale per molti algoritmi di machine learning.
 * </p>
 *
 * @see Attribute
 */
public class ContinuousAttribute extends Attribute {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Il valore massimo del dominio dell'attributo.
     * @serial
     */
    private double max;

    /**
     * Il valore minimo del dominio dell'attributo.
     * @serial
     */
    private double min;

    /**
     * Costruisce un nuovo attributo continuo.
     *
     * @param name  Il nome simbolico dell'attributo (es. "Temperatura").
     * @param index L'identificatore numerico (indice) dell'attributo.
     * @param min   Il valore minimo che l'attributo può assumere nel suo dominio.
     * @param max   Il valore massimo che l'attributo può assumere nel suo dominio.
     */
    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Normalizza un valore numerico nell'intervallo [0, 1] basandosi sul dominio [min, max] dell'attributo.
     * <p>
     * La normalizzazione viene calcolata con la formula: {@code (v - min) / (max - min)}.
     * Se il valore {@code v} è al di fuori del dominio, il risultato sarà minore di 0 o maggiore di 1.
     * </p>
     * <p>
     * <b>Attenzione:</b> se {@code max} e {@code min} sono uguali, questo metodo può restituire
     * {@code Infinity} o {@code NaN} a causa di una divisione per zero.
     * </p>
     *
     * @param v Il valore da normalizzare.
     * @return Il valore normalizzato (scalato) nell'intervallo [0, 1].
     */
    public double getScaledValue(double v) {
        return (v - this.min) / (this.max - this.min);
    }
}

