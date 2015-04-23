package intellidrink.intellidrink.SpecialGuiItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import intellidrink.intellidrink.R;

/**
 * Created by Terryn on 4/22/2015.
 */
public class LiteralIngredientListAdapter extends ArrayAdapter<LiteralIngredientListItem> {

    static int resource = R.layout.literal_ingredient_list_item;
    Context myContext;
    ArrayList<LiteralIngredientListItem> data;

    public LiteralIngredientListAdapter(Context context, ArrayList<LiteralIngredientListItem> data) {
        super(context, resource, data);
        myContext = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        IngredientHolder holder;
        if(row == null)
        {
            row = LayoutInflater.from(myContext).inflate(resource, null);
            holder = new IngredientHolder();
            row.setTag(holder);
            holder.name = (TextView) row.findViewById(R.id.literal_ingredient_name_text_view);
            holder.price =  (TextView) row.findViewById(R.id.literal_ingredient_shotprice_text_view);
        }
        else
        {
            holder = (IngredientHolder)convertView.getTag();
        }
        LiteralIngredientListItem item = data.get(position);
        holder.name.setText(item.name);
        holder.price.setText(item.shotPrice);

        return row;
    }

    private static class IngredientHolder
    {
        TextView name;
        TextView price;
    }

}
