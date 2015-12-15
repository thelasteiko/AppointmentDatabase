package com.eiko.back.table;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;

/**
 * Data holder class for a TableView. I couldn't see a better way of
 * doing this and still be able to have a variable number of attributes.
 * The hash map stores only the attributes that are required for the table
 * while being flexible enough to store any attribute that is needed. 
 * @author Melinda Robertson
 * @version 20151211
 */
public class CellValue {
	
	/**
	 * The hashmap stores the attributes of the cell value.
	 */
	private HashMap<String, SimpleStringProperty> attr;
	/**
	 * Creates the cell value as a wrapper for a hash map.
	 */
	public CellValue() {
		attr = new HashMap<String, SimpleStringProperty>(3);
	}
	/**
	 * Sets the property and its associated value as a String.
	 * @param key is the key or property name.
	 * @param value is the value associated with the property.
	 */
	public void set(String key, String value) {
		attr.put(key, new SimpleStringProperty(value));
	}
	/**
	 * Sets the property and its associated value as a Number.
	 * @param key is the key or property name.
	 * @param value is the value associated with the property.
	 * @param <T> is any number.
	 */
	public <T extends Number> void set(String key, T value) {
		attr.put(key, new SimpleStringProperty(String.valueOf(value)));
	}
	/**
	 * Sets the property and its associated value as a Date.
	 * @param key is the key or property name.
	 * @param value is the value associated with the property.
	 */
	public void set(String key, java.sql.Date value) {
		attr.put(key, new SimpleStringProperty(value.toString()));
	}
	/**
	 * Returns how many properties the hash map has stored.
	 * @return the size of the hash map.
	 */
	public int size() {
		return attr.size();
	}
	/**
	 * All get methods return the associated property.
	 * @return the String value of the property.
	 */
	public String getStudentID() {return get(attr.get("StudentID"));}
	public String getFirstName() {return get(attr.get("FirstName"));}
	public String getLastName() {return get(attr.get("LastName"));}
	public String getStudentStatus() {return get(attr.get("StudentStatus"));}
	public String getClassNumber() {return get(attr.get("ClassNumber"));}
	public String getClassName() {return get(attr.get("ClassName"));}
	public String getEnrollmentCode() {return get(attr.get("EnrollmentCode"));}
	public String getSection() {return get(attr.get("Section"));}
	public String getStartDate() {return get(attr.get("StartDate"));}
	public String getStartTime() {return get(attr.get("StartTime"));}
	public String getDuration() {return get(attr.get("Duration"));}
	/**
	 * Returns the StartDate as a date rather than a String.
	 * @return the start date of an appointment.
	 */
	public LocalDate getAsDate() {
		return LocalDate.parse(getStartDate());
	}
	/**
	 * Convenience method in case a SimpleStringProperty
	 * is not represented in the hash map.
	 * @param s is the SimpleStringProperty or null if it does not exist.
	 * @return a String or null.
	 */
	private String get(SimpleStringProperty s) {
		if (s != null) return s.get();
		else return null;
	}
	/**
	 * Returns a listing of all the attributes and their values as
	 * a String.
	 */
	public String toString() {
		String ret = "{";
		for(String s: attr.keySet()) {
			ret += s + ":" + attr.get(s).get() +",";
		}
		ret += "}";
		return ret;
	}

}
