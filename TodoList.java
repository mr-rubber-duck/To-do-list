
public class TodoList {
    public static void main(String[] args) {
        // Lancer l'interface graphique
        javax.swing.SwingUtilities.invokeLater(() -> {
            TaskGUI gui = new TaskGUI();
            gui.setVisible(true);
        });
    }
}
