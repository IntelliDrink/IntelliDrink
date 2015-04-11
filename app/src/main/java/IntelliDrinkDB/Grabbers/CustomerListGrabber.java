package IntelliDrinkDB.Grabbers;

import IntelliDrinkCore.Containers.CustomerListContainer;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class CustomerListGrabber extends GenericListGrabber{

    CustomerListContainer myContainer;


    public CustomerListGrabber(CustomerListContainer container, ServerDatabase db)
    {
        super(db);
        myContainer = container;
    }




}
