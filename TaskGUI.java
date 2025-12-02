import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Interface graphique moderne avec Swing pour la gestion des tâches
 */
public class TaskGUI extends JFrame {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    // Composants Swing
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private JComboBox<String> statusComboBox;
    private JTextField searchField;

    // Boutons
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton completeButton;
    private JButton resetButton;
    private JButton searchButton;
    private JButton sortByDateButton;
    private JButton sortByStatusButton;
    private JButton saveButton;
    private JButton loadButton;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Palette de couleurs moderne
    private static final Color PRIMARY_COLOR = new Color(99, 102, 241); // Indigo
    private static final Color PRIMARY_DARK = new Color(79, 70, 229);
    private static final Color SECONDARY_COLOR = new Color(139, 92, 246); // Purple
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94); // Green
    private static final Color DANGER_COLOR = new Color(239, 68, 68); // Red
    private static final Color WARNING_COLOR = new Color(251, 146, 60); // Orange
    private static final Color BACKGROUND_COLOR = new Color(248, 250, 252); // Light gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42); // Dark slate
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Slate
    private static final Color BORDER_COLOR = new Color(226, 232, 240);

    // Completed and pending task colors
    private static final Color COMPLETED_BG = new Color(220, 252, 231); // Light green
    private static final Color PENDING_BG = new Color(254, 249, 195); // Light yellow

    public TaskGUI() {
        taskManager = new TaskManager();
        initializeUI();
        loadTasksOnStartup();
    }

    private void initializeUI() {
        setTitle("✓ Gestionnaire de Tâches Moderne");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Panel principal avec fond coloré
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header avec titre stylisé
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Container pour input et table
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Panel de saisie (gauche)
        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel);

        // Panel de la table (droite)
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel des boutons d'action (bas)
        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("✓ Gestionnaire de Tâches");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel subtitleLabel = new JLabel("Organisez votre journée efficacement");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(BACKGROUND_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Titre de la section
        JLabel sectionTitle = new JLabel("➕ Nouvelle Tâche");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_PRIMARY);
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel("Titre *");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1.0;
        titleField = createStyledTextField(25);
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        descLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(descLabel, gbc);

        gbc.gridy = 3;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(4, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        formPanel.add(descScrollPane, gbc);

        // Date d'échéance
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel dateLabel = new JLabel("📅 Date d'échéance");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(dateLabel, gbc);

        gbc.gridy = 5;
        gbc.weightx = 1.0;
        dateField = createStyledTextField(15);
        JLabel dateHint = new JLabel("Format: jj/mm/aaaa");
        dateHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dateHint.setForeground(TEXT_SECONDARY);
        JPanel datePanel = new JPanel(new BorderLayout(0, 3));
        datePanel.setBackground(CARD_COLOR);
        datePanel.add(dateField, BorderLayout.NORTH);
        datePanel.add(dateHint, BorderLayout.SOUTH);
        formPanel.add(datePanel, gbc);

        // Statut
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel statusLabel = new JLabel("Statut");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(statusLabel, gbc);

        gbc.gridy = 7;
        gbc.weightx = 1.0;
        statusComboBox = new JComboBox<>(new String[] { "En cours", "Terminé" });
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusComboBox.setBackground(Color.WHITE);
        statusComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        formPanel.add(statusComboBox, gbc);

        // Boutons de gestion
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        buttonPanel.setBackground(CARD_COLOR);

        addButton = createStyledButton("➕ Ajouter", PRIMARY_COLOR, Color.WHITE);
        addButton.addActionListener(e -> addTask());
        buttonPanel.add(addButton);

        modifyButton = createStyledButton("✏️ Modifier", SECONDARY_COLOR, Color.WHITE);
        modifyButton.addActionListener(e -> modifyTask());
        buttonPanel.add(modifyButton);

        completeButton = createStyledButton("✓ Marquer Terminé", SUCCESS_COLOR, Color.WHITE);
        completeButton.addActionListener(e -> markAsCompleted());
        buttonPanel.add(completeButton);

        deleteButton = createStyledButton("🗑️ Supprimer", DANGER_COLOR, Color.WHITE);
        deleteButton.addActionListener(e -> deleteTask());
        buttonPanel.add(deleteButton);

        resetButton = createStyledButton("↺ Réinitialiser", new Color(107, 114, 128), Color.WHITE);
        resetButton.addActionListener(e -> resetFields());
        buttonPanel.add(resetButton);

        formPanel.add(buttonPanel, gbc);

        // Wrap form in scroll pane to ensure all buttons are visible
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(formScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Header avec titre et recherche
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(CARD_COLOR);

        JLabel sectionTitle = new JLabel("📋 Liste des Tâches");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_PRIMARY);
        headerPanel.add(sectionTitle, BorderLayout.NORTH);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(CARD_COLOR);

        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchPanel.add(searchLabel);

        searchField = createStyledTextField(20);
        searchPanel.add(searchField);

        searchButton = createStyledButton("Rechercher", PRIMARY_COLOR, Color.WHITE);
        searchButton.addActionListener(e -> searchTasks());
        searchPanel.add(searchButton);

        JButton showAllButton = createStyledButton("Tout Afficher", new Color(107, 114, 128), Color.WHITE);
        showAllButton.addActionListener(e -> refreshTable());
        searchPanel.add(showAllButton);

        headerPanel.add(searchPanel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Créer la table
        String[] columnNames = { "Titre", "Description", "Date d'échéance", "Statut" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskTable.setRowHeight(40);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskTable.getTableHeader().setBackground(new Color(241, 245, 249));
        taskTable.getTableHeader().setForeground(TEXT_PRIMARY);
        taskTable.getTableHeader().setBorder(new LineBorder(BORDER_COLOR));
        taskTable.setGridColor(BORDER_COLOR);
        taskTable.setShowGrid(true);
        taskTable.setIntercellSpacing(new Dimension(1, 1));

        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedTask();
            }
        });

        // Appliquer le rendu personnalisé pour les couleurs
        taskTable.setDefaultRenderer(Object.class, new ModernTaskTableCellRenderer());

        // Ajuster les largeurs de colonnes
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(BACKGROUND_COLOR);

        sortByDateButton = createStyledButton("📅 Trier par Date", new Color(59, 130, 246), Color.WHITE);
        sortByDateButton.addActionListener(e -> sortByDate());
        panel.add(sortByDateButton);

        sortByStatusButton = createStyledButton("📊 Trier par Statut", new Color(168, 85, 247), Color.WHITE);
        sortByStatusButton.addActionListener(e -> sortByStatus());
        panel.add(sortByStatusButton);

        saveButton = createStyledButton("💾 Sauvegarder", new Color(16, 185, 129), Color.WHITE);
        saveButton.addActionListener(e -> saveTasks());
        panel.add(saveButton);

        loadButton = createStyledButton("📂 Charger", new Color(245, 158, 11), Color.WHITE);
        loadButton.addActionListener(e -> loadTasks());
        panel.add(loadButton);

        return panel;
    }

    // Méthode utilitaire pour créer des boutons stylisés
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // Méthode utilitaire pour créer des champs de texte stylisés
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    // Méthodes d'action
    private void addTask() {
        try {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dateStr = dateField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (title.isEmpty()) {
                showStyledMessage("Le titre est obligatoire!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dueDate = null;
            if (!dateStr.isEmpty()) {
                try {
                    dueDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    showStyledMessage("Format de date invalide! Utilisez jj/mm/aaaa", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Task task = new Task(title, description, dueDate, status);
            taskManager.addTask(task);
            refreshTable();
            resetFields();

            showStyledMessage("Tâche ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("Erreur lors de l'ajout: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifyTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Veuillez sélectionner une tâche à modifier!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dateStr = dateField.getText().trim();
            String status = (String) statusComboBox.getSelectedItem();

            if (title.isEmpty()) {
                showStyledMessage("Le titre est obligatoire!", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dueDate = null;
            if (!dateStr.isEmpty()) {
                try {
                    dueDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    showStyledMessage("Format de date invalide! Utilisez jj/mm/aaaa", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Task updatedTask = new Task(title, description, dueDate, status);
            taskManager.updateTask(selectedRow, updatedTask);
            refreshTable();
            resetFields();

            showStyledMessage("Tâche modifiée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("Erreur lors de la modification: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Veuillez sélectionner une tâche à supprimer!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette tâche?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            taskManager.deleteTask(selectedRow);
            refreshTable();
            resetFields();
            showStyledMessage("Tâche supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markAsCompleted() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            showStyledMessage("Veuillez sélectionner une tâche à marquer comme terminée!", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = taskManager.getTask(selectedRow);
        if (task != null) {
            task.markAsCompleted();
            refreshTable();
            statusComboBox.setSelectedItem("Terminé");
            showStyledMessage("Tâche marquée comme terminée!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetFields() {
        titleField.setText("");
        descriptionArea.setText("");
        dateField.setText("");
        statusComboBox.setSelectedIndex(0);
        taskTable.clearSelection();
    }

    private void loadSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            Task task = taskManager.getTask(selectedRow);
            if (task != null) {
                titleField.setText(task.getTitle());
                descriptionArea.setText(task.getDescription());
                dateField.setText(task.getFormattedDate());
                statusComboBox.setSelectedItem(task.getStatus());
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Task task : taskManager.getAllTasks()) {
            Object[] row = {
                    task.getTitle(),
                    task.getDescription(),
                    task.getFormattedDate(),
                    task.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void searchTasks() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);
        for (Task task : taskManager.searchTasks(query)) {
            Object[] row = {
                    task.getTitle(),
                    task.getDescription(),
                    task.getFormattedDate(),
                    task.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void sortByDate() {
        taskManager.sortByDate();
        refreshTable();
    }

    private void sortByStatus() {
        taskManager.sortByStatus();
        refreshTable();
    }

    private void saveTasks() {
        try {
            taskManager.saveToFile();
            showStyledMessage("Tâches sauvegardées avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("Erreur lors de la sauvegarde: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasks() {
        try {
            taskManager.loadFromFile();
            refreshTable();
            showStyledMessage("Tâches chargées avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showStyledMessage("Erreur lors du chargement: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksOnStartup() {
        try {
            taskManager.loadFromFile();
            refreshTable();
        } catch (Exception e) {
            // Silently fail on startup if no file exists
        }
    }

    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Classe interne pour le rendu personnalisé moderne des cellules
    private class ModernTaskTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (!isSelected) {
                String status = (String) table.getValueAt(row, 3);
                if ("Terminé".equals(status)) {
                    c.setBackground(COMPLETED_BG);
                    c.setForeground(new Color(22, 101, 52)); // Dark green text
                } else {
                    c.setBackground(PENDING_BG);
                    c.setForeground(new Color(133, 77, 14)); // Dark yellow text
                }

                // Colonne statut avec badge
                if (column == 3) {
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    if ("Terminé".equals(status)) {
                        setText("✓ " + status);
                    } else {
                        setText("⏳ " + status);
                    }
                }
            } else {
                c.setBackground(PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
            }

            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskGUI gui = new TaskGUI();
            gui.setVisible(true);
        });
    }
}
