package com.eiko.back.table;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.beans.property.SimpleStringProperty;

/**
 * Data holder class for a TableView. I couldn't see a better way of
 * doing this and still be able to have a variable number of attributes.
 * @author Melinda Robertson
 * @version 20151201
 */
public class CellValue {
	
	/**
	 * The hashmap stores the attributes of the cell value.
	 */
	private HashMap<String, SimpleStringProperty> attr;
	
	public CellValue() {
		attr = new HashMap<String, SimpleStringProperty>(3);
	}
	
	public void set(String key, String value) {
		attr.put(key, new SimpleStringProperty(value));
	}
	
	public <T extends Number> void set(String key, T value) {
		attr.put(key, new SimpleStringProperty(String.valueOf(value)));
	}
	
	public void set(String key, java.sql.Date value) {
		attr.put(key, new SimpleStringProperty(value.toString()));
	}
	
	public int size() {
		return attr.size();
	}
	
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
	
	public LocalDate getAsDate() {
		return LocalDate.parse(getStartDate());
	}
	
	private String get(SimpleStringProperty s) {
		if (s != null) return s.get();
		else return null;
	}
	
	public String toString() {
		String ret = "{";
		for(String s: attr.keySet()) {
			ret += s + ":" + attr.get(s).get() +",";
		}
		ret += "}";
		return ret;
	}

}
