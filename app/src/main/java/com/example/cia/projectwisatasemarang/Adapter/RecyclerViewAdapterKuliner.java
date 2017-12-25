package com.example.cia.projectwisatasemarang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.cia.projectwisatasemarang.DeatailKontenActivity;
import com.example.cia.projectwisatasemarang.DetailActivityKulinerHotel;
import com.example.cia.projectwisatasemarang.Json.Result;
import com.example.cia.projectwisatasemarang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 09/10/2017.
 */

public class RecyclerViewAdapterKuliner extends RecyclerView.Adapter<RecyclerViewAdapterKuliner.ViewHolder> implements Filterable {

	private Context context;
	private List<Result> results;
	private List<Result> orig;
	ImageLoader imageLoader;

	public RecyclerViewAdapterKuliner(List<Result> getResult, Context context) {
		this.context = context;
		this.results = getResult;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
		ViewHolder holder = new ViewHolder(v);
		v.setOnClickListener(clickListener);
		v.setTag(holder);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		//final Result result = results.get(position);
		imageLoader = ImageAdapter.getInstance(context).getImageLoader();
		imageLoader.get(results.get(position).getGambar(), imageLoader.getImageListener(
				holder.img_wisata,
				R.mipmap.ic_launcher,
				android.R.drawable.ic_dialog_alert
				)
		);
		holder.nama_wisata.setText(results.get(position).getNama());
	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			ViewHolder vholder = (ViewHolder) view.getTag();
			int position = vholder.getPosition();
			Intent intent = new Intent(context, DetailActivityKulinerHotel.class);
			intent.putExtra("gambarKH", results.get(position).getGambar());
			// Pass all data gambar
			intent.putExtra("namaKH", results.get(position).getNama());
			// Pass all data nama
			intent.putExtra("alamatKH", results.get(position).getAlamat());
			// Pass all data alamat
			intent.putExtra("deskripsiKH",results.get(position).getDetail());
			// Pass all data deskripsi

			// Start SingleItemView Class
			context.startActivity(intent);
		}
	};

	@Override
	public int getItemCount() {
		return results.size();
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				final FilterResults oReturn = new FilterResults();
				final List<Result> results2 = new ArrayList<Result>();
				if (orig == null)
					orig = results;
				if (charSequence != null){
					if (results != null & orig.size()>0){
						for (final Result g : orig){
							if (g.getNama().toLowerCase().contains(charSequence.toString()))results2.add(g);
						}
					}
					oReturn.values = results2;
				}
				return oReturn;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				results = (ArrayList<Result>)filterResults.values;
				notifyDataSetChanged();
			}
		};
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		ImageView img_wisata;
		TextView nama_wisata,alamat_wisata,detail_wisata,event_wisata;

		public ViewHolder(View itemView) {
			super(itemView);
			img_wisata = (ImageView)itemView.findViewById(R.id.gambar_konten);
			nama_wisata = (TextView)itemView.findViewById(R.id.nama);
		}
	}
}