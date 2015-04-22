package intellidrink.intellidrink.SpecialGuiItems;

import IntelliDrinkCore.DrinkListItem;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class DrinkItem {

    private String drinkName;
    private double price;

    public DrinkItem()
    {

    }

    public DrinkItem(String name, double p)
    {
        this.drinkName = name;
        this.price = p;
    }

    public String getDrinkName()
    {
        return this.drinkName;
    }

    public double getPrice()
    {
        return this.price;
    }

}
