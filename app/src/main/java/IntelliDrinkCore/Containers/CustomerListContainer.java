package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.Transaction;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class CustomerListContainer {

    //static CustomerListContainer instance;
    ArrayList<Transaction> tabList;

    public CustomerListContainer()
    {
        tabList = new ArrayList<Transaction>();
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

    public Transaction getTransaction(int i)
    {
        Transaction tmp = new Transaction();
        tmp = this.tabList.get(i);
        return tmp;
    }


}
