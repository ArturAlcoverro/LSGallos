package presentation;

import business.Rapper;
import persistance.CompetitionDAO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Menu {

    private int option;
    private Scanner sc;

    public Menu(){
        sc = new Scanner(System.in);
    }

    public int printMenuRegister(){
        System.out.println("The competition hasn’t started yet. Do you want to:\n");
        System.out.print("1. Register\n2. Leave\n\nChoose an option: ");
        chooseOption();
        return this.option;
    }
    public int printMenuLogin(){
        System.out.println("The competition has started. Do you want to:\n");
        System.out.print("1. Log in\n2. Leave\n\nChoose an option: ");
        chooseOption();
        return this.option;
    }

    public void printMenuFinal(){}

    public void chooseOption(){
        this.option = sc.nextInt();
        sc.nextLine();
    }

    public void printCompetiton(CompetitionDAO dao){
        System.out.print(dao.toString());
    }

    public void Registration(CompetitionDAO dao) throws ParseException, IOException {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.println("Please, enter your personal information: ");
        System.out.print("- Full name: ");
        String name = sc.nextLine();
        System.out.print("- Artistic name: ");
        String artistic = sc.nextLine();
        System.out.print("- Date of birth (dd/MM/YYYY): ");
        String date = sc.nextLine();
        System.out.print("- Country: ");
        String country = sc.nextLine();
        System.out.print("- Level: ");
        int level = sc.nextInt();
        sc.nextLine();
        System.out.print("- Photo URL: ");
        String url = sc.nextLine();

        Rapper rapper = new Rapper(name, artistic, new SimpleDateFormat("dd/MM/yyyy").parse(date), country, level, url);
        if(dao.validateRapper(rapper)){
            System.out.println("\nRegistration Complete!");
            dao.addRapper(rapper);
        }else{
            System.out.println("\nRegistration not valid!");
        }
        System.out.println("-----------------------------------------------------------------------");

    }

    public void login(CompetitionDAO dao){
        boolean valid = true;
        do{
            System.out.print("Enter your artístic name: ");
            String name = sc.nextLine();
            if(!dao.validateLog(name)){
                System.out.println("Bro, there's no \"" + name + "\" on ma' list.");
            }else{
                valid = false;
            }
        }while(valid);
    }
}
