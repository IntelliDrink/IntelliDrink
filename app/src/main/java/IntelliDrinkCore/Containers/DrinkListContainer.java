package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.DrinkListGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 * SINGLETON to allow this data to stay around more, we can update it more frequently if needed
 */
public class DrinkListContainer implements IntelliDrinkContainer{

    static DrinkListContainer instance;
    ArrayList<DrinkListItem> tabList;

    DrinkListGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public DrinkListContainer(LocalDatabaseHelper db)
    {
        tabList = new ArrayList<>();
        myGrabber = new DrinkListGrabber(this, db);
    }

    public synchronized static DrinkListContainer getInstance(LocalDatabaseHelper db)
    {
        if (instance == null)
            instance = new DrinkListContainer(db);

        return instance;
    }

    public void setArrayList(ArrayList<DrinkListItem> list)
    {
        this.tabList = list;
    }

    public ArrayList<DrinkListItem> getArrayList()
    {
        return instance.tabList;
        //return this.tabList;
    }


    public DrinkListItem getDrink(int i)
    {
        DrinkListItem tmp = new DrinkListItem();
        tmp = this.tabList.get(i);
        return tmp;
    }

    /**
     * Updates the DB with the info on the class
     */
    @Override
    public void transfer() {

    }

    /**
     * Updates the Container from the DB
     */
    @Override
    public void update() {

    }
}
