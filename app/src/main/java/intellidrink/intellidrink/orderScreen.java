package intellidrink.intellidrink;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class orderScreen extends ActionBarActivity {

    ImageButton orderButton;
    ImageButton backButton;

    ImageView drinkImage;

    TextView drinkTitle;
    TextView drinkDescription;
    TextView ingredients;

    Button searchButton;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_order_screen);

        orderButton = (ImageButton) findViewById(R.id.orderDrinkButton);
        backButton = (ImageButton) findViewById(R.id.backButton);

        drinkImage = (ImageView) findViewById(R.id.drinkPictureImage);

        drinkTitle = (TextView) findViewById(R.id.drinkNameTextfield);
        drinkDescription = (TextView) findViewById(R.id.drinkDescriptionTextField);
        ingredients = (TextView) findViewById(R.id.drinkIngredientsTextField);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
    }

    public void onClickOrderScreen(View v)
    {
        if(v.getId() == R.id.orderDrinkButton)
        {
            ErrorAlert e = new ErrorAlert(this);
            e.showErrorDialog("NYI", "This event is not implemented yet");
        }
        else if(v.getId() == R.id.backButton)
        {
            Intent i = new Intent(this, CustomerTabActivity.class);
            startActivity(i);
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
}
