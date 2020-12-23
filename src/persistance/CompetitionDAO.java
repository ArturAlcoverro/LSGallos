package persistance;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompetitionDAO {

    private Path path;
    private JsonElement json;


    public CompetitionDAO(String path) throws IOException {
        this.path = Paths.get(path);
        json = JsonParser.parseString(Files.readString(this.path));
        json = json.getAsJsonObject().get("rappers");
    }
}

