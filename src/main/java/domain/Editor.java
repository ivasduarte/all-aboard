package domain;

public class Editor {
    private String name;
    private String ID;
    private String password;

    public Editor(String name, String ID, String password) {
        this.name = name;
        this.ID = ID;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getID() {
        return ID;
    }
}
