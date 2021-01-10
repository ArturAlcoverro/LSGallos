
package presentation;

import business.Competition;
import business.Country;
import business.Rapper;
import edu.salleurl.profile.Profile;
import edu.salleurl.profile.ProfileFactory;
import edu.salleurl.profile.Profileable;
import persistance.CompetitionDAO;
import persistance.CountryDAO;
import java.util.Collections;
import java.util.InputMismatchException;

import business.exceptions.WrongInputException;
import business.exceptions.WrongRapperNameException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Lobby {
    private Scanner scanner;
    private Competition competition;

    public Lobby(Competition competition) {
        this.competition = competition;
        scanner = new Scanner(System.in);
    }

    /**
     * Funció que imprimeix per pantalla el menú del login i demanarà quina opció
     * vol executar l'usuari
     * 
     * @return Opció introduida per l'usuari
     * @throws WrongInputException
     */
    public int showLobby() throws WrongInputException {
        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------");

        System.out.println(competition.getStatus());

        System.out.println(
                "----------------------------------------------------------------------------------------------------------------------");
        if (competition.isFinished()) {
            System.out.print(
                    "1. Start the battle (deactivated)\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        } else {
            System.out.print(
                    "1. Start the battle\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        }
        return chooseOption();
    }

    /**
     * Funció que estableix el header de la taula del ranking i crida la funció que
     * mostrarà els rapers amb la seva puntuació dins la taula
     */
    public void showRanking() {
        System.out.println("---------------------------------------");
        System.out.println("| Pos. | Name\t\t\t | Score |");
        System.out.println("---------------------------------------");
        competition.showRanking();
    }

    /**
     * Menú del Lobby on l'usuari que ha iniciat sessió pot triar entre jugar la
     * batalla, mirar el ranking, generar un perfil o abandonar la competicio
     * 
     * @throws IOException
     * @throws WrongInputException
     */
    public void menuLobby() throws IOException, WrongInputException {
        int option = 0;
        boolean error;

        competition.prepareNextPhase();

        do {
            do {
                try {
                    error = false;
                    option = showLobby();
                } catch (WrongInputException ex) {
                    error = true;
                }
            } while (error);
            switch (option) {
                case 1:
                    if (competition.isFinished()) {
                        System.out.println("Competition ended. You can't battle anyone else!");
                        System.out.println(competition.getResults());

                    } else {
                        competition.startBattles();
                        if (!competition.isFinished())
                            competition.prepareNextPhase();
                    }
                    break;

                case 2:
                    showRanking();
                    break;

                case 3:
                    do {
                        try {
                            error = false;
                            profile();

                        } catch (WrongRapperNameException e) {
                            error = true;
                        }
                    } while (error);
                    break;

                case 4:
                    competition.leave();
                    while (!competition.isFinished()) {
                        competition.prepareNextPhase();
                        competition.startBattles();
                    }
                    System.out.println(competition.getResults());
                    break;

                default:
                    System.out.println("Incorrect option. Must be between 1 and 4!\n");
            }
        } while (option != 4);
    }

    /**
     * Funció que obté el rapper del que es vol obtenir el perfil més endavant i
     * consulta les dades del seu país a la RESTApi
     * 
     * @throws IOException
     * @throws WrongRapperNameException
     */
    public void profile() throws IOException, WrongRapperNameException {
        Rapper rapper;
        System.out.print("Enter the name of the rapper: ");
        String name = scanner.nextLine();
        rapper = competition.getRapper(name);
        if (rapper == null) {
            throw new WrongRapperNameException("The name of the rapper does not exists!\n");
        }

        System.out
                .println("Getting the information about their country of origin (" + rapper.getNationality() + ")...");
        Country country = CountryDAO.getInfoCountry(rapper.getNationality());
        System.out.println("Generating HTML file...");

        if (country != null) {
            generateHtml(country, rapper);
            System.out.println("Done! The profile will open in your default browser.");
        } else {
            System.out.println("Error generating the HTML file.");
        }
    }

    /**
     * Funció que genera un fitxer HTML i l'obre en el navegador a partir de les
     * dades que rep per paràmetre
     * 
     * @param country Informació del pais on té nacionalitat el raper
     * @param rapper  Raper del qual volem generar el perfil en format html
     * @throws IOException
     */
    public void generateHtml(Country country, Rapper rapper) throws IOException {
        String name = lowerCamelCase(rapper.getStageName());
        String path = "data/HTML/" + name + ".html";
        File htmlTemplateFile = new File(path);
        int position;

        Profile profile = ProfileFactory.createProfile(htmlTemplateFile, rapper);
        profile.setCountry(country.getName());
        profile.setFlagUrl(country.getFlag());

        for (int i = 0; i < country.getLanguages().size(); i++) {
            profile.addLanguage(country.getLanguages().get(i));
        }

        position = rapper.getPosition();

        if (position > 0) {
            profile.addExtra("Points", String.format("%.2f",rapper.getScore()));
            if (position == 1 && competition.isFinished())
                profile.addExtra("Position", "Winner!");
            else
                profile.addExtra("Position", String.valueOf(position));
        }
        profile.writeAndOpen();
    }

    /**
     * Funció que rep un String i el transform en el format lowerCamelCase
     * 
     * @return String ja transformat en el format indicat
     */
    public String lowerCamelCase(String name) {

        StringBuffer result = new StringBuffer(name.length());
        String strl = name.toLowerCase();
        boolean bMustCapitalize = false;
        for (int i = 0; i < strl.length(); i++) {
            char c = strl.charAt(i);
            if (c >= 'a' && c <= 'z') {
                if (bMustCapitalize) {
                    result.append(strl.substring(i, i + 1).toUpperCase());
                    bMustCapitalize = false;
                } else {
                    result.append(c);
                }
            } else {
                bMustCapitalize = true;
            }
        }
        return result.toString();
    }

    /**
     * Funció que guarda la opció introduida per pantalla de l'usuari
     * 
     * @return opció introduida
     * @throws WrongInputException
     */
    public int chooseOption() throws WrongInputException {
        int option;

        try {
            option = scanner.nextInt();
        } catch (InputMismatchException ex) {
            scanner.nextLine();
            throw new WrongInputException("Invalid format!\n");
        }

        scanner.nextLine();
        return option;
    }

}
