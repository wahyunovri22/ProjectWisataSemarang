package com.example.cia.projectwisatasemarang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
	private CardView card1;
	private CardView card2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
		card2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "https://seputarwisatasemarang.000webhostapp.com/api/data/getwisata.php";
				Intent goDaftarwisata = new Intent(HomeActivity.this, KontenActivity.class);
				goDaftarwisata.putExtra("url",url);
				startActivity(goDaftarwisata);
			}
		});

		card1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "https://seputarwisatasemarang.000webhostapp.com/api/data/getwisataalam.php";
				Intent goDaftarwisata = new Intent(HomeActivity.this, KontenActivity.class);
				goDaftarwisata.putExtra("url",url);
				startActivity(goDaftarwisata);
			}
		});

	}

	private void initView() {
		card1 = (CardView) findViewById(R.id.card1);
		card2 = (CardView) findViewById(R.id.card2);
	}
}
