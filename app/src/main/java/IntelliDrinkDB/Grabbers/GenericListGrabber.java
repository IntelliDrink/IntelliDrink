package IntelliDrinkDB.Grabbers;

import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/9/15.
 */
public abstract class GenericListGrabber {

    protected ServerDatabase myDB;

    public String username = "Kiosk_1";
    public String password = "password";

    public GenericListGrabber(ServerDatabase db)
    {
        myDB = db;
    }




}
