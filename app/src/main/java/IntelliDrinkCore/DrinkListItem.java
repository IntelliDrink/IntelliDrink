package IntelliDrinkCore;

import java.util.ArrayList;

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

    public void setArduinoCode(String a) { arduinoCode = a; }

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
}
