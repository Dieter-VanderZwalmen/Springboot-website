package application;

import application.model.Coin;

public class CoinBuilder {
    private String name, land, unit;
    private Integer jaartal;
    private Double value;

    private CoinBuilder() {

    }

    public static CoinBuilder emptyCoin() {
        return new CoinBuilder();
    }

    public static CoinBuilder aValidCoin() {
        return emptyCoin().withName("2 Euros").withLand("Belgium").withUnit("Euro").withJaartal(2012).withValue(2.0);
    }
    public static CoinBuilder anCoinWithEmptyName() {
        return emptyCoin().withName("").withLand("Belgium").withUnit("Euro").withJaartal(2012).withValue(2.0);
    }
    public static CoinBuilder aCoinWithNegativeValue() {
        return emptyCoin().withName("2 Euros").withLand("Belgium").withUnit("Euro").withJaartal(2012).withValue(-2.0);
    }

    public CoinBuilder withName (String name) {
        this.name = name;
        return this;
    }

    public CoinBuilder withLand (String land){
        this.land = land;
        return this;
    }

    public CoinBuilder withUnit (String unit) {
        this.unit = unit;
        return this;
    }

    public CoinBuilder withJaartal (Integer jaartal) {
        this.jaartal = jaartal;
        return this;
    }
    public CoinBuilder withValue (Double value) {
        this.value = value;
        return this;
    }

    public Coin build() {
        Coin coin = new Coin();
        coin.setName(name);
        coin.setLand(land);
        coin.setUnit(unit);
        coin.setJaartal(jaartal);
        coin.setValue(value);
        return coin;
    }

}
