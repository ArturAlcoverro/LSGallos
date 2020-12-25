package business;

import java.util.ArrayList;
import java.util.Date;

public class Competition {
    private String name;
    private Date startDate;
    private Date endDate;
    private ArrayList<Phase> phases;
    private ArrayList<String> countries;
    private ArrayList<Rapper> rappers;

    public Competition(String name, Date startDate, Date endDate, ArrayList<Phase> phases, ArrayList<String> countries, ArrayList<Rapper> rappers) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.phases = phases;
        this.countries = countries;
        this.rappers = rappers;
    }

    public void addRapper(Rapper rapper){
        this.rappers.add(rapper);
    }
}
