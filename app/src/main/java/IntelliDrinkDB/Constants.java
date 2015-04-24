package IntelliDrinkDB;

/**
 * Created by David on 3/23/2015.
 */
public class Constants {
    protected static final String PREFERENCE_FILE = "com.intellidrink.my_sql_2.app";
    protected static final String KEY_KIOSK_NAME = "KIOSK_NAME";
    protected static final String KEY_KIOSK_PASSWORD = "KIOSK_PASSWORD";

    //URLS
    protected static final String BASE_URL = "http://192.168.1.10/IntelliDrink/";
    protected static final String URL_SLOT_LEVEL = "slotLevel.php";
    protected static final String URL_CUSTOMER_TRANSACTIONS = "customerTransactions.php";
    protected static final String URL_GET_SLOT_ATTRIBUTES = "getSlotAttributes.php";
    protected static final String URL_GET_CUSTOMERS = "getCustomers.php";
    protected static final String URL_CUSTOMER_INFO = "customer_info.php";
    protected static final String URL_CONFIGURE_KIOSK = "configure_kiosk_database.php";
    protected static final String URL_CHECK_OUT = "checkOut.php";
    protected static final String URL_DELETE_TRANSACTION = "deleteTransaction.php";
    protected static final String URL_EDIT_RFID = "EditRFID.php";
    protected static final String URL_GET_TRANSACTION_HISTORY = "getTransactionHistory.php";
    protected static final String URL_GET_CUSTOMER_ID = "getCustomerID.php";
    protected static final String URL_GET_LITERAL_INGREDIENTS = "getLiteralIngredients.php";
    protected static final String URL_GET_CUSTOMER_GENERIC_INGREDIENTS = "getGenericIngredients.php";
    protected static final String URL_SET_SLOT_ATTRIBUTE = "setSlotAttributes.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_CUSTOM = "Custom";
    protected static final String TAG_USERNAME = "username";
    protected static final String TAG_PASSWORD = "password";
    protected static final String TAG_INGREDIENT = "Ingredient";

    public static final String COL_AVAILABLE = "Available";

    // Common Table Column Names
    protected static final String COL_ID = "ID";
    protected static final String COL_CUSTOMER_NAME = "CustomerName";
    protected static final String COL_CUSTOMER_DOB = "CustomerDOB";
    protected static final String COL_CUSTOMER_RFID = "CustomerRFID";
    protected static final String COL_RECIPE_ID = "RecipeID";
    protected static final String COL_INGREDIENT_ID = "IngredientID";
    protected static final String COL_PRICE = "Price";
    protected static final String COL_DT_PURCHASED = "DT_Purchased";
    protected static final String COL_LITERAL_NAME = "LiteralName";


    // Table ActiveTransactions
    protected static final String TABLE_ACTIVE_TRANSACTIONS = "ActiveTransactions";
    protected static final String COL_CUSTOMER_ID = "CustomerID";

    //Table RecipeNeeds
    protected static final String TABLE_RECIPE_NEEDS = "RecipeNeeds";
    protected static final String COL_UNITS = "Units";

    //Table Metrics
    protected static final String TABLE_METRICS = "Metrics";
    protected static final String COL_Number = "Number";

    // Table Recipes
    protected static final String TABLE_RECIPES = "Recipes";
    protected static final String COL_RECIPE_NAME = "RecipeName";
    protected static final String COL_DESCRIPTION = "Description";

    //Table Customers
    protected static final String TABLE_CUSTOMERS = "Customers";
    protected static final String COL_EMAIL = "EMAIL";
    protected static final String COL_LAST_VISIT = "LAST_VISIT";
    protected static final String COL_BALANCE = "BALANCE";
    protected static final String COL_COOL_DOWN = "CoolDown";

    //Table GIngredient
    protected static final String TABLE_ACTIVE_GENERIC_INGREDIENT = "GIngredient";
    protected static final String COL_GENERIC_NAME = "GenericName";
    protected static final String COL_CUSTOM = "Custom";

    //Table LIngredient
    protected static final String TABLE_LITERAL_INGREDIENT = "LIngredient";
    protected static final String COL_GENERIC_ID_NUMBER = "GenericIDNumber";
    protected static final String COL_SHOT_PRICE = "ShotPrice";

    //Table Kiosks
    protected static final String TABLE_KIOSKS = "Kiosks";
    protected static final String COL_SLOT_NUMBER = "SlotNum";
    protected static final String COL_SLOT_LEVEL = "SlotLevel";

}
