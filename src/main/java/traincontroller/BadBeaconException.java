/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traincontroller;

/**
 *
 * @author Didge
 */
public class BadBeaconException extends Exception {

    /**
     * Creates a new instance of <code>BadBeaconException</code> without detail
     * message.
     */
    public BadBeaconException() {
    }

    /**
     * Constructs an instance of <code>BadBeaconException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BadBeaconException(String msg) {
        super(msg);
    }
}
