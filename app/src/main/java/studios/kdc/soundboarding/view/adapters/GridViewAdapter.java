package studios.kdc.soundboarding.view.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import studios.kdc.soundboarding.MediaPlayerHandler;
import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.models.Track;

public class GridViewAdapter extends BaseAdapter {

    private List<Track> allItemsResourceID;
    private LayoutInflater inflater;
    private int color;
    private int cardPosition;
    private Context context;
    private String trackName;
    private String groupName;

    public GridViewAdapter(Context context, List<Track> media, int color , int cardPosition, String groupName) {
        this.inflater = LayoutInflater.from(context);
        this.allItemsResourceID = media;
        this.color = color;
        this.cardPosition = cardPosition;
        this.context = context;
        this.groupName = groupName;
    }

    @Override
    public int getCount() {
        return allItemsResourceID.size();
    }

    @Override
    public Object getItem(int position) {
        return allItemsResourceID.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.track_content, parent, false);
            holder = new ViewHolder(view);

            holder.getTextView().setTextColor(this.color);
            GradientDrawable drawable = (GradientDrawable) holder.getColor().getBackground();
            drawable.setColor(this.color);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (!allItemsResourceID.get(position).getName().equals("")) {
            holder.getTextView().setText(allItemsResourceID.get(position).getName());
        }
        setOnLongClickListener(view, this.cardPosition , holder.getTextView().getText().toString());
        setOnClickListener(view, this.cardPosition , holder.getTextView().getText().toString());
        this.trackName = holder.getTextView().getText().toString();
        return view;
    }

    private void setOnLongClickListener(View v, int position, String name) {
        v.setOnLongClickListener(new ChoiceTouchListener(position , name, context));
    }

    private void setOnClickListener(View v, int position, String name) {
        v.setOnClickListener(new ChoiceClickListener(position , name, context));
    }

    private class ViewHolder {
        private TextView getTextView() {
            return textView;
        }

        private RelativeLayout getColor() {
            return color;
        }

        private TextView textView;
        private RelativeLayout color;


        ViewHolder(View view) {
            color = view.findViewById(R.id.color_div);
            textView = view.findViewById(R.id.music_name);
        }

    }




    private class ChoiceTouchListener implements View.OnLongClickListener, View.OnClickListener {
        @SuppressLint("NewApi")
        private int position;
        private String name;
        private Context context;
        private MediaPlayerHandler mediaPlayerHandler;

        private ChoiceTouchListener(int position, String name, Context context) {
            this.position = position;
            this.name = name;
            this.context = context;
            mediaPlayerHandler = new MediaPlayerHandler(context);
        }

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText( String.valueOf(this.position) + "&&@" +  name, "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;

        }

        @Override
        public void onClick(View view) {
            mediaPlayerHandler.playSong(groupName + "/" + name);
        }
    }


    private class ChoiceClickListener implements View.OnClickListener {
        @SuppressLint("NewApi")
        private int position;
        private String name;
        private MediaPlayerHandler mediaPlayerHandler;

        private ChoiceClickListener(int position, String name, Context context) {
            this.position = position;
            this.name = name;
            mediaPlayerHandler = new MediaPlayerHandler(context);
        }

        @Override
        public void onClick(View view) {
            mediaPlayerHandler.playSong(groupName + "/" + name);
        }
    }



}

