package intellidrink.intellidrink;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.Transaction;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import IntelliDrinkUSB.driver.UsbSerialDriver;
import IntelliDrinkUSB.driver.UsbSerialPort;
import IntelliDrinkUSB.driver.UsbSerialProber;
import IntelliDrinkUSB.util.HexDump;
import IntelliDrinkUSB.util.SerialInputOutputManager;
import intellidrink.intellidrink.SpecialGuiItems.TabAdapter;
import intellidrink.intellidrink.SpecialGuiItems.TabAdapterItem;


public class CustomerTabActivity extends ActionBarActivity {

    private static String ADMIN_USERNAME = "Admin";
    private static String ADMIN_PASSWORD = "12345678";

    private final String TAG = CustomerTabActivity.class.getSimpleName();

    ImageButton orderDrinkButton;
    ImageButton backButton;

    TextView customerTotalEditText;
    TextView customersNameText;

    ListView customersTabView;


    ServerDatabase db;
    TabListContainer myContainer;
    static String RFID;

    ImageView advertisementImage;
    Handler handler = new Handler();
    final int interval = 3000;
    int location = 0;
    int imageIds[] = {R.drawable.tmpimg1, R.drawable.tmpimg2};

    boolean adminMode;

    ServerDatabase database;
    LocalDatabaseHelper localDataBase;

    CustomerInformation myActiveCustomer;
    String balance;

    static UsbSerialPort port = null;

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


        if(this.getIntent().hasExtra("Admin Mode"))
        {
            Bundle b = new Bundle();
            adminMode = b.getBoolean("Admin Mode");
        }
        else
            adminMode = false;
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

        Log.d(TAG, "Initializing USB... ");
        initUSB();

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
            b.putBoolean("Admin Mode", this.adminMode);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
        else if(v.getId() == R.id.backButton)
        {
            Intent i = new Intent(this, MainScreen.class);
            Bundle b = new Bundle();
            b.putBoolean("Admin Mode" , this.adminMode);
            i.putExtras(b);
            startActivity(i);
            finish();
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


    /**
     * ***************************
     * USB CODE
     * ***************************
     */

    void initUSB() {

        boolean cont = false;
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            Log.d(TAG, "No drivers found");
        } else {
            Log.d(TAG, "Driver found: " + availableDrivers.size());
            cont = true;
        }
        if (cont) {
            UsbSerialDriver driver = availableDrivers.get(0);
            port = driver.getPorts().get(0);
            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                cont = false;
                Log.d(TAG, "Connection is false");
            } else {
                Log.d(TAG, "Conenction worked: " + connection.getSerial());
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (port != null) {
            try {
                port.close();
            } catch (IOException e) {
                // Ignore.
            }
            port = null;
        }
        finish();
    }

    /**
     * lol
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, port=" + port);
        if (port == null) {
        } else {
            final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            //usbManager.requestPermission(device, );
            UsbDeviceConnection connection = usbManager.openDevice(port.getDriver().getDevice());
            usbManager.requestPermission(port.getDriver().getDevice(), PendingIntent.getBroadcast(this, 0, new Intent(UsbManager.EXTRA_PERMISSION_GRANTED), 0));
            if (connection == null) {
                return;
            }

            try {
                port.open(connection);
                port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                try {
                    port.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                port = null;
                return;
            }
            Log.d(TAG, "Serial device: " + port.getClass().getSimpleName());
        }
        onDeviceStateChange();
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (port != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(port, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();


    private SerialInputOutputManager mSerialIoManager;
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    CustomerTabActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Second Activity", "Trying to run thread");
                            //if(readState)
                            CustomerTabActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };


    /**
     * This should not be called in this activity.  If it is please check with your
     * local ABC store because you're in for a long night....
     * @param data
     */
    void updateReceivedData(final byte[] data)
    {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.d(TAG, "Message " + message);
//
//        //Log.d(TAG, "Attempting to splice message");
//
//        String tmp = HexDump.dumpHexString(data);
//        if(data[data.length-1] == 0x0A)
//        {
//            Log.d(TAG, "Return line found!");
//            rfidFound = true;
//        }
//        StringBuilder b = new StringBuilder();
//        b.append(RFID);
//        b.append("" + tmp);
//        RFID = b.toString();
//        if(rfidFound)
//        {
//            Log.d(TAG, "RFID STRING ********");
//            Log.d(TAG, RFID);
//            this.fixRFID();
//        }
    }



    final int TIMER_INTERVAL = 10000;
    static String TOTAL_OWED = "Total owed: ";
}
