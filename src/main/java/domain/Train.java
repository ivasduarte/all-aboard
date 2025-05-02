package domain;


public class Train {
    private String name;
    private int id;
    private Date dateOfPurchase;

    public Train(String name, int id, Date dateOfPurchase) {
        this.name = name;
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
    }

    // getters
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    // validations
}
