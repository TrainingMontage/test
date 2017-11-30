/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traincontroller;

import shared.*;
import trainmodel.Train;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;

/**
 *
 * @author Didge
 */
public class MapTracker {
    
    protected StaticBlock currentBlock;
    protected StaticBlock lastBlock;
    protected StaticBlock nextBlock;
    protected StaticTrack theTrack;
    protected Train theTrain;
    protected boolean startblock = true; // At start, this is the first block you are on.
    protected boolean onSwitch;
//    protected double distanceTraveled;

    
    public MapTracker(Train theTrain) {
        currentBlock = null;
        lastBlock = null;
        nextBlock = null;
        theTrack = TrackModel.getTrackModel().getStaticTrack();
        this.theTrain = theTrain;
    }
    
    public MapTracker(StaticTrack initTrack, Train theTrain) {
        theTrack = initTrack;
        lastBlock = null;
        currentBlock = null;
        this.theTrain = theTrain;
    }
    
    public MapTracker(StaticTrack initTrack, StaticBlock startBlock, Train theTrain) {
        currentBlock = startBlock;
        theTrack = initTrack;
        nextBlock = theTrack.getStaticBlock(startBlock.getNextId());
        this.theTrain = theTrain;
    }
    
    public void setCurrentBlock(StaticBlock startBlock) {
        currentBlock = startBlock;
        if(theTrack != null)
            getNextBlock();
    }
    
    public void setTrack(StaticTrack initTrack) {
        theTrack = initTrack;
        if(currentBlock != null)
            getNextBlock();
    }
    
    /**
     * Computes the remaining distance the train can go before reaching the
     * end of its authority.
     * 
     * @return the distance to the end of authority
     */
    protected double distToAuthEnd(double distanceTraveled) { 
        // I need the length of the block I'm on, my estimated distance traveled on
        // that block, whether or not I have authority for the next block, and the
        // distance of that block.
        double distToStop = currentBlock.getLength();
        // Add the distance of the next block if we have authority
        if(theTrain.getAuthority())
            distToStop += theTrack.getStaticBlock(currentBlock.getNextId()).getLength();
//        updateDistTraveled();
        distToStop -= distanceTraveled;
        return distToStop;
    }
    
    /**
     * Checks to see if we've changed blocks, resets distanceTraveled.
     * 
     * @return returns true if we've changed block, false if not.
     */
    protected boolean blockChange() {
        if(theTrain.blockChange())
        {
            if(startblock)
                startblock = false;
            if(onSwitch)
                onSwitch = false;
            currentBlock = nextBlock;
            return true;
        }
        return false;
    }
    
    protected void getNextBlock() {
        if(startblock)
        {
            nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
        }
        else if (currentBlock.getNextId() != lastBlock.getId())
        {
            nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
        }
        else if (currentBlock.getNextId() != lastBlock.getId())
        {
            nextBlock = theTrack.getStaticBlock(currentBlock.getPreviousId());
        }
    }
    
    protected String getStation(int stationID) {
        return theTrack.getStaticBlock(stationID).getStation();
    }
    
    protected void doSwitchBlock(int switchID) {
        nextBlock = theTrack.getStaticSwitch(switchID).getActiveLeaf();
        onSwitch = true;
    }
    
}
