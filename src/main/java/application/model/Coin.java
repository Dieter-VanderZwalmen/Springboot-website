package application.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "coin")
public class Coin {
    //Class fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "munt.name.missing")
    private String name;

    @NotBlank(message = "munt.land.missing")
    private String land;


    @Min(0)
    private Double value;

    @NotBlank(message = "munt.unit.missing")
    private String unit;

    @Min(0)
    private Integer jaartal;

    private String collectionName;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @Valid
    private Collection collection;

    public Collection getCollection() {
        return this.collection;
    }

    public void setCollectionName() {
        if(this.collection != null) {
            collectionName = collection.getTitel();
        } else {
            collectionName = null;
        }
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }


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

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getJaartal() {
        return jaartal;
    }

    public void setJaartal(Integer jaartal) {
        this.jaartal = jaartal;
    }
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setAttributes(Coin coin) {
        this.jaartal = coin.jaartal;
        this.land = coin.land;
        this.value = coin.value;
        this.name = coin.name;
        this.unit = coin.unit;
        this.collection = coin.collection;
        this.collectionName = coin.collectionName;
    }
}
