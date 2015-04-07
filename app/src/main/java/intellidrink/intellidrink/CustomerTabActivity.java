package intellidrink.intellidrink;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class CustomerTabActivity extends ActionBarActivity {

    ImageButton orderDrinkButton;
    ImageButton backButton;

    TextView customerTotalEditText;
    TextView customersNameText;

    ListView tabListView;

    ImageView advertisementImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_customer_tab);

        orderDrinkButton = (ImageButton) findViewById(R.id.orderDrinkButton);
        backButton = (ImageButton) findViewById(R.id.backButton);

        customerTotalEditText = (TextView) findViewById(R.id.customerTotalEditText);
        customersNameText = (TextView) findViewById(R.id.customersNameText);

        tabListView = (ListView) findViewById(R.id.customersTabView);

        setupTimer();

    }

    void setupTimer()
    {
        final int imageIds[] = {R.drawable.tmpimage1, R.drawable.tmpimage2};
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    public void run() {
                        int location = 0;
                        if (location > imageIds.length) {
                            location = 0;
                        } else
                            advertisementImage.setImageResource(imageIds[location++]);

                    }
                });

            }
        }, System.currentTimeMillis(), TIMER_INTERVAL);
    }

    public void onClickCustomerTabActivity(View v)
    {
        if(v.getId() == R.id.orderDrinkButton)
        {
            Intent i = new Intent(this, orderScreen.class);
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


    final int TIMER_INTERVAL = 10000;
}
