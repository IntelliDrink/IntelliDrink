package intellidrink.intellidrink;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import IntelliDrinkUSB.driver.UsbSerialDriver;
import IntelliDrinkUSB.driver.UsbSerialPort;
import IntelliDrinkUSB.driver.UsbSerialProber;
import IntelliDrinkUSB.util.HexDump;
import IntelliDrinkUSB.util.SerialInputOutputManager;


public class MainScreen extends ActionBarActivity {

    private static String ADMIN_USERNAME = "Admin";
    private static String ADMIN_PASSWORD = "1";

    private final String TAG = MainScreen.class.getSimpleName();

    private boolean adminMode;

    static int BAUD = 9600;

    ImageButton mainButton;
    ImageButton adminButton;
    ServerDatabase database;
    LocalDatabaseHelper localDataBase;
    String RFID;
    boolean rfidFound = false;

    private static UsbSerialPort port = null;

    private static final int MESSAGE_REFRESH = 101;
    private static final long REFRESH_TIMEOUT_MILLIS = 5000;

    private UsbSerialPort myPort = null;
    private boolean readState;


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
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Second Activity", "Trying to run thread");
                            //if(readState)
                                MainScreen.this.updateReceivedData(data);
                        }
                    });
                }
            };

    void updateReceivedData(final byte[] data)
    {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.d(TAG, "Message " + message);

        //Log.d(TAG, "Attempting to splice message");

        String tmp = HexDump.dumpHexString(data);
        if(data[data.length-1] == 0x0A)
        {
            Log.d(TAG, "Return line found!");
            rfidFound = true;
        }
        StringBuilder b = new StringBuilder();
        b.append(RFID);
        b.append("" + tmp);
        RFID = b.toString();
        if(rfidFound)
        {
            Log.d(TAG, "RFID STRING ********");
            Log.d(TAG, RFID);
            this.fixRFID();
        }
    }

    /**
     * Kappa
     */
    void fixRFID()
    {
        String[] tmp = RFID.split("\n");
        StringBuilder finalBuilder= new StringBuilder();
        for(int a = 0; a < tmp.length; a++)
        {
            String testBase = tmp[a];
            StringBuilder builder = new StringBuilder();
            for(int i = 63-4; i < testBase.length(); i++)
            {
                char c = testBase.charAt(i);
                if(c != '.')
                    builder.append(testBase.charAt(i));
            }
            String maybe = new String(builder);
            Log.d("Maybe a built string", maybe);
            //Toast.makeText(this, maybe, Toast.LENGTH_SHORT).show();
            finalBuilder.append(maybe);
        }
        String newRFID = finalBuilder.toString();
        Toast.makeText(this, "New RFID: " + newRFID , Toast.LENGTH_SHORT).show();
        RFID = newRFID;
        if(RFID.length() < 10 && RFID.length() > 4)
            this.loadOrderScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if(this.getIntent().hasExtra("Admin Mode"))
        {
            Bundle b = this.getIntent().getExtras();
            adminMode = b.getBoolean("Admin Mode");
            Log.d(TAG, "Admin Mode: " + adminMode);
        }
        else
            adminMode = false;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initUSB();

        RFID = "";

        database = new ServerDatabase();
        localDataBase = new LocalDatabaseHelper(this);
        mainButton = (ImageButton) findViewById(R.id.mainButton);
        adminButton = (ImageButton) findViewById(R.id.adminButton);

    }

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



    void loadOrderScreen()
    {
        Intent i = new Intent(this, CustomerTabActivity.class);
        //TODO: pass RFID????
        Bundle extras = new Bundle();
        extras.putString("RFID", RFID);
        i.putExtras(extras);

        startActivity(i);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    public void onClickMainScreen(View v)
    {
        if(v.getId() == R.id.mainButton)
        {
            if(!this.adminMode){

            this.rfidFound = false;
            RFID = "";
            try {
                //byte[] data = HexDump.toByteArray('R');
                byte[] data = {(byte) 0x52};
                if(port != null)
                    //port.write(blink, 10);
                    port.write(data, 10);
                else
                    Log.d(TAG, "Port is null shitbag");
            } catch (IOException e) {
                e.printStackTrace();
            }}
            else
            {
                RFID = "1437722";
                loadOrderScreen();
            }
        }
        else if(v.getId() == R.id.adminButton)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Admin Password");
            alert.setMessage("Please enter the Admin Password to COntinue");

// Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if(value.equals(ADMIN_PASSWORD))
                    {
                        launchAdminPanel();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });

            alert.show();

        }
        else
        {
            Toast.makeText(this, "Wow, you broke our app you shitbag", Toast.LENGTH_SHORT);
        }
    }

    void launchAdminPanel()
    {
        Intent i = new Intent(this, AdminPanel.class);
        Bundle b = new Bundle();
        b.putBoolean("Admin Mode" , adminMode);
        startActivity(i);
        finish();
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
}
