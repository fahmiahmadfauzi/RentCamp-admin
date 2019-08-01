package com.if9.rentcampadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.if9.rentcampadmin.AlatTokoDetailActivity;
import com.if9.rentcampadmin.Model.ModelDataAlat;
import com.if9.rentcampadmin.R;
import com.if9.rentcampadmin.Session.SessionManager;

import java.util.List;

public class AdapterDataAlat extends RecyclerView.Adapter<AdapterDataAlat.HolderData> {

    List<ModelDataAlat> mListItems;
    Bitmap bitmap;
    Context context;
    SessionManager sessionManager;

    public AdapterDataAlat(Context context, List<ModelDataAlat> items) {
        this.mListItems = items;
        this.context = context;
    }


    @Override
    public AdapterDataAlat.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_alat, parent, false);
        AdapterDataAlat.HolderData holderData = new AdapterDataAlat.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterDataAlat.HolderData holder, int position) {
        ModelDataAlat mlist = mListItems.get(position);

        holder.tv_title.setText(mlist.getNama());
        holder.tv_keterangan.setText(mlist.getHarga());
        holder.tv_stok.setText(mlist.getStok());
        //loading image
        Glide.with(context).load(mlist.getFoto()).thumbnail(0.5f).transition(new DrawableTransitionOptions().crossFade()).into(holder.thubnail);
        // new GetImageFromURL(holder.thubnail).execute(mlist.getFoto());
        holder.md = mlist;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder {


        ImageView thubnail;
        TextView tv_title, tv_keterangan, tv_stok;
        Button btnHapus, btnEdit;

        ModelDataAlat md;

        public HolderData(View v) {
            super(v);
            thubnail = v.findViewById(R.id.img_cover);
            tv_title = v.findViewById(R.id.tv_title);
            tv_keterangan = v.findViewById(R.id.tv_description);
            tv_stok = v.findViewById(R.id.tv_stok);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail = new Intent(context, AlatTokoDetailActivity.class);
                    // detail.putExtra("klik",1);
                    detail.putExtra("id_alat", md.getId_alat());
                    detail.putExtra("nama_alat", md.getNama());
                    detail.putExtra("harga_alat", md.getHarga());
                    detail.putExtra("foto", md.getFoto());


                    context.startActivity(detail);


                }
            });


        }
    }


}
