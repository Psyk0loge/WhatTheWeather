public class Weather {
    private double cuTemp;
    private String description;
    private double[] temperaturen;
    private Weather[] Next4Days = new Weather[4];
    private int humidity;
    private int cloudines;



    //Konstruktoren
    public void setTemp(double gradKelvin){
        double Umrechentemp=gradKelvin-273.15;
        Umrechentemp= Umrechentemp *100;
        Umrechentemp = Math.round(Umrechentemp);
        Umrechentemp = Umrechentemp/100;
        this.cuTemp=Umrechentemp;
    }

    public Weather(double temp, String description, double[] temperaturinh) {
        setTemp(temp);
        this.description = description;
        this.temperaturen = temperaturinh;
    }
    public Weather(){

    }

    public double getTemp() {
        return cuTemp;
    }

    public String getDescription() {
        return description;
    }
}
