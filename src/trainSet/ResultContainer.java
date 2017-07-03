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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class ResultContainer provides an interface to close
 * the objects PreparedStatement and ResultSet after using them
 *  to query the database.
 */

public class ResultContainer {

	private PreparedStatement pst;
	private ResultSet rs;
	
	public ResultContainer(PreparedStatement pst, ResultSet rs){
		this.pst = pst;
		this.rs = rs;
	}
	
	public ResultSet getRs() {
		return rs;
	}

	public void close() throws SQLException{
		pst.close();
		rs.close();
	}
}
