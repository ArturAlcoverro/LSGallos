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
        //faltar comprovar el pais existeis API
        if(rapper.getBirth().after(new Date())){
            return false;
        }
        for (int i = 0; i < this.rappers.size(); i++) {
            if(rapper.getStageName().equals(this.rappers.get(i).getStageName())){
                return false;
            }
        }
        return true;
    }

    public boolean validateLog(String name){
        for (int i = 0; i < this.rappers.size(); i++) {
            if(name.equals(this.rappers.get(i).getStageName())){
                return true;
            }
        }
        return false;
    }
    
    public void showRanking(){
        for (int i = 0; i < this.rappers.size(); i++) {
            System.out.print("  " + (i+1));
            System.out.print(" \t\t" + this.rappers.get(i).getStageName());
            System.out.println("\t\t\t\t\t\t" + this.rappers.get(i).getScore());
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public int getPhaseSize(){
        return this.phases.size();
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
