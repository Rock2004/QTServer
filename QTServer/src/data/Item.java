package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un generico item, che rappresenta una coppia attributo-valore
 * (ad esempio, Outlook="Sunny" o Temperature=30.5).
 * <p>
 * Questa classe è destinata ad essere estesa da classi più specifiche
 * che rappresentano item discreti ({@link DiscreteItem}) e continui ({@link ContinuousItem}).
 * <p>
 * Implementa {@link java.io.Serializable} per consentire la serializzazione
 * delle sue sottoclassi. Affinché la serializzazione funzioni correttamente,
 * anche i campi {@code attribute} e {@code value} (e le loro rispettive classi,
 * come {@link Attribute} e il tipo concreto di {@code value}) devono essere serializzabili.
 * Java genererà automaticamente un serialVersionUID se non definito esplicitamente.
 * </p>
 *
 * @see Attribute
 * @see DiscreteItem
 * @see ContinuousItem
 * @see java.io.Serializable
 */
abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
     * L'attributo a cui questo item si riferisce.
     * Ad esempio, potrebbe essere un'istanza di {@link DiscreteAttribute} o {@link ContinuousAttribute}.
     * Questo campo è incluso nella forma serializzata di default della classe,
     * assumendo che la classe concreta di {@link Attribute} sia serializzabile.
     * @serial
     */
    private Attribute attribute;
    
    /**
     * Il valore specifico assunto dall'attributo per questo item.
     * Il tipo concreto di questo oggetto dipenderà dalla sottoclasse di {@code Item}
     * (es. {@link String} per {@link DiscreteItem}, {@link Double} per {@link ContinuousItem}).
     * Questo campo è incluso nella forma serializzata di default della classe,
     * assumendo che il tipo concreto del valore sia serializzabile.
     * @serial
     */
    private Object value;

    /**
     * Costruttore protetto per la classe {@code Item}.
     * Viene tipicamente invocato dai costruttori delle sottoclassi.
     *
     * @param attribute L'attributo (es. "Outlook", "Temperature") associato a questo item.
     * Non dovrebbe essere nullo.
     * @param value     Il valore (es. "Sunny", 30.5) assunto dall'attributo per questo item.
     * Può essere nullo a seconda della logica delle sottoclassi.
     */
    protected Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'oggetto {@link Attribute} associato a questo item.
     *
     * @return L'attributo dell'item.
     */
    protected Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore grezzo ({@link Object}) associato a questo item.
     * Il tipo effettivo di questo oggetto (es. String, Double) dipenderà
     * dalla sottoclasse concreta di {@code Item}.
     *
     * @return Il valore dell'item.
     */
    protected Object getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale dell'item.
     * Per default, questa implementazione restituisce la rappresentazione stringa
     * del campo {@code value} dell'item.
     * Le sottoclassi possono sovrascrivere questo metodo se è necessaria una
     * rappresentazione differente.
     *
     * @return Una stringa che rappresenta il valore dell'item. Se {@code value} è nullo,
     * potrebbe lanciare una {@link NullPointerException} a seconda dell'implementazione
     * di {@code value.toString()}.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Metodo astratto per calcolare la distanza tra il valore di questo item
     * e un altro valore fornito (tipicamente il valore di un altro item dello stesso attributo).
     * <p>
     * L'implementazione concreta di questo metodo è demandata alle sottoclassi
     * ({@link DiscreteItem}, {@link ContinuousItem}), poiché la logica di calcolo
     * della distanza dipende dalla natura discreta o continua dell'attributo e del valore.
     * </p>
     *
     * @param a L'oggetto (generalmente un valore dello stesso tipo di quello contenuto in questo item)
     * rispetto al quale calcolare la distanza.
     * @return La distanza calcolata come valore {@code double}.
     */
    protected abstract double distance(Object a);
}