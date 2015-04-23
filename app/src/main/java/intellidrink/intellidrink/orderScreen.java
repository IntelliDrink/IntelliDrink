package intellidrink.intellidrink;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;
import intellidrink.intellidrink.SpecialGuiItems.DrinkAdapterItem;
import intellidrink.intellidrink.SpecialGuiItems.DrinkArrayAdapter;


public class OrderScreen extends ActionBarActivity {

    String RFID = "";


    ImageButton orderButton;
    ImageButton backButton;

    ImageView drinkImage;

    TextView drinkTitle;
    TextView drinkDescription;
    TextView ingredients;

    Button searchButton;
    EditText searchEditText;

    ListView drinkListView;
    String[] tmpList = {"tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp", "tmp"};


    TabListContainer myTabListContainer;
    DrinkListContainer myDrinkContainer;
    ServerDatabase database;
    LocalDatabaseHelper localDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_order_screen);
        identifyGUIObjects();

        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        RFID = b.getString("RFID");

        drinkListView = (ListView) findViewById(R.id.drinkListView);

        buildDatabase();



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

    ArrayList<DrinkAdapterItem> buildDummyList()
    {
        ArrayList<DrinkAdapterItem> bullshit = new ArrayList<>();
        DrinkAdapterItem shit;
        shit = new DrinkAdapterItem("Cosmo", "$7.00");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Dry Martini", "$8.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Rum and Coke", "$4.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Vodka Tonic", "$3.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Cosmo", "$7.00");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Dry Martini", "$8.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Rum and Coke", "$4.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Vodka Tonic", "$3.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Cosmo", "$7.00");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Dry Martini", "$8.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Rum and Coke", "$4.50");
        bullshit.add(shit);
        shit = new DrinkAdapterItem("Vodka Tonic", "$3.50");
        return bullshit;
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

        ArrayList<DrinkAdapterItem> myListData = buildDummyList();
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



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void onClickOrderScreen(View v)
    {
        if(v.getId() == R.id.orderButton)
        {
            //ErrorAlert e = new ErrorAlert(this);
            //Toast.makeText(this, "ordering Drink", Toast.LENGTH_SHORT).show();
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

        //TODO: ADD IN THE DRINK NAME TO THE STRING
        //TODO: ADD IN CHARGE TO YOUR ACCOUNT STRING
        alertDialog2.setMessage("Are you sure you want this drink?");

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
            Toast.makeText(this, "Drink Made", Toast.LENGTH_SHORT).show();
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

        //TODO: ADD IN THE DRINK NAME TO THE STRING
        //TODO: ADD IN CHARGE TO YOUR ACCOUNT STRING
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
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}
