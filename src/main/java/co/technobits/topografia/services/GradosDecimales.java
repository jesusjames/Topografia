package co.technobits.topografia.services;


public class GradosDecimales {
	
	public double convertir(String coordenada) {
        Double coord = toDecimal(coordenada);
        return coord;
    }
	
    public static Double toDecimal(String latOrLng) {
        try {
            String[] latlng = latOrLng.replaceAll("[^0-9.\\s-]", "").split(" ");
            Double dlatlng = toDecimal(latlng); 
            return dlatlng;
        } catch(Exception ex) {
            return null;
        }
    }
 
    public static Double toDecimal(String[] coord) {
        double d = Double.parseDouble(coord[0]);
        double m = Double.parseDouble(coord[1]);
        double s = Double.parseDouble(coord[2]);
        double signo = 1;
        if (coord[0].startsWith("-"))
            signo = -1;
        return signo * (Math.abs(d) + (m / 60.0) + (s / 3600.0));
    }
    
    public String Reverse(double decimal) {
    	double coo = decimal;
    	int deg = (int) Math.floor(Math.abs(coo));
    	int min = (int) Math.floor((Math.abs(coo)-deg)*60);
    	int sec = (int) Math.floor(((Math.abs(coo)-deg)*60-min)*60);
    	return deg+"°"+min+"'"+sec+"''";
    }

}
