package business;

import java.util.ArrayList;
import java.util.HashMap;

public class Theme {
    private String name;
    private HashMap<String, ArrayList<String>> rhymes;

    public Theme(String name, HashMap<String, ArrayList<String>> rhymes) {
        this.name = name;
        this.rhymes = rhymes;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, ArrayList<String>> getRhymes() {
        return rhymes;
    }
}
