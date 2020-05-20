import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RequestWeather {
    //URL parts that are fixed to put together ur with user input
    private static final String WebsideForRequest ="http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String appid= "f83e506f73bdb831078f69cfbda65b7f";
    private static  final String URLmidCode ="&units=metric&appid=";

    private static HttpURLConnection connection;
    public static void main(String[] args) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            System.out.println("Von was für einer Stadt wollen Sie das Wetter wissen?");
            String city;
            Scanner scan = new Scanner(System.in);
            city = scan.nextLine();
            scan.close();
            //URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Mannheim&units=metric&appid=f83e506f73bdb831078f69cfbda65b7f");
            String URLString = WebsideForRequest + city + URLmidCode +appid;
            URL url = new URL(URLString);
            connection= (HttpURLConnection) url.openConnection();
            //Request Setup
            connection.setRequestMethod("GET");
            //wann der Timeouten soll in millisekunden
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            //System.out.println(status); status 200 -> successfull connection
            //the answer we get form the request is a input stream we need an inputStream reader
            //falls der status > 299 -> Problem dann Error msg einlesen
            if(status>299){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line=reader.readLine())!=null){
                    responseContent.append(line);
                }
                reader.close();
            }else{
                //if connection is succesfull
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=reader.readLine())!=null){
                    responseContent.append(line);
                }
                reader.close();
            }
            System.out.println(responseContent.toString());
            parse(responseContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
    }
    public static String parse(String responseBody){
        //loop fro JSon array to get information for each Opject in the array

       JSONObject WeatherStats = new JSONObject(responseBody);
       String weather = WeatherStats.getJSONArray("weather").getJSONObject(0).getString("description");
        System.out.println(weather);
       double temp = WeatherStats.getJSONObject("main").getDouble("temp");
       System.out.println(temp);
       String city = WeatherStats.getString("name");
       System.out.println(city);

        return null;
    }
}
