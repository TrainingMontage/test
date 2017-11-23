/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *     __  ___                 __               /____/
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 * @author Alec Rosenbaum
 */

package trackmodel;

import shared.BlockStatus;

/**
 * StaticBlock is a class, just getters for statically visible stuff about track.
 * Authority and BlockStatus are enums.
 */


public interface TrackModelInterface {
    // For the train model
    public boolean getTrainAuthority(int trainId);
    public double getTrainSpeed(int trainId);
    public int getTrainBeacon(int trainId);
    public int getTrainPassengers(int trainId);
    public boolean isIcyTrack(int trainId);
    public double getGrade(int trainId);

    // For the wayside controller
    public boolean isOccupied(int blockId);
    public BlockStatus getStatus(int blockId);
    public boolean setSignal(int blockId, boolean value);
    public boolean getSignal(int blockId);
    public boolean setSwitch(int blockId, boolean value);
    public boolean getSwitch(int blockId);
    public boolean setCrossingState(int blockId, boolean active); // setCrossing -> setCrossingState
    public boolean getCrossingState(int blockId); // getCrossing -> getCrossingState
    public boolean setAuthority(int blockId, boolean authority);
    public int setSpeed(int blockId, int speed);

    // for everybody
    public StaticSwitch getStaticSwitch(int switchID);
    public StaticBlock getStaticBlock(int blockId);
}