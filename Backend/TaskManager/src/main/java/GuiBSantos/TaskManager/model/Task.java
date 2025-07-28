package GuiBSantos.TaskManager.model;

import GuiBSantos.TaskManager.Enum.TaskStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskStatusHistory> statusHistory;

    @Column(nullable = false)
    private Boolean deleted = false;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<TaskStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<TaskStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task task)) return false;
        return Objects.equals(getId(), task.getId()) &&
                Objects.equals(getTitle(), task.getTitle()) &&
                Objects.equals(getDescription(), task.getDescription()) &&
                getStatus() == task.getStatus() &&
                Objects.equals(getAssignedTo(), task.getAssignedTo()) &&
                Objects.equals(getTeam(), task.getTeam()) &&
                Objects.equals(getStatusHistory(), task.getStatusHistory()) &&
                Objects.equals(getDeleted(), task.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getStatus(),
                getAssignedTo(), getTeam(), getStatusHistory(), getDeleted());
    }
}
