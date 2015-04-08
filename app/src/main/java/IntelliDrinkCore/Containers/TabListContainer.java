package IntelliDrinkCore.Containers;

import android.content.Context;

import java.util.ArrayList;

import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.TabGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class TabListContainer implements IntelliDrinkContainer{

    String customersName;
    ArrayList<Transaction> transactionHistory;
    TabGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public TabListContainer(LocalDatabaseHelper db)
    {
        customersName = "";
        transactionHistory = new ArrayList<>();
        myGrabber = new TabGrabber(this, db);
    }

    public void setName(String name)
    {
        customersName = name;
    }

    public void addTransaction(Transaction a)
    {
        transactionHistory.add(a);
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

    }

    /**
     * Updates the Container from the DB
     */
    public void update()
    {

    }



}
