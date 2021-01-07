package persistance;

import business.Theme;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ThemesDAO {
    private Path path;
    private ArrayList<Theme> themes;

    public ThemesDAO(String path) throws IOException{
        themes = new ArrayList<>();
        Gson gson = new Gson();
        this.path = Paths.get(path);

        JsonObject json = JsonParser.parseString(Files.readString(this.path)).getAsJsonObject();
        JsonArray array = json.getAsJsonArray("themes");

        for (int i = 0; i < array.size(); i++) {
            HashMap<String, ArrayList<String>> rhymes = new HashMap<>();
            JsonArray array_rhymes = array.get(i).getAsJsonObject().get("rhymes").getAsJsonArray();

            rhymes.put("1", new ArrayList<>(Arrays.asList(gson.fromJson(array_rhymes.get(0).getAsJsonObject().get("1").getAsJsonArray(), String[].class))));
            rhymes.put("2", new ArrayList<>(Arrays.asList(gson.fromJson(array_rhymes.get(0).getAsJsonObject().get("2").getAsJsonArray(), String[].class))));

            themes.add(new Theme(array.get(i).getAsJsonObject().get("name").getAsString(), rhymes));
        }
        //Comprovació de prints per veure si esta guardant bé tot l'array de temes
        /*for (int i = 0; i < themes.size(); i++) {
            System.out.println(themes.get(i).getName());
            System.out.println(themes.get(i).getRhymes().get("1").toString());
            System.out.println(themes.get(i).getRhymes().get("2").toString());
        }*/
    }
}
