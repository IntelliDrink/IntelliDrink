package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.CustomerListGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class CustomerListContainer implements IntelliDrinkContainer{

    //static CustomerListContainer instance;
    ArrayList<Transaction> tabList;

    CustomerListGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public CustomerListContainer(ServerDatabase db)
    {
        tabList = new ArrayList<Transaction>();
        myGrabber = new CustomerListGrabber(this, db);
    }

    /*public synchronized static CustomerListContainer getInstance()
    {
        if (instance == null)
            instance = new CustomerListContainer();

        return instance;
    }*/

    public void setArrayList(ArrayList<Transaction> list)
    {
        this.tabList = list;
    }

    public ArrayList<Transaction> getArrayList()
    {
        //return instance.tabList;
        return this.tabList;
    }


    public Transaction getTransaction(int i)
    {
        Transaction tmp = new Transaction();
        tmp = this.tabList.get(i);
        return tmp;
    }


    /**
     * Updates the DB with the info on the class
     */
    @Override
    public void transfer() {

    }

    /**
     * Updates the Container from the DB
     */
    @Override
    public void update() {

    }
}
