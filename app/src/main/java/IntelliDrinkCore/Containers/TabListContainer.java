package IntelliDrinkCore.Containers;

import android.content.Context;

import java.util.ArrayList;

import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.TabGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class TabListContainer implements IntelliDrinkContainer{

    CustomerInformation myInformation;
    ArrayList<Transaction> transactionHistory;
    TabGrabber myGrabber;
    String RFID;

    //TODO INTERFACE WITH THE GRABBER
    public TabListContainer(ServerDatabase db)
    {
        myInformation = new CustomerInformation();
        transactionHistory = new ArrayList<>();
        myGrabber = new TabGrabber(this, db);
    }

    public CustomerInformation getMyInformation()
    {
        return myInformation;
    }

    public ArrayList<Transaction> getTransactionHistory()
    {
        return transactionHistory;
    }

    public void orderDrink(DrinkListItem drink)
    {
        this.myGrabber.pushToServer(drink, myInformation);
    }

    public void addTransaction(Transaction a)
    {
        transactionHistory.add(a);
        transfer();
    }

    public void addTransaction(int id, double price)
    {
        Transaction tmp = new Transaction(id, price);
        transactionHistory.add(tmp);
    }

    /**
     * Updates the DB with the info on the class
     */
    public void transfer()
    {
        //myGrabber.pushToServer(transactionHistory.get(transactionHistory.size()), myInformation);

    }

    /**
     * Updates the Container from the DB
     */
    public void update()
    {
        build(RFID);
    }

    public void build(String RFID)
    {
        if(transactionHistory.size() != 0)
        {
            transactionHistory.clear();
        }
        myGrabber.buildContainer(RFID);
    }

    public void builder(CustomerInformation info, ArrayList<Transaction> list)
    {
        myInformation = info;
        transactionHistory = list;
        RFID = myInformation.getCustomerRFID();
    }
}
