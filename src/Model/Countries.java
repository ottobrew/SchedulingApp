package Model;

/**
 * Countries class
 */
public class Countries {
    private int id;
    private String name;

/**
 * Constructor for Countries objects
  * @param id
 * @param name
 */
    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
