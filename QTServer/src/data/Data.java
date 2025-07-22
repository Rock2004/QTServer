	package data;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import database.DbAccess;
import database.Example;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

/**
 * Rappresenta un intero dataset, caricato da una fonte dati come un database.
 * <p>
 * Questa classe agisce da contenitore per un insieme di transazioni ({@link Example})
 * e per lo schema degli attributi ({@link Attribute}) che le descrivono.
 * Si occupa di interrogare il database per costruire dinamicamente lo schema,
 * distinguendo tra attributi discreti e continui, e di caricare i dati grezzi.
 * </p>
 */
public class Data {

	/**
	 * L'insieme di transazioni
	 * Ogni elemento della lista è un oggetto che rappresenta un insieme di attributi.
     * Utilizza un {@link ArrayList} per la memorizzazione.
	 */
    private List<Example> data = new ArrayList<Example>();
    
    /**
     * Cardinalità dell'insieme di transazioni.
     */
    private int numberOfExamples;
    
    /**
     * Schema della lista di dati.
	 * Ogni elemento della lista è un oggetto che rappresenta un attributo corrispondente ad una tupla.
     * Utilizza un {@link LinkedList} per la memorizzazione.
     */
    private List<Attribute> explanatorySet = new LinkedList<>();

    /**
     * Costruisce e popola l'oggetto Data connettendosi a un database.
     * <p>
     * Il costruttore esegue le seguenti operazioni:
     * <ol>
     * <li>Inizializza una connessione al database.</li>
     * <li>Carica lo schema della tabella specificata.</li>
     * <li>Per ogni colonna dello schema, determina se l'attributo è discreto o continuo.</li>
     * <li>Per gli attributi continui, esegue query di aggregazione (MIN, MAX) per determinarne il dominio.</li>
     * <li>Per gli attributi discreti, esegue una query per ottenere tutti i valori distinti del dominio.</li>
     * <li>Carica tutte le transazioni (tuple) distinte dalla tabella.</li>
     * <li>Garantisce la chiusura della connessione al database al termine delle operazioni.</li>
     * </ol>
     *
     * @param tableName Il nome della tabella nel database da cui caricare i dati.
     * @throws Exception Se si verifica un errore SQL o un altro problema durante l'interazione con il database.
     */
    public Data(String tableName) throws Exception {
        DbAccess db = new DbAccess(); // Dichiara db fuori dal blocco try
        try {
            db.initConnection(); // Apre la connessione
            TableData tableData = new TableData(db);

            // Caricamento delle transazioni
            this.data = tableData.getDistinctTransazioni(tableName);
            
            // Imposta correttamente il numero di esempi
            this.numberOfExamples = this.data.size();

            // Inizializzazione dello schema degli attributi
            TableSchema schema = new TableSchema(db, tableName);

            for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                TableSchema.Column col = schema.getColumn(i);
                if (col.isNumber()) {
                    // Usa Number e poi converti a double per maggiore flessibilità
                    Number min = (Number) tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MIN);
                    Number max = (Number) tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MAX);
                    explanatorySet.add(new ContinuousAttribute(col.getColumnName(), i, min.doubleValue(), max.doubleValue()));
                } else {
                    Set<Object> values = tableData.getDistinctColumnValues(tableName, col);
                    String[] nominalValues = values.stream()
                                                   .map(Object::toString)
                                                   .toArray(String[]::new);
                    explanatorySet.add(new DiscreteAttribute(col.getColumnName(), i, nominalValues));
                }
            }
        } finally {
            // Il blocco 'finally' viene eseguito SEMPRE, sia che il try abbia successo
            // sia che lanci un'eccezione.
            if (db != null) {
                db.closeConnection(); // Garantisce la chiusura della connessione
                System.out.println("[DEBUG] Connessione al database chiusa correttamente.");
            }
        }
    }


    /**
     * Restituisce l'intero che indica la cardinalità dell'insieme di transazioni.
     * 
     * @return il numero di esempi
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce l'intero che indica il numero di attributi dello schema.
     * 
     * @return la dimensione dello schema.
     */
    public int getNumberOfExplanatoryAttributes() {
        return explanatorySet.size();
    }

    /**
     * Restituisce lo schema di transazioni.
     * 
     * @return lo schema.
     */
    public List<Attribute> getAttributeSchema() {
        return explanatorySet;
    }

    /**
     * Restituisce un valore specifico dalla matrice dei dati.
     * <p>
     * <b>Attenzione:</b> la versione attuale del codice contiene un bug. Il controllo
     * sull'indice dell'attributo ({@code attributeIndex}) viene erroneamente
     * eseguito confrontandolo con il numero di esempi ({@code numberOfExamples})
     * anziché con il numero di attributi.
     * </p>
     *
     * @param exampleIndex   L'indice di riga (esempio).
     * @param attributeIndex L'indice di colonna (attributo).
     * @return Il valore nella cella specificata, o {@code null} se gli indici sono fuori range.
     */
    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        if (exampleIndex >= 0 && exampleIndex < data.size()) {
            Example e = data.get(exampleIndex);
            if (attributeIndex >= 0 && attributeIndex < numberOfExamples) {
                return e.get(attributeIndex);
            }
        }
        return null;
    }

    /**
     * Restituisce una rappresentazione testuale dell'insieme di transazioni.
     * 
     * @return Una stringa che rappresenta l'insieme delle transazioni.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfExamples; i++) {
            sb.append(i).append(":");
            sb.append(data.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     * Crea e restituisce un oggetto {@link Tuple} a partire da una riga del dataset.
     * <p>
     * Per ogni attributo nello schema, questo metodo determina il suo tipo (discreto o continuo)
     * usando controlli {@code instanceof} (RTTI) e crea l'oggetto {@link Item}
     * corrispondente ({@link DiscreteItem} o {@link ContinuousItem}).
     * Include una logica difensiva per gestire mancate corrispondenze di tipo,
     * stampando avvisi su {@code System.err}.
     * </p>
     *
     * @param exampleIndex L'indice della riga (esempio) da cui creare la tupla.
     * @return Una {@link Tuple} che modella la riga del dataset.
     * @throws IndexOutOfBoundsException se l'{@code exampleIndex} non è valido.
     */
    public Tuple getItemSet(int exampleIndex) {
        if (exampleIndex < 0 || exampleIndex >= numberOfExamples) {
            throw new IndexOutOfBoundsException("Indice dell'esempio " + exampleIndex + " non valido. Validi: 0-" + (numberOfExamples - 1));
        }

        Tuple tuple = new Tuple(explanatorySet.size());
        Example example = data.get(exampleIndex); // Usa la lista di Example

        for (int i = 0; i < explanatorySet.size(); i++) {
            Attribute attribute = explanatorySet.get(i); // Ottiene l'attributo
            Object value = example.get(i); // Ottiene il valore dell'esempio corrente

            if (attribute instanceof DiscreteAttribute) {
                if (value instanceof String) {
                    tuple.add(new DiscreteItem((DiscreteAttribute) attribute, (String) value), i);
                } else {
                    System.err.println("Attenzione: Previsto String per DiscreteAttribute '" + attribute.getName() + 
                                       "' all'esempio " + exampleIndex + ", ma trovato " + (value != null ? value.getClass().getName() : "null") + 
                                       ". Si tenta la conversione con toString().");
                    tuple.add(new DiscreteItem((DiscreteAttribute) attribute, value != null ? value.toString() : ""), i);
                }
            } else if (attribute instanceof ContinuousAttribute) {
                if (value instanceof Double) {
                    tuple.add(new ContinuousItem(attribute, (Double) value), i);
                } else if (value instanceof Number) {
                    System.err.println("Attenzione: Previsto Double per ContinuousAttribute '" + attribute.getName() +
                                       "' all'esempio " + exampleIndex + ", ma trovato " + value.getClass().getName() +
                                       ". Si tenta la conversione a Double.");
                    tuple.add(new ContinuousItem(attribute, ((Number) value).doubleValue()), i);
                } else {
                    System.err.println("Errore: Previsto Double/Number per ContinuousAttribute '" + attribute.getName() +
                                       "' all'esempio " + exampleIndex + ", ma trovato " + (value != null ? value.getClass().getName() : "null") + ".");
                    tuple.add(new ContinuousItem(attribute, Double.NaN), i);
                }
            } else {
                System.err.println("Attenzione: Tipo di attributo non gestito in getItemSet: " + attribute.getClass().getName());
                final Object finalValue = value;
                tuple.add(new Item(attribute, finalValue) {
					private static final long serialVersionUID = 1L;

					@Override
                    public double distance(Object a) {
                        System.err.println("Distanza non implementata per item generico di " + getAttribute().getName());
                        return Double.POSITIVE_INFINITY;
                    }
                }, i);
            }
        }

        return tuple;
    }

}