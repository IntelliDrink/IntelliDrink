package intellidrink.intellidrink.SpecialGuiItems;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import IntelliDrinkCore.Containers.TabListContainer;
import IntelliDrinkCore.Transaction;
import intellidrink.intellidrink.R;

/**
 * Created by Terryn-Fredrickson on 4/22/15.
 */
public class TabAdapter extends ArrayAdapter<TabAdapterItem> {

    ArrayList<TabAdapterItem> data;
    TabListContainer tabListContainer;
    static int layoutResourseID = R.layout.tab_item;

    /**
     * Constructor
     *
     * @param context  The current context.
     */
    public TabAdapter(Context context, TabListContainer container) {
        super(context, layoutResourseID);
        this.tabListContainer = container;
        TabAdapterItem tmpItem;
        for(Transaction transaction : container.getTransactionHistory())
        {
            String name = transaction.getRecipeName();
            double price = transaction.getPrice();
            tmpItem = new TabAdapterItem(name, price);
            data.add(tmpItem);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        TabAdapterItem item = getItem(position);

        TabHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            holder = new TabHolder();
            convertView = inflater.inflate(layoutResourseID, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.customerTabItemName);
            holder.price = (TextView) convertView.findViewById(R.id.customerTabItemPrice);

            convertView.setTag(holder);
        }
        else
            holder = (TabHolder) convertView.getTag();

        holder.name.setText(item.getDrinkName());
        holder.price.setText(String.valueOf(item.getPrice()));

        return row;
    }

    static class TabHolder
    {
        TextView name;
        TextView price;
    }
}
