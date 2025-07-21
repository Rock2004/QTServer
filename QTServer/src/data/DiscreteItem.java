package data;

/**
 * Classe pubblica che rappresenta un item discreto.
 * Un item discreto rappresenta una coppia {@code <Attributo discreto, valore discreto>}.
 * <p>
 * Estende la classe astratta Item
 * </p>
 * 
 * @see Item
 */

public class DiscreteItem extends Item {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore di DiscreteItem.
     * 
     * @param attribute L'attributo discreto associato all'item.
     * @param value Il valore discreto associato all'attributo.
     */
    public DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore di questo item e un valore fornito.
     * La distanza è 0 se i due valori sono uguali, 1 altrimenti.
     * 
     * @param a Il valore rispetto al quale calcolare la distanza.
     * @return 0.0 se i valori sono uguali, 1.0 altrimenti.
     */
    @Override
    public double distance(Object a) {
        if (this.getValue().equals(a)) {
            return 0.0;
        } else {
            return 1.0;
        }
    }
}