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
 * Classe pubblica che rappresenta un insieme di transazioni (o esempi) e il loro schema.
 * Memorizza i dati in una matrice e fornisce metodi per accedere a questi dati e al loro schema.
 * Lo schema degli attributi è memorizzato in una Lista.
 * L'attributo 'Temperature' è trattato come continuo con range [3.2, 38.7].
 * 
 * @see java.util.List
 * @see java.util.ArrayList
 * @see java.util.LinkedList
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

 // File: Data.java - Versione del costruttore CORRETTA

    /**
     * Costruisce un'istanza di Data caricando le transazioni e lo schema
     * da una tabella specificata nel database.
     *
     * @param tableName Il nome della tabella da cui caricare i dati.
     * @throws Exception Se si verifica un errore durante l'accesso al database
     * o la costruzione dello schema/dati.
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
     * Restituisce il valore corrispondente dell'attributo indicato.
     * 
     * @param exampleIndex l'indice dell'esempio
     * @param attributeIndex l'indice dell'attributo selezionato
     * @return l'attributo corrispondente alla posizione indicata oppure null se non presente.
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
     * Crea e restituisce un oggetto {@link Tuple} che rappresenta la riga (esempio)
     * specificata del dataset. Ogni elemento della tupla è un {@link Item}
     * ({@link DiscreteItem} o {@link ContinuousItem}) creato in base al tipo
     * dell'attributo corrispondente (determinato tramite RTTI). [cite: 1]
     *
     * @param exampleIndex l'indice della riga del dataset da trasformare in tupla. [cite: 1]
     * @return una {@link Tuple} contenente i dati della riga specificata. [cite: 1]
     * @throws IndexOutOfBoundsException se l'indice fornito è fuori dal range valido.
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