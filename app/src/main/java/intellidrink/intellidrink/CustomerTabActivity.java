package intellidrink.intellidrink;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.Grabbers.TabGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import intellidrink.intellidrink.SpecialGuiItems.TabAdapter;
import intellidrink.intellidrink.SpecialGuiItems.TabAdapterItem;


public class CustomerTabActivity extends ActionBarActivity {

    ImageButton orderDrinkButton;
    ImageButton backButton;

    TextView customerTotalEditText;
    TextView customersNameText;

    ListView customersTabView;


    ServerDatabase db;
    TabListContainer myContainer;
    String RFID;

    ImageView advertisementImage;
    Handler handler = new Handler();
    final int interval = 3000;
    int location = 0;
    int imageIds[] = {R.drawable.tmpimg1, R.drawable.tmpimg2};


    ServerDatabase database;
    LocalDatabaseHelper localDataBase;

    CustomerInformation myActiveCustomer;
    String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_tab);

        this.customersNameText = (TextView) findViewById(R.id.customerTabCustomersNameText);
        this.customerTotalEditText = (TextView) findViewById(R.id.customerTotalEditText);

        customersNameText.setText("Test Customer");
        customerTotalEditText.setText("Total: 9,99$");

        db = new ServerDatabase();
        myContainer = new TabListContainer(db);

        RFID = getIntent().getExtras().getString("RFID");
        //TODO REMOVE THIS CHECKING STUFF
        RFID = "TERRAN";
        myContainer.build(RFID);

        myActiveCustomer = myContainer.getMyInformation();

        customersNameText.setText(myActiveCustomer.getCustomerName());
        //Toast.makeText(this, "Amount owed: " + String.valueOf(myActiveCustomer.getBalance()), Toast.LENGTH_LONG).show();
        balance = new String(TOTAL_OWED + String.valueOf(myActiveCustomer.getBalance()));
        customerTotalEditText.setText(balance);

        orderDrinkButton = (ImageButton) findViewById(R.id.orderDrinkButton);
        backButton = (ImageButton) findViewById(R.id.backButton);


        customersTabView = (ListView) findViewById(R.id.customersTabView);
        advertisementImage = (ImageView) findViewById(R.id.advertisementImageView);


        advertisementImage.setImageResource(imageIds[location++]);
        location = 1;
        handler.postDelayed(updateTimerThread, 3000);


        ArrayList<TabAdapterItem> adapterItems = new ArrayList<>();
        TabAdapterItem item;
        for(Transaction transaction : myContainer.getTransactionHistory())
        {
            String n = transaction.getRecipeName();
            String p = "$ " + String.valueOf(transaction.getPrice());
            item = new TabAdapterItem(n, p);
            adapterItems.add(item);
        }

        TabAdapter myTabAdapter = new TabAdapter(this, adapterItems);
        customersTabView.setAdapter(myTabAdapter);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            //Log.d("Timer", "Event1");
            advertisementImage.setImageResource(imageIds[location++]);
            if(location > imageIds.length - 1)
            {
                location = 0;
            }
            handler.postDelayed(this, 3000);
        }
    };


    void setupTimer()
    {
        advertisementImage.setImageResource(imageIds[location++]);
    }

    public void onClickCustomerTabActivity(View v)
    {
        if(v.getId() == R.id.orderDrinkButton)
        {
            Intent i = new Intent(this, OrderScreen.class);
            Bundle b = new Bundle();
            b.putCharSequence("RFID", RFID);
            i.putExtras(b);
            startActivity(i);
        }
        else if(v.getId() == R.id.backButton)
        {
            Intent i = new Intent(this, MainScreen.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(this, "Well that wasn't supposed to happen", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadDBInfo(LocalDatabaseHelper localDB, ServerDatabase DB)
    {
        this.localDataBase = localDB;
        this.database = DB;
    }


    final int TIMER_INTERVAL = 10000;
    static String TOTAL_OWED = "Total owed: ";
}
