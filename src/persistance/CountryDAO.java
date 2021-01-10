package persistance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import business.Country;

public class CountryDAO {

    /**
     * @param nationality nom país.
     * @return Un país amb les dades de la API.
     * @throws IOException
     */
    public static Country getInfoCountry(String nationality) throws IOException {
        String url = "https://restcountries.eu/rest/v2/name/" + nationality;
        url = removeSpace(url);
        // System.out.println(url);
        URL urlRequest = new URL(url);
        String readLine;
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            // System.out.println(response.toString());
            String data = response.toString();
            JsonElement element = JsonParser.parseString(data);
            JsonArray array = element.getAsJsonArray();
            JsonArray arrayLanguages = array.get(0).getAsJsonObject().get("languages").getAsJsonArray();
            ArrayList<String> languages = new ArrayList<>();
            for (int i = 0; i < arrayLanguages.size(); i++) {
                languages.add(arrayLanguages.get(i).getAsJsonObject().get("name").getAsString());
                // System.out.println(languages.get(i));
            }
            Country country = new Country(array.get(0).getAsJsonObject().get("name").getAsString(),
                    array.get(0).getAsJsonObject().get("flag").getAsString(), languages);
            return country;
        } else {
            System.out.println("Getting the information form the RESTapi is not working.");
        }
        return null;
    }

    /**
     * Funció que rep un string per paràmetre i retorna el mateix string canviant els espais per el valor %20
     * @param s String que volem transformar
     * @return  String ja transformat en el format indicat
     */
    private static String removeSpace(String s) {
        StringBuilder withoutspaces = new StringBuilder();
        String replace = "%20";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                withoutspaces.append(s.charAt(i));
            } else {
                withoutspaces.append(replace);
            }
        }
        return withoutspaces.toString();
    }
    
}
