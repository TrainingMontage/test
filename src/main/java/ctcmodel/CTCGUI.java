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
 * @author Mitchell Moran
 */



package CTCModel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;
import shared.Convert;
import shared.Environment;
import wayside.WaysideController;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.spriteManager.Sprite;

public class CTCGUI implements ViewerListener{
    // dummy modules that the GUI needs to interact with
    //private static TrackModel trackModel;
    //private static TrainModel trainModel;
    // private train list
    //private static boolean dataValid;
    //private static int dataTrainID;
    //private static int dataBlockID;
    //private static int dataSpeed;
    //private static String dataAuthority;
    //private static int dataOrigin;
    //private static int dataDestination;
    //private static JLabel trainLabel;
    // private env temperature
    private static CTCGUI myself = null;
    private static double temperature;
    // constants for initializing GUI components
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    final static int TextAreaWidth = 80;
    final static int TextAreaHeight = 16;
    // menu handle (needs to be added in createAndShowGUI)
    private static JMenuBar menu;
    // GUI button handles
    private static JButton manualButton;
    private static JButton autoButton;
    private static JButton newTrainButton;
    private static JButton launchTrainButton;
    // global textArea handles
    private static JTextArea temperatureText = null;
    // train textArea handles
    private static JTextArea trainIDText;
    private static JTextArea trainBlockText;
    private static JTextArea trainSpeedText;
    private static JTextArea trainAuthorityText;
    //private static JTextArea trainOriginText;
    private static JTextArea trainDestinationText;
    // track textArea handles
    private static JTextArea trackIDText;
    private static JTextArea trackOccupiedText;
    private static JTextArea trackSpeedText;
    private static JTextArea trackLengthText;
    private static JTextArea trackGradeText;
    private static JTextArea trackElevationText;
    private static JTextArea trackPassableText;
    private static JTextArea trackHeaterText;
    private static JTextArea trackUndergroundText;
    private static JTextArea trackLightText;
    private static JTextArea trackSwitchText;
    private static JTextArea trackStationText;
    private static JTextArea trackPassengersText;
    private static JTextArea trackCrossingText;
    // line combobox
    private static JComboBox<String> lineComboBox;
    // textArea and button for select
    private static JTextArea selectBlockText;
    private static JButton selectBlockButton;
    
    private static Graph graph;
    //private static ViewPanel graphView;
    private static Viewer viewer;
    private static ViewerPipe fromViewer;
    private static SpriteManager sm;
    private static final String styleSheet =
        "node {" +
        "   size: 1px;" +
        "	fill-color: black;" +
        "   text-offset: 15, 0;" +
        "}" +
        "node.yard {" +
        "   text-color: red;" +
        "   text-background-mode: plain;" +
        "   size: 4px;" +
        "	fill-color: red;" +
        "   text-size: 12;" +
        "   text-offset: -20, -7;" +
        "}" +
        "edge {" +
        "	fill-color: darkgreen;" +
        "   text-offset: 0, 8;" +
        "   text-alignment: along;" +
        "   text-background-mode: plain;" +
        "   arrow-size: 4, 4;" +
        //"   size: 2px;" +
        "}" +
        "node:clicked {" +
        "   fill-color: red;" +
        "}" +
        //"edge:clicked {" +
        //"   fill-color: red;" +
        //"}" +
        "node:selected {" +
        "   fill-color: red;" +
        "}" +
        //"sprite#occupied {" +
        //"   fill-color: red;" +
        //"   shape: flow;" +
        //"   size: 30px;" +
        //"}" +
        //"sprite#unoccupied {" +
        //"   fill-color: red;" +
        //"   shape: flow;" +
        //"   size: 30px;" +
        //"}" +
        //"edge:selected {" +
        //"   fill-color: red;" +
        //"}" +
        "edge.broken {" +
        "   fill-color: purple;" +
        "}" +
        "edge.occupied {" +
        "	fill-color: green;" +
        //"   text-color: green;" +
        "   text-size: 12;" +
        "}";



    //has the user pushed the new train button but not yet launched it
    private static boolean isNewTrain;
    
    //CTCGUI(TrackModel atrackModel, TrainModel atrainModel){
    CTCGUI(){
        //trackModel = atrackModel;
        //trainModel = atrainModel;
    }
    
    public static void init(){
        if(myself == null){
            myself = new CTCGUI();
        }
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("Map");//allow directed graphs
        //sm = new SpriteManager(graph);
        //Sprite occ = sm.addSprite("occupied");
        //Sprite unocc = sm.addSprite("unoccupied");
        graph.addAttribute("ui.stylesheet",styleSheet);
        
        
        
        Node nodeYard = graph.addNode("Yard");
        nodeYard.addAttribute("ui.label", "YARD");
        nodeYard.addAttribute("ui.class", "yard");
        //read in from track
        StaticTrack t = TrackModel.getTrackModel().getStaticTrack();
        ArrayList<Integer> bIds = TrackModel.getTrackModel().getBlockIds();
        Iterator itr = bIds.iterator();
        //add all the nodes first, it makes adding edges easier
        while(itr.hasNext()){
            int blockId = (Integer)itr.next();
            if(blockId == 151 || blockId == 152){
                //special exception for blocks connected to the yard
                continue;
            }
            Node n1,n2;
            //System.out.println(""+blockId);
            StaticSwitch ss = t.getStaticBlock(blockId).getStaticSwitch();
            if(ss != null){
                //special exception for switches
                n1 = graph.getNode(ss.getRoot().getId()+" "+ss.getDefaultLeaf().getId()+" "+ss.getActiveLeaf().getId());
                if(n1 == null){
                    n1 = graph.addNode(ss.getRoot().getId()+" "+ss.getDefaultLeaf().getId()+" "+ss.getActiveLeaf().getId());
                    //n1.addAttribute("ui.label", ss.getRoot().getId()+" "+ss.getDefaultLeaf().getId()+" "+ss.getActiveLeaf().getId());
                }//else it already exists; do nothing
            }else{
                int nextId = t.getStaticBlock(blockId).getNextId();
                int prevId = t.getStaticBlock(blockId).getPreviousId();
                String nextStr,prevStr;
                if(nextId < blockId){
                    nextStr = nextId+" "+blockId;
                }else{
                    nextStr = blockId+" "+nextId;
                }
                n1 = graph.getNode(nextStr);
                if(prevId < blockId){
                    prevStr = prevId+" "+blockId;
                }else{
                    prevStr = blockId+" "+prevId;
                }
                n2 = graph.getNode(prevStr);
                if(n1 == null){
                    n1 = graph.addNode(nextStr);
                    //n1.addAttribute("ui.label", nextStr);
                }
                if(n2 == null){
                    n2 = graph.addNode(prevStr);
                    //n2.addAttribute("ui.label", prevStr);
                }
            }
        }
        //now all nodes are added, just connect with edges (AKA blocks of track)
        itr = bIds.iterator();
        while(itr.hasNext()){
            int blockId = (Integer)itr.next();
            if(blockId == 151 || blockId == 152){
                //special exception for blocks connected to the yard
                continue;
            }
            Node n1,n2;
            Edge e;
            StaticBlock sb = t.getStaticBlock(blockId);
            StaticSwitch ss = sb.getStaticSwitch();
            if(ss != null){
                //special exception for switches
                n1 = graph.getNode(ss.getRoot().getId()+" "+ss.getDefaultLeaf().getId()+" "+ss.getActiveLeaf().getId());
                int nextId = sb.getNextId();
                int prevId = sb.getPreviousId();
                boolean nextDirection = true;
                String nextStr,prevStr;
                if(nextId < blockId){
                    nextStr = nextId+" "+blockId;
                }else{
                    nextStr = blockId+" "+nextId;
                }
                n2 = graph.getNode(nextStr);
                if(n2 == null){
                    //oops, next is pointing into the switch; prev is pointing away from the switch
                    nextDirection = false;
                    if(prevId < blockId){
                        prevStr = prevId+" "+blockId;
                    }else{
                        prevStr = blockId+" "+prevId;
                    }
                    n2 = graph.getNode(prevStr);
                }
                if(nextDirection && sb.isBidirectional()){
                    e = graph.addEdge(""+blockId, n1, n2, false);
                }else if(nextDirection && !sb.isBidirectional()){
                    e = graph.addEdge(""+blockId, n1, n2, true);
                }else if(!nextDirection && sb.isBidirectional()){
                    e = graph.addEdge(""+blockId, n2, n1, false);
                }else{
                    e = graph.addEdge(""+blockId, n2, n1, true);
                }
                //add switch specific edge properties
                if(blockId == ss.getRoot().getId()){
                    e.addAttribute("track.isSwitch", new Boolean(true));
                    e.addAttribute("track.switch", new Boolean(false));
                }else{
                    e.addAttribute("track.isSwitch", new Boolean(false));
                }
            }else{
                //ordinary piece of track with 2 neighbors
                int nextId = sb.getNextId();
                int prevId = sb.getPreviousId();
                String nextStr,prevStr;
                if(nextId < blockId){
                    nextStr = nextId+" "+blockId;
                }else{
                    nextStr = blockId+" "+nextId;
                }
                n1 = graph.getNode(nextStr);
                if(prevId < blockId){
                    prevStr = prevId+" "+blockId;
                }else{
                    prevStr = blockId+" "+prevId;
                }
                n2 = graph.getNode(prevStr);
                if(sb.isBidirectional()){
                    e = graph.addEdge(""+blockId, n2, n1, false);
                }else{
                    e = graph.addEdge(""+blockId, n2, n1, true);
                }
                //add non switch track properties
                e.addAttribute("track.isSwitch", new Boolean(false));
            }
            //add edge properties
            //e.addAttribute("ui.label",e.getId()+"");
            e.addAttribute("ui.selected", new Boolean(true));
            e.addAttribute("ui.clicked", new Boolean(true));
            e.addAttribute("layout.weight", new Double(sb.getLength()));
            e.addAttribute("track.time", new Double(sb.getLength()/sb.getSpeedLimit()));
            e.addAttribute("track.occupied", new Boolean(false));
            if(sb.isCrossing()){
                e.addAttribute("track.isCrossing", new Boolean(true));
                e.addAttribute("track.crossing", new Boolean(false));
            }else{
                e.addAttribute("track.isCrossing", new Boolean(false));
            }
            //unocc.attachToEdge(""+blockId);
        }
        //manually add the blocks connected to the yard
        //green line: 151 in, 152 out
        StaticBlock sb2 = t.getStaticBlock(151);
        StaticSwitch ss2 = sb2.getStaticSwitch();
        Node nodePrev = graph.getNode(ss2.getRoot().getId()+" "+ss2.getDefaultLeaf().getId()+" "+ss2.getActiveLeaf().getId());
        Edge greenIn = graph.addEdge("151", nodePrev, nodeYard, false);
        greenIn.addAttribute("layout.weight", new Double(t.getStaticBlock(151).getLength()));
        greenIn.addAttribute("track.time", new Double(t.getStaticBlock(151).getLength()/t.getStaticBlock(151).getSpeedLimit()));
        greenIn.addAttribute("track.occupied", new Boolean(false));
        greenIn.addAttribute("track.isCrossing", new Boolean(false));
        greenIn.addAttribute("track.isSwitch", new Boolean(false));
        //greenIn.addAttribute("ui.label",greenIn.getId()+"");
        sb2 = t.getStaticBlock(152);
        ss2 = sb2.getStaticSwitch();
        Node nodeNext = graph.getNode(ss2.getRoot().getId()+" "+ss2.getDefaultLeaf().getId()+" "+ss2.getActiveLeaf().getId());
        Edge greenOut = graph.addEdge("152", nodeYard, nodeNext, false);
        greenOut.addAttribute("layout.weight", new Double(t.getStaticBlock(152).getLength()));
        greenOut.addAttribute("track.time", new Double(t.getStaticBlock(152).getLength()/t.getStaticBlock(152).getSpeedLimit()));
        greenOut.addAttribute("track.occupied", new Boolean(false));
        greenOut.addAttribute("track.isCrossing", new Boolean(false));
        greenOut.addAttribute("track.isSwitch", new Boolean(false));
        //greenOut.addAttribute("ui.label",greenOut.getId()+"");
        
        //hardcode some positions to make the graph look better
        //nodeYard.setAttribute("xyz", 9, 4, 0);
        //graph.getNode("14 15").setAttribute("xyz", 7, 2, 0);
        //graph.getNode("78 79").setAttribute("xyz", 3, 1, 0);
        //graph.getNode("84 85").setAttribute("xyz", 0, 0, 0);
        
        ArrayList<Node> toBePos = new ArrayList<Node>();
        ArrayList<Node> donePos = new ArrayList<Node>();
        nodeYard.setAttribute("xyz", 0.0, 0.0, 0.0);
        donePos.add(nodeYard);
        Iterator<Edge> edgeIter = nodeYard.getEachEdge().iterator();
        
        while(edgeIter.hasNext()){
            Edge toAdd = (Edge) edgeIter.next();
            if(!donePos.contains(toAdd.getNode0()) && !toBePos.contains(toAdd.getNode0())){
                toBePos.add(toAdd.getNode0());
            }
            if(!donePos.contains(toAdd.getNode1()) && !toBePos.contains(toAdd.getNode1())){
                toBePos.add(toAdd.getNode1());
            }
        }//only add the nodes connected to the yard for now. this ensures that we start building off of the yard node
        
        while(toBePos.size() != 0){
            Node nn = toBePos.get(0);
            Iterator neighIter = nn.getNeighborNodeIterator();
            int numPlacedNeigh = 0;
            double avgx = 0;
            double avgy = 0;
            Node neigh;
            boolean yardCpy = false;
            Node lastNode = null;
            while(neighIter.hasNext()){
                neigh = (Node)neighIter.next();
                if(neigh.equals(nodeYard)){
                    yardCpy = true;
                }
                if(donePos.contains(neigh)){
                    lastNode = neigh;
                    numPlacedNeigh++;
                    Object[] pos = neigh.getAttribute("xyz");
                    avgx += ((Double) pos[0]);
                    avgy += ((Double) pos[1]);
                    //could do a weighted avg b/t edges
                }
            }
            avgx = avgx/numPlacedNeigh;
            avgy = avgy/numPlacedNeigh;
            System.out.println("numPlacedNeigh: "+numPlacedNeigh);
            System.out.println("yardCpy: "+yardCpy);
            if(numPlacedNeigh == 3 || (numPlacedNeigh == 2 && nn.getDegree() != 3)){
                //if 2 or 3 neighbors are placed
                nn.setAttribute("xyz", avgx, avgy, 0.0);
                toBePos.remove(nn);
                donePos.add(nn);
            }else{
                //if 1 neighbor is placed
                if(yardCpy){//this placed neighbor is the yard
                    //place up or down
                    if(donePos.size() == 1){
                        //the yard is the only node placed
                        avgx = 1.0;
                        avgy = 1.0;
                    }else{
                        avgx = -1.0;
                        avgy = -1.0;
                    }
                    nn.setAttribute("xyz", avgx, avgy, 0.0);
                    toBePos.remove(nn);
                    donePos.add(nn);
                }else{
                    //not next to the yard
                    if(lastNode.getDegree() == 2){
                        //previously placed node is not a switch, follow the last angle
                        Node prevPrevNode = null;
                        Iterator prevNeighIter = lastNode.getNeighborNodeIterator();
                        while(prevNeighIter.hasNext()){
                            prevPrevNode = (Node) prevNeighIter.next();
                            if(!prevPrevNode.equals(nn)){
                                break;//we found the last 2 nodes, now we can find the angle b/t them
                            }
                        }
                        Object[] pos2 = prevPrevNode.getAttribute("xyz");
                        avgx += avgx - ((Double) pos2[0]);
                        avgy += avgy - ((Double) pos2[1]);
                        nn.setAttribute("xyz", avgx, avgy, 0.0);
                        toBePos.remove(nn);
                        donePos.add(nn);
                    }else{
                        //previously placed node is a switch, make angles even
                        Node prevPrevNode1 = null;
                        Node prevPrevNode2 = null;
                        Iterator prevNeighIter = lastNode.getNeighborNodeIterator();
                        while(prevNeighIter.hasNext()){
                            prevPrevNode1 = (Node) prevNeighIter.next();
                            if(!prevPrevNode1.equals(nn)){
                                if(prevPrevNode2 == null){
                                    prevPrevNode2 = prevPrevNode1;
                                }else{
                                    break;//we found the last 2 nodes, now we can find the angle b/t them
                                }
                            }
                        }
                        //if other 2 from switch are placed
                        if(donePos.contains(prevPrevNode1) && donePos.contains(prevPrevNode2)){
                            //this is messy
                            System.out.println("my mess");
                            System.out.println(lastNode.getId());
                            System.out.println(prevPrevNode1.getId());
                            System.out.println(prevPrevNode2.getId());
                            Object[] pos1 = lastNode.getAttribute("xyz");
                            Object[] pos2 = prevPrevNode1.getAttribute("xyz");
                            Object[] pos3 = prevPrevNode2.getAttribute("xyz");
                            double x0 = (Double) pos1[0];
                            double y0 = (Double) pos1[1];
                            double x1 = (((Double) pos2[0]) + ((Double) pos3[0]))/2;
                            double y1 = (((Double) pos2[1]) + ((Double) pos3[1]))/2;
                            double m = (y1-y0)/(x1-x0);
                            avgx = Math.sqrt(1/(1+m*m))+x0;
                            double negavgx = -avgx;
                            avgy = y0+m*(avgx-x0);
                            double negavgy = y0+m*(negavgx-x0);
                            if((avgx-x1)*(avgx-x1)+(avgy-y1)*(avgy-y1) > (negavgx-x1)*(negavgx-x1)+(negavgy-y1)*(negavgy-y1)){
                                //oops we put the new line on the wrong side
                                System.out.println("negative stuff");
                                avgx = negavgx;
                                avgy = negavgy;
                            }
                        }else{
                            //only one of the switch nodes is placed
                            if(donePos.contains(prevPrevNode2)){
                                prevPrevNode1 = prevPrevNode2;
                            }//now the already placed node is in prevPrevNode1. we no longer care about prevPrevNode2
                            //find the point where prevPrevNode1 would be rotated 120 degrees around the switch center
                            Object[] pos1 = lastNode.getAttribute("xyz");
                            Object[] pos2 = prevPrevNode1.getAttribute("xyz");
                            double x0 = (Double) pos1[0];//pivot point
                            double y0 = (Double) pos1[1];
                            double x1 = (Double) pos2[0];//point to rotate
                            double y1 = (Double) pos2[1];
                            double s = Math.sin(2*Math.PI/3);//-120 degrees in radians
                            double c = Math.cos(2*Math.PI/3);
                            
                            //translate to origin
                            x1 -= x0;
                            y1 -= y0;
                            
                            //do rotation
                            avgx = x1*c - y1*s;
                            avgy = x1*s + y1*c;
                            
                            //translate back
                            avgx += x0;
                            avgy += y0;
                        }
                        
                        
                        nn.setAttribute("xyz", avgx, avgy, 0.0);
                        toBePos.remove(nn);
                        donePos.add(nn);
                    }
                }
            }
            System.out.println("added node "+nn.getId()+" at pos x="+avgx+" y="+avgy);
            //find its edges and add those
            edgeIter = nn.getEachEdge().iterator();
            while(edgeIter.hasNext()){
                Edge toAdd = (Edge) edgeIter.next();
                if(!donePos.contains(toAdd.getNode0()) && !toBePos.contains(toAdd.getNode0())){
                    toBePos.add(toAdd.getNode0());
                }
                if(!donePos.contains(toAdd.getNode1()) && !toBePos.contains(toAdd.getNode1())){
                    toBePos.add(toAdd.getNode1());
                }
            }
        }
        
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(myself);
        //fromViewer.addSink(graph);
    }
    
    public static void addComponentsToPane(Container pane){
        if(RIGHT_TO_LEFT){
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        isNewTrain = false;
        //dataValid = false;
        temperature = 70.0;
        
        JButton button;
        JLabel label;
        JTextArea textArea;
        JCheckBox checkBox;
        JComboBox comboBox;
        
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if(shouldFill){
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        c.weightx = 0.5;
        c.weighty = 0.5;
        
        //menu declarations
        menu = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        //JMenuItem menuCreateTrain = new JMenuItem("Create Train");
        JMenuItem menuUploadSchedule = new JMenuItem("Upload Schedule");
        //TODO: more menu items?
        
        //sub-panel declarations
        JPanel panelCTCInfo = new JPanel();
        JPanel panelTrainInfo = new JPanel();
        JPanel panelTrackInfo = new JPanel();
        JPanel panelSchedule = new JPanel();
        JPanel panelTrackGraph = new JPanel();
        JPanel panelSelect = new JPanel();
        
        //new Dimension(width,height)
        panelCTCInfo.setPreferredSize(new Dimension(300,62));
        panelTrainInfo.setPreferredSize(new Dimension(300,175));
        panelTrackInfo.setPreferredSize(new Dimension(300,310));
        panelSchedule.setPreferredSize(new Dimension(600,300));
        panelTrackGraph.setPreferredSize(new Dimension(700,643));
        panelSelect.setPreferredSize(new Dimension(300,50));
        
        // -------------------- menu bar --------------------
        //menuCreateTrain.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent ae) {
        //        
        //    }
        //});
        //menuFile.add(menuCreateTrain);
        menuFile.add(menuUploadSchedule);
        menu.add(menuFile);
        //added to frame in createAndShowGUI
        
        // -------------------- main panel --------------------
        label = new JLabel("CTC GUI");
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        pane.add(label,c);
        
        label = new JLabel("8:00 AM");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(label,c);
        
        manualButton = new JButton("Manual");
        manualButton.setEnabled(false);
        manualButton.setBackground(Color.GREEN);
        //manualButton.setOpaque(true);
        manualButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                manualButton.setEnabled(false);
                autoButton.setEnabled(true);
                manualButton.setBackground(Color.GREEN);
                //manualButton.setOpaque(true);
                autoButton.setBackground(new JButton().getBackground());
                newTrainButton.setEnabled(true);
                trainSpeedText.setEnabled(true);
                trainAuthorityText.setEnabled(true);
                trainDestinationText.setEnabled(true);
                launchTrainButton.setEnabled(true);
                if(isNewTrain){
                    //enable the text boxes
                    //trainBlockText.setEnabled(true);
                    //trainSpeedText.setEnabled(true);
                    //trainAuthorityText.setEnabled(true);
                    //trainDestinationText.setEnabled(true);
                    lineComboBox.setEnabled(true);
                    //enable the submit button
                    //launchTrainButton.setText("Launch Train");
                }
                //viewer.disableAutoLayout();
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 2;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(manualButton,c);
        
        autoButton = new JButton("Automatic");
        autoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                autoButton.setEnabled(false);
                manualButton.setEnabled(true);
                autoButton.setBackground(Color.GREEN);
                //autoButton.setOpaque(true);
                manualButton.setBackground(new JButton().getBackground());
                newTrainButton.setEnabled(false);
                trainSpeedText.setEnabled(false);
                trainAuthorityText.setEnabled(false);
                trainDestinationText.setEnabled(false);
                launchTrainButton.setEnabled(false);
                if(isNewTrain){
                    //disable the text boxes
                    //trainBlockText.setEnabled(false);
                    //trainSpeedText.setEnabled(false);
                    //trainAuthorityText.setEnabled(false);
                    //trainDestinationText.setEnabled(false);
                    //disable the submit button
                    lineComboBox.setEnabled(false);
                    //launchTrainButton.setText("Edit Train");
                }
                //viewer.enableAutoLayout();
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 3;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(autoButton,c);
        
        // -------------------- CTC panel --------------------
        panelCTCInfo.setLayout(new GridBagLayout());
        
        
        label = new JLabel("Temperature");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelCTCInfo.add(label,c);
        
        label = new JLabel("Throughput");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(label,c);
        
        temperatureText = new JTextArea("70°F");
        temperatureText.setEnabled(false);
        temperatureText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(temperatureText,c);
        
        textArea = new JTextArea("5000 people");
        textArea.setEnabled(false);
        textArea.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(textArea,c);
        
        panelCTCInfo.setBorder(BorderFactory.createTitledBorder("CTC Info"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        pane.add(panelCTCInfo,c);
        
        // -------------------- Train panel --------------------
        panelTrainInfo.setLayout(new GridBagLayout());
        
        label = new JLabel("Train ID");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Current Block");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Suggested Speed");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Given Authority");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Line");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Destination");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        trainIDText = new JTextArea("");
        trainIDText.setEnabled(false);
        trainIDText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainIDText,c);
        
        trainBlockText = new JTextArea("");
        trainBlockText.setEnabled(false);
        trainBlockText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainBlockText,c);
        
        trainSpeedText = new JTextArea("");
        //trainSpeedText.setEnabled(false);
        trainSpeedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainSpeedText,c);
        
        trainAuthorityText = new JTextArea("");
        //trainAuthorityText.setEnabled(false);
        trainAuthorityText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainAuthorityText,c);
        
        lineComboBox = new JComboBox<>();
        lineComboBox.addItem("Green");
        lineComboBox.addItem("Red");
        lineComboBox.setSelectedIndex(0);
        lineComboBox.setEnabled(false);
        lineComboBox.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //trainOriginText = new JTextArea("Shadyside");
        //trainOriginText.setEnabled(false);
        //trainOriginText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(lineComboBox,c);
        
        trainDestinationText = new JTextArea("");
        //trainDestinationText.setEnabled(false);
        trainDestinationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainDestinationText,c);
        
        newTrainButton = new JButton("New Train");
        newTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //clear the text boxes
                trainIDText.setText("");
                trainBlockText.setText("");
                trainSpeedText.setText("");
                trainAuthorityText.setText("");
                //trainOriginText.setText("");
                trainDestinationText.setText("");
                //enable the text boxes
                //trainBlockText.setEnabled(true);
                //trainSpeedText.setEnabled(true);
                //trainAuthorityText.setEnabled(true);
                //trainDestinationText.setEnabled(true);
                lineComboBox.setEnabled(true);
                //enable the submit button
                //launchTrainButton.setEnabled(true);
                launchTrainButton.setText("Launch Train");
                isNewTrain = true;
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(newTrainButton,c);
        
        launchTrainButton = new JButton("Edit Train");
        //launchTrainButton.setEnabled(false);
        launchTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //reset all borders
                Border border = new JTextArea("").getBorder();
                //trainBlockText.setBorder(border);
                trainSpeedText.setBorder(border);
                trainAuthorityText.setBorder(border);
                trainDestinationText.setBorder(border);
                int blockFromLine;
                if(lineComboBox.getSelectedIndex() == 0){
                    //green line
                    blockFromLine = 152;
                }else{
                    //red line
                    blockFromLine = -1;//FIXME: update when there is a red line
                }
                //check text
                int error = CTCModel.checkTrainInputs(blockFromLine,
                                             trainSpeedText.getText(),
                                             trainAuthorityText.getText(),
                                             trainDestinationText.getText());
                switch(error){
                case 0:
                    if(isNewTrain){
                        //adding a new train
                        
                        //input valid
                        if(WaysideController.isOccupied(blockFromLine)){
                            //block is occupied, can't put a train there
                            trainBlockText.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                            break;
                        }
                        //disable text boxes
                        //trainBlockText.setEnabled(false);
                        trainSpeedText.setEnabled(false);
                        trainAuthorityText.setEnabled(false);
                        trainDestinationText.setEnabled(false);
                        lineComboBox.setEnabled(false);
                    
                        //send to createTrain
                        CTCModel.createTrain(blockFromLine,
                                             Convert.MPHToMetersPerSecond(Double.parseDouble(trainSpeedText.getText())),
                                             trainAuthorityText.getText(),
                                             Integer.parseInt(trainDestinationText.getText()));
                                             //don't send trainID get from return value
                        //update own train data
                        //dataValid = true;
                        //dataTrainID = 1;
                        //dataBlockID = Integer.parseInt(trainBlockText.getText());
                        //dataSpeed = Integer.parseInt(trainSpeedText.getText());
                        //dataAuthority = trainAuthorityText.getText();
                        //dataOrigin = dataBlockID;
                        //dataDestination = Integer.parseInt(trainDestinationText.getText());
                        //trainLabel.setText("Train");
                        //disable the submit button
                        trainSpeedText.setEnabled(true);
                        trainAuthorityText.setEnabled(true);
                        trainDestinationText.setEnabled(true);
                        //launchTrainButton.setEnabled(false);
                        launchTrainButton.setText("Edit Train");
                        isNewTrain = false;
                        try{
                            Thread.sleep(500);//sleep to give the track model time to register the new train
                        }catch(InterruptedException ex){
                        }
                        fillTrackInfo(blockFromLine);
                    }else{
                        //editing existing train
                        int tId = -1;
                        try{
                            tId = Integer.parseInt(trainIDText.getText());
                        }catch(NumberFormatException nfe){}
                        
                        CTCTrainData data = CTCModel.getTrainDataTrainId(tId);
                        if(data != null){
                            //update train data
                            CTCModel.editTrain(tId,
                                               Convert.MPHToMetersPerSecond(Double.parseDouble(trainSpeedText.getText())),
                                               trainAuthorityText.getText(),
                                               Integer.parseInt(trainDestinationText.getText()));
                            //update to data.block
                            try{
                                Thread.sleep(500);//sleep to give the track model time to register the new train
                            }catch(InterruptedException ex){
                            }
                            fillTrackInfo(data.getBlockID());
                        }
                    }
                    break;
                case 1:
                    lineComboBox.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 2:
                    trainSpeedText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 3:
                    trainAuthorityText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 4:
                    trainDestinationText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                default:
                    //unknown error; do nothing
                    break;
                }
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(launchTrainButton,c);
        
        panelTrainInfo.setBorder(BorderFactory.createTitledBorder("Train Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        pane.add(panelTrainInfo,c);
        
        // -------------------- Select panel --------------------
        panelSelect.setLayout(new GridBagLayout());
        
        selectBlockText = new JTextArea("");
        selectBlockText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelSelect.add(selectBlockText,c);
        
        
        
        selectBlockButton = new JButton("Select Block");
        selectBlockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //read textArea and check if it is edge id
                String s = selectBlockText.getText();
                Edge e = graph.getEdge(s);
                //if yes call fill
                if(e != null){
                    fillTrackInfo(Integer.parseInt(s));
                }
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelSelect.add(selectBlockButton,c);
        
        
        
        panelSelect.setBorder(BorderFactory.createTitledBorder("Select Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 1;
        pane.add(panelSelect,c);
        
        // -------------------- Track panel --------------------
        panelTrackInfo.setLayout(new GridBagLayout());
        
        label = new JLabel("Block ID");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Occupied State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Speed Limit");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Length");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Grade");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Elevation");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        //label = new JLabel("Block Passable");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        //c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        //panelTrackInfo.add(label,c);
        
        label = new JLabel("Heater State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Underground");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 7;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Light State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 8;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Switch State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 9;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Station Name");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 10;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Waiting Passengers");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 11;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Crossing State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 12;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        trackIDText = new JTextArea("");
        trackIDText.setEnabled(false);
        trackIDText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackIDText,c);
        
        trackOccupiedText = new JTextArea("");
        trackOccupiedText.setEnabled(false);
        trackOccupiedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackOccupiedText,c);
        
        trackSpeedText = new JTextArea("");
        trackSpeedText.setEnabled(false);
        trackSpeedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackSpeedText,c);
        
        trackLengthText = new JTextArea("");
        trackLengthText.setEnabled(false);
        trackLengthText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackLengthText,c);
        
        trackGradeText = new JTextArea("");
        trackGradeText.setEnabled(false);
        trackGradeText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackGradeText,c);
        
        trackElevationText = new JTextArea("");
        trackElevationText.setEnabled(false);
        trackElevationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackElevationText,c);
        
        /*trackPassableText = new JTextArea("Passable");
        trackPassableText.setEnabled(false);
        trackPassableText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackPassableText,c);*/
        
        trackHeaterText = new JTextArea("");
        trackHeaterText.setEnabled(false);
        trackHeaterText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackHeaterText,c);
        
        trackUndergroundText = new JTextArea("");
        trackUndergroundText.setEnabled(false);
        trackUndergroundText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 7;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackUndergroundText,c);
        
        trackLightText = new JTextArea("");
        trackLightText.setEnabled(false);
        trackLightText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 8;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackLightText,c);
        
        trackSwitchText = new JTextArea("");
        trackSwitchText.setEnabled(false);
        trackSwitchText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 9;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackSwitchText,c);
        
        trackStationText = new JTextArea("");
        trackStationText.setEnabled(false);
        trackStationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 10;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackStationText,c);
        
        trackPassengersText = new JTextArea("");
        trackPassengersText.setEnabled(false);
        trackPassengersText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 11;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackPassengersText,c);
        
        trackCrossingText = new JTextArea("");
        trackCrossingText.setEnabled(false);
        trackCrossingText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 12;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackCrossingText,c);
        
        panelTrackInfo.setBorder(BorderFactory.createTitledBorder("Track Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 3;
        pane.add(panelTrackInfo,c);
        
        // -------------------- Schedule panel --------------------
        panelSchedule.setLayout(new GridBagLayout());
        
        panelSchedule.setBorder(BorderFactory.createTitledBorder("Schedule Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.gridheight = 1;
        pane.add(panelSchedule,c);
        
        // -------------------- Map panel --------------------
        panelTrackGraph.setLayout(new GridBagLayout());
        /*
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        trainLabel = new JLabel(" ", SwingConstants.CENTER);
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(trainLabel,c);
        
        label = new JLabel("Yard", SwingConstants.CENTER);
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(0);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(1);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(2);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(3);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 4;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        */
        viewer.disableAutoLayout();
        ViewPanel graphView = viewer.addDefaultView(false);   // false indicates "no JFrame".
        graphView.setPreferredSize(new Dimension(600,600));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(graphView,c);
        
        panelTrackGraph.setBorder(BorderFactory.createTitledBorder("Map Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 5;
        pane.add(panelTrackGraph,c);
    }
    
    static void fillTrackInfo(int blockID){
        //fill static info
        StaticBlock staticBlock = TrackModel.getTrackModel().getStaticBlock(blockID);
        StaticSwitch staticSwitch = staticBlock.getStaticSwitch();
        trackIDText.setText("" + staticBlock.getId());
        trackSpeedText.setText("" + Convert.metersPerSecondToMPH(staticBlock.getSpeedLimit()) + " mph");
        trackLengthText.setText("" + Convert.metersToFeet(staticBlock.getLength()) + " ft");
        trackGradeText.setText("" + staticBlock.getGrade() + "%");
        trackElevationText.setText("" + Convert.metersToFeet(staticBlock.getElevation()) + " ft");
        trackUndergroundText.setText("" + staticBlock.isUnderground());
        //boolean hasLight = staticBlock.hasLight();
        //boolean hasSwitch = staticBlock.hasSwitch();
        boolean hasSwitch = (staticSwitch != null);
        boolean hasRailway = staticBlock.isCrossing();
        boolean hasHeater = staticBlock.hasHeater();
        String stationName = staticBlock.getStation();
        if(!hasSwitch){
            trackSwitchText.setText("No Switch");
            trackLightText.setText("No Lights");
        }
        if(stationName == null || stationName.equals("")){
            trackStationText.setText("No Station");
            trackPassengersText.setText("");
        }else{
            trackStationText.setText(stationName);
            trackPassengersText.setText("0");
        }
        if(!hasRailway){
            trackCrossingText.setText("No Railway Crossing");
        }
        if(!hasHeater){
            trackHeaterText.setText("No Heater");
        }
        
        //fill dynamic info
        if(WaysideController.isOccupied(blockID)){
            trackOccupiedText.setText("Occupied");
        }else{
            trackOccupiedText.setText("Not Occupied");
        }
        //BlockStatus asdf = WaysideController.getStatus(blockID);
        //switch(asdf){
        //case OPERATIONAL:
        //    trackPassableText.setText("Passable");
        //    break;
        //case BROKEN:
        //    trackPassableText.setText("Broken");
        //    break;
        //case IN_REPAIR:
        //    trackPassableText.setText("Maintenance");
        //    break;
        //default:
        //    //unknown status; leave blank
        //    trackPassableText.setText("");
        //    break;
        //}
        if(hasSwitch){
            if(WaysideController.getSignal(blockID)){
                trackLightText.setText("Green");
            }else{
                trackLightText.setText("Red");
            }
        }//TODO should I clear the old value?
        if(hasSwitch){
            //TODO maybe add checks for null values?
            //find the root
            StaticBlock root = staticSwitch.getRoot();
            StaticBlock selectedLeaf;
            //get switch state (dynamic)
            if(WaysideController.getSwitch(root.getId())){
                //in the activated switch state
                selectedLeaf = staticSwitch.getDefaultLeaf();
            }else{
                //in the default switch state
                selectedLeaf = staticSwitch.getActiveLeaf();
            }
            //output rootID -> selectedLeafID
            trackSwitchText.setText(root.getId() + " -> " + selectedLeaf.getId());
        }
        if(hasRailway){
            if(WaysideController.getCrossing(blockID)){
                trackCrossingText.setText("Active");
            }else{
                trackCrossingText.setText("Inactive");
            }
        }
        if(hasHeater){
            if(temperature <= 32){
                trackHeaterText.setText("Heater Active");
            }else{
                trackHeaterText.setText("Heater Inactive");
            }
        }
        
        //if there is a train there, fill train info
        //but first disable the train data entering
        if(isNewTrain){
            //trainSpeedText.setEnabled(false);
            //trainAuthorityText.setEnabled(false);
            //trainDestinationText.setEnabled(false);
            lineComboBox.setEnabled(false);
            //launchTrainButton.setEnabled(false);
            launchTrainButton.setText("Edit Train");
            isNewTrain = false;
        }
        CTCTrainData data = CTCModel.getTrainData(blockID);
        if(data != null){
            trainIDText.setText("" + data.getTrainID());
            trainBlockText.setText("" + data.getBlockID());
            trainSpeedText.setText("" + Convert.metersPerSecondToMPH(data.getSpeed()));
            trainAuthorityText.setText(data.getAuthority());
            if(data.getOrigin() == 152){
                lineComboBox.setSelectedIndex(0);
            }else{
                lineComboBox.setSelectedIndex(1);
            }
            trainDestinationText.setText("" + data.getDestination());
        }else{
            //blank everything
            trainIDText.setText("");
            trainBlockText.setText("");
            trainSpeedText.setText("");
            trainAuthorityText.setText("");
            //trainOriginText.setText("");
            trainDestinationText.setText("");
        }
        
    }
    //these functions have to be public void so the ViewerPipe can find them
    public void viewClosed(String id) {
        ;//loop = false;
    }

    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);
    }

    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }
    public static void handleGraphEvents(){
        fromViewer.pump();
        temperature = Environment.temperature;
        if(temperatureText != null){
            temperatureText.setText(temperature+"°F");
        }
    }
    public static Graph getGraph(){
        return graph;
    }
    public static void createAndShowGUI(){
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace(); 
        }
        UIManager.put("TextArea.inactiveForeground", Color.DARK_GRAY);
        //TODO: figure out why I can't set the background
        //UIManager.put("TextArea.inactiveBackground", Color.LIGHT_GRAY);
        //UIManager.put("TextArea.inactiveForeground", Color.DARK_GRAY);
        
        //Create and set up the window.
        JFrame frame = new JFrame("CTC GUI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        frame.setJMenuBar(menu);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public static void run(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CTCGUI.createAndShowGUI();
            }
        });
    }
}