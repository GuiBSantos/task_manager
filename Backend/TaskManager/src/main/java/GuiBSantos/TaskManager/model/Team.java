package GuiBSantos.TaskManager.model;

import jakarta.persistence.*;
import org.springframework.scheduling.config.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "team")
    private List<User> users;

    @OneToMany(mappedBy = "team")
    private List<Task> tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getId(), team.getId()) && Objects.equals(getName(), team.getName()) && Objects.equals(getCompany(), team.getCompany()) && Objects.equals(getUsers(), team.getUsers()) && Objects.equals(getTasks(), team.getTasks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCompany(), getUsers(), getTasks());
    }
}

