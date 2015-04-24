package IntelliDrinkCore;

import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;

import IntelliDrinkUSB.util.HexDump;

/**
 * Created by David on 4/7/2015.
 */
public class DrinkListItem {
    private int ID = 0;
    private int recipeID = 0;
    private String recipeName;
    private double price = 0;
    private String description;
    private boolean available = false;
    private String arduinoCode;
    ArrayList<DrinkIngredients> ingredients;
    private String baseLiquor;
    private byte[] arduinoCodeByteArray;

    public DrinkListItem()
    {
        String recipeName = new String();
        String description = new String();
        arduinoCode = new String();
        ingredients = new ArrayList<DrinkIngredients>();
    }

    public DrinkListItem(String name, String description, String arduinoCode, int id, int recipeId, double price)
    {
        this.ID = id;
        this.recipeID = recipeId;
        this.recipeName = name;
        this.description = description;
        this.price = price;
        this.arduinoCode = arduinoCode;
    }

    public void setBaseLiquor(String liquor)
    {
        this.baseLiquor = liquor;
    }

    public String getBaseLiquor()
    {
        return this.baseLiquor;
    }

    public String getArduinoCode() {return this.arduinoCode;}

    public void setArduinoCode(String a) {
        arduinoCode = a;
        this.buildByteArray();
    }

    public void addIngredients(DrinkIngredients i) {ingredients.add(i);}

    public void setIngredients(ArrayList<DrinkIngredients> i)
    {
        ingredients = i;
    }

    public ArrayList<DrinkIngredients> getIngredients()  {return this.ingredients;}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getIngredientsString() {

        StringBuilder myBuilder = new StringBuilder();
        for(DrinkIngredients ingredient : this.ingredients)
        {
            myBuilder.append(ingredient.getName());
            myBuilder.append("\n");
        }
        myBuilder.append(this.arduinoCode);
        String toReturn = new String(myBuilder);
        return toReturn;
    }

    void buildByteArray()
    {
        this.arduinoCodeByteArray = HexDump.hexStringToByteArray(this.arduinoCode);
        //this.arduinoCode = HexDump.dumpHexString(arduinoCodeByteArray);
    }

/*
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }*/

    /**
     * Kapparino
     *//*
    public void buildByteArray()
    {
        byte[] myBytes = {};
        byte individualbyte;
        for(int i = 0; i < arduinoCode.length(); i ++)
        {
            char c = arduinoCode.charAt(i);
            individualbyte = getFromChar(c);
            Log.d("Creating ByteArray" , ""+individualbyte);
            myBytes = appendArray(myBytes, individualbyte);
        }
        this.arduinoCodeByteArray = myBytes;
    }

    byte getFromChar(char c)
    {
        byte returnByte;
        Log.d("Char", ""+c);
        switch (c)
        {
            case '0':
                returnByte = 0x30;
                break;
            case '1':
                returnByte = 0x31;
                break;
            case '2':
                returnByte = 0x32;
                break;
            case '3':
                returnByte = 0x33;
                break;
            case '4':
                returnByte = 0x34;
                break;
            case '5':
                returnByte = 0x35;
                break;
            case '6':
                returnByte = 0x36;
                break;
            case '7':
                returnByte = 0x37;
                break;
            case '8':
                returnByte = 0x38;
                break;
            case '9':
                returnByte = 0x39;
                break;
            default:
                Log.d("Fucked Up", "Something fucked up getting your byte from char");
                returnByte = 0x00;
                break;
        }
        return returnByte;
    }

    byte[] appendArray(byte[] array, byte individual)
    {
        byte[] returnbytes = new byte[array.length + 1];
        System.arraycopy(array, 0, returnbytes, 0, array.length);
        byte[] tmp = {individual};
        System.arraycopy(tmp, 0, returnbytes, array.length, returnbytes.length);
        return returnbytes;
    }*/


    public byte[] getArduinoCodeByteArray()
    {
        return this.arduinoCodeByteArray;
    }

    public byte getByteArrayItem(int i)
    {
        return this.arduinoCodeByteArray[i];
    }
}
