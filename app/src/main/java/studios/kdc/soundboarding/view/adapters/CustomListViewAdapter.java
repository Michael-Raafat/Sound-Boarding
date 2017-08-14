package studios.kdc.soundboarding.view.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studios.kdc.soundboarding.R;


public class CustomListViewAdapter extends ArrayAdapter<String> {

    private List<String> names;
    private List<Integer> icons;

    public CustomListViewAdapter(Context context , List<String> names) {
        super(context, R.layout.options_list_view , names);
        this.names = names;
        this.initializeIcons();

    }

   private void initializeIcons(){
       this.icons = new ArrayList<>();
       this.icons.add(R.drawable.upload_black);
       this.icons.add(R.drawable.create);
       this.icons.add(R.drawable.about);
   }



    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.options_list_view, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.text);
            viewHolder.icon = convertView.findViewById(R.id.image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.txtName.setText(names.get(position));
        viewHolder.icon.setImageResource(icons.get(position));

        return convertView;
    }

    private static class ViewHolder {
        TextView txtName;
        ImageView icon;
    }

}