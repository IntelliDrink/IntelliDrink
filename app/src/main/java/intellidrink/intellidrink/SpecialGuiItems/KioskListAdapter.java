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
public class KioskListAdapter extends ArrayAdapter<KioskListItem> {

static int resource = R.layout.literal_ingredient_list_item;
    Context myContext;
    ArrayList<KioskListItem> data;

    public KioskListAdapter(Context context, ArrayList<KioskListItem> data) {
        super(context, resource, data);
        myContext = context;
        this.data = data;
    }


@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        KioskHolder holder;
        if(row == null)
        {
             row = LayoutInflater.from(myContext).inflate(resource, null);
             holder = new KioskHolder();
             row.setTag(holder);
             holder.name = (TextView) row.findViewById(R.id.literal_ingredient_name_text_view);
        }
        else
        {
            holder = (KioskHolder)convertView.getTag();
        }
        KioskListItem item = data.get(position);
        holder.name.setText(item.KioskName);

        return row;
    }

    private static class KioskHolder
    {
        TextView name;
    }

}
