package application.model;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.regex.Pattern;

/*
//Deze code zorgt ervoor dat je niet 2 keer dezelfde email + regio kan hebben (Dit werkt)
//Momenteel in commentaar omdat het de app breekt
@Table(name = "club", uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "regio"})})
*/
@Entity
@Table(name = "club", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "regio"})
})
public class Club{

//(cascade=CascadeType.ALL)
    @ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "clubs")
    private List<Collector> collectors = new ArrayList<Collector>();

    //primary key naam van de club => club naam moet uniek zijn
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "name.missing")
    private String name;

    //email + regio moet unieke combinatie zijn
    @NotBlank(message = "email.missing")
    @Email(message = "email.not.valid")
    @javax.validation.constraints.Pattern(regexp="^(.+)@(.+)(\\.)(.+)$", message = "email.not.valid")
    //dit staat hier omdat de @Email mails toelaat in de vorm van naam@naam
    private String email;

    //niet null en niet empty
    @NotBlank(message = "regio.missing")
    private String regio;

    //tussen 5 en 100
    @Min(value = 5, message = "maxLeden.LowerThan5")//niet zeker of dit inclusief is en of dit werkt
    @Max(value = 100, message = "maxLeden.HigherThan100")//niet zeker of dit inclusief is en of dit werkt
    @NotNull(message = "maxLeden.missing")
    private Integer maxLeden;


    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        System.out.println("setEmail wordt gebruikt met mail:" + email);
        String regex = "^(.+)@(.+)(\\.)(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        System.out.println(matcher.matches());
        this.email = email;


    }

    public String getRegio() {
        return regio;
    }

    public void setRegio(String regio) {
        this.regio = regio;
    }

    public Integer getMaxLeden() {
        return maxLeden;
    }

    public void setMaxLeden(Integer maxLeden) {
        this.maxLeden = maxLeden;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAantalLeden(){
        return this.collectors.size();
    }

    public void addCollector(Collector collector) {
        this.collectors.add(collector);
    }


   /* @Override
    public boolean equals(Object obj) {
        //indien tekst en titel het zelfde zijn?
        if(!(obj instanceof Club)) return false;
        Club c = (Club) obj;
        return this.email.equals(c.email) && this.name.equals(c.name) && this.regio.equals(c.regio) && this.maxLeden == c.maxLeden;
    }*/

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", regio='" + regio + '\'' +
                ", maxLeden=" + maxLeden +
                '}';
    }



}

