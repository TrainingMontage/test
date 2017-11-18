package trackmodel;

class StaticBlock {
    private int id;
    private String region;
    private double grade;
    private double elevation;
    private double length;
    private String station;
    private StaticSwitch staticSwitch;
    private int nextId;
    private int previousId;
    private boolean bidirectional;

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

    /**
     * Sets the static switch.
     *
     * @param      staticSwitch  The static switch
     *
     * @return     the new switch connected to this block
     */
    protected StaticSwitch setStaticSwitch(StaticSwitch staticSwitch) {
        this.staticSwitch = staticSwitch;
        return this.staticSwitch;
    }

    /**
     * Gets the static switch connected to this block.
     *
     * @return     The static switch.
     */
    public StaticSwitch getStaticSwitch() {
        return this.staticSwitch;
    }

    /**
     * Custom equals function (based on id's)
     *
     * @param      o     comparison object
     *
     * @return     true for equals, false otherwise
     */
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        return this.getId() == ((StaticBlock) o).getId();
    }

    /**
     * Sets the id of the next block.
     *
     * @param      id    The identifier
     *
     * @return     the new next id
     */
    protected int setNextId(int nextId) {
        this.nextId = nextId;
        return this.nextId;
    }

    /**
     * Gets the id of the next block.
     *
     * @return     The id of the next block.
     */
    public int getNextId() {
        return this.nextId;
    }

    public String toString() {
        return Integer.toString(this.id);
    }

    /**
     * Sets whether the block is bidirectional.
     *
     * @param      bidirectional  if block is bidirectional
     *
     * @return     the new bidirectional value
     */
    protected boolean setBidirectional(boolean bidirectional) {
        this.bidirectional = bidirectional;
        return this.bidirectional;
    }

    /**
     * Gets the bidirectional.
     *
     * @return     The bidirectional.
     */
    public boolean isBidirectional() {
        return this.bidirectional;
    }

    /**
     * Sets the id of the previous block.
     *
     * @param      id    The identifier
     *
     * @return     the new previous id
     */
    protected int setPreviousId(int previousId) {
        this.previousId = previousId;
        return this.previousId;
    }

    /**
     * Gets the id of the previous block.
     *
     * @return     The id of the previous block.
     */
    public int getPreviousId() {
        return this.previousId;
    }
}