package IntelliDrinkCore;

/**
 * Created by David on 4/7/2015.
 */
public class KioskConfiguration {


    private int recipeID;
    private int ingredientID;   //LiteralIngredient.ID
    private int units;
    private int slotNum;
    private int slotLevel;
    private double price;
    private double shotPrice;

    public KioskConfiguration()
    {

    }

    public double getShotPrice() {
        return shotPrice;
    }

    public void setShotPrice(double shotPrice) {
        this.shotPrice = shotPrice;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getSlotNum() {
        return slotNum;
    }

    public void setSlotNum(int slotNum) {
        this.slotNum = slotNum;
    }

    public int getSlotLevel() {
        return slotLevel;
    }

    public void setSlotLevel(int slotLevel) {
        this.slotLevel = slotLevel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLiteralName() {
        return literalName;
    }

    public void setLiteralName(String literalName) {
        this.literalName = literalName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String literalName;
    private String recipeName;
    private String description;




}
