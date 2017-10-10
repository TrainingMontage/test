package trackmodel;

class StaticBlock {
    private int id;
    private String region;
    private double grade;
    private double elevation;
    private double length;
    private String station;

    protected StaticBlock() {}

    /**
     * Sets the identifier.
     *
     * @param      id    The identifier
     *
     * @return     the new id
     */
    protected int setId(int id) {
        this.id = id;
        return this.id;
    }

    /**
     * Gets the identifier.
     *
     * @return     The identifier.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the region.
     *
     * @param      region  The region
     *
     * @return     the new region
     */
    protected String setRegion(String region) {
        this.region = region;
        return this.region;
    }

    /**
     * Gets the region.
     *
     * @return     The region.
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Sets the grade.
     *
     * @param      grade  The grade
     *
     * @return     the new grade
     */
    protected double setGrade(double grade) {
        this.grade = grade;
        return this.grade;
    }

    /**
     * Gets the grade.
     *
     * @return     The grade.
     */
    public double getGrade() {
        return this.grade;
    }

    /**
     * Sets the elevation.
     *
     * @param      elevation  The elevation
     *
     * @return     the new elevation
     */
    protected double setElevation(double elevation) {
        this.elevation = elevation;
        return this.elevation;
    }

    /**
     * Gets the elevation.
     *
     * @return     The elevation.
     */
    public double getElevation() {
        return this.elevation;
    }

    /**
     * Sets the length.
     *
     * @param      length  The length
     *
     * @return     the new length
     */
    protected double setLength(double length) {
        this.length = length;
        return this.length;
    }

    /**
     * Gets the length.
     *
     * @return     The length.
     */
    public double getLength() {
        return this.length;
    }

    /**
     * Sets the station.
     *
     * @param      station  The station
     *
     * @return     the new station
     */
    protected String setStation(String station) {
        this.station = station;
        return this.station;
    }

    /**
     * Gets the station.
     *
     * @return     The station.
     */
    public String getStation() {
        return this.station;
    }
}