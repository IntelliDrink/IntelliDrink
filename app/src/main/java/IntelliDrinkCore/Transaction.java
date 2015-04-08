package IntelliDrinkCore;

/**
 * Created by David on 4/6/2015.
 */
public class Transaction {
    private int ID;
    private double price;

    public Transaction()
    {
        ID = 0;
        price = 0.0;
    }

    public Transaction(int id, double p)
    {
        ID = id;
        price = p;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private String recipeName;

}
