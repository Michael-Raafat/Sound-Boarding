package studios.kdc.soundboarding.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerContract;
import studios.kdc.soundboarding.media.singlePlayer.MediaPlayerController;
import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.models.Track;

public class GridViewAdapter extends BaseAdapter   {

    private List<Track> allItemsResourceID;
    private LayoutInflater inflater;
    private int color;
    private int cardPosition;
    private Activity context;
    private int cardWidth;
    private int cardHeight;


    public GridViewAdapter(Activity context, List<Track> media, int color , int cardPosition , int cardWidth , int cardHeight) {
        this.inflater = LayoutInflater.from(context);
        this.allItemsResourceID = media;
        this.color = color;
        this.cardPosition = cardPosition;
        this.context = context;
        this.cardHeight = cardHeight;
        this.cardWidth = cardWidth;
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
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((cardWidth), cardHeight);
            holder.getCardView().setLayoutParams(params);
            GradientDrawable drawable = (GradientDrawable) holder.getColor().getBackground();
            drawable.setColor(this.color);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (!allItemsResourceID.get(position).getName().equals("")) {
            holder.getTextView().setText(allItemsResourceID.get(position).getName());
        }
        setOnLongClickListener(view, position, holder.getTextView().getText().toString());
        setOnClickListener(view, holder.getCardView() , allItemsResourceID.get(position).getType() ,allItemsResourceID.get(position).getPath(), context);
        return view;
    }

    private void setOnLongClickListener(View v, int position, String name) {
        v.setOnLongClickListener(new ChoiceTouchListener(position , name, context));
    }


    private void setOnClickListener(View v, CardView card ,  String type, String path , Context context) {
        v.setOnClickListener(new ChoiceClickListener(card, type, path, context));
    }

    private class ViewHolder {


        private TextView textView;
        private RelativeLayout color;
        private CardView cardView;

        private TextView getTextView() {
            return textView;
        }
        private RelativeLayout getColor() {
            return color;
        }
        private CardView getCardView() {
            return cardView;
        }



        ViewHolder(View view) {
            color = view.findViewById(R.id.color_div);
            textView = view.findViewById(R.id.music_name);
            cardView = view.findViewById(R.id.card_view);
        }

    }




    private class ChoiceTouchListener implements View.OnLongClickListener {
        @SuppressLint("NewApi")
        private int position;
        private String name;
        private Context context;

        private ChoiceTouchListener(int position, String name, Context context) {
            this.position = position;
            this.name = name;
            this.context = context;
        }

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText( String.valueOf(cardPosition) + context.getResources().getString(R.string.separator) +  name   + context.getResources().getString(R.string.separator) + this.position
                    + context.getResources().getString(R.string.separator) + "track", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            return true;

        }
    }


    private class ChoiceClickListener implements View.OnClickListener, MediaPlayerContract.OnCompletionListener {

        private String type;
        private String path;
        private CardView card;
        private Context context;
        private ChoiceClickListener(CardView cardView ,String type, String path , Context context) {

            this.type = type;
            this.path = path;
            this.card = cardView;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            MediaPlayerController.getInstance(context).singlePlayAndPauseTrack(this.type, this.path, this);
            this.toggleCardColor();
        }

        private void toggleCardColor() {
          if(card.getCardBackgroundColor() == context.getResources().getColorStateList(R.color.cardview_shadow_end_color))
               setColorOnPlaying();
           else if(card.getCardBackgroundColor() == context.getResources().getColorStateList(R.color.light_grey))
               setColorOnCompletion();
        }

        private void setColorOnPlaying(){
            card.setCardBackgroundColor(context.getResources().getColorStateList(R.color.light_grey));
        }
        private void setColorOnCompletion(){
            card.setCardBackgroundColor(context.getResources().getColorStateList(R.color.cardview_shadow_end_color));
        }
        @Override
        public void notifyOnTrackCompletion() {
            setColorOnCompletion();
        }
    }



}