package business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public abstract class Battle extends Thread implements Scorable {
    private ArrayList<Rapper> rappers;
    private Theme theme;
    private boolean yourBattle;
    private Random random;
    private Scanner scanner;

    public Battle(Theme theme, Rapper rapper1, Rapper rapper2) {
        this.theme = theme;
        rappers = new ArrayList<Rapper>();
        rappers.add(rapper1);
        rappers.add(rapper2);
        random = new Random();
        scanner = new Scanner(System.in);
    }

    /**
     * Iniciem la batalla paral·lelament a la resta ded batalles
     */
    public void run() {
        if ((rappers.get(0).isYou() && !rappers.get(0).isOut()) || (rappers.get(1).isYou() && !rappers.get(1).isOut()))
            playBattle();
        else
            simulateBattle();
    }

    /**
     * Iniciem la batalla que l'usuari te que jugar
     */
    private void playBattle() {
        int i;
        Rapper first, second;
        ArrayList<String> strophesFirst = new ArrayList<>();
        ArrayList<String> strophesSecond = new ArrayList<>();
        double scoreFirst, scoreSecond;
        String auxStrophe;

        System.out.println("\n-----------------------------------------------------------------------");

        System.out.println("Topic: " + theme.getName());
        System.out.println("A coin is tossed in the air and...");

        if (random.nextInt(2) == 1) {
            first = rappers.get(0);
            second = rappers.get(1);
        } else {
            first = rappers.get(1);
            second = rappers.get(0);
        }

        //Organitzem el diferents torns depenen si l'usuari es el primer o no
        if (first.isYou()) {
            for (i = 0; i < 2; i++) {
                System.out.println("\nYour turn");
                auxStrophe = playTurn();

                if (auxStrophe.length() > 2)
                    strophesFirst.add(auxStrophe);
                else
                    System.out.println("\nVery sad! Go home!");

                System.out.println(second.getStageName() + " it's your turn! Drop it!\n");
                auxStrophe = simulateTurn(second.getLevel());

                if (auxStrophe.length() > 2) {
                    System.out.println(auxStrophe);
                    strophesSecond.add(auxStrophe);
                } else
                    System.out.println("\nVery sad! Go home!");
            }
        } else {
            for (i = 0; i < 2; i++) {
                System.out.println(first.getStageName() + " it's your turn! Drop it!\n");
                auxStrophe = simulateTurn(first.getLevel());

                if (auxStrophe.length() > 2) {
                    System.out.println(auxStrophe);
                    strophesFirst.add(auxStrophe);
                } else
                    System.out.println("\nVery sad! Go home!");

                System.out.println("\nYour turn");
                auxStrophe = playTurn();

                if (auxStrophe.length() > 2)
                    strophesSecond.add(auxStrophe);
                else
                    System.out.println("\nVery sad! Go home!");
            }
        }

        //Calculem la puntuacio del rapers
        scoreFirst = calculateScore(countRhymes(strophesFirst));
        first.addScore(scoreFirst);
        scoreSecond = calculateScore(countRhymes(strophesSecond));
        second.addScore(scoreSecond);

        System.out.println("\nThis Round is for ");
        if (scoreFirst > scoreSecond) {
            first.win();
            System.out.println(first.getStageName() + "!");
        } else {
            second.win();
            System.out.println(second.getStageName() + "!");
        }

        System.out.println("\n-----------------------------------------------------------------------");

    }

    /**
     * Simula una battalla entre dos rapers
     */
    private void simulateBattle() {
        double scoreFirst = 0, scoreSecond = 0;
        int i;

        Rapper first = rappers.get(0);
        Rapper second = rappers.get(1);

        ArrayList<String> strophesFirst = new ArrayList<>();
        ArrayList<String> strophesSecond = new ArrayList<>();

        for (i = 0; i < 2; i++) {
            if (!first.isOut())
                strophesFirst.add(simulateTurn(first.getLevel()));
            if (!second.isOut())
                strophesSecond.add(simulateTurn(second.getLevel()));
        }

        if (!first.isOut()) {
            scoreFirst = calculateScore(countRhymes(strophesFirst));
            first.addScore(scoreFirst);
        }
        if (!second.isOut()) {
            scoreSecond = calculateScore(countRhymes(strophesSecond));
            second.addScore(scoreSecond);
        }
        if (scoreFirst > scoreSecond)
            first.win();
        else
            second.win();

    }

    /**
     * Li demana versos al usuari fins que acabi l'estrofa
     * @return la estrofa
     */
    private String playTurn() {
        StringBuilder strophe = new StringBuilder();
        String str;
        boolean verse;

        System.out.println("Enter your verse (blank line to finish):");

        do {
            str = scanner.nextLine();
            verse = !str.equals("");
            if (verse) {
                strophe.append(str).append("\n");
            }
        } while (!str.equals(""));

        return strophe.toString();
    }

    /**
     * Simula una torn depenent del nivell
     * @param lvl nivell del raper
     * @return  estrofa
     */
    private String simulateTurn(int lvl) {
        ArrayList<String> strophes = theme.getRhymes().get("" + lvl);

        if (strophes != null) {
            int size = strophes.size();
            if (size > 0) {
                return strophes.get(random.nextInt(size)).replace(",\n ", ",\n").replace(",\n", "\n").replace(".", "");
            } else {
                return "";
            }
        }
        else return "";
    }

    /**
     * Funció que rep les estrofes que ha jugat el raper en la seva batalla i
     * calcula quantes rimes ha aconseguit fer en total
     * 
     * @param estrofes Estrofes jugades per el raper en la batalla
     * @return El total de rimes que hi ha dins les estrofes
     */
    private int countRhymes(ArrayList<String> estrofes) {
        int countRhymes = 0;
        int countTotal = 0;
        String lastTwoCharacters, lastTwoCharacters2;

        for (String estrofe : estrofes) {
            String[] parts = estrofe.split("\n");
            ArrayList<String> versos = new ArrayList<>(Arrays.asList(parts));

            for (int j = 0; j < versos.size(); j++) {
                if (versos.get(j).length() > 2) {
                    lastTwoCharacters = versos.get(j).substring(versos.get(j).length() - 2);

                    for (int j2 = 0; j2 < versos.size(); j2++) {
                        if (j != j2 && versos.get(j2).length() > 2) {
                            lastTwoCharacters2 = versos.get(j2).substring(versos.get(j2).length() - 2);
                            if (lastTwoCharacters.equals(lastTwoCharacters2)) {
                                countRhymes++;
                                break;
                            }
                        }
                    }
                }
            }
            countTotal += countRhymes;
            countRhymes = 0;
        }
        return countTotal;
    }
}
