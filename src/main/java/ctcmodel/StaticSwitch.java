package CTCModel;

public class StaticSwitch {

	private StaticBlock root;
	private StaticBlock inactiveLeaf;
	private StaticBlock activeLeaf;
    private int id;
	/**
	 * Constructs the Static Switch. Sets all attributes to null by default.
	 */
	protected StaticSwitch() {
		this.root = null;
		this.inactiveLeaf = null;
		this.activeLeaf = null;
	}

    public int getId(){
        return this.id;
    }
	/**
	 * Gets the root of the switch.
	 *
	 * @return     The root.
	 */
	public StaticBlock getRoot() {
		return root;
	}

	/**
	 * Sets the root of the switch.
	 *
	 * @param      root  The root
	 *
	 * @return     the new root
	 */
	protected StaticBlock setRoot(StaticBlock root) {
		this.root = root;
		return this.root;
	}

	/**
	 * Gets the default leaf. This is the leaf the switch connects to when not
	 * active.
	 *
	 * @return     The default leaf.
	 */
	public StaticBlock getInactiveLeaf() {
		return inactiveLeaf;
	}

	/**
	 * Sets the default leaf.
	 *
	 * @param      inactiveLeaf  The default leaf
	 *
	 * @return     returns the new default leaf value
	 */
	protected StaticBlock setInactiveLeaf(StaticBlock inactiveLeaf) {
		this.inactiveLeaf = inactiveLeaf;
		return this.inactiveLeaf;
	}

	/**
	 * Gets the active leaf. This is the leaf the switch connects to when
	 * activated.
	 *
	 * @return     The active leaf.
	 */
	public StaticBlock getActiveLeaf() {
		return activeLeaf;
	}

	/**
	 * Sets the active leaf.
	 *
	 * @param      activeLeaf  The active leaf
	 *
	 * @return     the new value of the active leaf
	 */
	protected StaticBlock setActiveLeaf(StaticBlock activeLeaf) {
		this.activeLeaf = activeLeaf;
		return this.activeLeaf;
	}

}