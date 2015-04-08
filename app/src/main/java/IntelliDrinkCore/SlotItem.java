package IntelliDrinkCore;

/**
 * Created by David on 4/7/2015.
 */
public class SlotItem {
    private int ID;
    private int slotNumber;
    private String literalName;
    private int ingredientID;
    private int slotLevel;
    private double shotPrice;
    private boolean available;

    public double getShotPrice() {
        return shotPrice;
    }

    public void setShotPrice(double shotPrice) {
        this.shotPrice = shotPrice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getLiteralName() {
        return literalName;
    }

    public void setLiteralName(String literalName) {
        this.literalName = literalName;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public int getSlotLevel() {
        return slotLevel;
    }

    public void setSlotLevel(int slotLevel) {
        this.slotLevel = slotLevel;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
