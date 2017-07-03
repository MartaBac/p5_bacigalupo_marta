package trainSet;
/*
 * Autore: Bacigalupo Marta
 *
 * Data: 02/07/2017
 * 
 * The purpose of the project is to provide a tool for speeding up and simplifying
 * the analysis of the performance of automatic classifiers
 * serializing them with different input configurations, train
 * Sets and test sets that will be specified by the user.
 * In particular this part of implementation deals with the the management of 
 * the train set that the user wishes to use and manages adding, setting, and 
 * removing them.
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * The Database class provides the interface to query the SQLite database.
 * Specifically, it contains methods designed to perform the insertion,
 * erase and retrieve of information from the tables in which the database is made.
 */

public class Database {
	private Connection connection;
	private String path;

	public Database(String path) throws Exception {
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection(path);
		this.path = path;
	}

    /**
     * This method allows to query the database and to extract
     * all the models of the specified FileType.
     *
     * @param fileType
     * @return ArrayList<Model>
     * @throws Exception
     */
	
	public ArrayList<Model> retrieveFromTable(FileType fileType)
			throws Exception {
		ArrayList<Model> data = new ArrayList<Model>();

		String query = "SELECT * FROM " + Database.getTableName(fileType);

		ResultContainer rw = this.executeQuery(query);

		ResultSet rs = rw.getRs();
		
		while (rs.next()) {
			/*
			 *  Extracts the attributes from the database
			 */
			
			
			Integer id = rs.getInt("ID");
			String name = rs.getString("NAME");
			Integer checked = rs.getInt("CHECKED");
			String path = "";
				path = rs.getString("PATH");
			
			/*
			 * Creates a new Model with the values ​​just extracted from the database.
			 */
			
			Model model = new Model(id, name, path, checked == 1, fileType);
			data.add(model);
		}

		rw.close();
		return data;
	}

	
    /**
     *  This method allows you to enter an element named "name"
     * and with "path" path in the FileType table "fileType".
     *
     * @param name
     * @param path
     * @param fileType
     * @throws Exception
     */
	
	public void insertIntoTable(String name, String path, FileType fileType)
			throws Exception {
		String query = "INSERT INTO " + Database.getTableName(fileType);

		query = query + " VALUES(null,'" + name + "','" + path + "',0)";
		
		this.executeUpdate(query);
	}

	
	/**
	 * This method allows you to delete an element named "name"
     * from the table indicated by the "fileType"
	 * 
	 * @param name
     * @param fileType
	 * @throws Exception
	 */
	
	public boolean removeFromTable(String name, FileType fileType)
			throws Exception {
		String q = "DELETE FROM " + Database.getTableName(fileType)
				+ " WHERE NAME ='" + name + "'";
		
		this.executeUpdate(q);
		return true;
	}

	/**
	 * This method allows you to run the indicated SQLite query
     * From the string called "query"
	 * 
	 * @param query
	 * @return ResultSet
	 * @throws SQLException
	 */
	
	private ResultContainer executeQuery(String query) throws SQLException {
		connectionValidator();
		PreparedStatement pst = this.connection.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return new ResultContainer(pst, rs);
	}
	
	/**
	 *  Executes the SQLite update query indicated by the 
	 *  String called "query"
     *
	 * @param query
	 * @throws SQLException
	 */

	private void executeUpdate(String query) throws SQLException {
		connectionValidator();
		PreparedStatement pst = this.connection.prepareStatement(query);
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * This static method allows to get the proper name
     * of the table for the type specified by "fileType".
	 * 
	 * @param fileType
	 * @return String
	 * @throws Exception
	 */
	
	public static String getTableName(FileType fileType) throws Exception {
		switch (fileType) {
		case TRAIN:
			return "TRAIN_SET_TABLE";
		default:
			throw new Exception("Unrecognized file type");
		}
	}
	
	/**
	 * This method allows you to set the value of the "CHECKED" field to "clicked"
	 * performing an update query in the appropriate table based on "fileType"
	 * 
	 * @param selectedName
	 * @param clicked
	 * @param fileType
	 * @throws Exception
	 */
	
	public void updateClicked(String selectedName, int clicked,
			FileType fileType) throws Exception {
		String whereClause = "";
		if (!selectedName.equals("*"))
			whereClause = " WHERE NAME = '" + selectedName + "'";
		String q = "UPDATE " + getTableName(fileType) + " SET CHECKED = "
				+ clicked + whereClause;
		this.executeUpdate(q);
	}

	
	/**
	 * This method allows you to extract all models that have the "CHECKED" field
	 * set to "1" from the table indicated by Database.getTableName(fileType)
     *
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws Exception
	 */
	
	public ArrayList<Model> getClickedModels(FileType fileType)
			throws Exception {
		ArrayList<Model> data = new ArrayList<Model>();

		String query = "SELECT * FROM " + Database.getTableName(fileType)
				+ " WHERE CHECKED = 1 ";
		ResultContainer rw = this.executeQuery(query);

		ResultSet rs = rw.getRs();
		
		while (rs.next()) {
			
			/*
			 * Getting the attributes from the database
			 */
			
			Integer id = rs.getInt("ID");
			String name = rs.getString("NAME");
			String path = "";
			path = rs.getString("PATH");
			
			/*
			 *  Create and add the new Model 'fileType' 
			 */
			
			Model model = new Model(id, name, path, true, fileType);
			data.add(model);
		}

		rw.close();
		return data;
	}
	
	/**
	 * Closes the current connection
	 * @throws SQLException
	 */
	
	public void closeConnection() throws SQLException{
		this.connection.abort(Executors.newSingleThreadExecutor());
		this.connection.close();
	}

	/**
	 * Set of the connection
	 * @throws SQLException
	 */
	
	public void setConnection(Connection connection){
		this.connection = connection;
	}

	/**
	 * Get of the connection
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	
	public Connection getConnection(){
		return this.connection;
	}
	
	/**
	 * Validator of the connection
	 * @throws SQLException
	 */
	
	public void connectionValidator() throws SQLException{
		if(!this.connection.isValid(1000))
			this.connection = DriverManager.getConnection(path);
	}
}
