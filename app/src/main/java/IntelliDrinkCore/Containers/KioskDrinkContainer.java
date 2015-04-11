package IntelliDrinkCore.Containers;

import java.lang.reflect.Array;
import java.util.ArrayList;

import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkDB.Grabbers.KioskGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class KioskDrinkContainer implements IntelliDrinkContainer{

    ArrayList<LiteralIngredient> ingredients;
    LiteralIngredient activeIngredient;

    KioskGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public KioskDrinkContainer(ServerDatabase db)
    {
        ingredients = new ArrayList<LiteralIngredient>();
        activeIngredient = new LiteralIngredient();
        myGrabber = new KioskGrabber(this, db);
    }

    public LiteralIngredient getIngredient(int i)
    {
        LiteralIngredient tmp = new LiteralIngredient();
        tmp = ingredients.get(i);
        return tmp;
    }

    public ArrayList<LiteralIngredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(KioskDrinkContainer k)
    {
        ingredients = k.getIngredients();
        activeIngredient = k.getActiveIngredient();
    }

    public void addIngredient(LiteralIngredient i) {
        ingredients.add(i);
    }

    public void setActiveIngredient(LiteralIngredient i)
    {
        activeIngredient = i;
    }

    public LiteralIngredient getActiveIngredient()
    {
        return this.activeIngredient;
    }

    /**
     * Updates the DB with the info on the class
     */
    @Override
    public void transfer() {

    }

    /**
     * Updates the Container from the DB
     */
    @Override
    public void update() {

    }
}
