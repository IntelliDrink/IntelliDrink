package IntelliDrinkCore;

/**
 * Created by Terryn-Fredrickson on 4/11/15.
 */
public class DrinkIngredients {


    String name;
    int slot;
    int amount;

    public DrinkIngredients()
    {
        name = "";
    }

    public DrinkIngredients(String name, int slot, int amount)
    {
        this.name = name;
        this.slot = slot;
        this.amount = amount;
    }

    public String getName()
    { return this.name; }

    public int getSlot() { return slot; }

    public int getAmount() { return amount; }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

}
