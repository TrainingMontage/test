package shared;

public class Convert {
    
    /**
     * Converts meters to feet.
     *
     * @param      meters  The meters quantity to convert
     *
     * @return     feet
     */
    public static double metersToFeet(double meters) {
        return meters * 3.28084;
    }

    /**
     * Converts feet to meters.
     *
     * @param      feet  The feet quantity to convert
     *
     * @return     meters
     */
    public static double feetToMeters(double feet) {
        return feet / 3.28084;
    }
}