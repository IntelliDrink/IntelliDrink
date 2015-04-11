package intellidrink.intellidrink;

import android.app.Activity;
import android.content.Intent;
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

import IntelliDrinkCore.Containers.DrinkListContainer;


public class AdminPanel extends Activity{

    Button loadKioskButton;
    Button testUSBButton;
    Button checkoutButton;
    Button reloadKioskButton;


    ListView kioskDispensaryListView;
    ListView drinkSelectorListView;
    String[] tmpList = {"tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_admin_pannel);

        loadKioskButton = (Button) findViewById(R.id.loadAppButton);
        testUSBButton = (Button) findViewById(R.id.USBTestButton);
        checkoutButton = (Button) findViewById(R.id.checkoutScreenButton);
        reloadKioskButton = (Button) findViewById(R.id.reloadKioskButton);

        kioskDispensaryListView = (ListView) findViewById(R.id.kioskDispensaryList);
        drinkSelectorListView = (ListView) findViewById(R.id.drinkSelectorListView);

        //TODO FIX THIS ADAPTER
        ArrayAdapter<String> tmpAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, tmpList);
        kioskDispensaryListView.setAdapter(tmpAdapter);
        drinkSelectorListView.setAdapter(tmpAdapter);


    }

    public void buttonClick(View v)
    {
        if(v.getId() == R.id.loadAppButton)
        {
            Intent i = new Intent(this, MainScreen.class);
            //handle any passing here, but we really shouldn't need to
            startActivity(i);
        }
        else if(v.getId() == R.id.USBTestButton)
        {
            //RUNS USB TEST CODE, IGNORE FOR NOW
            debugUSB();
        }
        else if(v.getId() == R.id.checkoutScreenButton)
        {
            Intent i = new Intent(this, CashOutActivity.class);
            //handle any passing here, but we really shouldn't need to
            startActivity(i);
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
}
