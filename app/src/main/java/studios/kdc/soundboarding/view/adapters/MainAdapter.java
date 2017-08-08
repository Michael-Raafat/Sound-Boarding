package studios.kdc.soundboarding.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.models.Group;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {



    private List<Group> groups;
    private Context context;


    public MainAdapter(Context context) {
        this.context = context;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cards, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       if(!this.groups.get(position).getTracks().isEmpty()) {
            GridViewAdapter gridViewAdapter = new GridViewAdapter(this.context, this.groups.get(position).getTracks(),
                    this.groups.get(position).getColor() ,
                    position,
                    this.groups.get(position).getName());
            holder.getGridView().setNumColumns(this.groups.get(position).getTracks().size());
            holder.getGridView().setAdapter(gridViewAdapter);
            holder.getGroupName().setText(this.groups.get(position).getName());
            holder.getGroupName().setBackgroundColor(this.groups.get(position).getColor());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.groups.get(position).getTracks().size() * (int) context.getResources().getDimension(R.dimen.small_card_width), LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.getGridView().setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }


  class ViewHolder extends RecyclerView.ViewHolder {

        private GridView gridView;
        private  CardView cardView;
        private TextView getGroupName() {
            return groupName;
        }

      private HorizontalScrollView scrollView;
        private TextView groupName;



        private ViewHolder(final View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridview);
            cardView = itemView.findViewById(R.id.card_view);
            groupName = itemView.findViewById(R.id.group_name);
            scrollView = itemView.findViewById(R.id.horizontalScrollView);

        }
        private CardView getCardView() {
            return cardView;
        }


        private GridView getGridView() {
            return gridView;
        }

        private HorizontalScrollView getScrollView() {
          return scrollView;
        }


  }


}

