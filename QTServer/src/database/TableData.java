package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import database.TableSchema.Column;

/**
 * Classe pubblica che rappresenta l'insieme di transazioni collezionate in una tabella di un database.
 * <p>
 * Questa classe si connette al database tramite un'istanza di {@link DbAccess}
 * </p>
 * 
 * @see java.sql.ResultSet
 * @see java.sql.SQLException
 * @see java.sql.Statement
 * @see java.util.ArrayList
 * @see java.util.Iterator
 * @see java.util.LinkedList
 * @see java.util.List
 * @see java.util.Set
 * @see java.util.TreeSet
 */

public class TableData {

	/**
	 * Riferimento all'oggetto {@link DbAccess} utilizzato per ottenere la connessione al database.
     * Questo campo ha visibilità privata.
	 */
	private DbAccess db;
	
	/**
	 * Costruttore della classe TableData
	 * @param db L'oggetto {@link DbAccess} configurato per la connessione al database.
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
	 * Ricava lo schema della tabella indicata con table.
	 * <p>
	 * Esegue una interrogazione per estrarre le tuple distinte da tale tabella.
	 * Per ogni tupla del ResultSet, si crea un oggetto, istanza della classe {@link Example}, il cui riferimento va incluso nella lista da restituire. 
	 * In particolare, per la tupla corrente del ResultSet si estraggono i valori dei singoli campi (usando getFloat() o getString()),
	 * e li si aggiungono all'oggetto istanza della classe Example che si sta costruendo.
	 * </p>
	 * 
	 * @param table il nome della tabella nel database
	 * @return la lista di transazioni distinte memorizzate nella tabella
	 * @throws SQLException se la tabella non contiene attributi
	 * @throws EmptySetException se la tabella non contiene tuple
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		System.out.println("[SERVER-DEBUG] Esecuzione query: " + query);
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;
		while (rs.next()) {
			empty=false;
			Example currentTuple=new Example();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i+1));
				else
					currentTuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if(empty) throw new EmptySetException();
		
		
		return transSet;

	}

	/**
	 * Formula ed esegue uina interrogazione SQL per estrarre i valori ordinati di column e popolare un insieme da restituire.
	 * 
	 * @param table il nome della tabella nel database
	 * @param column il nome della colonna nella tabella
	 * @return l'insieme di valori distinti ordinati in modalità ascendente che l'attributo identificato da nome column assume nella tabella identificata dal nome table.
	 * @throws SQLException se la tabella non contiene attributi
	 */
	public Set<Object> getDistinctColumnValues(String table,Column column) throws SQLException{
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select distinct ";
		
		query+= column.getColumnName();
		
		query += (" FROM "+table);
		
		query += (" ORDER BY " +column.getColumnName());
		
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
				if(column.isNumber())
					valueSet.add(rs.getDouble(1));
				else
					valueSet.add(rs.getString(1));
			
		}
		rs.close();
		statement.close();
		
		return valueSet;

	}

	/**
	 * Formula ed esegue ina interrogazione SQL per estrarre il valore aggregato (valore  minimo o valore massimo) cercato nella colonna di nome column della tabella di nome table.
	 * 
	 * @param table il nome della tabella nel database
	 * @param column il nome della colonna nella tabella
	 * @param aggregate l'operatore SQL di aggregazione (min, max)
	 * @return L'aggregato cercato
	 * @throws SQLException se la tabella non contiene attributi
	 * @throws NoValueException se il ResultSet è vuoto o il valore calcolato è pari a null.
	 */
	public Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;
		if (rs.next()) {
				empty=false;
				if(column.isNumber())
					value=rs.getFloat(1);
				else
					value=rs.getString(1);
			
		}
		rs.close();
		statement.close();
		if(value==null)
			throw new NoValueException("No " + aggregateOp+ " on "+ column.getColumnName());
		else if(empty==true)
			throw new NoValueException("Empty table");
			
		return value;

	}
}
