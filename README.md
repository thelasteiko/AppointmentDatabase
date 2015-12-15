#Tutoring Center Database Readme

The tutoring database is a utility program designed to view classes, students and appointments and allows the user to insert, delete and modify tutoring appointments.

##License

This program is licensed under General Public License, version 3 [GPL-3.0](https://opensource.org/licenses/GPL-3.0).
Copyright 2015.

##Installation

To run the program:

1. The user must have a MySQL server and be able to run the SQL script packaged with the program.
2. Start the MySQL server.
3. Run ApptDBSetup.sql to create the required database with sample data.
4. Double click the ApptDBV2.jar file to run the program.
5. Login to the database via the menu bar -> Account

##Usage

In the menu bar you can specify the database connector, host, username and password. They are pre-filled with default values reflecting the MySQL Java connector, localhost, root as the username and no password. Make sure the fields reflect your setup and the appropriate server is running before pressing 'Sign in'.

Once you have signed into the database the search panel shows. Enter a value into the text field to search for something. The radio buttons determine the format of what is retrieved from the database:

- Students are searched for by Student ID, first and last name.
- Classes are searched for by Class Number or name.
- Appointments are searched for by start date or student ID.

Once the data comes up, click on one of the retrieved records and click on 'Open' to view details for that record. Appointments can be modified by either by searching for the specific appointment or by searching for the student and opening the record. After modification the program always returns to the top screen.

##Class Breakdown
Connection to database:
- Connector
- TutorDBConnector

Displays database information in tables:
- CellValue
- ModifiableScrollTable
- TableMaker

The main program and error window:
- ErrorHandle
- MainFrame

Panels that display detailed information:
- AbstractGridPane
- ApptPanel
- ClassPanel
- SearchPanel
- StudentPanel

Manages which panels are displayed:
- AbstractStackTab
- TabHolder
- TabSearch

##Contributors

- Levi Bingham
- Melinda Robertson
- Indiana Davis

##Known Problems

- There are some classes not in use. They are for a feature that was never implemented. Originally there were going to be multiple tabs to display usage and statistics. We ran out of time to implement everything.
