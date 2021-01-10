package business;

import com.google.gson.annotations.Expose;

public class Phase {
    @Expose
    private double budget;

    @Expose
    private String country;

    public Phase(double budget, String country) {
        this.budget = budget;
        this.country = country;
    }
}
