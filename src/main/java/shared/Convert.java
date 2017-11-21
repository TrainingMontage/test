/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *                                              /____/
 *     __  ___                 __
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 */

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

    public static double metersPerSecondToMPH(double mps) {
        return 2.23694 * mps;
    }

    public static double MPHToMetersPerSecond(double mph) {
        return mph/2.23694;
    }
}
