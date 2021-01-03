package com.example;

import business.Rapper;
import persistance.CompetitionDAO;
import persistance.ThemesDAO;
import presentation.Lobby;
import presentation.Menu;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CompetitionDAO dao = new CompetitionDAO("data/competició.json");
        ThemesDAO dao_theme = new ThemesDAO("data/batalles.json");
        Menu menu = new Menu();
        Lobby lobby = new Lobby();
        //Date date = new Date(System.currentTimeMillis()); //data actual linia correcte, la seguent es per fer comprovacions
        Date date = new Date("2020/12/25"); //data per provar si funciona quan encara no ha començat la comepticio
        int option;

        menu.printCompetiton(dao);
        if(date.before(dao.getStartDate())){
            option = menu.printMenuRegister();
            switch (option){
                case 1:
                    menu.Registration(dao);
                    break;
                case 2:
                    //option leaves
                    break;
            }
        }else if(date.after(dao.getStartDate()) && date.before(dao.getEndDate())){
            option = menu.printMenuLogin();
            switch (option){
                case 1:
                    menu.login(dao);
                    option = lobby.showLobby(dao);
                    switch (option){
                        case 1:
                            if(lobby.getContPhase() > dao.getPhaseSize()){
                                System.out.println("Competition ended. You can't battle anyone else!");
                            }else{
                                //fer tot el tema batalles i tal
                                lobby.incrementPhase();
                            }
                            break;
                        case 2:
                            lobby.showRanking(dao);
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 2:
                    //options leaves
                    break;
            }
        }else{
            //mostrar qui ha guanyat el torneig i dona l'opcio per sortir
        }

    }
}
