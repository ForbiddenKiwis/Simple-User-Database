# Contact Management App

The Contact Management App is a Java application that provides a graphical user interface (GUI) for managing contacts. It allows you to read and write contact information from a SQLite database.

## Description

The application is designed to simplify the management of contacts by providing an intuitive GUI interface. With this app, you can easily add new contacts, update existing contact records, and search for contacts based on their first name.

## Functionalities

The Contact Management App offers the following functionalities:

1. **Database Setup**

   - Before using the application, you need to set up a SQLite database named `contacts.db`.
   - Create a table named `contact` with fields like `Id`, `firstName`, `lastName`, `age`, and `phone`.
   - You can add sample records of your choice to the `contact` table.

2. **GUI Interface**

   - Upon launching the app, the main GUI window will be displayed.
   - The contact IDs are loaded into a combo box for easy selection.

3. **Contact Information Display**

   - When you select a contact ID from the combo box, the corresponding contact's information, such as first name, last name, age, and telephone, will be displayed in the respective text fields.

4. **Add and Update Contacts**

   - The upper half of the GUI contains two buttons:
     - **Add:** Click this button to add a new contact record to the database.
     - **Update:** Use this button to update an existing contact record in the database.

5. **Search Contacts**

   - The lower half of the GUI includes a text box, a search button, and a JTable for displaying search results.
   - You can search for contacts based on their first name.
   - Enter the contact's first name in the text box and click the search button.
   - The JTable will display the contact information that matches the search.

## Project Files

Please ensure that you include the following files in your project's repository:

- All your Java files.
- The `contacts.db` SQLite database file.

## Usage

To run the application, compile and execute the main Java file. Make sure the SQLite JDBC driver is included in the classpath.

```shell
javac ContactManagementApp.java
java ContactManagementApp
