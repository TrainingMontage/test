package TrackModel;

import java.io.*;

public interface TrackModelInterface {

    /**
     * returns a StaticBlock containing static information about that block
     *
     * @param      block_name  The block name
     */
    // public StaticBlockInterface getStaticBlock(String block_name);
    

    /**
     * Initialized the track model.
     */
    public void init();

    /**
     * Create a model of the track based on the contents of the file.
     *
     * @param      filename  The file
     *
     * @return     True if successful, false otherwise
     */
    public boolean importTrack(String filename);

    /**
     * Export the track based on the current model
     *
     * @param      filename  The file
     *
     * @return     True if successful, false otherwise
     */
    public boolean exportTrack(String filename);
}