package IntelliDrinkDB;

/**
 * Created by David on 2/8/2015.
 * This guy actually builds the drinklist.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.IntelliDrinkContainer;
import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkCore.GenericIngredient;
import IntelliDrinkCore.KioskConfiguration;
import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkCore.SlotItem;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "IntelliDrink";

    public int minSlotLevel = 7;

    // Table Names
    public static final String TABLE_DRINK_LIST = "Drink_List";
    public static final String TABLE_KIOSK_SLOTS = "Kiosk_Slots";
    public static final String TABLE_RECIPE_NEEDS = "Recipe_Needs";

    // Table Create Statements
    public static final String CREATE_TABLE_KIOSK_SLOTS = "CREATE TABLE "
            + TABLE_KIOSK_SLOTS + " ("
            + Constants.COL_ID + " INTEGER PRIMARY KEY, "
            + Constants.COL_SLOT_NUMBER + " INTEGER, "
            + Constants.COL_LITERAL_NAME + " TEXT, "
            + Constants.COL_INGREDIENT_ID + " INTEGER, "
            + Constants.COL_SLOT_LEVEL + " INTEGER, "
            + Constants.COL_SHOT_PRICE + " DOUBLE, "
            + Constants.COL_AVAILABLE + " BOOLEAN)";

    public static final String CREATE_TABLE_DRINK_LIST = "CREATE TABLE "
            + TABLE_DRINK_LIST + " ("
            + Constants.COL_ID + " INTEGER PRIMARY KEY, "
            + Constants.COL_RECIPE_ID + " INTEGER, "
            + Constants.COL_RECIPE_NAME + " TEXT, "
            + Constants.COL_PRICE + " DOUBLE, "
            + Constants.COL_DESCRIPTION + " TEXT, "
            + Constants.COL_AVAILABLE + " BOOLEAN)";

    public static final String CREATE_TABLE_RECIPE_NEEDS = "CREATE TABLE "
            + TABLE_RECIPE_NEEDS + " ("
            + Constants.COL_ID + " INTEGER PRIMARY KEY, "
            + Constants.COL_RECIPE_ID + " INTEGER, "
            + Constants.COL_INGREDIENT_ID + " TEXT, "
            + Constants.COL_UNITS + " INTEGER)";

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        resetTables();
        db.execSQL(CREATE_TABLE_DRINK_LIST);
        db.execSQL(CREATE_TABLE_RECIPE_NEEDS);
        db.execSQL(CREATE_TABLE_KIOSK_SLOTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_NEEDS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_KIOSK_SLOTS);
        // create new tables
        onCreate(db);
    }

    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_NEEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KIOSK_SLOTS);
        db.execSQL(CREATE_TABLE_DRINK_LIST);
        db.execSQL(CREATE_TABLE_RECIPE_NEEDS);
        db.execSQL(CREATE_TABLE_KIOSK_SLOTS);
    }

    /**
     * @param username
     * @param password Description:
     *                 Polls the server for what drinks it can make, what is in each slot, and sets up the internal database
     *                 <p/>
     *                 <p/>
     *                 Requires:
     *                 username: Kiosk name, e.g. Kiosk_1
     *                 password: the password
     */
    public void configureDatabase(String username, String password) {
        resetTables();
        SQLiteDatabase WRITE = this.getWritableDatabase();
        SQLiteDatabase READ = this.getReadableDatabase();
        String SQLString;
        resetTables();
        ServerDatabase SD = new ServerDatabase();
        ArrayList<KioskConfiguration> downloaded = SD.configureKioskDatabase(username, password);
        ArrayList<DrinkListItem> drinkList = new ArrayList<>();
        ArrayList<SlotItem> slotList = new ArrayList<>();
        ArrayList<Integer> recipeIDs = new ArrayList<>();
        ArrayList<Integer> slotNums = new ArrayList<>();
        DrinkListItem drink;
        SlotItem slot;
        for (int i = 0; i < downloaded.size(); i++) {
            String rName = downloaded.get(i).getRecipeName();
            String description = downloaded.get(i).getDescription();
            String LiteralName = downloaded.get(i).getLiteralName();
            int rID = downloaded.get(i).getRecipeID();
            int ingredientID = downloaded.get(i).getIngredientID();
            int units = downloaded.get(i).getUnits();
            int slotNumber = downloaded.get(i).getSlotNum();
            int SlotLevel = downloaded.get(i).getSlotLevel();
            double price = downloaded.get(i).getPrice();
            double shotPrice = downloaded.get(i).getShotPrice();
            SQLString = "";
            //The information returned from the Server is a 1-1 input for the RecipeNeeds table
            SQLString = "INSERT INTO " + TABLE_RECIPE_NEEDS + "("
                    + Constants.COL_RECIPE_ID + ", "
                    + Constants.COL_INGREDIENT_ID + ", "
                    + Constants.COL_UNITS + ") "
                    + "VALUES("
                    + rID + ", "
                    + ingredientID + ", "
                    + units +
                    ")";
            WRITE.execSQL(SQLString);

            if (!slotNums.contains(slotNumber)) {
                slotNums.add(slotNumber);
                slot = new SlotItem();
                slot.setLiteralName(LiteralName);
                slot.setSlotLevel(SlotLevel);
                slot.setIngredientID(ingredientID);
                slot.setSlotNumber(slotNumber);
                slot.setShotPrice(shotPrice);
                slot.setAvailable(true);
                slotList.add(slot);
            }
            if (!recipeIDs.contains(rID)) {
                recipeIDs.add(rID);
                drink = new DrinkListItem();
                drink.setAvailable(true);
                drink.setPrice(price);
                drink.setDescription(description);
                drink.setRecipeID(rID);
                drink.setRecipeName(rName);
                drinkList.add(drink);
            }
        }
        for (int i = 0; i < drinkList.size(); i++) {
            SQLString = "INSERT INTO " + TABLE_DRINK_LIST + " ("
                    + Constants.COL_RECIPE_ID + ", "
                    + Constants.COL_RECIPE_NAME + ", "
                    + Constants.COL_PRICE + ", "
                    + Constants.COL_DESCRIPTION + ", "
                    + Constants.COL_AVAILABLE + ") "
                    + "VALUES ( "
                    + drinkList.get(i).getRecipeID() + ", "
                    + "'" + drinkList.get(i).getRecipeName() + "', "
                    + drinkList.get(i).getPrice() + ", "
                    + "'" + drinkList.get(i).getDescription() + "', "
                    + "1)";
            WRITE.execSQL(SQLString);
        }

        ArrayList<SlotItem> attributes = SD.getSlotAttributes(username, password);

        for (int i = 0; i < attributes.size(); i++) {
            SQLString = "INSERT INTO " + TABLE_KIOSK_SLOTS + " ("
                    + Constants.COL_SLOT_NUMBER + ", "
                    + Constants.COL_LITERAL_NAME + ", "
                    + Constants.COL_INGREDIENT_ID + ", "
                    + Constants.COL_SLOT_LEVEL + ", "
                    + Constants.COL_SHOT_PRICE + ", "
                    + Constants.COL_AVAILABLE + ") "
                    + "VALUES ( "
                    + attributes.get(i).getSlotNumber() + ", "
                    + "'" + attributes.get(i).getLiteralName() + "', "
                    + attributes.get(i).getIngredientID() + ", "
                    + attributes.get(i).getSlotLevel() + ", "
                    + attributes.get(i).getShotPrice() + ", "
                    + "1)";
            WRITE.execSQL(SQLString);

            SQLString = "INSERT INTO " + TABLE_DRINK_LIST + " ("
                    + Constants.COL_RECIPE_ID + ", "
                    + Constants.COL_RECIPE_NAME + ", "
                    + Constants.COL_PRICE + ", "
                    + Constants.COL_DESCRIPTION + ", "
                    + Constants.COL_AVAILABLE + ") "
                    + "VALUES ( "
                    + "1, "
                    + "'A Shot of " + attributes.get(i).getLiteralName() + "', "
                    + attributes.get(i).getShotPrice() + ", "
                    + "' A standard 1.5 oz shot of " + attributes.get(i).getLiteralName() + "', "
                    + "1)";
            WRITE.execSQL(SQLString);

        }



        checkSlot();
    }


    /*

    ================================================================================================
        Check Methods
    ================================================================================================
    */

    /**
     * Description:
     * Checks the slot levels to see if they are high enough to produce drinks.
     * If not high enough, it will disable drinks
     * If the drinks are high enough and were disabled it will enable them.
     * <p/>
     * Requires:
     * n/a
     */
    public void checkSlot() {
        SQLiteDatabase READ = this.getReadableDatabase();
        SQLiteDatabase WRITE = this.getWritableDatabase();
        String SQL;
        SQL = "SELECT " + Constants.COL_SLOT_NUMBER + ", "
                + Constants.COL_SLOT_LEVEL + ", "
                + Constants.COL_INGREDIENT_ID + ", "
                + Constants.COL_AVAILABLE + " "
                + "FROM " + TABLE_KIOSK_SLOTS;
        Cursor point;
        point = READ.rawQuery(SQL, null);
        int slotNum = 0;
        int slotLevel = 0;
        int ingredientID = 0;
        boolean status = false;
        while (point.moveToNext()) {
            slotNum = point.getInt(point.getColumnIndex(Constants.COL_SLOT_NUMBER));
            slotLevel = point.getInt(point.getColumnIndex(Constants.COL_SLOT_LEVEL));
            ingredientID = point.getInt(point.getColumnIndex(Constants.COL_INGREDIENT_ID));
            status = point.getInt(point.getColumnIndex(Constants.COL_AVAILABLE)) > 0;
            if ((slotLevel <= minSlotLevel) && (status == true)) {
                //Running low on ingredients, set any recipe that uses it to unavailable

                //Kiosk Slot marked as unavailable
                SQL = "UPDATE " + TABLE_KIOSK_SLOTS
                        + " SET " + Constants.COL_AVAILABLE + " = 0"
                        + " WHERE " + Constants.COL_SLOT_NUMBER + " = " + slotNum;
                WRITE.execSQL(SQL);

                //Grab all of the RecipeIDs that use the unavailable ingredient
                SQL = "SELECT " + Constants.COL_INGREDIENT_ID + ", "
                        + Constants.COL_RECIPE_ID + " "
                        + "FROM " + TABLE_RECIPE_NEEDS + " "
                        + "WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
                Cursor inner = READ.rawQuery(SQL, null);

                // Set all of the Recipes that use the unavailable ingredient to unavailable
                while (inner.moveToNext()) {
                    SQL = "UPDATE " + TABLE_DRINK_LIST
                            + " SET " + Constants.COL_AVAILABLE + " = 0"
                            + " WHERE " + Constants.COL_RECIPE_ID + " = " + inner.getInt(inner.getColumnIndex(Constants.COL_RECIPE_ID));
                }
            } else if ((slotLevel > minSlotLevel) && (status == false)) {
                // An ingredient that has been marked as unavailable and has been refilled
                // Make the recipes that uses it to be available

                //Kiosk Slot marked as available
                SQL = "UPDATE " + TABLE_KIOSK_SLOTS
                        + " WHERE " + Constants.COL_SLOT_NUMBER + " = 1" + slotNum;
                WRITE.execSQL(SQL);

                //Grab all of the RecipeIDs that use the now available ingredient
                SQL = "SELECT " + Constants.COL_INGREDIENT_ID + ", "
                        + Constants.COL_RECIPE_ID + " "
                        + "FROM " + TABLE_RECIPE_NEEDS + " "
                        + "WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
                Cursor inner = READ.rawQuery(SQL, null);

                // Set all of the Recipes that use the now available ingredient to available
                while (inner.moveToNext()) {
                    SQL = "UPDATE " + TABLE_DRINK_LIST
                            + " SET " + Constants.COL_AVAILABLE + " = 1"
                            + " WHERE " + Constants.COL_RECIPE_ID + " = " + inner.getInt(inner.getColumnIndex(Constants.COL_RECIPE_ID));
                }
            }
        }
    }

    /*
    ================================================================================================
        Add Methods
    ================================================================================================
    */

    /*
    ================================================================================================
        Get Methods
    ================================================================================================
    */

    /**
     * @return Description:
     * Returns an array list of a java object, DrinkListItem.  It only returns drinks that are
     * marked as available.  (Has above the minimum level)
     * <p/>
     * Requires:
     * n/a
     */
    public ArrayList<DrinkListItem> getAvailableRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String MySQLString = "SELECT * FROM " + TABLE_DRINK_LIST + " "
                + "WHERE " + Constants.COL_AVAILABLE + " = 1";
        Log.d("getAvailableRecipes", MySQLString);
        Cursor point = db.rawQuery(MySQLString, null);
        ArrayList<DrinkListItem> recipeList = new ArrayList<>();
        DrinkListItem recipe;
        int i = 0;
        while (point.moveToNext()) {
            Log.d("Loop Checking", "penis x " + i);
            recipe = new DrinkListItem();
            recipe.setID(point.getInt(point.getColumnIndex(Constants.COL_ID)));
            recipe.setRecipeID(point.getInt(point.getColumnIndex(Constants.COL_RECIPE_ID)));
            recipe.setRecipeName(point.getString(point.getColumnIndex(Constants.COL_RECIPE_NAME)));
            recipe.setPrice(point.getDouble(point.getColumnIndex(Constants.COL_PRICE)));
            recipe.setDescription(point.getString(point.getColumnIndex(Constants.COL_DESCRIPTION)));
            recipe.setAvailable(true);
            recipeList.add(recipe);
        }
        if (recipeList.size() == 0)
            Log.d(this.toString(), "recipelist is returning as a size 0, something is wrong");
        return recipeList;
    }

    /**
     * @return Description:
     * Returns the size of the available recipes it can make.  (Based upon minimum slot level)
     * <p/>
     * Requires:
     * n/a
     */
    public int getSizeOfAvailableRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String MySQLString = "SELECT COUNT (ID) FROM " + TABLE_DRINK_LIST + " "
                + "WHERE " + Constants.COL_AVAILABLE + " = 1";
        Cursor sCount = db.rawQuery(MySQLString, null);
        sCount.moveToFirst();
        return sCount.getInt(0);
    }

    /**
     * @param ID
     * @return Description:
     * Returns the string to make the recipe to send to the arduino.
     * e.g.
     * 2 shots from slot 1, 3 shots from slot 2, 3 shots from slot 3, and 1 shot from slot 8
     * Returns a format of 112223338
     * <p/>
     * Requires:
     * ID:
     * The RecipeID of the recipe
     */
    public String getArduinoCode(int ID) {
        Log.d("ARDUINO ID", "" + ID);


        String codeToReturn = "";
        int ingredientID;
        int units;
        int slotNumber = 0;
        int slotLevel = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        //This SQL Statement selects all of the needed ingredients needed in to make a drink
        String MySQLString = "SELECT * FROM " + TABLE_RECIPE_NEEDS + " WHERE " + Constants.COL_RECIPE_ID + "=" + ID;
        Cursor recipeNeedsPointer;
        Cursor kioskTablePointer;
        recipeNeedsPointer = db.rawQuery(MySQLString, null);

        while (recipeNeedsPointer.moveToNext()) {
            Log.d("TACO", "TUESDAYS");
            ingredientID = recipeNeedsPointer.getInt(recipeNeedsPointer.getColumnIndex(Constants.COL_INGREDIENT_ID));
            Log.d("ingredientID", "" + ingredientID);
            units = recipeNeedsPointer.getInt(recipeNeedsPointer.getColumnIndex(Constants.COL_UNITS));
            Log.d("Units" , String.valueOf(units));
            //This SQL Statement matches the correct Ingredient from the Recipe Needs table to the correct slot in the Kiosk Slot Table
            MySQLString = "SELECT * FROM " + TABLE_KIOSK_SLOTS + " WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
            kioskTablePointer = db.rawQuery(MySQLString, null);
            slotNumber = 0;
            slotLevel = 0;
// There are more than 1 match for the Ingredient ID, take the one with the highest SlotLevel
                //kioskTablePointer.moveToFirst();
                while (kioskTablePointer.moveToNext()) {
                    MySQLString = "SELECT * FROM " + TABLE_KIOSK_SLOTS + " WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
                    Log.d("Table Kiosk" , MySQLString);
                    Cursor kioskTablePointer2 = db.rawQuery(MySQLString, null);
                    Log.d("KioskTablePointer2", "Row count: "+kioskTablePointer.getCount());
                    while (kioskTablePointer2.moveToNext()) {
                        int tempSlotNum = kioskTablePointer2.getInt(kioskTablePointer2.getColumnIndex(Constants.COL_SLOT_NUMBER));
                        Log.d("Slot Number", String.valueOf(tempSlotNum));
                        int tempSlotLevel = kioskTablePointer2.getInt(kioskTablePointer2.getColumnIndex(Constants.COL_SLOT_LEVEL));
                        Log.d(" tempSlotLevel", String.valueOf(tempSlotLevel));
                        if (tempSlotLevel >= slotLevel) {
                            Log.d("slotNumber check" , ""+slotNumber);
                            slotNumber = tempSlotNum;
                            slotLevel = tempSlotLevel;
                        }
                    }
                }

            for (int i = units; i > 0; i--) {
                Log.d("Code Appendage", ""+slotNumber);
                codeToReturn += "" + slotNumber;
            }
            kioskTablePointer.close();
        }
        recipeNeedsPointer.close();
        if (codeToReturn.length() == 0) {
            Log.d(this.toString(), "Arduino Code is returning with nothing");
        }
        Log.d("Arduino Code returned: ", codeToReturn);
        return codeToReturn;
    }

    /**
     * @param ID
     * @return Description:
     * Returns the string to make the recipe to send to the arduino.
     * e.g.
     * 2 shots from slot 1, 3 shots from slot 2, 3 shots from slot 3, and 1 shot from slot 8
     * Returns a format of 112223338
     * <p/>
     * Requires:
     * ID:
     * The RecipeID of the recipe
     */
    public ArrayList<String> getDrinkIngredients(int ID) {
        Log.d("Drink ID getDrinks", String.valueOf(ID));
        ArrayList<String> ingredientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int litID = 0;
        //This SQL Statement selects all of the needed ingredients needed in to make a drink
        String MySQLString = "SELECT " + Constants.COL_INGREDIENT_ID + " FROM " + TABLE_RECIPE_NEEDS + " WHERE " + Constants.COL_RECIPE_ID + " = " + ID;
        String SQLString;
        Cursor recipeNeedsPointer = db.rawQuery(MySQLString, null);
        Cursor litPointer;
        while (recipeNeedsPointer.moveToNext()) {
            litID = recipeNeedsPointer.getInt(recipeNeedsPointer.getColumnIndex(Constants.COL_INGREDIENT_ID));
            SQLString = "SELECT " + Constants.COL_LITERAL_NAME + " FROM " + TABLE_KIOSK_SLOTS + " WHERE " + Constants.COL_INGREDIENT_ID + " = " + litID;
            litPointer = db.rawQuery(SQLString, null);
            while (litPointer.moveToNext()) {
                ingredientList.add(litPointer.getString(litPointer.getColumnIndex(Constants.COL_LITERAL_NAME)));
                Log.d("Ingredient Name", ingredientList.get(0));
            }
        }
        if (ingredientList.size() == 0) {
            Log.d(this.toString(), "Ingredients size == 0, something is wrong");
        }
        return ingredientList;
    }

    /*
    ================================================================================================
        Set Methods
    ================================================================================================
    */

    /**
     * @param units
     * @param slotNumber Description:
     *                   Updates the local databases slot levels.
     *                   <p/>
     *                   Requires:
     *                   units - how many units it takes to make a recipe
     *                   slotNumber - the slot number to update
     */
    public void setSlotLevel(int units, int slotNumber) {
        SQLiteDatabase WRITE = this.getWritableDatabase();

        String SQL = "UPDATE " + TABLE_KIOSK_SLOTS + " "
                + "SET " + Constants.COL_SLOT_LEVEL + " = " + Constants.COL_SLOT_LEVEL + " - " + units + " "
                + "WHERE " + Constants.COL_SLOT_NUMBER + " = " + slotNumber;

        WRITE.execSQL(SQL);
    }

    public ArrayList<SlotItem> getSlotItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        SlotItem slot;
        ArrayList<SlotItem> arrayOfSlots = new ArrayList<>();
        String SQL;
        SQL = "SELECT * FROM " + TABLE_KIOSK_SLOTS;


        Cursor slotItemCursor = db.rawQuery(SQL, null);
        while (slotItemCursor.moveToNext()) {
            slot = new SlotItem();

            slot.setID(slotItemCursor.getInt(slotItemCursor.getColumnIndex(Constants.COL_ID)));
            slot.setSlotNumber(slotItemCursor.getInt(slotItemCursor.getColumnIndex(Constants.COL_SLOT_NUMBER)));
            slot.setLiteralName(slotItemCursor.getString(slotItemCursor.getColumnIndex(Constants.COL_LITERAL_NAME)));
            slot.setIngredientID(slotItemCursor.getInt(slotItemCursor.getColumnIndex(Constants.COL_INGREDIENT_ID)));
            slot.setSlotLevel(slotItemCursor.getInt(slotItemCursor.getColumnIndex(Constants.COL_SLOT_LEVEL)));
            slot.setShotPrice(slotItemCursor.getDouble(slotItemCursor.getColumnIndex(Constants.COL_SHOT_PRICE)));

            int taco = slotItemCursor.getInt(slotItemCursor.getColumnIndex(Constants.COL_RECIPE_NAME));
            if (taco < 1) {
                slot.setAvailable(false);
            } else {
                slot.setAvailable(true);
            }

            arrayOfSlots.add(slot);
        }

        return arrayOfSlots;
    }

}


