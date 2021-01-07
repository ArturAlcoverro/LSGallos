package persistance;

import business.Competition;
import business.Country;
import business.Phase;
import business.Rapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
     * @param nationality nom país.
     * @return Un país amb les dades de la API.
     * @throws IOException
     */
    public Country getInfoCountry(String nationality) throws IOException{
        String url = "https://restcountries.eu/rest/v2/name/" + nationality;
        url = removeSpace(url);
        //System.out.println(url);
        URL urlRequest = new URL(url);
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null){
                response.append(readLine);
            }in .close();

            //System.out.println(response.toString());
            String data = response.toString();
            JsonElement element = JsonParser.parseString(data);
            JsonArray array = element.getAsJsonArray();
            JsonArray arrayLanguages = array.get(0).getAsJsonObject().get("languages").getAsJsonArray();
            ArrayList<String> languages = new ArrayList<>();
            for (int i = 0; i < arrayLanguages.size(); i++) {
                languages.add(arrayLanguages.get(i).getAsJsonObject().get("name").getAsString());
                //System.out.println(languages.get(i));
            }
            Country country = new Country(
                    array.get(0).getAsJsonObject().get("name").getAsString(),
                    array.get(0).getAsJsonObject().get("flag").getAsString(),
                    languages
            );
            return country;
        }else{
            System.out.println("Getting the information form the RESTapi is not working.");
        }
        return null;
    }

    public String removeSpace(String s) {
        String withoutspaces = "";
        String replace = "%20";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' '){
                withoutspaces += s.charAt(i);
            }else{
                withoutspaces += replace;
            }
        }
        return withoutspaces;
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

