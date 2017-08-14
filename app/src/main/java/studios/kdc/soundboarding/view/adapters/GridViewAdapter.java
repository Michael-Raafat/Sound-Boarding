package studios.kdc.soundboarding.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
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
    private String groupName;
    private int cardWidth;
    private int cardHeight;
    private MediaPlayerController mediaPlayerController;

    public GridViewAdapter(Activity context, List<Track> media, int color , int cardPosition, String groupName , int cardWidth , int cardHeight) {
        this.inflater = LayoutInflater.from(context);
        this.allItemsResourceID = media;
        this.color = color;
        this.cardPosition = cardPosition;
        this.context = context;
        this.groupName = groupName;
        this.cardHeight = cardHeight;
        this.cardWidth = cardWidth;
        this.mediaPlayerController = MediaPlayerController.getInstance(context);
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
        setOnLongClickListener(view, this.cardPosition , holder.getTextView().getText().toString());
        if (mediaPlayerController.checkTrackChanged(view, this.cardPosition, holder.getTextView().getText().toString())) {
            setOnClickListener(view, holder.getCardView() , holder.getTextView().getText().toString(), context);
        }
        return view;
    }

    private void setOnLongClickListener(View v, int position, String name) {
        v.setOnLongClickListener(new ChoiceTouchListener(position , name, context));
    }


    private void setOnClickListener(View v, CardView card ,  String name, Context context) {
        v.setOnClickListener(new ChoiceClickListener(card ,name, groupName, context));
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

        public CardView getCardView() {
            return cardView;
        }

        private CardView cardView;

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
            ClipData data = ClipData.newPlainText( String.valueOf(this.position) + context.getResources().getString(R.string.separator) +  name, "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            return true;

        }
    }


    private class ChoiceClickListener implements View.OnClickListener, MediaPlayerContract.OnCompletionListener {

        private String name;
        private String groupName;
        private CardView card;
        private Context context;
        private ChoiceClickListener(CardView cardView ,String name, String groupName , Context context) {

            this.name = name;
            this.groupName = groupName;
            this.card = cardView;
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            mediaPlayerController.singlePlayAndPauseTrack(this.groupName, name, this);
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