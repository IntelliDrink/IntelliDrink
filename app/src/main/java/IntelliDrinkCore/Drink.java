package IntelliDrinkCore;

import java.util.ArrayList;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class Drink {

    ArrayList<Alcohol> myIngredients;
    String myDescription;
    String myName;
    String myUID;

    /*
    * 0 = UID
    * 1 = Recipe Name
    * 2 = Description
    * 3 = Ingredients (List)*/
    public Drink(ArrayList<String> SQLVomit)
    {
        myIngredients = new ArrayList<Alcohol>();
        myUID = SQLVomit.get(0);
        myName = SQLVomit.get(1);
        myDescription = SQLVomit.get(2);
        int i = 3;
        int d =  SQLVomit.size();
        while(i < d)
        {
            String str = SQLVomit.get(i);
            String parts[] = str.split("-");
            String part0 = parts[0];
            String part1 = parts[1];
            Alcohol tmp = new Alcohol(part0, part1);
            //litteral = 0, genaric = 1
            myIngredients.add(tmp);
            i++;
        }
    }

    public Drink(Alcohol a,  String UID, String name, String desc) {
        myIngredients = new ArrayList<Alcohol>();
        myIngredients.add(a);
        myUID = UID;
        myName = name;
        myDescription = desc;
    }

    public ArrayList<Alcohol> getIngredients()
    {
        return myIngredients;
    }

    public String getMyDescription()
    {
        return myDescription;
    }

    public String getMyId()
    {
        return myUID;
    }
}
