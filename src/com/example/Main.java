package com.example;

import persistance.CompetitionDAO;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CompetitionDAO dao = new CompetitionDAO("data/competició.json");

        System.out.println();

    }
}
