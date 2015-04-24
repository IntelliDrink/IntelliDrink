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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;


public class CashOutActivity extends ActionBarActivity {


    private static String ADMIN_USERNAME = "Admin";
    private static String ADMIN_PASSWORD = "12345678";

    Button createNewCustomerButton;
    Button cashOutCustomerButton;
    Button editCustomersTabButton;
    Button editRFIDButton;
    Button editCustomersDetailsButton;
    Button removeCustomerButton;
    Button toggleActiveCustomersButton;
    Button backButton;
    Button orderAsAdminButton;
    Button editPricingButton;

    Button searchButton;
    EditText searchEditText;

    ListView customerListView;

    TextView customerNameTextEdit;
    TextView customersRFIDTextEdit;
    TextView customersCurrentTabTextEdit;

    ListView tabListView;

    boolean adminMode;


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

        setContentView(R.layout.activity_cash_out);

        createNewCustomerButton = (Button) findViewById(R.id.createNewCustomerButton);
        cashOutCustomerButton = (Button) findViewById(R.id.cashOutCustomerButton);
        editCustomersDetailsButton = (Button) findViewById(R.id.editDetailsButton);
        editCustomersTabButton = (Button) findViewById(R.id.editTabButton);
        editRFIDButton = (Button) findViewById(R.id.editRFIDButton);
        removeCustomerButton = (Button) findViewById(R.id.removeCustomerButton);
        orderAsAdminButton = (Button) findViewById(R.id.orderAsAdminButton);
        editPricingButton = (Button) findViewById(R.id.editPricingButton);
        toggleActiveCustomersButton = (Button) findViewById(R.id.toggleActiveCustomersButton);

        backButton = (Button) findViewById(R.id.backButtonCashOutActivity);

        searchButton = (Button) findViewById(R.id.searchForNameButton);
        searchEditText = (EditText) findViewById(R.id.searchForNameEditText);

        customerListView = (ListView) findViewById(R.id.customerListView);
        loadCustomerListViewAdapter();



        customerNameTextEdit = (TextView) findViewById(R.id.customerNameTextEdit);
        customersRFIDTextEdit = (TextView) findViewById(R.id.customersRFIDTextEdit);
        customersCurrentTabTextEdit = (TextView) findViewById(R.id.customersCurrentTabTextEdit);

        tabListView = (ListView) findViewById(R.id.tabListViewCashOutActivity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    void loadCustomerListViewAdapter()
    {
        ArrayAdapter<String> tmpAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cash_out, menu);
        return true;
    }

    public void onClickCashOutActivity(View v)
    {
        if(v.getId() == R.id.createNewCustomerButton)
        {

        }
        else if(v.getId() == R.id.cashOutCustomerButton)
        {

        }
        else if(v.getId() == R.id.editDetailsButton)
        {

        }
        else if(v.getId() == R.id.editTabButton)
        {

        }
        else if(v.getId() == R.id.editRFIDButton)
        {

        }
        else if(v.getId() == R.id.removeCustomerButton)
        {

        }
        else if(v.getId() == R.id.toggleActiveCustomersButton)
        {

        }
        else if(v.getId() == R.id.editPricingButton)
        {

        }
        else if(v.getId() == R.id.orderAsAdminButton)
        {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    CashOutActivity.this);

            alertDialog2.setTitle("Admin Mode");
            alertDialog2.setMessage("Would you like to enter admin mode?  This allows you " +
                    "to order drinks without going through the server");
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), MainScreen.class);
                            startActivity(i);
                            finish();
                            dialog.cancel();
                        }
                    });
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog2.show();

        }
        else if(v.getId() == R.id.backButtonCashOutActivity)
        {

            Intent i = new Intent(this, AdminPanel.class);
            Bundle b = new Bundle();
            b.putBoolean("Admin Mode" , this.adminMode);
            i.putExtras(b);
            startActivity(i);
            finish();
        }

        else
        {
            Toast.makeText(this, "Bad button press somewhere", Toast.LENGTH_SHORT);
        }

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
