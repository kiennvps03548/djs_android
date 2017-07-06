package com.example.djs.djsandroid.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daniribalbert.customfontlib.views.CustomFontTextView;
import com.example.djs.djsandroid.R;
import com.example.djs.djsandroid.model.CommonClass;

import java.util.List;

/**
 * Created by vinh on 07/29/2016.
 */
public class TasklistAdapter extends RecyclerView.Adapter<TasklistAdapter.MyViewHolder> {

    private List<CommonClass> mListEmail;
    private Context context;
    NavigationClickListener listener;
    private int focusedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomFontTextView txtName, txtValue;
        public CardView card_view;
        public ImageView layout_view, icon;
        public MyViewHolder(View view) {
            super(view);
            txtName = (CustomFontTextView) view.findViewById(R.id.txt_name);
            txtValue = (CustomFontTextView) view.findViewById(R.id.txt_value);
            card_view = (CardView) view.findViewById(R.id.card_view);
            layout_view = (ImageView) view.findViewById(R.id.thumbnail);
            icon = (ImageView) view.findViewById(R.id.icon);
            view.setClickable(true);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface NavigationClickListener{
        void onItemClick(View v, int position);
    }


    public TasklistAdapter(Context context, List<CommonClass> moviesList, NavigationClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.mListEmail = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);
        final MyViewHolder pvh = new MyViewHolder(itemView);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "aaaaaa", Toast.LENGTH_LONG).show();
//                listener.onItemClick(v, pvh.getLayoutPosition());
//                notifyItemChanged(focusedItem);
//                focusedItem = pvh.getLayoutPosition();
//                notifyItemChanged(focusedItem);

            //}
        //});
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                // Return false if scrolled to the bounds and allow focus to move off the list
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }

                return false;
            }
        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int tryFocusItem = focusedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }

        return false;
    }

    public int getFocusedItem(){
        return focusedItem;
    }

    public void setFocusedItem(int pos){
        this.focusedItem = pos;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(focusedItem==position){
            holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.dark_yellow));
        }else holder.card_view.setCardBackgroundColor(context.getResources().getColor(R.color.md_material_blue_600));
        CommonClass menu = mListEmail.get(position);
        holder.txtName.setText(menu.getId());
        holder.txtValue.setText(menu.getName());
        holder.itemView.setSelected(focusedItem == position);
        if(menu.getId().equalsIgnoreCase("TARGET"))
            holder.icon.setImageResource(R.drawable.target);
        else if(menu.getId().equalsIgnoreCase("TEMPERATURE"))
            holder.icon.setImageResource(R.drawable.temperature);
        else if(menu.getId().equalsIgnoreCase("SPEED"))
            holder.icon.setImageResource(R.drawable.speed);
        else if(menu.getId().equalsIgnoreCase("WIDTH"))
            holder.icon.setImageResource(R.drawable.witdh);
        holder.layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(focusedItem);
                focusedItem = position;
                notifyItemChanged(focusedItem);
                listener.onItemClick(v, position);

            }
        });
        //22animate(holder.itemView, position);
    }

    private void animate(View view, final int pos) {
        view.animate().cancel();
        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(pos * 100);
    }


    @Override
    public int getItemCount() {
        return mListEmail.size();
    }

}