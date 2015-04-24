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
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class TabAdapter extends ArrayAdapter<TabAdapterItem> {

    ArrayList<TabAdapterItem> data;
    static int layoutResourseID = R.layout.tab_item;
    Context myContext;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public TabAdapter(Context context, ArrayList<TabAdapterItem> data) {
        super(context, layoutResourseID, data);
        this.data = data;
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        //Log.d("GetView: " , "Building View");
        TabHolder holder;
        if (row == null) {
            row = LayoutInflater.from(myContext).inflate(layoutResourseID, null);
            holder = new TabHolder();
            row.setTag(holder);
            holder.name = (TextView) row.findViewById(R.id.customerTabItemName);
            holder.price = (TextView) row.findViewById(R.id.customerTabItemPrice);
        } else {
            holder = (TabHolder) convertView.getTag();
        }
        TabAdapterItem item = data.get(position);
        holder.name.setText(item.getDrinkName());
        holder.price.setText(item.getPrice());


        return row;
    }


    private static class TabHolder {
        TextView name;
        TextView price;
    }
}
