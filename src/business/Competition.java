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
    
    public void showRanking(Rapper you){
        //ordenar abans l'array en funció del atribut score abans de fer tot el ranking
        Object[][] table = new String[this.rappers.size()][];
        for (int i = 0; i < this.rappers.size(); i++) {
            if(you.getStageName().equals(this.rappers.get(i).getStageName())){
                table[i] = new String[] {String.valueOf((i+1)), this.rappers.get(i).getStageName() + " (you)", String.valueOf(this.rappers.get(i).getScore())};
            }else{
                table[i] = new String[] {String.valueOf((i+1)), this.rappers.get(i).getStageName(), String.valueOf(this.rappers.get(i).getScore())};
            }
        }
        for (final Object[] row : table) {
            System.out.format("  %-7s%-25s%-15s\n", row);

        }
    }

    public Rapper getMyRapper(String name){
        for (Rapper rapper: this.rappers) {
            if(name.equals(rapper.getStageName())){
                return rapper;
            }
        }
        return null;
    }

    public Rapper getRapper(String name){
        for (Rapper rapper: this.rappers) {
            if(name.equals(rapper.getStageName()) || name.equals(rapper.getRealName())){
                return rapper;
            }
        }
        return null;
    }

    public void getChampion(){
        //ordenar array rappers en funció score per acaba dassegurar
        System.out.println("The finalist of the competition: " + name + " is the rapper: " + this.rappers.get(0).getStageName() + " with a Score of: " + this.rappers.get(0).getScore() + " points");
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
