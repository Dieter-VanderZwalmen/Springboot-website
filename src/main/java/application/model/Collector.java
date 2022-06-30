package application.model;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "collectors")
public class Collector {


    @ManyToMany(cascade=CascadeType.ALL)
    private List<Club> clubs;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "name.missing")
    @Length( min = 3, message = "min.length.is.3")
    private String name;


    @NotBlank(message = "firstname.missing")
    private String firstname;

    @NotBlank(message = "regio.missing")
    private String regio;

    @Min(value = 18, message = "min.age.is.18")
    @Max(value = 110, message = "max.age.is.110")
    private Integer age;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getRegio() {
        return regio;
    }

    public void setRegio(String regio) {
        this.regio = regio;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void addClub(Club club) {
        // club toevoegen aan de lijst met clubs van de patient
        this.clubs.add(club);
        //deze collector toevoegen aan de collectors lijst van de club
        //dit is de bidirectionele binding
        club.addCollector(this);
    }

    public void deleteClub(Club club) {
        for (String clubName: getClubsNames()){
            if (club.getName().equals(clubName)){

                getClubsNames().remove(clubName);
                clubs.remove(club);
            }
        }
    }

    public List<String> getClubsNames() {
        if(clubs == null){
            return null;
        }
        List<String> clubNames = new ArrayList<>();
        for (Club club : clubs){
            clubNames.add(club.getName());
        }
        return clubNames;
    }

    public void setAttributes(Collector collector) {
        this.name = collector.name;
        this.firstname = collector.firstname;
        this.regio = collector.regio;
        this.age = collector.age;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collector that = (Collector) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(regio, that.regio) &&
                Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstname, regio, age);
    }

    @Override
    public String toString() {
        return "Collector{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", regio='" + regio + '\'' +
                ", age=" + age +
                '}';
    }



}
