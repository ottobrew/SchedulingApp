package Model;

/**
 * Division class
 */
public class Division {
    private int divId;
    private String name;
    private int countryId;

    /**
     * Constructor for Division objects
     * @param divId
     * @param name
     * @param countryId
     */
    public Division(int divId, String name, int countryId) {
        this.divId = divId;
        this.name = name;
        this.countryId = countryId;
    }

    public int getDivId() {
        return divId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    @Override
    public String toString() {
        return getName();
    }
}

