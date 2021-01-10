package persistance;

import business.Competition;
import business.Phase;
import business.Rapper;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class CompetitionDAO {

    private Path path;
    private Gson gson;
    private Competition competition;

    public CompetitionDAO(String path) throws IOException, ParseException {
        this.path = Paths.get(path);
        JsonObject json = JsonParser.parseString(Files.readString(this.path)).getAsJsonObject();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        JsonObject competitionJson = json.getAsJsonObject().getAsJsonObject("competition");
        ArrayList<Phase> phases = new ArrayList<Phase>(Arrays.asList(gson.fromJson(competitionJson.get("phases"), Phase[].class)));
        ArrayList<String> countries = new ArrayList<String>(Arrays.asList(gson.fromJson(json.get("countries"), String[].class)));
        ArrayList<Rapper> rappers = new ArrayList<Rapper>(Arrays.asList(gson.fromJson(json.get("rappers"), Rapper[].class)));

        competition = new Competition(
                competitionJson.get("name").getAsString(),
                formatter.parse(competitionJson.get("startDate").getAsString()),
                formatter.parse(competitionJson.get("endDate").getAsString()),
                phases,
                countries,
                rappers
        );
    }

    /**
     * Actualitza el fitxer Json a partir de les dades de la competició
     * @throws IOException Si el fitxer es inaccessible
     */
    public void save() throws IOException {
        JsonElement element;
        JsonObject json = new JsonObject();

        json.add("competition", JsonParser.parseString(gson.toJson(competition)).getAsJsonObject());
        element = json.get("competition").getAsJsonObject().get("countries");
        json.add("countries", element);
        json.get("competition").getAsJsonObject().remove("countries");

        element = json.get("competition").getAsJsonObject().get("rappers");
        json.add("rappers", element);
        json.get("competition").getAsJsonObject().remove("rappers");

        Files.writeString(this.path, gson.toJson(json));
    }

    /**
     * Afegeix un raper a la competicio i al fitxer Json
     * @param rapper
     * @throws IOException
     */
    public void addRapper(Rapper rapper) throws IOException {
        this.competition.addRapper(rapper);
        save();
    }

    /**
     * @return La competició
     */
    public Competition getCompetition() {
        return competition;
    }
}

