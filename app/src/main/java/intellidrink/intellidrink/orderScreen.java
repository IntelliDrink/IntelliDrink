package intellidrink.intellidrink;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import IntelliDrinkUSB.driver.UsbSerialDriver;
import IntelliDrinkUSB.driver.UsbSerialPort;
import IntelliDrinkUSB.driver.UsbSerialProber;
import IntelliDrinkUSB.util.HexDump;
import IntelliDrinkUSB.util.SerialInputOutputManager;
import intellidrink.intellidrink.SpecialGuiItems.DrinkAdapterItem;
import intellidrink.intellidrink.SpecialGuiItems.DrinkArrayAdapter;


public class OrderScreen extends ActionBarActivity {


    private static String ADMIN_USERNAME = "Admin";
    private static String ADMIN_PASSWORD = "12345678";

    String RFID = "";
    boolean adminMode = false;

    DrinkListItem activeDrink;

    ImageButton orderButton;
    ImageButton backButton;

    ImageView drinkImage;

    TextView drinkTitle;
    TextView drinkDescription;
    TextView ingredients;

    Button searchButton;
    EditText searchEditText;

    ListView drinkListView;

    TabListContainer myTabListContainer;
    DrinkListContainer myDrinkContainer;
    ServerDatabase database;
    LocalDatabaseHelper localDataBase;


    static UsbSerialPort port = null;
    private final String TAG = OrderScreen.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_order_screen);
        identifyGUIObjects();

        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        RFID = b.getString("RFID");

        if(this.getIntent().hasExtra("Admin Mode"))
        {
            adminMode = b.getBoolean("Admin Mode");
        }
        else
            adminMode = false;

        drinkListView = (ListView) findViewById(R.id.drinkListView);


        buildDatabase();

        initUSB();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Build GUI Objects
     */
    void identifyGUIObjects()
    {
        orderButton = (ImageButton) findViewById(R.id.orderButton);
        backButton = (ImageButton) findViewById(R.id.backButton);

        drinkImage = (ImageView) findViewById(R.id.drinkPictureImage);

        drinkTitle = (TextView) findViewById(R.id.drinkNameTextfield);
        drinkDescription = (TextView) findViewById(R.id.drinkDescriptionTextField);
        ingredients = (TextView) findViewById(R.id.drinkIngredientsTextField);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);

    }

    /**
     * sets up the DB objects and the required containers
     */
    void buildDatabase()
    {
        this.database = new ServerDatabase();
        this.localDataBase = new LocalDatabaseHelper(this.getApplicationContext());
        this.myDrinkContainer = new DrinkListContainer(database, localDataBase);
        this.myTabListContainer = new TabListContainer(database);


        myTabListContainer.build(this.RFID);
        myDrinkContainer.build();

        ArrayList<DrinkAdapterItem> myListData = new ArrayList<>();
        DrinkAdapterItem dataItem;
        for(DrinkListItem drink : myDrinkContainer.getArrayList())
        {
            String name = drink.getRecipeName();
            String price = String.valueOf(drink.getPrice());
            dataItem = new DrinkAdapterItem(name, price);
            myListData.add(dataItem);
        }
        DrinkArrayAdapter adapter = new DrinkArrayAdapter(this, myListData);
        drinkListView.setAdapter(adapter);
        drinkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doListItemOnClick(parent, view, position, id);
            }
        });



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    void doListItemOnClick(AdapterView<?> parent, View view, int position, long id)
    {
        activeDrink = this.myDrinkContainer.getDrink(position);
        drinkTitle.setText(activeDrink.getRecipeName());
        drinkDescription.setText(activeDrink.getDescription());
        this.ingredients.setText(activeDrink.getIngredientsString());
    }



    public void onClickOrderScreen(View v)
    {
        if(v.getId() == R.id.orderButton)
        {
            //ErrorAlert e = new ErrorAlert(this);
            //Toast.makeText(this, "ordering Drink", Toast.LENGTH_SHORT).show();
            if(this.activeDrink != null)
                confirmationDialog();

            //e.showErrorDialog("NYI", "This event is not implemented yet");
        }
        else if(v.getId() == R.id.backButton)
        {
            Intent i = new Intent(this, CustomerTabActivity.class);
            Bundle b = new Bundle();
            b.putString("RFID", RFID);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
        else if(v.getId() == R.id.searchButton)
        {
            ErrorAlert e = new ErrorAlert(this);
            e.showErrorDialog("NYI", "This event is not implemented yet");
        }
        else
        {
            Toast.makeText(this, "Shits broken yo", Toast.LENGTH_SHORT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_screen, menu);
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

    void confirmationDialog()
    {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                OrderScreen.this);

        alertDialog2.setTitle("Drink Confirmation");

        String msg = "Are you sure you would like a " +  this.activeDrink.getRecipeName() + "? \n" +
                "You will be charged: " + activeDrink.getPrice() + " to your account";
        alertDialog2.setMessage(msg);

        //TODO: SET THIS PICTURE TO THE ID OF THE DRINK
        //alertDialog2.setIcon(R.drawable.delete);

        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        drinkMixing(true);
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        drinkMixing(false);
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }

    void drinkMixing(boolean confirmation)
    {
        if(confirmation)
        {
            if(activeDrink != null)
            try {
                if(port == null)
                {
                    Log.d(TAG, "Port is null");
                }
                else{
                    port.write(activeDrink.getArduinoCode().getBytes(), 200);
                }

                //TODO David's database stuff for making a drink

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Toast.makeText(this, "Drink Made", Toast.LENGTH_LONG).show();
            wouldYouLikeAnotherDialog();
        }
        else
        {
            Toast.makeText(this, "Drink NOT Made", Toast.LENGTH_SHORT).show();
        }
    }

    void wouldYouLikeAnotherDialog()
    {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                OrderScreen.this);

        alertDialog2.setTitle("Moar???");
        alertDialog2.setMessage("Would you like another drink?");

        //TODO: SET THIS PICTURE TO THE ID OF THE DRINK
        //alertDialog2.setIcon(R.drawable.delete);

        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(i);
                        finish();
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
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
                    OrderScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Second Activity", "Trying to run thread");
                            //if(readState)
                            OrderScreen.this.updateReceivedData(data);
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

}
