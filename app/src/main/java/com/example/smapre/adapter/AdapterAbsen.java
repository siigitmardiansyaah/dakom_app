package com.example.smapre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapre.R;
import com.example.smapre.model.AbsenData;

import java.util.List;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.HolderData> {
    private Context ctx;
    private List<AbsenData> listData;
    String hari,hari_indo,jam,tanggal;


    public AdapterAbsen(Context ctx, List<AbsenData> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        AbsenData dm = listData.get(position);
        holder.keterangan.setText("Absen " + dm.getKeterangan());
        String[] kata = dm.getWaktu_absen().split(",");
        hari = kata[0];
        tanggal = kata[1];
        jam = kata[2];
        if(hari.equals("Monday"))
        {
            hari_indo = "Senin";
        }else if(hari.equals("Tuesday"))
        {
            hari_indo = "Selasa";
        }else if(hari.equals("Wednesday"))
        {
            hari_indo = "Rabu";
        }else if(hari.equals("Thursday"))
        {
            hari_indo = "Kamis";
        }else if(hari.equals("Friday"))
        {
            hari_indo = "Jumat";
        }else if(hari.equals("Saturday")){
            hari_indo = "Sabtu";
        }else{
            hari_indo = "Minggu";
        }
        holder.jam_absen.setText(hari_indo + " , "+ tanggal + " , " + jam);
        if(dm.getKeterangan().equals("Datang"))
        {
            holder.gambar.setImageResource(R.drawable.datang);
        }else if(dm.getKeterangan().equals("Pulang"))
        {
            holder.gambar.setImageResource(R.drawable.pulang);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView keterangan, jam_absen;
        ImageView gambar;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            keterangan = itemView.findViewById(R.id.textViewSub2Title);
            jam_absen = itemView.findViewById(R.id.hiyaa);
            gambar = itemView.findViewById(R.id.imageView);
        }


    }




}

