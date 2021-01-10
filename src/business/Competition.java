package business;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import com.google.gson.annotations.Expose;

import persistance.ThemesDAO;

public class Competition {
    @Expose
    private String name;
    @Expose
    private Date startDate;
    @Expose
    private Date endDate;
    @Expose
    private ArrayList<Phase> phases;
    @Expose
    private ArrayList<String> countries;
    @Expose
    private ArrayList<Rapper> rappers;
    
    private int contPhase;
    private Random random;
    private ThemesDAO themesDAO;

    private Rapper you;
    private Rapper nextRival;
    private int nextPhaseType;
    private ArrayList<Battle> battles;

    public Competition(String name, Date startDate, Date endDate, ArrayList<Phase> phases, ArrayList<String> countries,
            ArrayList<Rapper> rappers) throws IOException {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.phases = phases;
        this.countries = countries;
        this.rappers = rappers;
        this.contPhase = 0;
        this.random = new Random();
        this.themesDAO = new ThemesDAO("data/batalles.json");
    }

    /**
     * Afegeix un raper a la competició
     *
     * @param rapper
     */
    public void addRapper(Rapper rapper) {
        this.rappers.add(rapper);
    }

    /**
     * Valida que les dades d'un raper siguin valides. (data naixement i nom
     * artistic unic)
     *
     * @param rapper
     * @return si es valid
     */
    public boolean validateRapper(Rapper rapper) {
        if (!countries.contains(rapper.getNationality())) {
            return false;
        }
        if (rapper.getBirth().after(new Date())) {
            return false;
        }
        for (int i = 0; i < this.rappers.size(); i++) {
            if (rapper.getStageName().equals(this.rappers.get(i).getStageName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rep un nom i indica si hi ha algun raper amb aquest nom a la competició
     *
     * @param name
     * @return retorna true si el troba
     */
    public boolean validateLog(String name) {
        for (Rapper rapper : this.rappers) {
            if (name.equals(rapper.getStageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Printa tots els rappers
     */
    public void showRanking() {
        sortRappers(false);
        Object[][] table = new String[this.rappers.size()][];
        for (int i = 0; i < this.rappers.size(); i++) {
            DecimalFormat df = new DecimalFormat("#.##");
            
            String formatted = df.format((double)this.rappers.get(i).getScore());
            if (you.getStageName().equals(this.rappers.get(i).getStageName())) {
                table[i] = new String[] { String.valueOf((i + 1)), this.rappers.get(i).getStageName() + " (you)",
                        formatted };
            } else {
                table[i] = new String[] { String.valueOf((i + 1)), this.rappers.get(i).getStageName(),
                        formatted };
            }
        }
        for (final Object[] row : table) {
            System.out.format("  %-7s%-25s%-15s\n", row);
        }
    }

    /**
     * Funció que estableix que el rapper que rep per paràmetre es l'usuari que ha
     * inciat sessió en la competició i per tant el guarda en la variable rapper you
     * de la competició per tenir-ne registre
     *
     * @param you Rapper que ha inciat sessió en la competició
     */
    public void setYou(Rapper you) {
        this.you = you;
    }

    /**
     * @param name
     * @return Un rapper segons el nom artistic
     */
    public Rapper getMyRapper(String name) {
        for (Rapper rapper : this.rappers) {
            if (name.equals(rapper.getStageName())) {
                return rapper;
            }
        }
        return null;
    }

    /**
     * @param name
     * @return Un rapper segons el nom real o el nom artistic
     */
    public Rapper getRapper(String name) {
        for (Rapper rapper : this.rappers) {
            if (name.equals(rapper.getStageName()) || name.equals(rapper.getRealName())) {
                return rapper;
            }
        }
        return null;
    }

    /**
     * Abandona la competció
     */
    public void leave() {
        this.you.leave();
    }

    /**
     * Printa el raper amb punts (score).
     */
    public void getChampion() {
        sortRappers(true);
        Rapper champion = this.rappers.get(0);
        System.out.println("The finalist of the competition: " + name + " is the rapper: " + champion.getStageName()
                + " with a Score of: " + champion.getScore() + " points");
    }

    /**
     * @return Data inici de la competició
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return Ultim dia de la competició
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
    public int getPhaseSize() {
        return this.phases.size();
    }

    /**
     * @return Tots els rapers de la competició
     */
    public ArrayList<Rapper> getRappers() {
        return rappers;
    }

    /**
     * Funció que retorna true si la competició s'ha acabat, false si encara segueix
     * en curs
     */
    public boolean isFinished() {
        return this.contPhase >= phases.size();
    }

    /**
     * Funció encarregada de preparar la següent fase de la competició i establir de
     * quin tipus (Blood, Written o Acapella) serà la fase
     */
    public void prepareNextPhase() {
        this.nextPhaseType = random.nextInt(3) + 1;
        if (phases.size() == 3) {
            switch (this.contPhase + 1) {
                case 1:
                    firstPhase();
                    break;
                case 2:
                    secondPhase();
                    break;
                case 3:
                    finalPhase();
                    break;
            }
        } else {
            if (this.contPhase == 0)
                firstPhase();
            else
                finalPhase();
        }
    }

    /**
     * Funció que rep tots els participants de la competició i n'escull un d'ells
     * aleatoriament
     *
     * @param participants Llista de rapers dins la competició
     * @return Raper escollit aleatoriament entre els participants
     */
    private Rapper getRandomRapper(ArrayList<Rapper> participants) {
        Rapper rapper;
        int i = random.nextInt(participants.size());
        rapper = participants.get(i);
        participants.remove(rapper);
        return rapper;
    }

    /**
     * Funció que prepara les batalles per la primera fase de la competició
     */
    private void firstPhase() {
        ArrayList<Rapper> participants = new ArrayList<Rapper>(rappers);
        prepareBattles(participants, true);
    }

    /**
     * Funció que prepara les batalles per la segona fase de la competició
     */
    private void secondPhase() {
        ArrayList<Rapper> participants = new ArrayList<Rapper>(rappers);

        for (Iterator<Rapper> it = participants.iterator(); it.hasNext();) {
            if (!it.next().isQualified())
                it.remove();
        }

        prepareBattles(participants, you.isQualified());
    }

    /**
     * Funció que prepara les batalles per la última i tercera fase de la competició
     */
    private void finalPhase() {
        sortRappers(true);
        battles = new ArrayList<>();

        boolean youParticipate = rappers.get(0).equals(you) || rappers.get(1).equals(you);

        if (youParticipate)
            nextRival = rappers.get(0).equals(you) ? rappers.get(1) : rappers.get(0);
        else
            nextRival = null;

        switch (this.nextPhaseType) {
            case 1:
                battles.add(new Acapella(themesDAO.getRandomTheme(), rappers.get(0), rappers.get(1)));
                break;
            case 2:
                battles.add(new Blood(themesDAO.getRandomTheme(), rappers.get(0), rappers.get(1)));
                break;
            case 3:
                battles.add(new Written(themesDAO.getRandomTheme(), rappers.get(0), rappers.get(1)));
                break;
        }

    }

    /**
     * Funció que realitza tots els encreuaments aletoris de totes les batalles que
     * es jugaran en la fase indicada
     *
     * @param participants   Participants de la competició
     * @param youParticipate Comprovar si el nostre usuari participa o no en la fase
     *                       indicada
     */
    private void prepareBattles(ArrayList<Rapper> participants, boolean youParticipate) {
        participants.remove(you);
        battles = new ArrayList<>();

        if (youParticipate) {
            nextRival = getRandomRapper(participants);
            switch (this.nextPhaseType) {
                case 1:
                    battles.add(new Acapella(themesDAO.getRandomTheme(), nextRival, you));
                    break;
                case 2:
                    battles.add(new Blood(themesDAO.getRandomTheme(), nextRival, you));
                    break;
                case 3:
                    battles.add(new Written(themesDAO.getRandomTheme(), nextRival, you));
                    break;
            }
        } else {
            nextRival = null;
        }

        while (participants.size() >= 2) {
            switch (this.nextPhaseType) {
                case 1:
                    battles.add(new Acapella(themesDAO.getRandomTheme(), getRandomRapper(participants),
                            getRandomRapper(participants)));
                    break;
                case 2:
                    battles.add(new Blood(themesDAO.getRandomTheme(), getRandomRapper(participants),
                            getRandomRapper(participants)));
                    break;
                case 3:
                    battles.add(new Written(themesDAO.getRandomTheme(), getRandomRapper(participants),
                            getRandomRapper(participants)));
                    break;
            }
        }
    }

    /**
     * Funció que arranca les batalles on es decidira el millor de cada encreuament
     * a través de un thread, o si és la ultima fase el guanyador de la competició.
     * Llavors s'espera a que s'acabi l'execució del thread amb el join
     */
    public void startBattles() {
        for (Battle battle : battles)
            battle.start();
        for (Battle battle : battles) {
            try {
                battle.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.contPhase++;
        sortRappers(true);
    }

    /**
     * Funcio que genera un String amb el estat de la competició i l'estat en que es
     * troba el nostre Rapper i l'estat de la pròxima batalla si es que n'hi ha
     *
     * @return String on hem concatenat tota la informació necessaria
     */
    public String getStatus() {
        sortRappers(false);
        String type = "";
        String rivalName = "";
        String nextBattle = "";
        int phase = this.contPhase +1; 

        if (nextRival != null)
            rivalName = nextRival.getStageName();

        if (isFinished()) {
            if (getRappers().get(0).getStageName().equals(you.getStageName()))
                return "| " + getName() + " | Competition finished | Score: "
                        + String.format("%.2f", (double)getMyRapper(you.getStageName()).getScore()) + " | You win, kid. Congratulations!. |";
            return "| " + getName() + " |  | Score: "
                    + String.format("%.2f", (double)getMyRapper(you.getStageName()).getScore()) + " | You lost, kid. Try to get good. |";
        }
        switch (nextPhaseType) {
            case 1:
                type = "Acapella";
                break;
            case 2:
                type = "Blood";
                break;
            case 3:
                type = "Written";
                break;
        }

        if (phase == 1 || (phase < getPhaseSize() && you.isQualified())
                || (phase == getPhaseSize() && (getRappers().get(0).getStageName().equals(you.getStageName())
                        || getRappers().get(1).getStageName().equals(you.getStageName()))))
            nextBattle = " | Next battle: " + type + " vs " + "\"" + rivalName + "\" |";

        return "| " + getName() + " | Phase " + phase + "/" + getPhaseSize() + " | Score: "
                + String.format("%.2f",getMyRapper(you.getStageName()).getScore()) + nextBattle;
    }

    /**
     * @return Retorna els resultats de la competició
     */
    public String getResults() {
        sortRappers(true);

        String str = "----------------------------------------------------------------------------------------------------------------------\n" +
                "The Winner is: " + rappers.get(0).getStageName() + "! with a total score of "
                + String.format("%.2f", (double) rappers.get(0).getScore()) + " points!" + "\n" +
                "----------------------------------------------------------------------------------------------------------------------\n";
        return str;
    }

    /**
     * Ordena el rapers segons la seva puntuacio i actualizar les posicions
     * @param updatePosition per determina si volem que acutalitzi les posicions
     */
    public void sortRappers(boolean updatePosition) {
        int i;

        Collections.sort(rappers);

        if (updatePosition)
            for (i = 0; i < rappers.size(); i++)
                rappers.get(i).setPosition(i + 1);
    }

    /**
     * @return String amb tota la informació principal de la competició
     */
    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "Welcome to the Competition: " + name + "\n\nStarts on " + dateFormat.format(startDate) + "\nEnds on "
                + dateFormat.format(endDate) + "\nIt has " + phases.size() + " phases" + "\nCurrently there are "
                + rappers.size() + " participants\n\n";
    }

}
