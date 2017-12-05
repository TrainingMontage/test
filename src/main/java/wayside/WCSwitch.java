
package wayside;

public class WCSwitch {
    public final int id;
    public final int root;
    public final int def;
    public final int active;

    public WCSwitch(int id, int root, int def, int active) {
        this.id = id;
        this.root = root;
        this.def = def;
        this.active = active;
    }

    public boolean equals(Object other) {
        if (other instanceof WCSwitch) {
            WCSwitch that = (WCSwitch) other;
            return that.id == this.id &&
                that.root == this.root &&
                that.def == this.def &&
                that.active == this.active;
        }
        return false;
    }
}