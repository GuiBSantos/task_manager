package GuiBSantos.TaskManager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "task_status_history")
public class TaskStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "new_status", nullable = false)
    private String newStatus;

    @ManyToOne
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    private Boolean deleted;

    public TaskStatusHistory() {
    }

    public TaskStatusHistory(Long id, Task task, String oldStatus, String newStatus, User changedBy, LocalDateTime changedAt, Boolean deleted) {
        this.id = id;
        this.task = task;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public User getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(User changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskStatusHistory that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTask(), that.getTask()) && Objects.equals(getOldStatus(), that.getOldStatus()) && Objects.equals(getNewStatus(), that.getNewStatus()) && Objects.equals(getChangedBy(), that.getChangedBy()) && Objects.equals(getChangedAt(), that.getChangedAt()) && Objects.equals(getDeleted(), that.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTask(), getOldStatus(), getNewStatus(), getChangedBy(), getChangedAt(), getDeleted());
    }
}