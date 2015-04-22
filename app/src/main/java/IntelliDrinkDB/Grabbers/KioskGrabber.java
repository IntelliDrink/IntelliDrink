package IntelliDrinkDB.Grabbers;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.KioskConfigurationContainer;
import IntelliDrinkCore.KioskConfiguration;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class KioskGrabber extends GenericListGrabber {

    KioskConfigurationContainer myContainer;

    public KioskGrabber(KioskConfigurationContainer container, ServerDatabase db)
    {
        super(db);
        myContainer = container;

    }

    public void buildContainer()
    {
        ArrayList<KioskConfiguration> data = this.myDB.configureKioskDatabase("Kiosk_1", "password");
    }

    public void pushToServer()
    {
        //isn't really needed?
    }




}
