package IntelliDrinkCore.Containers;

import java.util.ArrayList;

import IntelliDrinkCore.KioskConfiguration;
import IntelliDrinkDB.Grabbers.KioskGrabber;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn on 4/22/2015.
 */
public class KioskConfigurationContainer implements IntelliDrinkContainer{

    ArrayList<KioskConfiguration> myKioskConfiguration;
    KioskGrabber myGrabber;
    int myTotalDrinks;

    public KioskConfigurationContainer(ServerDatabase db)
    {
        myKioskConfiguration = new ArrayList<>();
        myGrabber = new KioskGrabber(this, db);
    }

    public KioskConfiguration getSpecificKioskConfiguration(int location)
    {
        if(location >= myTotalDrinks);
        return this.myKioskConfiguration.get(location);
    }

    public ArrayList<KioskConfiguration> getAllConfigurations()
    {
        return this.myKioskConfiguration;
    }

    public double getShotPrice(int location)
    {
        return this.myKioskConfiguration.get(location).getShotPrice();
    }

    public void setMyTotalDrinks(int d)
    {
        myTotalDrinks = d;
    }

    public int getMyTotalDrinks()
    {
        return this.myTotalDrinks;
    }

    @Override
    public void transfer() {
        //this doesnt really need anything????
    }

    @Override
    public void update() {
        this.build();
    }

    public void build()
    {
        if(myKioskConfiguration.size() > 0)
            myKioskConfiguration.clear();
        myGrabber.buildContainer();
    }
}
