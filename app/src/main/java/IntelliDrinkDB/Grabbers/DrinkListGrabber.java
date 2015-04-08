package IntelliDrinkDB.Grabbers;

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class DrinkListGrabber {

    DrinkListContainer myContainer;

    LocalDatabaseHelper myDb;

    public DrinkListGrabber(DrinkListContainer container, LocalDatabaseHelper db)
    {
        myContainer = container;
        myDb = db;
    }



}
