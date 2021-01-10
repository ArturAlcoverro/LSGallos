package com.example;

import persistance.CompetitionDAO;
import presentation.Menu;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) {
        
        CompetitionDAO competitionDAO;
        try {
            competitionDAO = new CompetitionDAO("data/competició.json");
            Menu menu = new Menu(competitionDAO);
            menu.start();

        } catch (IOException | ParseException e) {

            System.out.println("ERROR. Data inaccesible!");

            e.printStackTrace();
        }

    }
}
