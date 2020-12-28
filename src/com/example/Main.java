package com.example;

import business.Rapper;
import persistance.CompetitionDAO;
import persistance.ThemesDAO;
import presentation.Menu;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CompetitionDAO dao = new CompetitionDAO("data/competició.json");
        ThemesDAO dao_theme = new ThemesDAO("data/batalles.json");
        Menu menu = new Menu();
        //Date date = new Date(System.currentTimeMillis()); //data actual linia correcte, la seguent es per fer comprovacions
        Date date = new Date("2020/12/06"); //data per provar si funciona quan encara no ha començat la comepticio
        int option;

        menu.printCompetiton(dao);
        if(date.before(dao.getCompetition().getStartDate())){
            option = menu.printMenuRegister();
            switch (option){
                case 1:
                    menu.Registration(dao);
                    break;
                case 2:
                    //option leaves
                    break;
            }
        }else if(date.after(dao.getCompetition().getStartDate()) && date.before(dao.getCompetition().getEndDate())){
            option = menu.printMenuLogin();
            switch (option){
                case 1:
                    //fer login
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
