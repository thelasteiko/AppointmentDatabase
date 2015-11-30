package com.eiko.back.table;

import javafx.beans.property.SimpleStringProperty;

public class Student {
	
	private final SimpleStringProperty studentID;
	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty status;
	
	public Student(String studentID, String firstName, String lastName,
			String status) {
		super();
		this.studentID = new SimpleStringProperty(studentID);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.status = new SimpleStringProperty(status);
	}

}
