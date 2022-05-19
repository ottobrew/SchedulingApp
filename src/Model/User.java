package Model;

/**
 * User class
 */
public class User {
    private int id;
    private String userName;
    private String password;

/**
 * Constructor for User objects
 * @param id
 * @param userName
 * @param password
 */

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return Integer.toString(getId());
    }

}
