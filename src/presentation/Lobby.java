package presentation;

import business.Competition;
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
    private Scanner scanner;
    private Competition competition;

    public Lobby(Competition competition){
        this.competition = competition;
        scanner = new Scanner(System.in);
        this.contPhase = 1;
    }

    public int showLobby(Rapper you){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("| "+ competition.getName() + " | Phase " + this.contPhase + "/" + competition.getPhaseSize() + " | Score: " + competition.getMyRapper(you.getStageName()).getScore() + " | Next battle: ");
        System.out.println("-------------------------------------------------------------------------");
        if(this.contPhase > competition.getPhaseSize()){
            System.out.print("1. Start the battle (deactivated)\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        }else{
            System.out.print("1. Start the battle\n2. Show ranking\n3. Create profile\n4. Leave competition\n\nChoose an option: ");
        }
        return chooseOption();
    }

    public void showRanking(Rapper you){
        System.out.println("--------------------------------------");
        System.out.println("| Pos. | Name\t\t\t\t | Score |");
        System.out.println("--------------------------------------");
        competition.showRanking(you);
    }

    public void profile(CompetitionDAO dao) throws IOException {
        Rapper rapper;
        do {
            System.out.print("Enter the name of the rapper: ");
            String name = scanner.nextLine();
            rapper = competition.getRapper(name);
        }while(rapper == null);

        System.out.println("Getting the information about their country of origin (" + rapper.getNationality() + ")...");
        Country country = dao.getInfoCountry(rapper.getNationality());
        System.out.println("Generating HTML file...");

        if (country != null) {
            generateHtml(country, rapper);
            System.out.println("Done! The profile will open in your default browser.");
        }else{
            System.out.println("Error generating the HTML file.");
        }
    }

    public void generateHtml(Country country, Rapper rapper) throws IOException {
        String name = lowerCamelCase(rapper.getStageName());
        String path = "data/HTML/" + name + ".html";
        File htmlTemplateFile = new File(path);
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
        Profile profile = ProfileFactory.createProfile(htmlTemplateFile, profileable);
        profile.setCountry(country.getName());
        profile.setFlagUrl(country.getFlag());
        for (int i = 0; i < country.getLanguages().size(); i++) {
            profile.addLanguage(country.getLanguages().get(i));
        }
        profile.writeAndOpen();
    }

    public String lowerCamelCase(String name){

        StringBuffer result = new StringBuffer(name.length());
        String strl = name.toLowerCase();
        boolean bMustCapitalize = false;
        for (int i = 0; i < strl.length(); i++)
        {
            char c = strl.charAt(i);
            if (c >= 'a' && c <= 'z')
            {
                if (bMustCapitalize)
                {
                    result.append(strl.substring(i, i+1).toUpperCase());
                    bMustCapitalize = false;
                }
                else
                {
                    result.append(c);
                }
            }
            else
            {
                bMustCapitalize = true;
            }
        }
        System.out.println(result);
        return result.toString();
    }

    public int chooseOption(){
        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    public void incrementPhase(){
        this.contPhase++;
    }

    public int getContPhase() {
        return contPhase;
    }
}
