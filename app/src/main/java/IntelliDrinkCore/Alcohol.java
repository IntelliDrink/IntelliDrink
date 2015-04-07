package IntelliDrinkCore;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class Alcohol {
    String myType;
    String myName;
    //int myUnits;


    public Alcohol(String name, String type/*, int shots*/)
    {
        myType = type;
        myName = name;
        //myUnits = shots;
    }

    public String myBaseType()
    {
        return this.myType;
    }

    public String getMyName()
    {
        return myName;
    }

    /*public int getMyUnits()
    {
        return myUnits;
    }*/


}
