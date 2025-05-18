package domain;

/**
 * Represents a user in the system with attributes such as name, email, role ID, and password.
 * This class provides getter and setter methods to access and modify user properties.
 */
public class User {
    private String name;
    private String email;
    private String roleId;
    private String password;

    /**
     * Constructs a new User instance with the specified name, email, role ID, and password.
     *
     * @param name      The name of the user. Cannot be null or empty.
     * @param email     The email address of the user. Must be in a valid email format.
     * @param roleId    The role ID associated with the user. Cannot be null or empty.
     * @param password  The user's password. Cannot be null or empty.
     */
    public User(String name, String email, String roleId, String password) {
        this.name = name;
        this.email = email;
        this.roleId = roleId;
        this.password = password;
    }

    /**
     * Retrieves the role ID associated with the user.
     *
     * @return  The role ID of the user as a String.
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID associated with the user.
     *
     * @param roleId    The role ID to be assigned to the user. Cannot be null or empty.
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * Retrieves the name associated with the current instance.
     *
     * @return  The name as a String.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the user.
     *
     * @param name  The name to be assigned to the user. Cannot be null or empty.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password of the user as a String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password  The password to be assigned to the user. Cannot be null or empty.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the email address associated with the user.
     *
     * @return The email address of the user as a String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for the user.
     *
     * @param email  The email address to be assigned to the user. Must be in a valid email format.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
