package Model;

/**
 * Contact class
 */
public class Contact {
    private int id;
    private String name;
    private String email;

/**
 * Constructor for Contact objects
 * @param id
 * @param name
 * @param email
 */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return getName();
    }
}
