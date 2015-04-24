package intellidrink.intellidrink;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import IntelliDrinkUSB.driver.ProbeTable;
import intellidrink.intellidrink.SpecialGuiItems.KioskListAdapter;
import intellidrink.intellidrink.SpecialGuiItems.KioskListItem;
import intellidrink.intellidrink.SpecialGuiItems.LiteralIngredientListAdapter;
import intellidrink.intellidrink.SpecialGuiItems.LiteralIngredientListItem;


public class AdminPanel extends Activity{


    private static String ADMIN_USERNAME = "Admin";
    private static String ADMIN_PASSWORD = "12345678";

    Button loadKioskButton;
    Button testUSBButton;
    Button checkoutButton;
    Button reloadKioskButton;

    boolean adminMode;

    ListView kioskDispensaryListView;
    ListView drinkSelectorListView;
    String[] tmpList = {"tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp"};

    KioskListAdapter kioskListAdapter;
    ArrayList<KioskListItem> kioskData;
    LiteralIngredientListAdapter literalIngredientListAdapter;
    ArrayList<LiteralIngredientListItem> ingredientsData;

    ServerDatabase database;
    LocalDatabaseHelper localDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(this.getIntent().hasExtra("Admin Mode"))
        {
            Bundle b = new Bundle();
            adminMode = b.getBoolean("Admin Mode");
        }
        else
            adminMode = false;


        setContentView(R.layout.activity_admin_pannel);

        doGUIThings();
        doServerThings();

        //TODO FIX THIS ADAPTER
        doAdapterThings();
    }

    void doGUIThings()
    {
        loadKioskButton = (Button) findViewById(R.id.loadAppButton);
        testUSBButton = (Button) findViewById(R.id.USBTestButton);
        checkoutButton = (Button) findViewById(R.id.checkoutScreenButton);
        reloadKioskButton = (Button) findViewById(R.id.reloadKioskButton);

        kioskDispensaryListView = (ListView) findViewById(R.id.kioskDispensaryList);
        drinkSelectorListView = (ListView) findViewById(R.id.drinkSelectorListView);

    }

    public void doServerThings()
    {
        //loljk right now

    }

    ArrayList<KioskListItem> getDummyKiosks()
    {
        ArrayList<KioskListItem> dummyList = new ArrayList<>();
        KioskListItem kioskthing;
        kioskthing = new KioskListItem("Kiosk_1");
        dummyList.add(kioskthing);
        kioskthing = new KioskListItem("Kiosk_2");
        dummyList.add(kioskthing);
        kioskthing = new KioskListItem("Kiosk_3");
        dummyList.add(kioskthing);
        kioskthing = new KioskListItem("Kiosk_4");
        dummyList.add(kioskthing);
        kioskthing = new KioskListItem("Kiosk_5");
        dummyList.add(kioskthing);
        return dummyList;
    }

    ArrayList<LiteralIngredientListItem> getIngredientsData()
    {
        ArrayList<LiteralIngredientListItem> dummyList = new ArrayList<>();
        LiteralIngredientListItem ingredItem;
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        ingredItem = new LiteralIngredientListItem("Rum", "1.50");
        dummyList.add(ingredItem);
        return dummyList;
    }

    void doAdapterThings()
    {
        //kioskData = new ArrayList<>();
        kioskData = getDummyKiosks();
        kioskListAdapter = new KioskListAdapter(this, kioskData);


        //ingredientsData = new ArrayList<>();
        ingredientsData = getIngredientsData();
        literalIngredientListAdapter = new LiteralIngredientListAdapter(this, ingredientsData);


        kioskDispensaryListView.setAdapter(kioskListAdapter);
        drinkSelectorListView.setAdapter(literalIngredientListAdapter);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void buttonClick(View v)
    {
        if(v.getId() == R.id.loadAppButton)
        {
            Intent i = new Intent(this, MainScreen.class);
            Bundle b = new Bundle();
            b.putBoolean("Admin Mode" , this.adminMode);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
        else if(v.getId() == R.id.USBTestButton)
        {
            //RUNS USB TEST CODE, IGNORE FOR NOW
            debugUSB();
        }
        else if(v.getId() == R.id.checkoutScreenButton)
        {
            Intent i = new Intent(this, CashOutActivity.class);
            Bundle b = new Bundle();
            b.putBoolean("Admin Mode" , this.adminMode);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
        else if(v.getId() == R.id.reloadKioskButton)
        {
            Toast.makeText(this, "Reloading Kiosk, please wait", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Well you fucked up the app somehow, good job", Toast.LENGTH_SHORT).show();
        }
    }

    void debugUSB()
    {
        Toast.makeText(this, "We're hoping we dont actually need to use this, but this is a placeholder message for now", Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_pannel, menu);
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
}
