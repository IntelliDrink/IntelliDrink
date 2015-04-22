package IntelliDrinkDB.Grabbers;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.CustomerListContainer;
import IntelliDrinkCore.Transaction;
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

    public int buildFromRFID(String RFID)
    {
        int id = myDB.getCustomerIDByRFID("Kiosk_1", "password", RFID);
        build(id);
        return id;
    }

    public int buildfromEmail(String Email)
    {
        int id = myDB.getCustomerIDByEMAIL("Kiosk_1", "password", Email);
        build(id);
        return id;
    }

    void build(int customerID)
    {
        ArrayList<Transaction> tmpList = new ArrayList<>();
        tmpList = myDB.getTransactionHistory("Kiosk_1", "password", customerID);
        myContainer.build(tmpList);
    }

    public boolean checkout(int id)
    {
        return false;
    }


    public void buildContainer()
    {

    }

    public void pushToServer()
    {

    }
}
