package presentation;

import business.Competition;
import business.Rapper;
import persistance.CompetitionDAO;
import business.exceptions.WrongInputException;
import business.exceptions.WrongRapperNameException;

import java.util.InputMismatchException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    private CompetitionDAO competitionDAO;
    private Competition competition;
    private Lobby lobby;

    public Menu(CompetitionDAO competitionDAO) {
        this.competitionDAO = competitionDAO;
        this.competition = competitionDAO.getCompetition();
        this.scanner = new Scanner(System.in);
        this.lobby = new Lobby(this.competition);
    }

    /**
     * Funció que indica el començament del programa, depenent de la data actual i
     * les dates de la competició mostrarà una informacio o una altre
     * 
     * @throws IOException
     * @throws ParseException
     */
    public void start() throws IOException, ParseException {
        //Date date = new Date();
        Date date = new Date("2020/12/25"); // data per provar si funciona quan encara no ha començat la comepticio
        boolean error;
        printCompetition();
        if (date.before(competition.getStartDate())) {
            do {
                try {
                    error = false;
                    menuRegister();
                } catch (WrongInputException ex) {
                    error = true;
                }
            } while (error);
        } else if (date.after(competition.getStartDate()) && date.before(competition.getEndDate())){
            do {
                try {
                    error = false;
                    menuLogin();
                } catch (WrongInputException ex) {
                    error = true;
                }
            } while (error);
        }
        else
            competition.getChampion();
    }

    /**
     * Funció que mostra les opcions a l'usuari i li demana quina opcio vol escollir
     * 
     * @return Opcio introduida
     * @throws WrongInputException
     */
    public int printMenuRegister() throws WrongInputException {
        System.out.println("The competition hasn’t started yet. Do you want to:\n");
        System.out.print("1. Register\n2. Leave\n\nChoose an option: ");
        return chooseOption();
    }

    /**
     * Menú de registre que controla quines opcions ha introduit l'usuari i per cada
     * una realitzar una acció diferent
     * 
     * @throws IOException
     * @throws ParseException
     * @throws WrongInputException
     */
    public void menuRegister() throws IOException, ParseException, WrongInputException {
        int option;

        do {
            option = printMenuRegister();
            if (option == 1)
                registration();
            else if (option != 2)
                throw new WrongInputException("Incorrect Option. Must be 1 or 2!\n");
        } while (option != 2);
    }

    /**
     * Funció que mostra les opcions a l'usuari del menú login i li demana quina
     * opció vol escollir
     * 
     * @return Opció introduida
     * @throws WrongInputException
     */
    public int printMenuLogin() throws WrongInputException {
        System.out.println("The competition has started. Do you want to:\n");
        System.out.print("1. Log in\n2. Leave\n\nChoose an option: ");
        return chooseOption();
    }

    /**
     * Funció que controla el menú de login i en funció de la opció escollida duu a
     * terme una acció o altre
     * 
     * @throws IOException
     * @throws WrongInputException
     */
    public void menuLogin() throws IOException, WrongInputException {
        int option = 0;
        boolean error;
        do {
            do {
                try {
                    error = false;
                    option = printMenuLogin();
                } catch (WrongInputException ex) {
                    error = true;
                }
            } while (error);

            if (option == 1) {
                do {
                    try {
                        error = false;
                        competition.setYou(login());
                    } catch (WrongRapperNameException e) {
                        error = true;
                    }
                } while (error);

                lobby.menuLobby();
            } else if (option != 2)
                throw new WrongInputException("Incorrect Option. Must be 1 or 2!\n");

        } while (option != 2 && option != 1);
    }

    /**
     * Recull l'opció introduida per l'usuari en una variable i la retorna
     * 
     * @return Opció introduida
     * @throws WrongInputException
     */
    public int chooseOption() throws WrongInputException {
        int option;
        try{
            option = scanner.nextInt();
        }catch(InputMismatchException ex){
            scanner.nextLine();
            throw new WrongInputException("Invalid format!\n");
        }
        scanner.nextLine();
        return option;
    }

    /**
     * Mostra per pantalla la informació general de la competició
     */
    public void printCompetition() {
        System.out.print(competition.toString());
    }

    /**
     * Funció encarregada de dur a terme el registre de un nou raper, demanant-li
     * totes les dades necessaries i validar que el registre sigui correcte o no
     * 
     * @throws ParseException
     * @throws IOException
     */
    public void registration() throws ParseException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        boolean error;
        
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("Please, enter your personal information: ");
        System.out.print("- Full name: ");
        String name = scanner.nextLine();
        System.out.print("- Artistic name: ");
        String artistic = scanner.nextLine();
        
        do{
            try {
                error = false;
                System.out.print("- Date of birth (dd/MM/yyyy): ");
                String strDate = scanner.nextLine();
                date = dateFormat.parse(strDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format!");
                error = true;
            }
        }while(error);

        System.out.print("- Country: ");
        String country = scanner.nextLine();
        System.out.print("- Level: ");
        int level = scanner.nextInt();
        scanner.nextLine();
        System.out.print("- Photo URL: ");
        String url = scanner.nextLine();

        Rapper rapper = new Rapper(name, artistic, date, country, level, url);
        if (competition.validateRapper(rapper)) {
            System.out.println("\nRegistration Complete!");
            competitionDAO.addRapper(rapper);
        } else {
            System.out.println("\nRegistration not valid!");
        }
        System.out.println("-----------------------------------------------------------------------");

    }

    /**
     * Funció d'inici de sessió que demana el nom artístic per accedir a la
     * competició i si no es troba cap raper amb aquell nom se li demana a l'usuari
     * que el torni a introduir
     * 
     * @return Raper que ha inciat sessió en la competició
     * @throws WrongRapperNameException
     */
    public Rapper login() throws WrongRapperNameException {
        boolean exists;
        String name;
        Rapper rapper;

        System.out.print("Enter your artístic name: ");
        name = scanner.nextLine();
        exists = competition.validateLog(name);
        if (!exists)
            throw new WrongRapperNameException("Bro, there's no \"" + name + "\" on ma' list.\n");

        rapper = competition.getMyRapper(name);
        rapper.setYou();
        return rapper;
    }

}
