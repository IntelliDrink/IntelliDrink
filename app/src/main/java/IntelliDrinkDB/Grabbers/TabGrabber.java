package IntelliDrinkDB.Grabbers;

import android.content.Context;

import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class TabGrabber {

    TabListContainer container;
    LocalDatabaseHelper myDB;

    public TabGrabber(TabListContainer container, LocalDatabaseHelper db)
    {
        this.container = container;
        myDB = db;
    }


}
