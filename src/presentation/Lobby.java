package presentation;

import business.Country;
import business.Rapper;
import edu.salleurl.profile.Profile;
import edu.salleurl.profile.ProfileFactory;
import edu.salleurl.profile.Profileable;
import persistance.CompetitionDAO;


import java.io.File;
import java.io.IOException;
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

    public void showRanking(CompetitionDAO dao, Rapper you){
        System.out.println("--------------------------------------");
        System.out.println("| Pos. | Name\t\t\t\t | Score |");
        System.out.println("--------------------------------------");
        dao.showRanking(you);
    }

    public void profile(CompetitionDAO dao) throws IOException {
        System.out.print("Enter the name of the rapper: ");
        String name = sc.nextLine();
        Rapper rapper = dao.getRapper(name);
        System.out.println("Getting the information about their country of origin (" + rapper.getNationality() + ")...");
        Country country = dao.getInfoCountry(rapper.getNationality());
        System.out.println("Generating HTML file...");
        generateHtml(country, rapper);
        System.out.println("Done! The profile will open in your default browser.");
    }

    public void generateHtml(Country country, Rapper rapper) throws IOException {
        File htmlTemplateFile = new File("data/HTML/trueno.html");
        ProfileFactory factory = new ProfileFactory();
        Profileable profileable = new Profileable() {
            @Override
            public String getName() {
                return rapper.getRealName();
            }

            @Override
            public String getNickname() {
                return rapper.getStageName();
            }

            @Override
            public String getBirthdate() {
                return String.valueOf(rapper.getBirth());
            }

            @Override
            public String getPictureUrl() {
                return rapper.getPhoto();
            }
        };
        Profile profile = factory.createProfile(htmlTemplateFile, profileable);
        profile.setCountry(country.getName());
        profile.setFlagUrl(country.getFlag());
        for (int i = 0; i < country.getLanguages().size(); i++) {
            profile.addLanguage(country.getLanguages().get(i));
        }
        profile.writeAndOpen();
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
