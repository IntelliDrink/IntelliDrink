package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.CustomerListGrabber;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 *
 * ONLY USED FOR ADMIN FUNCITONALITY
 * IMPLEMENTING LAST OF THE 4
 */
public class CustomerListContainer implements IntelliDrinkContainer{


    /**
     * CLASS IS WRONG
     */
    ArrayList<CustomerInformation> tabList;
    String RFID;
    int id;
    double balance;

    CustomerListGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public CustomerListContainer(ServerDatabase db, String rfid)
    {
        //tabList = new ArrayList<Transaction>();
        myGrabber = new CustomerListGrabber(this, db);
        balance = 0.00;
        this.RFID = rfid;
    }


    public void setArrayList(ArrayList<Transaction> list)
    {
        //this.tabList = list;
    }

    public ArrayList<Transaction> getArrayList()
    {
        return null;
    }


    public Transaction getTransaction(int i)
    {
        Transaction tmp = new Transaction();
        //tmp = this.tabList.get(i);
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

    /**
     * returns false if checkout options failed.
     * @return
     */
    public double checkOut()
    {/*
        if(myGrabber.checkout())
        {
            tabList.clear();
            return balance;
        }
        else*/
            return -1;
    }

    public void setBalance(double val)
    {
        this.balance = val;
    }

    public String getRFID()
    {
        return this.RFID;
    }

    public int getId()
    {
        return this.id;
    }

    /**
     * Updates the DB with the info on the class
     */
    @Override
    public void transfer() {
        //TODO NOT USED???
    }

    /**
     * Updates the Container from the DB
     */
    @Override
    public void update() {
        myGrabber.buildContainer();
    }

    public void buildListOnly(ArrayList<Transaction> tablist)
    {
        //this.tabList = tablist;
    }

    public void build(ArrayList<Transaction> tabList, CustomerInformation info)
    {
        //this.tabList = tabList;
        //this.myCustomerInfo = info;
    }

}
