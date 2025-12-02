import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe représentant une tâche
 */
public class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status; // "En cours" ou "Terminé"

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Task(String title, String description, LocalDate dueDate, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Méthode pour marquer comme terminé
    public void markAsCompleted() {
        this.status = "Terminé";
    }

    // Méthode pour obtenir la date formatée
    public String getFormattedDate() {
        return dueDate != null ? dueDate.format(DATE_FORMATTER) : "";
    }

    // Méthode toString pour l'affichage
    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s",
                title,
                description,
                dueDate != null ? dueDate.toString() : "",
                status);
    }

    // Méthode pour créer une tâche à partir d'une chaîne
    public static Task fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 4) {
            LocalDate date = parts[2].isEmpty() ? null : LocalDate.parse(parts[2]);
            return new Task(parts[0], parts[1], date, parts[3]);
        }
        return null;
    }
}
