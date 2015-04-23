package intellidrink.intellidrink.SpecialGuiItems;

import IntelliDrinkCore.DrinkListItem;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class DrinkAdapterItem {

    public String drinkName;
    public String price;
    //TODO maybe later
    //public String baseLiquor;

    public DrinkAdapterItem()
    {

    }

    public DrinkAdapterItem(String name, String p)
    {
        this.drinkName = name;
        this.price = p;
    }
}
