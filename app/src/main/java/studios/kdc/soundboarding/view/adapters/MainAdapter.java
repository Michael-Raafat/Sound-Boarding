package studios.kdc.soundboarding.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import studios.kdc.soundboarding.R;
import studios.kdc.soundboarding.Utils;
import studios.kdc.soundboarding.models.Group;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Group> groups;
    private Activity context;



    public MainAdapter(Activity context) {
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

     //  if(!this.groups.get(position).getTracks().isEmpty()) {
           int cardWidth = (int)(Utils.getScreenWidth(context) / 4.32);
           int cardHeight = (int)((Utils.getScreenHeight(context) / 7.68));
            GridViewAdapter gridViewAdapter = new GridViewAdapter(this.context, this.groups.get(position).getTracks(),
                    this.groups.get(position).getColor() , position, cardWidth , cardHeight);
            holder.getGridView().setNumColumns(this.groups.get(position).getTracks().size());
            holder.getGridView().setAdapter(gridViewAdapter);
            holder.getGroupName().setText(this.groups.get(position).getName());
            holder.getGroupName().setBackgroundColor(this.groups.get(position).getColor());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.groups.get(position).getTracks().size() * cardWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.getGridView().setLayoutParams(params);
            holder.getCardView().setOnLongClickListener(new OnCardDragListener(position));
        //}
    }

    @Override
    public int getItemCount() {
        return this.groups.size();
    }


  class ViewHolder extends RecyclerView.ViewHolder {

        private GridView gridView;
        private TextView getGroupName() {
            return groupName;
        }
        private TextView groupName;

      public CardView getCardView() {
          return cardView;
      }

      private CardView cardView;

        private ViewHolder(final View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridview);
            groupName = itemView.findViewById(R.id.group_name);
            cardView = itemView.findViewById(R.id.card_view);
        }


        private GridView getGridView() {
            return gridView;
        }
  }






    private class OnCardDragListener implements View.OnLongClickListener {
        @SuppressLint("NewApi")
        private int groupPosition;

        private OnCardDragListener(int position) {
            this.groupPosition = position;
        }

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText( String.valueOf(this.groupPosition) , "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.setTag("group");
            view.startDrag(data, shadowBuilder, view, 0);
            return true;

        }
    }





}

