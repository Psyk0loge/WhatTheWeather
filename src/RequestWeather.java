import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestWeather {
    private class Weather{
        int id;
        String longitude;
        String latitude;
        String main;
        String description;
        double temperature;
        double temperatureMIN;
        double getTemperatureMAX;
        double windSpeed;

    };
    private static HttpURLConnection connection;
    public static void main(String[] args) {
        //2 ways for HTTP request in JAVA
        //Method 1 Java.net.httpURLConnection
        BufferedReader reader;
        //to read each line to build our response together...
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Mannheim&units=metric&appid=f83e506f73bdb831078f69cfbda65b7f");
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
