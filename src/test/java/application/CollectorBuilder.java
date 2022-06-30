package application;

import application.model.Collector;

public class CollectorBuilder {

    //deze klasse dient enkel om objecten aan te maken zodat je die nadien kan testen


    //alle variabelen (behalve id aangezien die toch niet in de constructor staat)
    private String name,firstname,regio;
    private int age;
    private long id;
    //lege constructor
    public CollectorBuilder() {
    }

    //Deze functies dienen om een object aan te kunnen maken
    public CollectorBuilder withName(String name){
        this.name = name;
        return this; //de rede dat je telkens this returned is zodat als je een collector aanmaakt je gewoon withX().withY().withZ() etc kan doen
    }
    public CollectorBuilder withFirstname(String firstname){
        this.firstname = firstname;
        return this;
    }
    public CollectorBuilder withRegio(String regio){
        this.regio = regio;
        return this;
    }public CollectorBuilder withAge(int age){
        this.age = age;
        return this;
    }
    public CollectorBuilder withID(long id){
        this.id = id;
        return this;
    }

    //geeft lege CollectorBuilder terug deze wordt bij elk test object gebruikt
    public static CollectorBuilder aCollector(){
        return new CollectorBuilder();
    }

    //valid collector
    public static CollectorBuilder aCollectorJohan(){
        return aCollector().withFirstname("johan").withName("Charlier").withRegio("Overijse").withAge(25);
    }
    public static CollectorBuilder aCollectorDieter(){
        return aCollector().withFirstname("johan").withName("Charlier").withRegio("Overijse").withAge(25).withID(1);
    }
    //valid collector v2 (stel je wilt meerdere mensen in de je repo om iets te checken?)
    public static CollectorBuilder aCollectorPieter(){
        return aCollector().withFirstname("Pieter").withName("Smets").withRegio("Maleizen").withAge(64);
    }
    //valid collector v3 underaged
    public static CollectorBuilder aCollectorMiyo(){
        return aCollector().withFirstname("Pieter").withName("Smets").withRegio("Maleizen").withAge(12);
    }

    //invalid collector no age
    public static CollectorBuilder anInvalidCollectorNoAge(){
        return aCollector().withFirstname("johan").withName("Charlier").withRegio("Overijse");
    }
    //invalid collector no FirstName
    public static CollectorBuilder anInvalidCollectorNoFirstName(){
        return aCollector().withName("Charlier").withRegio("Overijse").withAge(64);
    }
    //invalid collector no Name
    public static CollectorBuilder anInvalidCollectorNoName(){
        return aCollector().withFirstname("Pieter").withRegio("Maleizen").withAge(64);
    }
    //invalid collector no Regio
    public static CollectorBuilder anInvalidCollectorNoRegio(){
        return aCollector().withFirstname("Pieter").withName("Smets").withAge(64);
    }


    public Collector build() {
        Collector collector = new Collector();
        collector.setName(name);
        collector.setFirstname(firstname);
        collector.setAge(age);
        collector.setRegio(regio);
        return collector;
    }

}
