package com.if9.rentcampadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.if9.rentcampadmin.Model.ModelDataTransaksi;
import com.if9.rentcampadmin.R;
import com.if9.rentcampadmin.Session.SessionManager;
import com.if9.rentcampadmin.TransaksiKonfirActivity;

import java.util.List;

public class AdapterDataTransaksi extends RecyclerView.Adapter<AdapterDataTransaksi.HolderData> {
    List<ModelDataTransaksi> mListItems;
    Bitmap bitmap;
    Context context;

    SessionManager sessionManager;

    public AdapterDataTransaksi(Context context, List<ModelDataTransaksi> items) {
        this.mListItems = items;
        this.context = context;
    }


    @Override
    public AdapterDataTransaksi.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_transaksi, parent, false);
        AdapterDataTransaksi.HolderData holderData = new AdapterDataTransaksi.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterDataTransaksi.HolderData holder, int position) {
        ModelDataTransaksi mlist = mListItems.get(position);
        if (mlist.getStatus().equals("1")) {
            holder.tv_idtransaksi.setText(mlist.getId_transaksi());
            holder.tv_nama_alat.setText(mlist.getNama_alat());
            holder.tv_harga_alat.setText(mlist.getHarga_alat());
            holder.tv_id_toko.setText(mlist.getId_toko());
            holder.tv_tgl.setText(mlist.getTgl());
            holder.tv_status.setText("Dalam Proses");

        } else {
            holder.tv_idtransaksi.setText(mlist.getId_transaksi());
            holder.tv_nama_alat.setText(mlist.getNama_alat());
            holder.tv_harga_alat.setText(mlist.getHarga_alat());
            holder.tv_id_toko.setText(mlist.getId_toko());
            holder.tv_tgl.setText(mlist.getTgl());
            holder.tv_status.setText("Lunas");
            holder.btnKonfir.setVisibility(View.GONE);

        }

        //loading image
        // Glide.with(context).load(mlist.getFoto()).thumbnail(0.5f).transition(new DrawableTransitionOptions().crossFade()).into(holder.thubnail);
        // new GetImageFromURL(holder.thubnail).execute(mlist.getFoto());
        holder.md = mlist;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {


        TextView tv_idtransaksi, tv_tgl, tv_id_toko, tv_nama_alat, tv_harga_alat, tv_status;
        Button btnKonfir;
        ModelDataTransaksi md;

        public HolderData(View v) {
            super(v);
            tv_idtransaksi = v.findViewById(R.id.idtransaksi);
            tv_tgl = v.findViewById(R.id.edtgl);
            tv_id_toko = v.findViewById(R.id.edidtoko);
            tv_nama_alat = v.findViewById(R.id.ednamaalat);
            tv_harga_alat = v.findViewById(R.id.edhargaalat);
            tv_status = v.findViewById(R.id.edstatus);
            btnKonfir = v.findViewById(R.id.btnkonfir);

            btnKonfir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent(context, TransaksiKonfirActivity.class);
                    go.putExtra("id_transaksi", md.getId_transaksi());
                    context.startActivity(go);
                }
            });

            sessionManager = new SessionManager(context);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Intent detail=new Intent(context, ProfileTokoActivity.class);

                    // detail.putExtra("klik",1);
                    detail.putExtra("id_toko", md.getId_toko());
                    detail.putExtra("nama_toko", md.getNama());
                    detail.putExtra("alamat_toko", md.getAlamat());
                    detail.putExtra("foto", md.getFoto());
                    sessionManager.createSessionIdToko(md.getId_toko());
                    context.startActivity(detail);*/


                }
            });


        }
    }


}