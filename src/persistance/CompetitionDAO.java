package persistance;

import business.Competition;
import business.Phase;
import business.Rapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
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
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").setPrettyPrinting().create();

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

    public void addRapper(Rapper rapper) throws IOException {
        this.competition.addRapper(rapper);
        save();
    }
}

