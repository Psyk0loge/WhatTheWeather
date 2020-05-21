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
    //Api-Key
    private static final String appid= "9ffde3100f0029f1e405581994739b1e";

    //URL parts to get the longitude and the latitude
    private static final String WebsideForRequest ="http://api.openweathermap.org/data/2.5/weather?q=";
    private static  final String URLmidCode ="&units=metric&appid=";
    private static HttpURLConnection connection;

    //URL stuff to get the data
    private static final String WebsideDataRequest ="https://api.openweathermap.org/data/2.5/onecall?";
    private static String URLmidDataRequest = "&%20exclude=hourly&appid=";
    private static String LatURL="lat=";
    private static String LonURL="&lon=";

    //changable lon and lat Strings for dynamic request
    public static double Lat;
    public static double Lon;


    public static void main(String[] args) {
        currentWeather a = verbindung(true);
        System.out.println(a.getTemp());
    }
    public static void parse1(String response){
        JSONObject WeatherStats = new JSONObject(response);
        double lon = WeatherStats.getJSONObject("coord").getDouble("lon");
        double lat = WeatherStats.getJSONObject("coord").getDouble("lat");
        Lon = lon;
        Lat = lat;
        System.out.println(lon);
        System.out.println(lat);
        double[] geoData = {lon,lat};
    }
    private static class currentWeather{
       private double temp;
       private String description;
       private double temperaturin1h;
       //Konstruktoren
        public void setTemp(double gradKelvin){
            double Umrechentemp=gradKelvin-273.15;
           Umrechentemp= Umrechentemp *100;
           Umrechentemp = Math.round(Umrechentemp);
           Umrechentemp = Umrechentemp/100;
           this.temp=Umrechentemp;
        }

        public currentWeather(double temp, String description, double temperaturin1h) {
            setTemp(temp);
            this.description = description;
            this.temperaturin1h = temperaturin1h;
        }
        public currentWeather(){

        }

        public double getTemp() {
            return temp;
        }

        public String getDescription() {
            return description;
        }

    }
    public static currentWeather parse2(String responseBody){
        JSONObject currentWeather = new JSONObject(responseBody);
        double temp = currentWeather.getJSONObject("current").getDouble("temp");
        double temperaturin1h  = currentWeather.getJSONArray("hourly").getJSONObject(0).getDouble("temp");
        String description = currentWeather.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("description");
       //currentWeather currentWeather1 = new currentWeather(J)
        currentWeather a = new currentWeather(temp, description, temperaturin1h);
        System.out.println(temp +" "+description+ " "+temperaturin1h);
        return a;
    }

    public static currentWeather verbindung(boolean geoAbfrage){
        String URLString;
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        currentWeather b = new currentWeather();
        if(geoAbfrage){
            System.out.println("Von was für einer Stadt wollen Sie das Wetter wissen?");
            String city;
            Scanner scan = new Scanner(System.in);
            city = scan.nextLine();
            scan.close();
            URLString = WebsideForRequest + city + URLmidCode +appid;
        }else{
            URLString = WebsideDataRequest + LatURL+Lat+LonURL+Lon+URLmidDataRequest+appid;;
        }
        try {
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
            if(geoAbfrage){
                parse1(responseContent.toString());
               b= verbindung(false);
                connection.disconnect();
            }else{
                System.out.println(Lon+" "+Lat);
                b= parse2(responseContent.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return b;
    }
}
