package IntelliDrinkDB;

/**
 * Created by David on 2/8/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
            + Constants.COL_INGREDIENT_ID + "INTEGER, "
            + Constants.COL_SLOT_LEVEL + "INTEGER, "
            + Constants.COL_SHOT_PRICE + "DOUBLE, "
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
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_DRINK_LIST);
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
        SQLiteDatabase db = this.getWritableDatabase();
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
            SQLString = "INSERT INTO " + TABLE_RECIPE_NEEDS + " ("
                    + Constants.COL_RECIPE_ID + ", "
                    + Constants.COL_INGREDIENT_ID + ", "
                    + Constants.COL_UNITS + ", "
                    + Constants.COL_AVAILABLE + ") "
                    + "VALUES ( "
                    + rID + ", "
                    + ingredientID + ", "
                    + units + ")";

            db.execSQL(SQLString);

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
        for (int i = 0; i < slotList.size(); i++) {
            SQLString = "INSERT INTO " + TABLE_KIOSK_SLOTS + " ("
                    + Constants.COL_SLOT_NUMBER + ", "
                    + Constants.COL_LITERAL_NAME + ", "
                    + Constants.COL_INGREDIENT_ID + ", "
                    + Constants.COL_SLOT_LEVEL + ", "
                    + Constants.COL_SHOT_PRICE + ", "
                    + Constants.COL_AVAILABLE + ") "
                    + "VALUES ( "
                    + slotList.get(i).getSlotNumber() + ", "
                    + "'" + slotList.get(i).getLiteralName() + "', "
                    + slotList.get(i).getIngredientID() + ", "
                    + slotList.get(i).getSlotLevel() + ", "
                    + slotList.get(i).getShotPrice() + ", "
                    + "true)";
            db.execSQL(SQLString);
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
                    + "true)";
            db.execSQL(SQLString);
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
                        + " SET " + Constants.COL_AVAILABLE + " = FALSE"
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
                            + " SET " + Constants.COL_AVAILABLE + " = FALSE"
                            + " WHERE " + Constants.COL_RECIPE_ID + " = " + inner.getInt(inner.getColumnIndex(Constants.COL_RECIPE_ID));
                }
            } else if ((slotLevel > minSlotLevel) && (status == false)) {
                // An ingredient that has been marked as unavailable and has been refilled
                // Make the recipes that uses it to be available

                //Kiosk Slot marked as available
                SQL = "UPDATE " + TABLE_KIOSK_SLOTS
                        + " SET " + Constants.COL_AVAILABLE + " = TRUE"
                        + " WHERE " + Constants.COL_SLOT_NUMBER + " = " + slotNum;
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
                            + " SET " + Constants.COL_AVAILABLE + " = TRUE"
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
    protected ArrayList<DrinkListItem> getAvailableRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String MySQLString = "SELECT * FROM " + TABLE_DRINK_LIST + " "
                + "WHERE " + Constants.COL_AVAILABLE + " = TRUE";
        Cursor point = db.rawQuery(MySQLString, null);
        ArrayList<DrinkListItem> recipeList = new ArrayList<>();
        DrinkListItem recipe = null;
        while (point.moveToNext()) {
            recipe = new DrinkListItem();
            recipe.setID(point.getInt(point.getColumnIndex(Constants.COL_ID)));
            recipe.setRecipeID(point.getInt(point.getColumnIndex(Constants.COL_RECIPE_ID)));
            recipe.setRecipeName(point.getString(point.getColumnIndex(Constants.COL_RECIPE_NAME)));
            recipe.setPrice(point.getDouble(point.getColumnIndex(Constants.COL_PRICE)));
            recipe.setDescription(point.getString(point.getColumnIndex(Constants.COL_DESCRIPTION)));
            recipe.setAvailable(true);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    /**
     * @return Description:
     * Returns the size of the available recipes it can make.  (Based upon minimum slot level)
     * <p/>
     * Requires:
     * n/a
     */
    protected int getSizeOfAvailableRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String MySQLString = "SELECT COUNT ID FROM " + TABLE_DRINK_LIST + " "
                + "WHERE " + Constants.COL_AVAILABLE + " = TRUE";
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
    protected String getRecipe(int ID) {
        String codeToReturn = "";
        int ingredientID;
        int units;
        int slotNumber = 0;
        int slotLevel = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        //This SQL Statement selects all of the needed ingredients needed in to make a drink
        String MySQLString = "SELECT * FROM " + TABLE_RECIPE_NEEDS + " WHERE" + Constants.COL_RECIPE_ID + " = " + ID;
        Cursor recipeNeedsPointer;
        Cursor kioskTablePointer;
        recipeNeedsPointer = db.rawQuery(MySQLString, null);

        recipeNeedsPointer.moveToFirst();
        do {
            ingredientID = recipeNeedsPointer.getInt(recipeNeedsPointer.getColumnIndex(Constants.COL_INGREDIENT_ID));
            units = recipeNeedsPointer.getInt(recipeNeedsPointer.getColumnIndex(Constants.COL_UNITS));
            //This SQL Statement matches the correct Ingredient from the Recipe Needs table to the correct slot in the Kiosk Slot Table
            MySQLString = "SELECT * FROM " + TABLE_KIOSK_SLOTS + "WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
            kioskTablePointer = db.rawQuery(MySQLString, null);
            slotNumber = 0;
            slotLevel = 0;
            if (kioskTablePointer.getCount() > 1) {  // There are more than 1 match for the Ingredient ID, take the one with the highest SlotLevel
                kioskTablePointer.moveToFirst();
                do {
                    MySQLString = "SELECT * FROM " + TABLE_KIOSK_SLOTS + "WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
                    kioskTablePointer = db.rawQuery(MySQLString, null);
                    int tempSlotNum = kioskTablePointer.getInt(kioskTablePointer.getColumnIndex(Constants.COL_SLOT_NUMBER));
                    int tempSlotLevel = kioskTablePointer.getInt(kioskTablePointer.getColumnIndex(Constants.COL_SLOT_LEVEL));
                    if (tempSlotLevel >= slotLevel) {
                        slotNumber = tempSlotNum;
                        slotLevel = tempSlotLevel;
                    }
                } while (recipeNeedsPointer.moveToNext());
            } else {
                do {
                    MySQLString = "SELECT * FROM " + TABLE_KIOSK_SLOTS + "WHERE " + Constants.COL_INGREDIENT_ID + " = " + ingredientID;
                    kioskTablePointer = db.rawQuery(MySQLString, null);
                    slotNumber = kioskTablePointer.getInt(kioskTablePointer.getColumnIndex(Constants.COL_SLOT_NUMBER));
                    slotLevel = kioskTablePointer.getInt(kioskTablePointer.getColumnIndex(Constants.COL_SLOT_LEVEL));
                } while (recipeNeedsPointer.moveToNext());
            }
            for (int i = units; i > 0; i--) {
                codeToReturn += "" + slotNumber;
            }
        } while (recipeNeedsPointer.moveToNext());
        recipeNeedsPointer.close();
        kioskTablePointer.close();
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
    protected ArrayList<String> getDrinkIngredients(int ID) {
        ArrayList<String> ingredientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //This SQL Statement selects all of the needed ingredients needed in to make a drink
        String MySQLString = "SELECT " + Constants.COL_LITERAL_NAME +  "  FROM " + TABLE_RECIPE_NEEDS + " WHERE" + Constants.COL_RECIPE_ID + " = " + ID;
        Cursor recipeNeedsPointer = db.rawQuery(MySQLString, null);
        while (recipeNeedsPointer.moveToNext()) {
            ingredientList.add(recipeNeedsPointer.getString(recipeNeedsPointer.getColumnIndex(Constants.COL_LITERAL_NAME)));
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

}


