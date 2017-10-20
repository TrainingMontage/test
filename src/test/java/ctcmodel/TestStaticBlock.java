

public class TestStaticBlock{
    final private int blockID;
    final private double speedLimit;
    final private double length;
    final private double grade;
    final private double elevation;
    final private boolean isUnderground;
    final private boolean hasLight;
    final private boolean hasSwitch;
    final private boolean hasStation;
    final private String stationName;
    final private boolean hasRailway;
    final private boolean hasHeater;
    
    TestStaticBlock(int blockID, double speedLimit, double length, double grade,
                    double elevation, boolean isUnderground, boolean hasLight,
                    boolean hasSwitch, boolean hasStation, String stationName,
                    boolean hasRailway, boolean hasHeater){
        this.blockID = blockID;
        this.speedLimit = speedLimit;
        this.length = length;
        this.grade = grade;
        this.elevation = elevation;
        this.isUnderground = isUnderground;
        this.hasLight = hasLight;
        this.hasSwitch = hasSwitch;
        this.hasStation = hasStation;
        this.stationName = stationName;
        this.hasRailway = hasRailway;
        this.hasHeater = hasHeater;
    }
    
    public int getBlockID(){
        return this.blockID;
    }
    public double getSpeedLimit(){
        return this.speedLimit;
    }
    public double getLength(){
        return this.length;
    }
    public double getGrade(){
        return this.grade;
    }
    public double getElevation(){
        return this.elevation;
    }
    public boolean isUnderground(){
        return this.isUnderground;
    }
    public boolean hasLight(){
        return this.hasLight;
    }
    public boolean hasSwitch(){
        return this.hasSwitch;
    }
    public boolean hasStation(){
        return this.hasStation;
    }
    public String getStationName(){
        return this.stationName;
    }
    public boolean hasRailway(){
        return this.hasRailway;
    }
    public boolean hasHeater(){
        return this.hasHeater;
    }
}