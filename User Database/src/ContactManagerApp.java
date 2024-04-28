import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//Jerwinson-Flores Cunanan

public class ContactManagerApp extends JFrame {
    // Database connection details
    private static final String URL = "jdbc:sqlite:C:\\SQLite\\contacts.db";

    // Components
    private JComboBox<String> idComboBox = new JComboBox<>();
    private JTextField firstNameField = new JTextField(20);
    private JTextField lastNameField = new JTextField(20);
    private JTextField ageField = new JTextField(20);
    private JTextField phoneField = new JTextField(20);
    private JButton addButton = new JButton("Add");
    private JButton updateButton = new JButton("Update");
    private JTextField searchField = new JTextField(20);
    private JButton searchButton = new JButton("Search");
    private JTable contactTable = new JTable();

    public ContactManagerApp() {
        setTitle("Contact Manager");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
        loadContactIds();
        setupListeners();
    }

    private static Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel contactDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridConstraint = new GridBagConstraints();
        gridConstraint.gridwidth = GridBagConstraints.REMAINDER;
        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.insets = new Insets(4, 4, 4, 4);

        // Adding components to the contactDetailsPanel
        contactDetailsPanel.add(new JLabel("Contact ID:"), gridConstraint);
        contactDetailsPanel.add(idComboBox, gridConstraint);
        contactDetailsPanel.add(new JLabel("First Name:"), gridConstraint);
        contactDetailsPanel.add(firstNameField, gridConstraint);
        contactDetailsPanel.add(new JLabel("Last Name:"), gridConstraint);
        contactDetailsPanel.add(lastNameField, gridConstraint);
        contactDetailsPanel.add(new JLabel("Age:"), gridConstraint);
        contactDetailsPanel.add(ageField, gridConstraint);
        contactDetailsPanel.add(new JLabel("Phone:"), gridConstraint);
        contactDetailsPanel.add(phoneField, gridConstraint);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(contactDetailsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        add(new JScrollPane(contactTable), BorderLayout.CENTER);

        add(buttonsPanel, BorderLayout.SOUTH);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    private void setupListeners() {
        idComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) idComboBox.getSelectedItem();
                updateContactDetails(selectedId);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateContact();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContacts(searchField.getText());
            }
        });
    }



    private void loadContactIds() {
        String sql = "SELECT Id FROM contact";
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                idComboBox.addItem(String.valueOf(result.getInt("Id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateContactDetails(String id) {
        String sql = "SELECT firstName, lastName, age, phone FROM contact WHERE Id = ?";
        try (Connection conn = this.connect();
             PreparedStatement prepstatem = conn.prepareStatement(sql)) {

            prepstatem.setInt(1, Integer.parseInt(id));
            ResultSet result = prepstatem.executeQuery();

            if (result.next()) {
                firstNameField.setText(result.getString("firstName"));
                lastNameField.setText(result.getString("lastName"));
                ageField.setText(String.valueOf(result.getInt("age")));
                phoneField.setText(result.getString("phone"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addContact() {
        String sql = "INSERT INTO contact (firstName, lastName, age, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement prepstatem = conn.prepareStatement(sql)) {
            prepstatem.setString(1, firstNameField.getText());
            prepstatem.setString(2, lastNameField.getText());
            prepstatem.setInt(3, Integer.parseInt(ageField.getText()));
            prepstatem.setString(4, phoneField.getText());
            prepstatem.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contact added successfully!");
            loadContactIds(); // Reload IDs in the combo box
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Error adding contact: " + e.getMessage());
        }

    }

    private void updateContact() {
        String sql = "UPDATE contact SET firstName = ?, lastName = ?, age = ?, phone = ? WHERE Id = ?";
        try (Connection conn = this.connect();
             PreparedStatement prepstatem = conn.prepareStatement(sql)) {
            prepstatem.setString(1, firstNameField.getText());
            prepstatem.setString(2, lastNameField.getText());
            prepstatem.setInt(3, Integer.parseInt(ageField.getText()));
            prepstatem.setString(4, phoneField.getText());
            prepstatem.setInt(5, Integer.parseInt((String) idComboBox.getSelectedItem()));
            prepstatem.executeUpdate();
            JOptionPane.showMessageDialog(this, "Contact updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Error updating contact: " + e.getMessage());
        }
    }

    private void searchContacts(String name) {
        String sql = "SELECT Id, firstName, lastName, age, phone FROM contact WHERE firstName LIKE ?";
        try (Connection conn = this.connect();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setString(1, "%" + name + "%");
            ResultSet result = stm.executeQuery();

            // Create a model for the table
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Age", "Phone"}, 0);
            while (result.next()) {
                model.addRow(new Object[]{
                        result.getInt("Id"),
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getInt("age"),
                        result.getString("phone")
                });
            }
            contactTable.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Error searching contacts: " + e.getMessage());
        }
    }
}
