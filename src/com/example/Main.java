package com.example;

import business.Rapper;
import persistance.CompetitionDAO;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        CompetitionDAO dao = new CompetitionDAO("data/competició.json");
    }
}
