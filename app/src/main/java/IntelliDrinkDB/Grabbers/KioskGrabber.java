package IntelliDrinkDB.Grabbers;

import IntelliDrinkCore.Containers.KioskDrinkContainer;
import IntelliDrinkCore.Containers.KioskSlotContainer;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class KioskGrabber {

    KioskSlotContainer slotContainer;
    KioskDrinkContainer drinkContainer;

    LocalDatabaseHelper myDb;

    public KioskGrabber(KioskSlotContainer container, LocalDatabaseHelper db)
    {
        slotContainer = container;
        myDb = db;
    }

    public KioskGrabber(KioskDrinkContainer container, LocalDatabaseHelper db)
    {
        drinkContainer = container;
        myDb = db;
    }


}
