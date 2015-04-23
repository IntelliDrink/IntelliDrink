package IntelliDrinkDB.Grabbers;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IntelliDrinkCore.Containers.DrinkListContainer;
import IntelliDrinkCore.DrinkIngredients;
import IntelliDrinkCore.DrinkListItem;
import IntelliDrinkCore.LiteralIngredient;
import IntelliDrinkDB.LocalDatabaseHelper;
import IntelliDrinkDB.ServerDatabase;

/**
 * Created by Terryn-Fredrickson on 4/7/15.
 */
public class DrinkListGrabber extends GenericListGrabber{

    DrinkListContainer myContainer;
    LocalDatabaseHelper davidsDB;


    public DrinkListGrabber(DrinkListContainer container, ServerDatabase db, LocalDatabaseHelper db2)
    {
        super(db);
        myContainer = container;
        davidsDB = db2;
    }

    public void buildContainer()
    {
        ArrayList<DrinkListItem> tmpList = new ArrayList<>();
        ArrayList<DrinkIngredients> ingredients = new ArrayList<>();

        String arduinoCode = "";
        //first grab the whole list of drinks
        //davidsDB.configureDatabase("Kiosk_1", "password");
        tmpList = davidsDB.getAvailableRecipes();
        int MAX = tmpList.size();
        int id;
        for(int i = 0; i < MAX; i ++)
        {
            id = tmpList.get(i).getID();
            arduinoCode = davidsDB.getArduinoCode(id);
            ArrayList<String> ingredientStrings = davidsDB.getDrinkIngredients(id);
            Map<String, List<Integer>> stuff = getValuesAndAmounts(arduinoCode);
            List<Integer> values = stuff.get("values");
            List<Integer> amounts = stuff.get("amounts");
            DrinkIngredients ingredient;
            if(values.size() == amounts.size())
            {
                ingredient = new DrinkIngredients();
                for(int d = 0; d < values.size(); d++)
                {
                    ingredient.setName(ingredientStrings.get(d));
                    ingredient.setSlot(values.get(d));
                    ingredient.setAmount(amounts.get(d));
                    tmpList.get(i).addIngredients(ingredient);
                }
                tmpList.get(i).setArduinoCode(arduinoCode);
            }
            else
            {
                Log.d(this.toString(), "values and amounts are not the same, something is wrong");
            }
        }
        int test = davidsDB.getSizeOfAvailableRecipes();
        myContainer.setDrinkables(test);
        myContainer.builder(tmpList);
    }

    int ch(char c)
    {
        int v = (int) c;
        switch(v){
            case 49:
                v = 1;
                break;
            case 50:
                v = 2;
                break;
            case 51:
                v = 3;
                break;
            case 52:
                v = 4;
                break;
            case 53:
                v = 5;
                break;
            case 54:
                v = 6;
                break;
            case 55:
                v = 7;
                break;
            case 56:
                v = 8;
                break;
            case 57:
                v = 9;
                break;
        }
        return v;
    }

    public Map<String, List<Integer>> getValuesAndAmounts(String code)
    {
        List<Integer> vals = new ArrayList<Integer>();
        List<Integer> amounts = new ArrayList<Integer>();

        int z = ch(code.charAt(0));
        vals.add(z);
        amounts.add(0);
        int lowest = 0;
        for(int i = 0; i < code.length(); i ++)
        {
            int d = ch(code.charAt(i));
            System.out.println(Integer.toString(d));
            if(vals.get(lowest) == d)
            {
                int f = amounts.get(lowest);
                f++;
                amounts.set(lowest, f);
            }
            else
            {
                vals.add(d);
                amounts.add(1);
                lowest ++;
            }
        }

        Map<String, List<Integer>> myShit = new HashMap();
        myShit.put("values", vals);
        myShit.put("amounts", amounts);
        return myShit;
    }


    public void pushToServer()
    {
        int units, slotNumber;
        int max = myContainer.getLastDrink().getIngredients().size();
        for(int i = 0; i < max; i ++)
        {
            units = myContainer.getLastDrink().getIngredients().get(i).getAmount();
            slotNumber = myContainer.getLastDrink().getIngredients().get(i).getSlot();
            davidsDB.setSlotLevel(units, slotNumber);
        }
        int test = davidsDB.getSizeOfAvailableRecipes();
        if(test != myContainer.getDrinkables())
            myContainer.build();
    }

    public int getSizeOfAvailableRecipes() {
        return davidsDB.getSizeOfAvailableRecipes();
    }
}
