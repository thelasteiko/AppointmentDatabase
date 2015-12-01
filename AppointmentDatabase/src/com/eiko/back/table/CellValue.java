package com.eiko.back.table;

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
	 * The hasmap stores the attributes of the cell value.
	 */
	private HashMap<String, SimpleStringProperty> attr;
	
	public CellValue() {
		attr = new HashMap<String, SimpleStringProperty>(3);
	}
	
	public void set(String key, String value) {
		attr.put(key, new SimpleStringProperty(value));
	}
	
	public String getStudentID() {return attr.get("StudentID").get();}
	public String getFirstName() {return attr.get("FirstName").get();}
	public String getLastName() {return attr.get("LastName").get();}
	public String getStudentStatus() {return attr.get("StudentStatus").get();}
	public String getClassNumber() {return attr.get("ClassNumber").get();}
	public String getClassName() {return attr.get("ClassName").get();}
	public String getEnrollmentCode() {return attr.get("EnrollmentCode").get();}
	public String getSection() {return attr.get("Section").get();}
	public String getStartDate() {return attr.get("StartDate").get();}
	public String getStartTime() {return attr.get("StartTime").get();}
	public String getDuration() {return attr.get("Duration").get();}

}
