package TrackModel;

import java.util.*;

public interface StaticBlockInterface {

    /**
     * Gets the speed limit in meters per second.
     *
     * @return     The speed limit.
     */
    public int getSpeedLimit();

    /**
     * Gets the length in meters.
     *
     * @return     The length.
     */
    public int getLength();

    /**
     * Gets the grade in percentage. (ex: .5%)
     *
     * @return     The grade.
     */
    public double getGrade();

    /**
     * Gets the elevation in meters.
     *
     * @return     The elevation.
     */
    public int getElevation();

    /**
     * Gets the next blocks.
     *
     * @return     Id's of the next blocks (as Strings).
     */
    public List<String> getNextBlocks();

    /**
     * Determines if underground.
     *
     * @return     True if underground, False otherwise.
     */
    public boolean isUnderground();

    /**
     * Determines if bidirectional.
     *
     * @return     True if bidirectional, False otherwise.
     */
    public boolean isBidirectional();

    /**
     * Determines if this block has a track heater.
     * 
     * Note: This does not return the status (enabled/disabled), just if heater infrastructure is present.
     *
     * @return     True if has track heater, False otherwise.
     */
    public boolean hasTrackHeater();

    /**
     * Determines if this block has  arail road crossing.
     *
     * @return     True if it has rail road crossing, False otherwise.
     */
    public boolean hasRailRoadCrossing();

    /**
     * Determines if this block has station.
     *
     * @return     True if has station, False otherwise.
     */
    public boolean hasStation();

    /**
     * Gets the infrastructure (as a raw string).
     *
     * @return     The infrastructure.
     */
    public String getInfrastructure();
}