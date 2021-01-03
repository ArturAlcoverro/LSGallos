package presentation;

import persistance.CompetitionDAO;

import java.util.Scanner;

public class Lobby {
    private int contPhase;
    private int option;
    private Scanner sc;

    public Lobby(){
        sc = new Scanner(System.in);
        this.contPhase = 1;
    }

    public int showLobby(CompetitionDAO dao){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("| "+ dao.getName() + " | Phase " + this.contPhase + "/" + dao.getPhaseSize() + " | Score: " + " | Next battle: ");
        System.out.println("-------------------------------------------------------------------------");
        if(this.contPhase > dao.getPhaseSize()){
            System.out.print("1. Start the battle (deactivated)\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        }else{
            System.out.print("1. Start the battle\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        }
        chooseOption();
        return this.option;
    }

    public void showRanking(CompetitionDAO dao){
        System.out.println("---------------------------------------");
        System.out.println("| Pos. | Name\t\t\t | Score |");
        System.out.println("---------------------------------------");
        dao.showRanking();
    }

    public void chooseOption(){
        this.option = sc.nextInt();
        sc.nextLine();
    }

    public void incrementPhase(){
        this.contPhase++;
    }

    public int getContPhase() {
        return contPhase;
    }
}
