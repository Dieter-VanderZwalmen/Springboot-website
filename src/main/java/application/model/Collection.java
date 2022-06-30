package application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "collection", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"land", "jaartal"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Collection {

    //Class fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "titel.missing")
    private String titel;

    @NotBlank(message = "land.missing")
    private String land;


    @Max(2022)
    @Min(0)
    private Integer jaartal;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JoinTable(name="collection_coin", joinColumns ={@JoinColumn(name="collection_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name="coin_id", referencedColumnName = "id")})
    private List<Coin> coins;

    public void addCoin(Coin coin) {
        coins.add(coin);
        coin.setCollection(this);
    }

    public void deleteCoin(Coin coin) {
        coin.setCollection(null);
        coins.remove(coin);
    }

    public void deleteAllCoins() {
        coins.clear();
    }

    public List<Coin> getCoins() {
        return coins;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Integer getJaartal() {
        return jaartal;
    }

    public void setJaartal(Integer jaartal) {
        this.jaartal = jaartal;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Collection)) return false;
        return Objects.equals(this.titel, ((Collection) o).titel) && Objects.equals(this.land, ((Collection) o).land);
    }
    public void setAttributes(Collection collection) {
        this.titel = collection.titel;
        this.land = collection.land;
        this.jaartal = collection.jaartal;
    }
}
