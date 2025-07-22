package data;

import java.io.Serializable;

/**
 * Modella un generico item come coppia attributo-valore (es. Outlook="Sunny").
 * <p>
 * Questa classe astratta è la base per item più specifici, come
 * {@link DiscreteItem} e {@link ContinuousItem}. La sua visibilità è
 * {@code package-private}, indicando una scelta di design per cui la sua
 * estensione è controllata all'interno del package.
 * </p>
 * <p>
 * Implementa {@link Serializable} per consentire la persistenza delle sue sottoclassi.
 * </p>
 *
 * @see Attribute
 * @see DiscreteItem
 * @see ContinuousItem
 */
abstract class Item implements Serializable {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * L'attributo a cui questo item si riferisce (es. un'istanza di {@link ContinuousAttribute}).
     * @serial
     */
    private Attribute attribute;
    
    /**
     * Il valore specifico assunto dall'attributo per questo item.
     * <p>
     * Il tipo concreto di questo oggetto dipenderà dalla sottoclasse
     * (es. {@link String} per {@link DiscreteItem}).
     * </p>
     * @serial
     */
    private Object value;

    /**
     * Costruisce un nuovo item associando un attributo a un valore.
     *
     * @param attribute L'attributo dell'item (es. "Temperatura").
     * @param value Il valore dell'item (es. 30.5).
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
     * Restituisce il valore ({@link Object}) associato a questo item.
     *
     * @return Il valore dell'item.
     */
    protected Object getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale del valore dell'item.
     * <p>
     * <b>Attenzione:</b> questa implementazione invoca {@code value.toString()}. Se il campo
     * {@code value} è {@code null}, questo causerà una {@link NullPointerException}.
     * </p>
     *
     * @return La rappresentazione in stringa del valore.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Metodo astratto per calcolare la distanza tra questo item e un altro valore.
     * <p>
     * L'implementazione è demandata alle sottoclassi {@link DiscreteItem} e
     * {@link ContinuousItem}, poiché la metrica di distanza varia a seconda
     * che l'attributo sia discreto o continuo.
     * </p>
     *
     * @param a L'oggetto rispetto al quale calcolare la distanza.
     * @return La distanza calcolata come valore {@code double}.
     */
    protected abstract double distance(Object a);
}