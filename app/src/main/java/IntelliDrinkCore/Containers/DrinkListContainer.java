package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.DrinkListGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 * SINGLETON to allow this data to stay around more, we can update it more frequently if needed
 */
public class DrinkListContainer implements IntelliDrinkContainer{

    ArrayList<DrinkListItem> tabList;
    DrinkListGrabber myGrabber;

    DrinkListItem lastDrink;
    int drinkCounter = 0;

    int drinkables;

    public DrinkListContainer(ServerDatabase db, LocalDatabaseHelper db2)
    {
        tabList = new ArrayList<>();
        myGrabber = new DrinkListGrabber(this, db, db2);
        lastDrink = new DrinkListItem();
    }

    public void setArrayList(ArrayList<DrinkListItem> list)
    {
        this.tabList = list;
    }

    public ArrayList<DrinkListItem> getArrayList()
    {
        //return instance.tabList;
        return this.tabList;
    }

    public int getDrinkables() { return drinkables; }

    public void setDrinkables(int d) { drinkables = d; }


    public DrinkListItem getDrink(int i)
    {
        DrinkListItem tmp = new DrinkListItem();
        tmp = this.tabList.get(i);
        return tmp;
    }

    public DrinkListItem getLastDrink()
    {
        return lastDrink;
    }


    public void makeDrink(DrinkListItem drinkMade)
    {
        this.lastDrink = drinkMade;
        transfer();
        lastDrink = new DrinkListItem();
    }


    /**
     * Updates the DB with the info on the class
     */
    @Override
    public void transfer() {
        drinkCounter ++;
        myGrabber.pushToServer();
    }

    /**
     * Updates the Container from the DB
     */
    @Override
    public void update() {

        if(this.drinkCounter != myGrabber.getSizeOfAvailableRecipes())
        build();
    }

    /**
     * Used to build the actual list.  Call this on initialization
     */
    public void build()
    {
        if(tabList.size() > 0)
        {
            tabList.clear();
        }
        this.drinkCounter = myGrabber.getSizeOfAvailableRecipes();
        myGrabber.buildContainer();

    }

    public void builder(ArrayList<DrinkListItem> drinks)
    {
        this.tabList = drinks;
        for(DrinkListItem drink : drinks)
        {

        }
    }
}
