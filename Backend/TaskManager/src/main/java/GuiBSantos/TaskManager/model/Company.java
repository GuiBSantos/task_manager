package GuiBSantos.TaskManager.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "companies")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "company")
    private List<Team> teams;

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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Company company)) return false;
        return Objects.equals(getId(), company.getId()) && Objects.equals(getName(), company.getName()) && Objects.equals(getTeams(), company.getTeams());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTeams());
    }
}
