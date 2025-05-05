package domain;


import java.util.List;

public class Train {
    private String name;
    private int id;
    private Date dateOfPurchase;
    private List<Locomotive> selectedLocomotives;
    private List<Carriage> selectedCarriages;

    public Train(String name, int id, Date dateOfPurchase, List<Locomotive> selectedLocomotives, List<Carriage> selectedCarriages) {
        this.name = name;
        this.id = id;
        this.dateOfPurchase = dateOfPurchase;
        this.selectedLocomotives = selectedLocomotives;
        this.selectedCarriages = selectedCarriages;
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
    public List<Locomotive> getSelectedLocomotives() {
        return selectedLocomotives;
    }
    public List<Carriage> getSelectedCarriages() {
        return selectedCarriages;
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
    public void setSelectedLocomotives(List<Locomotive> selectedLocomotives) {
        this.selectedLocomotives = selectedLocomotives;
    }
    public void setSelectedCarriages(List<Carriage> selectedCarriages) {
        this.selectedCarriages = selectedCarriages;
    }

    // validations
}
