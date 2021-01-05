package business;

import java.util.ArrayList;

public class Country {
    private String name;
    private String flag;
    private ArrayList<String> languages;

    public Country(String name, String flag, ArrayList<String> languages){
        this.name = name;
        this.flag = flag;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }
}
