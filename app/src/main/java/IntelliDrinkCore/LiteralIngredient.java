package IntelliDrinkCore;

/**
 * Created by David on 4/6/2015.
 */
public class LiteralIngredient {
    private int ID;
    private String literalName;
    private int genericIDNumber;
    private Double shotPrice;

    public Double getShotPrice() {
        return shotPrice;
    }

    public void setShotPrice(Double shotPrice) {
        this.shotPrice = shotPrice;
    }

    public int getGenericIDNumber() {
        return genericIDNumber;
    }

    public void setGenericIDNumber(int genericIDNumber) {
        this.genericIDNumber = genericIDNumber;
    }

    public String getLiteralName() {
        return literalName;
    }

    public void setLiteralName(String literalName) {
        this.literalName = literalName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


}
