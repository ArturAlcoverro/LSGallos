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

    /**
     * @return Nom del país
     */
    public String getName() {
        return name;
    }

    /**
     * @return String que conte la url de la foto de la bandera del pais
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @return Les llengues que es parlen en el país
     */
    public ArrayList<String> getLanguages() {
        return languages;
    }
}
