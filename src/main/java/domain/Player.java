package domain;

public class Player {
    private String name;
    private int id;
    private String password;
    private int budget;

    public Player(String name, int id, String password, int budget) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getPassword() {
        return password;
    }
    public int getBudget() {
        return budget;
    }

    public int subtractBudget(int price) {
        budget -= price;
        return budget;
    }

    public int addBudget(int profit) {
        budget += profit;
        return budget;
    }
}
