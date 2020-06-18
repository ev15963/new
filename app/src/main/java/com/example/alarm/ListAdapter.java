package com.example.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {
    ArrayList<Alarm_Item> items = new ArrayList<>();

    public ListAdapter(){
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.alarm_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem (Alarm_Item alarm_item){
        items.add(alarm_item);
        notifyDataSetChanged();
    }

    /*@Override
    public boolean onItemMove(int from_position, int to_position) {
        Alarm_Item alarm_item = items.get(from_position);
        items.remove(from_position);
        items.add(to_position, alarm_item);

        notifyItemMoved(from_position, to_position);

        return true;
    }*/

    /*@Override
    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }*/

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime, tvAmPm;
        ImageView listImage;

        public  ItemViewHolder(View itemView){
            super(itemView);
            listImage = itemView.findViewById(R.id.imgDayOrNight);
            tvAmPm = itemView.findViewById(R.id.listAmPm);
            tvTime = itemView.findViewById(R.id.listTime);

        }

        public void onBind(Alarm_Item alarm_item){
            listImage.setImageResource(R.drawable.ic_launcher_background);
            tvTime.setText(alarm_item.getTime_hour() + alarm_item.getTime_min() + "id :" + alarm_item.getId());
            tvAmPm.setText(alarm_item.getTime_ampm() + "ampm");
        }
    }
}
