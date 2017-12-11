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
    protected boolean noNext = false;
//    protected double distanceTraveled;

    
    public MapTracker(Train theTrain) {
        currentBlock = null;
        lastBlock = null;
        nextBlock = null;
        theTrack = TrackModel.getTrackModel().getStaticTrack();
        this.theTrain = theTrain;
    }
    
    public MapTracker(Train theTrain, int blockID) {
        lastBlock = null;
        theTrack = TrackModel.getTrackModel().getStaticTrack();
        currentBlock = theTrack.getStaticBlock(blockID);
        getNextBlock();
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
     * @param distanceTraveled is the distance along the block the train has already traveled
     * @return the distance to the end of authority
     */
    protected double distToAuthEnd(double distanceTraveled) { 
        // I need the length of the block I'm on, my estimated distance traveled on
        // that block, whether or not I have authority for the next block, and the
        // distance of that block.
        double distToStop = currentBlock.getLength();
        // Add the distance of the next block if we have authority
        System.err.println("MAPTRACKER: AUTHORITY FOR BLOCK " + nextBlock.getId() + " IS " + theTrain.getAuthority());
        if(theTrain.getAuthority() && !noNext)
        {
//            distToStop += theTrack.getStaticBlock(currentBlock.getNextId()).getLength();
            distToStop += nextBlock.getLength();
        }
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
            lastBlock = currentBlock;
            currentBlock = nextBlock;
            System.out.println("block change detected");
            return true;
        }
        return false;
    }
    
    protected void getNextBlock() {
        if(theTrack == null)
            System.out.println("Null track");
        if(currentBlock == null)
            System.out.println("Null current block");
        else
            System.out.println("Current Block: " + currentBlock.getId());
//        try {
            if(startblock)
            {
                try{
                nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
                } catch(NullPointerException e) {
                    System.out.println("Trying to get previous");
                    nextBlock = theTrack.getStaticBlock(currentBlock.getPreviousId());
                }
            }
            else if (currentBlock.getNextId() != lastBlock.getId())
            {
                nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
            }
            else if (currentBlock.getNextId() != lastBlock.getId())
            {
                nextBlock = theTrack.getStaticBlock(currentBlock.getPreviousId());
            }
//        } catch (NullPointerException e) {
//            System.err.println("No next block found");
//            noNext = true;
//        }
    }
    
    protected String getStation(int stationID) {
        return theTrack.getStaticBlock(stationID).getStation();
    }
    
    protected void doSwitchBlock(int switchID) {
//        nextBlock = theTrack.getStaticSwitch(switchID).getActiveLeaf();
        currentBlock = theTrack.getStaticBlock(switchID);
        getNextBlock();
        onSwitch = true;
    }
    
}
