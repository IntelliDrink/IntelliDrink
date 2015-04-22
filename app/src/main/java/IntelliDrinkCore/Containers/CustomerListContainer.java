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

    ArrayList<Transaction> tabList;
    String RFID;
    int id;

    CustomerListGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public CustomerListContainer(ServerDatabase db)
    {
        tabList = new ArrayList<Transaction>();
        myGrabber = new CustomerListGrabber(this, db);
    }


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

    public void buildFromRFID(String RFID)
    {
        id = myGrabber.buildFromRFID(RFID);
    }

    public void buildFromEmail(String Email)
    {
        id = myGrabber.buildfromEmail(Email);
    }

    public void checkOut()
    {
        if(myGrabber.checkout(this.id))
        {

        }
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

    public void build(ArrayList<Transaction> tabList)
    {
        this.tabList = tabList;
    }

}
