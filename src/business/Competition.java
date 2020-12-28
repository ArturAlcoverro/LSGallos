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

    public boolean validateRapper(Rapper rapper){
        //faltar comprovar en la mateixa funció que la data de naixement es valida i que el pais existeis API
        for (int i = 0; i < this.rappers.size(); i++) {
            if(rapper.getStageName().equals(this.rappers.get(i).getStageName())){
                return false;
            }
        }
        return true;
    }
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Welcome to the Competition: " + name +
                "\n\nStarts on " + startDate +
                "\nEnds on " + endDate +
                "\nIt has " + phases.size() + " phases" +
                "\nCurrently there are " + rappers.size() + " participants\n\n";
    }
}
