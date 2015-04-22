package IntelliDrinkDB.Grabbers;

import IntelliDrinkCore.Containers.KioskDrinkContainer;
import IntelliDrinkCore.Containers.KioskSlotContainer;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class KioskGrabber extends GenericListGrabber {

    KioskSlotContainer slotContainer;
    KioskDrinkContainer drinkContainer;

    public KioskGrabber(KioskSlotContainer container, ServerDatabase db)
    {
        super(db);
        slotContainer = container;

    }

    public void buildContainer()
    {

    }

    public void pushToServer()
    {

    }




}
