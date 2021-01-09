package presentation;

import business.Competition;
import business.Rapper;
import persistance.CompetitionDAO;

import java.io.IOException;
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

    public void start() throws IOException, ParseException {
        //Date date = new Date();
        Date date = new Date("2020/12/25"); //data per provar si funciona quan encara no ha començat la comepticio

        printCompetition();
        if (date.before(competition.getStartDate()))
            menuRegister();
        else if (date.after(competition.getStartDate()) && date.before(competition.getEndDate()))
            menuLogin();
        else competition.getChampion();
    }

    public int printMenuRegister() {
        System.out.println("The competition hasn’t started yet. Do you want to:\n");
        System.out.print("1. Register\n2. Leave\n\nChoose an option: ");
        return chooseOption();
    }

    public void menuRegister() throws IOException, ParseException {
        int option;

        do {
            option = printMenuRegister();
            if(option == 1) registration();
            else if(option !=2) System.out.println("Invalid option");
        } while (option != 2);
    }

    public int printMenuLogin() {
        System.out.println("The competition has started. Do you want to:\n");
        System.out.print("1. Log in\n2. Leave\n\nChoose an option: ");
        return chooseOption();
    }

    public void menuLogin() throws IOException {
        int option = printMenuLogin();

        switch (option) {
            case 1:
                Rapper you = login();
                do{
                    option = lobby.showLobby(you);
                    switch (option) {
                        case 1:
                            if (lobby.getContPhase() > competition.getPhaseSize()) {
                                System.out.println("Competition ended. You can't battle anyone else!");
                            } else {
                                //fer tot el tema batalles i tal
                                lobby.incrementPhase();
                            }
                            break;
                        case 2:
                            lobby.showRanking(you);
                            break;
                        case 3:
                            lobby.profile(competitionDAO);
                            break;
                        case 4:
                            break;
                        default:
                            System.out.println("Option invalid!");
                            break;
                    }
                }while(option!=4);
                break;
            case 2:
                //options leaves
                break;
            default:
                System.out.println("Option invalid!");
                break;
        }
    }

    public int chooseOption() {
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    public void printCompetition() {
        System.out.print(competition.toString());
    }

    public void registration() throws ParseException, IOException {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("Please, enter your personal information: ");
        System.out.print("- Full name: ");
        String name = scanner.nextLine();
        System.out.print("- Artistic name: ");
        String artistic = scanner.nextLine();
        System.out.print("- Date of birth (dd/MM/YYYY): ");
        String date = scanner.nextLine();
        System.out.print("- Country: ");
        String country = scanner.nextLine();
        System.out.print("- Level: ");
        int level = scanner.nextInt();
        scanner.nextLine();
        System.out.print("- Photo URL: ");
        String url = scanner.nextLine();

        Rapper rapper = new Rapper(name, artistic, new SimpleDateFormat("dd/MM/yyyy").parse(date), country, level, url);
        if (competition.validateRapper(rapper)) {
            System.out.println("\nRegistration Complete!");
            competitionDAO.addRapper(rapper);
        } else {
            System.out.println("\nRegistration not valid!");
        }
        System.out.println("-----------------------------------------------------------------------");

    }

    public Rapper login() {
        boolean exists;
        String name;
        Rapper rapper;

        do {
            System.out.print("Enter your artístic name: ");
            name = scanner.nextLine();
            exists = competition.validateLog(name);
            if (!exists)
                System.out.println("Bro, there's no \"" + name + "\" on ma' list.");
        } while (!exists);

        rapper = competition.getMyRapper(name);
        return rapper;
    }

}
