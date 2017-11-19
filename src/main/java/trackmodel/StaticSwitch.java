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

public class StaticSwitch {

	private StaticBlock root;
	private StaticBlock defaultLeaf;
	private StaticBlock activeLeaf;
	private int id;

	/**
	 * Constructs the Static Switch. Sets all attributes to null by default.
	 *
	 * @param      id    The identifier
	 */
	protected StaticSwitch(int id) {
		this.id = id;
		this.root = null;
		this.defaultLeaf = null;
		this.activeLeaf = null;
	}

	public int getId() {
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
	public StaticBlock getDefaultLeaf() {
		return defaultLeaf;
	}

	/**
	 * Sets the default leaf.
	 *
	 * @param      defaultLeaf  The default leaf
	 *
	 * @return     returns the new default leaf value
	 */
	protected StaticBlock setDefaultLeaf(StaticBlock defaultLeaf) {
		this.defaultLeaf = defaultLeaf;
		return this.defaultLeaf;
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

	/**
	 * Check if switch contains a block
	 *
	 * @param      block  The block
	 *
	 * @return     true if block is in switch, false otherwise
	 */
	public boolean contains(StaticBlock block) {
		if (block.equals(this.root) || block.equals(this.defaultLeaf) || block.equals(this.activeLeaf)) {
			return true;
		} else {
			return false;
		}
	}

}