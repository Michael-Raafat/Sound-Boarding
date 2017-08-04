package studios.kdc.soundboarding.view.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import studios.kdc.soundboarding.R;


public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder>{

    private int minutes;
    private int seconds;

    public TimeLineAdapter(){
        this.minutes = 0;
        this.seconds = 0;
    }


    @Override
    public TimeLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.timeline, parent, false);

        return new TimeLineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeLineAdapter.ViewHolder holder, int position) {
         String min = String.valueOf(minutes).length() < 2 ? "0" + minutes : "" + minutes;
         String sec = String.valueOf(seconds).length() < 2 ? "0" +seconds : "" + seconds;

        holder.getTv().setText(min + ":" + sec);

        this.seconds += 30;

        if(seconds >= 60) {
            this.seconds = 0;
            this.minutes++;
        }


    }

    @Override
    public int getItemCount() {
        return 7;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView getTv() {
            return tv;
        }

        private TextView tv;


        private ViewHolder(final View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.minutes);


        }
    }

}
