package com.example;

import persistance.CompetitionDAO;
import persistance.ThemesDAO;
import presentation.Menu;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CompetitionDAO competitionDAO = new CompetitionDAO("data/competició.json");
        ThemesDAO themesDAO = new ThemesDAO("data/batalles.json");

        Menu menu = new Menu(competitionDAO);
        menu.start();
    }
}
