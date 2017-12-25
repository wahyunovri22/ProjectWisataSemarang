package com.example.cia.projectwisatasemarang;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cia.projectwisatasemarang.Adapter.RecyclerViewAdapter;
import com.example.cia.projectwisatasemarang.Json.RegisterApi;
import com.example.cia.projectwisatasemarang.Json.Result;
import com.example.cia.projectwisatasemarang.Json.Value;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class KontenActivity extends AppCompatActivity {

	private static Retrofit retrofit = null;
	List<Result> ListOfdataAdapter;

	RecyclerView recyclerView;

	public String HTTP_JSON_URL;

	public static String Image_Name_JSON = "nama";
	public static String IMAGE_URL_JSON = "gambar";
	public static String ALAMAT = "alamat";
	public static String DESKRIPSI = "deskripsi";
	public static String EVENT = "event";
	public static String LATITUDE = "latitude";
	public static String LONGITUDE = "longitude";


	JsonArrayRequest RequestOfJSonArray;

	RequestQueue requestQueue;

	RecyclerView.LayoutManager layoutManagerOfrecyclerView;

	RecyclerViewAdapter recyclerViewadapter;

	ArrayList<String> ImageTitleNameArrayListForClick;

	List<String> listaru;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_konten);

		HTTP_JSON_URL = getIntent().getStringExtra("url");

		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

		ImageTitleNameArrayListForClick = new ArrayList<>();

		ListOfdataAdapter = new ArrayList<>();
		listaru = new ArrayList<>();

		recyclerView.setHasFixedSize(true);

		layoutManagerOfrecyclerView = new LinearLayoutManager(this);

		recyclerView.setLayoutManager(layoutManagerOfrecyclerView);

		JSON_HTTP_CALL();

	}

	public void JSON_HTTP_CALL() {
		RequestOfJSonArray = new JsonArrayRequest(HTTP_JSON_URL,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						ParseJSonResponse(response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		requestQueue = Volley.newRequestQueue(KontenActivity.this);
		requestQueue.add(RequestOfJSonArray);
	}

	public void ParseJSonResponse(JSONArray array) {
		for (int i = 0; i < array.length(); i++) {

			//Result GetDataAdapter2 = new Result();
			Result GetData = new Result();
			//HashMap<String, String> map = new HashMap<String, String>();

			JSONObject json = null;
			try {
				json = array.getJSONObject(i);

				//GetDataAdapter2.setNama(json.getString(Image_Name_JSON));
				GetData.setNama(json.getString(Image_Name_JSON));

				// Adding image title name in array to display on RecyclerView click event.
				ImageTitleNameArrayListForClick.add(json.getString(Image_Name_JSON));

				//GetDataAdapter2.setGambar(json.getString(IMAGE_URL_JSON));

				GetData.setGambar(json.getString(IMAGE_URL_JSON));
				GetData.setAlamat(json.getString(ALAMAT));
				GetData.setDetail(json.getString(DESKRIPSI));
				GetData.setEvent(json.getString(EVENT));
				GetData.setLatitude(json.getDouble(LATITUDE));
				GetData.setLongitude(json.getDouble(LONGITUDE));

				listaru.add(json.getString(IMAGE_URL_JSON));

			} catch (JSONException e) {
				e.printStackTrace();
			}
			ListOfdataAdapter.add(GetData);
		}

		recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter, this);

		recyclerView.setAdapter(recyclerViewadapter);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);
		MenuItem item = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
		SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setIconifiedByDefault(true);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				recyclerViewadapter.getFilter().filter(newText);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
}
