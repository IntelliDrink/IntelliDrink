package IntelliDrinkDB.Grabbers;

import android.content.Context;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class TabGrabber extends GenericListGrabber{

    TabListContainer container;

    public TabGrabber(TabListContainer container, ServerDatabase db)
    {
        super(db);
        this.container = container;
    }

    public void buildContainer(String RFID)
    {
        CustomerInformation tmpInfo = myDB.cusomterInfo(username, password, RFID);
        int customerID = tmpInfo.getID();
        ArrayList<Transaction> tmpTransactions = myDB.getTransactionHistory("Kiosk_1", "password", customerID);
        container.builder(tmpInfo, tmpTransactions);
    }

    public void pushToServer(Transaction transaction, CustomerInformation info)
    {
        myDB.customerTransaction(username, password, info.getID(), transaction.getID(), 1, transaction.getPrice());
    }


}
