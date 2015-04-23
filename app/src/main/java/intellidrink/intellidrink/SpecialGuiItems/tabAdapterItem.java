package intellidrink.intellidrink.SpecialGuiItems;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class TabAdapterItem {

    private String drinkName;
    private String price;

    public TabAdapterItem()
    {}

    public TabAdapterItem(String name, String p)
    {
        drinkName = name;
        price = p;
    }

    public String getDrinkName()
    {
        return this.drinkName;
    }

    public String getPrice()
    {
        return this.price;
    }

    public void setDrinkName(String n)
    {
        this.drinkName = n;
    }

    public void setPrice(String n)
    {
        this.price = n;
    }

}
