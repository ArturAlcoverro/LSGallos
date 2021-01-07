package business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Afegeix un raper a la competicio
     * @param rapper
     */
    public void addRapper(Rapper rapper){
        this.rappers.add(rapper);
    }

    /**
     * Valida que les dades d'un raper siguin valides. (data naixement i nom artistic unic)
     * @param rapper
     * @return
     */
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

    /**
     *  Rep un nom i indica si hi ha algun raper amb aquest nom a la competició
     * @param name
     * @return
     */
    public boolean validateLog(String name){
        for (Rapper rapper : this.rappers) {
            if (name.equals(rapper.getStageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Printa tots els rappers
     * @param you
     */
    public void showRanking(Rapper you){
        Collections.sort(rappers);
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

    /**
     * @param name
     * @return Un rapper segons el nom artistic
     */
    public Rapper getMyRapper(String name){
        for (Rapper rapper: this.rappers) {
            if(name.equals(rapper.getStageName())){
                return rapper;
            }
        }
        return null;
    }

    /**
     * @param name
     * @return Un rapper segons el nom real o el nom artistic
     */
    public Rapper getRapper(String name){
        for (Rapper rapper: this.rappers) {
            if(name.equals(rapper.getStageName()) || name.equals(rapper.getRealName())){
                return rapper;
            }
        }
        return null;
    }

    /**
     * Printa el raper amb punts (score).
     */
    public void getChampion(){
        Collections.sort(rappers);
        Rapper champion = this.rappers.get(0);
        System.out.println("The finalist of the competition: " + name + " is the rapper: " + champion.getStageName() + " with a Score of: " + champion.getScore() + " points");
    }

    /**
     * @return Data inici de la competicio
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return Ultim dia de la competicio
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return Nom competició
     */
    public String getName() {
        return name;
    }

    /**
     * @return Numero de fases
     */
    public int getPhaseSize(){
        return this.phases.size();
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "Welcome to the Competition: " + name +
                "\n\nStarts on " + dateFormat.format(startDate) +
                "\nEnds on " + dateFormat.format(endDate) +
                "\nIt has " + phases.size() + " phases" +
                "\nCurrently there are " + rappers.size() + " participants\n\n";
    }
}
