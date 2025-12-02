import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

/**
 * Classe pour gérer la liste des tâches (CRUD en mémoire via ArrayList)
 */
public class TaskManager {
    private ArrayList<Task> tasks;
    private static final String SAVE_FILE = "tasks.txt";

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    // Ajouter une tâche
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Modifier une tâche
    public void updateTask(int index, Task updatedTask) {
        if (index >= 0 && index < tasks.size()) {
            tasks.set(index, updatedTask);
        }
    }

    // Supprimer une tâche
    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        }
    }

    // Obtenir toutes les tâches
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    // Obtenir une tâche par index
    public Task getTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            return tasks.get(index);
        }
        return null;
    }

    // Rechercher des tâches par titre ou statut
    public List<Task> searchTasks(String query) {
        String lowerQuery = query.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(lowerQuery) ||
                        task.getStatus().toLowerCase().contains(lowerQuery) ||
                        task.getDescription().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // Trier par date d'échéance
    public void sortByDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    // Trier par statut
    public void sortByStatus() {
        tasks.sort(Comparator.comparing(Task::getStatus));
    }

    // Sauvegarder dans un fichier
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        }
    }

    // Charger depuis un fichier
    public void loadFromFile() throws IOException {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return;
        }

        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromString(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
    }

    // Obtenir le nombre de tâches
    public int getTaskCount() {
        return tasks.size();
    }
}
