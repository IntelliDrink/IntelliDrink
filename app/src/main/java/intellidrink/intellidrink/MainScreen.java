package intellidrink.intellidrink;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkDB.ServerDatabase;


public class MainScreen extends ActionBarActivity {

    ImageButton mainButton;
    ImageButton adminButton;
    ServerDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        mainButton = (ImageButton) findViewById(R.id.mainButton);
        adminButton = (ImageButton) findViewById(R.id.adminButton);
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
            Intent i = new Intent(this, CustomerTabActivity.class);
            //handle RFID
            String RFID = handleRFID();
            if(RFID.length() > 0)
            {
                //TODO: pass RFID????
                Bundle extras = new Bundle();
                extras.putString("RFID", RFID);
                i.putExtras(extras);
                startActivity(i);
            }
            else
            {
                ErrorAlert e = new ErrorAlert(this);
                e.showErrorDialog("No RFID Recieed", "There was nothing read from the RFID.  " +
                        "Please Inform the Manager or call the Manufacturer");
                //TODO: REMOVE THIS WHEN RFID IS IMPLEMENTED
                startActivity(i);
            }
        }
        else if(v.getId() == R.id.adminButton)
        {
            Intent i = new Intent(this, AdminPanel.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(this, "Wow, you broke our app you shitbag", Toast.LENGTH_SHORT);
        }
    }

    //TODO: implement RFID Handler
    public String handleRFID()
    {
        String RFID = "";

        //TODO:REMOVETHIS
        RFID = "12345678";
        return RFID;
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
