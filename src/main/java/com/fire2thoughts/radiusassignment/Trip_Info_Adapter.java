package com.fire2thoughts.radiusassignment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Trip_Info_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Trips_Info> list_items;
    String[] s={"1","2","3"};

    public Trip_Info_Adapter(List<Trips_Info> list_items) {
       this.list_items=list_items;
       Log.e("adapter",""+list_items);
       Log.e("tripinf","");
    }

    @NonNull
    @Override
    public Trip_info onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_ui,viewGroup,false);
        return new Trip_info(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
     Trips_Info inf=list_items.get(i);
     Trip_info Holder=(Trip_info)viewHolder;
     Holder.start.setText(inf.start);
     Holder.end.setText(inf.end);
     Holder.dots.setText(".\n.\n.");
     Holder.symbol.setText(inf.symbol);
     Holder.s_t_date.setText(inf.sdate);
     Holder.e_t_date.setText(inf.edate);
     Holder.price.setText(inf.price);
     Holder.ftime.setText("Travel Time: "+inf.duration+"min");
    }

    @Override
    public int getItemCount() {
        return list_items.size();
    }

    public class Trip_info extends RecyclerView.ViewHolder {
        TextView start,end,dots,s_t_date,e_t_date,price,symbol,ftime;

        public Trip_info(@NonNull View itemView) {
            super(itemView);
            Log.e("inerclass","");
            start=itemView.findViewById(R.id.s_trip);
            end=itemView.findViewById(R.id.e_trip);
            dots=itemView.findViewById(R.id.dot);
            s_t_date=itemView.findViewById(R.id.s_t_date);
            e_t_date=itemView.findViewById(R.id.e_t_date);
            price=itemView.findViewById(R.id.t_price);
            symbol=itemView.findViewById(R.id.symbol);
            ftime=itemView.findViewById(R.id.travel_time);
        }
    }
}