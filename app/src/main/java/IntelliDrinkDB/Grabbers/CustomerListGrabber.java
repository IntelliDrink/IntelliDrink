package IntelliDrinkDB.Grabbers;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.CustomerListContainer;
import IntelliDrinkCore.CustomerInformation;
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
        myContainer.buildListOnly(myDB.getTransactionHistory(USERNAME, PASSWORD, id));
        return id;
    }

    public int buildfromEmail(String Email)
    {
        int id = myDB.getCustomerIDByEMAIL("Kiosk_1", "password", Email);
        myContainer.buildListOnly(myDB.getTransactionHistory(USERNAME, PASSWORD, id));
        //build();
        return id;
    }

    //TODO fix that these are using two different IDs (string RFID and int ID)????
    void build()
    {
        CustomerInformation info = myDB.cusomterInfo(USERNAME, PASSWORD, myContainer.getRFID());
        ArrayList<Transaction> transactions = myDB.getTransactionHistory(USERNAME, PASSWORD, info.getID());
        myContainer.build(transactions, info);
    }

    public boolean checkout(int id)
    {
        double balance = myDB.checkOut("Kiosk_1", "password", id);
        boolean checker = false;
        if(balance >= 0)
        {
            checker = true;
        }
        myContainer.setBalance(balance);
        return checker;
    }


    public void buildContainer()
    {
    }

    public void pushToServer()
    {
    }


    static String USERNAME = "Kiosk_1";
    static String PASSWORD = "password";
}
