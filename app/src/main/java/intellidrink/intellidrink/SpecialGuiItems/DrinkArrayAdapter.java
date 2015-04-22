package intellidrink.intellidrink.SpecialGuiItems;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.DrinkListContainer;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class DrinkArrayAdapter extends ArrayAdapter<DrinkItem> {

    private ArrayList<DrinkItem> objects;
    DrinkListContainer myContainer;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public DrinkArrayAdapter(Context context, int resource, DrinkListContainer container) {
        super(context, resource);
        myContainer = container;
    }


}
