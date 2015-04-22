package IntelliDrinkCore.Containers;

import android.content.Context;

import java.util.ArrayList;

import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkDB.Grabbers.KioskGrabber;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class KioskSlotContainer implements IntelliDrinkContainer{

    ArrayList<KioskDrinkContainer> kioskContainers;
    KioskGrabber myGrabber;

    //TODO INTERFACE WITH THE GRABBER
    public KioskSlotContainer(ServerDatabase db)
    {
        kioskContainers = new ArrayList<>();
        myGrabber = new KioskGrabber(this, db);
    }

    public void setKioskContainers(ArrayList<KioskDrinkContainer> kioskContainers)
    {
        this.kioskContainers = kioskContainers;
    }

    public void addContainer(KioskDrinkContainer k)
    {
        kioskContainers.add(k);
    }

    public void removeContainer() {
        int top = kioskContainers.size();
        kioskContainers.remove(top);
    }

    public void changeKioskDrink(KioskDrinkContainer k, int i)
    {
        kioskContainers.get(i).setIngredients(k);
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
