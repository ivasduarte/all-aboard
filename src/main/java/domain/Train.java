package domain;


public class Train {
    private String name;
    private String id;
    // add dateOfPurchase with class Date

    public Train(String name, String id) {   // add dateOfPurchase
        this.name = name;
        this.id = id;
    }

    // getters
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    // add getDateOfPurchase()

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    // add setDateOfPurchase()

    // validations
}
