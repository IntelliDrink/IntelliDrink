package IntelliDrinkDB.Grabbers;

import IntelliDrinkCore.Containers.CustomerListContainer;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class CustomerListGrabber {

    CustomerListContainer myContainer;

    LocalDatabaseHelper myDb;

    public CustomerListGrabber(CustomerListContainer container, LocalDatabaseHelper db)
    {
        myContainer = container;
        myDb = db;
    }


}
