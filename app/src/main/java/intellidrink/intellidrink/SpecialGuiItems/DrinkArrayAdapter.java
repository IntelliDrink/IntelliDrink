package intellidrink.intellidrink.SpecialGuiItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.DrinkListContainer;
import intellidrink.intellidrink.R;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class DrinkArrayAdapter extends ArrayAdapter<DrinkAdapterItem> {

    private ArrayList<DrinkAdapterItem> objects;
    static int resource = R.layout.drink_item;
    Context myContext;

    /**
     * Constructor
     *
     * @param context  just use this.  dont be a retard.
     */
    public DrinkArrayAdapter(Context context, ArrayList<DrinkAdapterItem> data) {
        super(context, resource, data);
        objects = data;
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        DrinkHolder holder;
        if(row == null)
        {
            row = LayoutInflater.from(myContext).inflate(resource, null);
            holder = new DrinkHolder();
            row.setTag(holder);
            holder.name = (TextView) row.findViewById(R.id.drinkListItemNameField);
            holder.price =  (TextView) row.findViewById(R.id.drinkListItemPriceField);
        }
        else
        {
            holder = (DrinkHolder)convertView.getTag();
        }
        DrinkAdapterItem item = objects.get(position);
        holder.name.setText(item.drinkName);
        holder.price.setText(item.price);

        return row;
    }

    private static class DrinkHolder
    {
        TextView name;
        TextView price;
    }
}
