package IntelliDrinkDB;

import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import IntelliDrinkCore.CustomerInformation;
import IntelliDrinkCore.GenericIngredient;
import IntelliDrinkCore.KioskConfiguration;
import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkCore.SlotItem;
import IntelliDrinkCore.Transaction;

/**
 * Created by David on 3/26/2015.
 */
public class ServerDatabase extends Constants {
    JSONParser jParser;

    //TODO SET USERNAME / PASSWORD IN CONSTRUCTOR
    public ServerDatabase(){

        jParser = new JSONParser();
        Log.d(this.getClass().toString(), "ServerDatabase Declared");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     *
     * @param passedinParams
     * @param passedInURL
     * @return
     *
     * Generic call to the database allows for a URL to be passed in
     */
    public JSONArray execute(List<NameValuePair> passedinParams, String passedInURL){
        JSONArray jArray = null;
        JSONObject json = jParser.makeHttpRequest(passedInURL, "GET", passedinParams);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jArray;
    }

    /**
     * @param username
     * @param password
     * @param RFID
     * @return
     *
     *		Returns the ID number of the customer based on
     *		what is passed in.
     *
     * Requires: Username, password, RFID
     * Returns: Customer's ID
     */
    public int getCustomerIDByRFID(String username, String password, String RFID){
        int customerID = 0;
        String URL = BASE_URL + URL_GET_CUSTOMER_ID;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_CUSTOMER_RFID, RFID));
        JSONArray jArray = null;
        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                // Storing each json item in variable
                customerID = Integer.parseInt(c.getString(COL_ID));
          }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerID;
    }

    /**
     * @param username
     * @param password
     * @param EMAIL
     * @return
     *
     *		Returns the ID number of the customer based on
     *		what is passed in.
     *
     * Requires: Username, password, EMAIL
     * Returns: Customer's ID
     */
    public int getCustomerIDByEMAIL(String username, String password, String EMAIL){
        int customerID = 0;
        String URL = BASE_URL + URL_GET_CUSTOMER_ID;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_EMAIL, EMAIL));
        JSONArray jArray = null;
        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                // Storing each json item in variable
                customerID = Integer.parseInt(c.getString(COL_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerID;
    }

    /**
     * @param username
     * @param password
     * @param ID
     * @return
     *
     * Performs the necessary operations to close out a tab.
     * Requires: Username, password, ID
     * Returns: Customer's balance
     */
    public double checkOut(String username, String password, int ID){
        double balance = -1.00;
        String URL = BASE_URL + URL_CHECK_OUT;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_ID, "" + ID));

        JSONArray jArray = null;
        ArrayList<HashMap<String, String>> responseList = null;
        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                // Storing each json item in variable
                balance = Float.parseFloat(c.getString(COL_RECIPE_ID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     *
     *		Query the database to get the correct drink listing based on what
     *		is currently available in the Kiosk's Ingredient Slots.
     *		The tablet uses this information to populate its local database.
     *
     * Requires: username, password
     * Returns :
     *      |  RecipeName  |  Description  |  RecipeID  |  IngredientID  |  Units  |  SlotNum  |  SlotLevel  |  LiteralName  |
     *      String			String			Integer		 Integer		  Integer   Integer     Integer       String
     *
     */
    public ArrayList<KioskConfiguration> configureKioskDatabase(String username, String password){
        String URL = BASE_URL + URL_CONFIGURE_KIOSK;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));

        JSONArray jArray = null;

        KioskConfiguration config = new KioskConfiguration();
        ArrayList<KioskConfiguration> list = new ArrayList<>();

        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                config = new KioskConfiguration();
                config.setLiteralName(c.getString(COL_LITERAL_NAME));
                config.setPrice(c.getDouble(COL_PRICE));
                config.setRecipeName(c.getString(COL_RECIPE_NAME));
                config.setDescription(c.getString(COL_DESCRIPTION));
                config.setIngredientID(c.getInt(COL_INGREDIENT_ID));
                config.setRecipeID(c.getInt(COL_RECIPE_ID));
                config.setSlotLevel(c.getInt(COL_SLOT_LEVEL));
                config.setSlotNum(c.getInt(COL_SLOT_NUMBER));
                config.setUnits(c.getInt(COL_UNITS));
                config.setShotPrice(c.getDouble(COL_SHOT_PRICE));
                list.add(config);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @param username
     * @param password
     * @param customerRFID
     * @return
     *
     *		Querys the server database for the Customer's information.
     *		It passes back if a cool down is in effect, Customer's balance,
     *
     * Requires: username, password, CustomerRFID
     * Returns :
     * 	|  ID  |  CustomerName  |  BALANCE  |  CoolDown  |  CustomerRFID
     *   Integer	String			Double		Boolean (int) String
     */
    public CustomerInformation cusomterInfo(String username, String password, String customerRFID){
        String URL = BASE_URL + URL_CUSTOMER_INFO;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_CUSTOMER_RFID, customerRFID));

        JSONArray jArray = null;
        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        CustomerInformation customerInfo = new CustomerInformation();
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                Log.d("CUSTOMER INFO CHECK", "CUSTOMER INFO CHECK");
                Log.d("CUSTOMER INFO CHECK", "CUSTOMER INFO CHECK");
                Log.d("CUSTOMER INFO CHECK", "CUSTOMER INFO CHECK");
                customerInfo.setID(c.getInt(COL_ID));
                customerInfo.setBalance(c.getDouble(COL_BALANCE));
                Log.d("Customer Balance Check" , String.valueOf(customerInfo.getBalance()));
                customerInfo.setCustomerName(c.getString(COL_CUSTOMER_NAME));
                customerInfo.setCustomerRFID(c.getString(COL_CUSTOMER_RFID));
                //TODO FIX THIS
                if(c.getInt(COL_COOL_DOWN) == 0)
                    customerInfo.setCoolDown(false);
                else
                    customerInfo.setCoolDown(true);
                //TODO THIS GIVES AN ERROR
                //customerInfo.setCoolDown(c.getBoolean(COL_COOL_DOWN));
                //ERROR: org.json.JSONException: Value 0 at CoolDown of type java.lang.String cannot be converted to boolean
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerInfo;
    }

    /**
     *
     * @param username
     * @param password
     * @param CustomerID
     * @return
     *
     * Requires: username, password, CustomerID
     * Returns :
     * 		|  ID    |  Price  |  RecipeName  |
     *      Integer   Double	   String
     */
    public ArrayList<Transaction> getTransactionHistory(String username, String password, int ID){
        String URL = BASE_URL + URL_GET_TRANSACTION_HISTORY;
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        //params.add(new BasicNameValuePair(COL_CUSTOMER_ID, "" + CustomerID));
        params.add(new BasicNameValuePair(COL_ID, "" + ID));


        Transaction item = new Transaction();
       ArrayList<Transaction> history = new ArrayList<>();
        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                item = new Transaction();
                item.setID(c.getInt(COL_ID));
                item.setPrice(c.getDouble(COL_PRICE));
                item.setRecipeName(c.getString(COL_RECIPE_NAME));
                history.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return history;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     *
     * Returns Literal Ingredients from the LIngredient table
     * Requires: username, password, CustomerRFID, ID
     * Returns :
     * 		|  ID  |  LiteralName  |
     *      Integer String
     */
    public ArrayList<LiteralIngredient> getLiteralIngredients(String username, String password){
        String URL = new String(BASE_URL + URL_GET_LITERAL_INGREDIENTS);
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));

        LiteralIngredient literal = new LiteralIngredient();
        ArrayList<LiteralIngredient> list = new ArrayList<>();


        JSONObject json = new JSONObject();
        json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                literal = new LiteralIngredient();
                literal.setID(c.getInt(COL_ID));
                literal.setLiteralName(c.getString(COL_LITERAL_NAME));
                literal.setGenericIDNumber(c.getInt(COL_GENERIC_ID_NUMBER));
                list.add(literal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param username
     * @param password
     * @param GenericIDNumber
     * @return
     *
     * Returns Literal Ingredients from the LIngredient table
     * Requires: username, password, GenericIDNumber
     * Returns :
     * |  ID  |  LiteralName  |
     * Integer String
     */
    public ArrayList<LiteralIngredient> getLiteralIngredients(String username, String password, int GenericIDNumber){
        String URL = BASE_URL + URL_GET_LITERAL_INGREDIENTS;
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_GENERIC_ID_NUMBER, "" + GenericIDNumber));

        ArrayList<HashMap<String, String>> responseList = null;

        LiteralIngredient literal = new LiteralIngredient();
        ArrayList<LiteralIngredient> list = new ArrayList<>();

        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                literal = new LiteralIngredient();
                literal.setID(c.getInt(COL_ID));
                literal.setLiteralName(c.getString(COL_LITERAL_NAME));
                literal.setGenericIDNumber(c.getInt(COL_GENERIC_ID_NUMBER));
                literal.setGenericIDNumber(c.getInt(COL_GENERIC_ID_NUMBER));
                list.add(literal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @param username
     * @param password
     * @return
     * Returns all of the generic ingredients
     * Requires: username, password
     * Returns :
     * |  ID  |  GenericName  |
     * Integer  String
     */
    public  ArrayList<GenericIngredient> getGenericIngredients(String username, String password){
        String URL = BASE_URL + URL_GET_LITERAL_INGREDIENTS;
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));

        GenericIngredient generic = new GenericIngredient();
        ArrayList<GenericIngredient> list = new ArrayList<>();

        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                generic = new GenericIngredient();
                generic.setID(c.getInt(COL_ID));
                generic.setGenericName(COL_GENERIC_NAME);
                list.add(generic);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     *
     * @param username
     * @param password
     *
     * Returns the Kiosk Attributes (What is in the Kiosk)
     *
     * Requires: username, password
     * Returns :
     * |  ID  |  LiteralName  |  Ingredient  |  SlotNum  |  SlotLevel  |
     * Integer  String   	 Integer    	Integer     Integer
     */
    public ArrayList<SlotItem> getSlotAttributes(String username, String password){
        String URL = BASE_URL + URL_GET_SLOT_ATTRIBUTES;
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;
        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        jParser.makeHttpRequest(URL, "GET", params);
        SlotItem slot;
        ArrayList<SlotItem> list = new ArrayList<>();

        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                slot = new SlotItem();
                slot.setID(c.getInt(COL_ID));
                slot.setLiteralName(c.getString(COL_LITERAL_NAME));
                slot.setSlotNumber(c.getInt(COL_SLOT_NUMBER));
                slot.setIngredientID(c.getInt(COL_INGREDIENT_ID));
                slot.setSlotLevel(c.getInt(COL_SLOT_LEVEL));
                list.add(slot);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param username
     * @param password
     *
     * Returns the Kiosk Attributes (What is in the Kiosk)
     *
     * Requires: username, password
     * Returns :
     * |  CustomerID  |  CustomerName  |  CustomerBalance  |  CustomerCoolDown  |  CustomerRFID  |
     *     Integer          String   	    Double    	        Boolean                 String
     */
    public ArrayList<CustomerInformation> getCustomers(String username, String password){
        String URL = BASE_URL + URL_GET_CUSTOMERS;
        JSONArray jArray = null;
        ArrayList<NameValuePair> params = new ArrayList<>() ;
        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        jParser.makeHttpRequest(URL, "GET", params);
        CustomerInformation customer;
        ArrayList<CustomerInformation> list = new ArrayList<>();

        JSONObject json = jParser.makeHttpRequest(URL, "GET", params);
        try {
            jArray = json.getJSONArray(TAG_CUSTOM);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);
                customer = new CustomerInformation();
                customer.setID(c.getInt(COL_ID));
                customer.setBalance(c.getDouble(COL_BALANCE));
                if(c.getInt(COL_COOL_DOWN) < 1){
                    customer.setCoolDown(false);
                } else{
                    customer.setCoolDown(true);
                }
                customer.setCustomerName(c.getString(COL_CUSTOMER_NAME));
                customer.setCustomerRFID(c.getString(COL_CUSTOMER_RFID));
                list.add(customer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     *
     * @param username
     * @param password
     * @param CustomerID
     * @param RecipeID
     * @param Number
     * @param Price
     * @return
     *
     *		Update the tables: Metric, ActiveTransactions, Customers
     *		Metric - Add in the Recipe ordered, how many, the date time stamp and the price
     *		ActiveTransactions - Add in the CustomerID, RecipeID, date time stamp and the price
     *		Customers - Update the Customer Balance
     *
     * Requires: username, password, CustomerID, RecipeID, Number, Price
     * Returns : none
     */
    public void customerTransaction(String username, String password, int CustomerID, int RecipeID, int Number, double Price){
        String URL = BASE_URL + URL_CUSTOMER_TRANSACTIONS;
        ArrayList<NameValuePair> params = new ArrayList<>() ;
        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_CUSTOMER_ID, "" + CustomerID));
        params.add(new BasicNameValuePair(COL_RECIPE_ID, "" + RecipeID));
        params.add(new BasicNameValuePair("Number", "" + Number));
        params.add(new BasicNameValuePair("Price", "" + Price));
        jParser.makeHttpRequest(URL, "GET", params);
    }

    /**
     *
     * @param username
     * @param password
     * @param SlotNum
     * @param RecipeID
     * @param Units
     * @return
     *
     *		Update the tables: KioskAttributes
     *		Update how much is in the Kiosk's particular slot
     *
     * Requires: username, password, SlotNum, RecipeID, Units
     * Returns : none
     */
    public void slotLevel(String username, String password, int SlotNum, int RecipeID, int Units){
        String URL = BASE_URL + URL_SLOT_LEVEL;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_SLOT_NUMBER, "" + SlotNum));
        params.add(new BasicNameValuePair(COL_RECIPE_ID, "" +  RecipeID));
        params.add(new BasicNameValuePair(COL_UNITS, "" +  Units));

        jParser.makeHttpRequest(URL, "GET", params);
    }

    /**
     *
     * @param username
     * @param password
     * @return
     *
     *		Deletes a transaction.
     *
     * Requires: username, password, ID
     * Returns :
     * 		|  ID  |  CustomerName  |  Price  |  RecipeName  |
     *      Integer     String	       Double	   String
     */
    public void deleteTransaction(String username, String password, int transactionID){
        String URL = BASE_URL + URL_DELETE_TRANSACTION;
        ArrayList<NameValuePair> params = new ArrayList<>() ;

        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_ID, "" + transactionID));

        jParser.makeHttpRequest(URL, "GET", params);
    }

    /**
     *
     * @param username
     * @param password
     * @param CustomerRFID
     * @param ID
     * @return
     *
     *		Updates the User's RFID code
     *
     * Requires: username, password, CustomerRFID, ID
     * Returns : none
     */
    public void EditRFID(String username, String password, String CustomerRFID, int ID){
        String URL = BASE_URL + URL_EDIT_RFID;
        ArrayList<NameValuePair> params = new ArrayList<>() ;
        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(COL_CUSTOMER_RFID, CustomerRFID));
        params.add(new BasicNameValuePair(COL_ID, "" + ID));
        jParser.makeHttpRequest(URL, "GET", params);
    }

    /**
     *
     * @param username
     * @param password
     * @param Ingredient
     * @param SlotNum
     * @param SlotLevel
     *
     * Either updates or inserts a new record into the Server Database
     *
     * Requires: username, password, Ingredient, SlotNum, SlotLevel
     * Returns : none
     */
    public void setSlotIngredient(String username, String password, int Ingredient, int SlotNum, int SlotLevel){
        String URL = BASE_URL + URL_SET_SLOT_ATTRIBUTE;
        ArrayList<NameValuePair> params = new ArrayList<>() ;
        params.add(new BasicNameValuePair(TAG_USERNAME, username));
        params.add(new BasicNameValuePair(TAG_PASSWORD, password));
        params.add(new BasicNameValuePair(TAG_INGREDIENT, "" + Ingredient));
        params.add(new BasicNameValuePair(COL_SLOT_NUMBER, "" + SlotNum));
        params.add(new BasicNameValuePair(COL_SLOT_LEVEL, "" + SlotLevel));
        jParser.makeHttpRequest(URL, "GET", params);
    }

}
