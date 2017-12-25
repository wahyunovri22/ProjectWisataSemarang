package com.example.cia.projectwisatasemarang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cia.projectwisatasemarang.Adapter.RecyclerViewAdapterKuliner;
import com.example.cia.projectwisatasemarang.Json.Result;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class DeatailKontenActivity extends AppCompatActivity {
    RecyclerView recyclerView, recyclerViewPenginapan;

    ImageView imageViewDeatail;
    TextView nama_detail,alamat_detail,detail_detail,event_detail;
    String Mnama;
    String Malamat;
    String Mdetail;
    String Mgambar;
    String Mevent;
    Double mLat;
    Double mLong;
    Button btnMaps;
    Double distance;
    static double PI_RAD = Math.PI / 180.0;

    private static String HTTP_JSON_URL_KULINER = "https://seputarwisatasemarang.000webhostapp.com/api/data/getkuliner.php";
    private static String HTTP_JSON_URL_PENGINAPAN = "https://seputarwisatasemarang.000webhostapp.com/api/data/getpenginapan.php";
    private static String Image_Name_JSON_KUILINER = "nama";
    private static String IMAGE_URL_JSON_KULINER = "gambar";
    private static String ALAMAT_KULINER = "alamat";
    private static String DESKRIPSI_KULINER = "deskripsi";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";

    JsonArrayRequest RequestOfJSONArrayKuliner, RequestOfJSONArrayPenginapan;

    RequestQueue requestQueueKuliner, requestQueuePenginapan;

    RecyclerView.LayoutManager layoutManagerOfRecyclerViewKuliner, layoutManagerPenginapan;

    RecyclerViewAdapterKuliner recyclerViewAdapterKuliner, recyclerViewAdapterPenginapan;

    ArrayList<String> ImageTitleNameArrayListForClickKuliner;
    List<String> listaru;
    List<Result> ListOfdataAdapter, ListPenginapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);

        Intent i = getIntent();

        Mgambar = i.getStringExtra("gambar");
        //get data gambar
        Mnama = i.getStringExtra("nama");
        // get data nama
        Malamat = i.getStringExtra("alamat");
        // get data alamat
        Mdetail = i.getStringExtra("deskripsi");
        // get data deskripsi
        Mevent = i.getStringExtra("event");
        //get data event
        mLat = i.getDoubleExtra("lat",0);
        mLong = i.getDoubleExtra("long",0);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewKuliner);
        recyclerViewPenginapan = (RecyclerView)findViewById(R.id.recyclerViewPenginapan);

        ImageTitleNameArrayListForClickKuliner = new ArrayList<>();
        ListOfdataAdapter = new ArrayList<>();
        ListPenginapan = new ArrayList<>();

        listaru = new ArrayList<>();

        recyclerView.setHasFixedSize(true);

        layoutManagerOfRecyclerViewKuliner = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManagerOfRecyclerViewKuliner);

        recyclerViewPenginapan.setHasFixedSize(true);
        layoutManagerPenginapan = new LinearLayoutManager(this);
        recyclerViewPenginapan.setLayoutManager(layoutManagerPenginapan);

        imageViewDeatail = (ImageView)findViewById(R.id.gambar_konten);
        nama_detail = (TextView) findViewById(R.id.namalabel);
        alamat_detail = (TextView)findViewById(R.id.alamatlabel);
        detail_detail = (TextView)findViewById(R.id.detaillabel);
        event_detail = (TextView)findViewById(R.id.eventlabel);

        Picasso.with(this).load(Mgambar).into(imageViewDeatail);
        btnMaps = (Button)findViewById(R.id.bt_go_maps);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=+"+Mnama+"+Semarang"));
                startActivity(intent);
            }
        });

        nama_detail.setText(Mnama);
        alamat_detail.setText(Malamat);
        detail_detail.setText(Mdetail);
        event_detail.setText(Mevent);

        JSON_HTTP_KULINER_CALL();
        JSON_HTTP_KULINER_CALL_PENGINNAPAN();
    }

    public void JSON_HTTP_KULINER_CALL(){
        RequestOfJSONArrayKuliner = new JsonArrayRequest(HTTP_JSON_URL_KULINER,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ParseJSonResponse2(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueueKuliner = Volley.newRequestQueue(DeatailKontenActivity.this);
        requestQueueKuliner.add(RequestOfJSONArrayKuliner);
    }

    public void JSON_HTTP_KULINER_CALL_PENGINNAPAN(){
        RequestOfJSONArrayPenginapan = new JsonArrayRequest(HTTP_JSON_URL_PENGINAPAN,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ParseJSonResponsePenginapan(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueuePenginapan = Volley.newRequestQueue(DeatailKontenActivity.this);
        requestQueuePenginapan.add(RequestOfJSONArrayPenginapan);
    }

    public void ParseJSonResponse2(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            Result GetDataAdapter2 = new Result();
            Result GetData = new Result();
            //HashMap<String, String> map = new HashMap<String, String>();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setNama(json.getString(Image_Name_JSON_KUILINER));
                GetData.setNama(json.getString(Image_Name_JSON_KUILINER));

                // Adding image title name in array to display on RecyclerView click event.
                ImageTitleNameArrayListForClickKuliner.add(json.getString(Image_Name_JSON_KUILINER));

                GetDataAdapter2.setGambar(json.getString(IMAGE_URL_JSON_KULINER));

                GetData.setGambar(json.getString(IMAGE_URL_JSON_KULINER));
                GetData.setAlamat(json.getString(ALAMAT_KULINER));
                GetData.setDetail(json.getString(DESKRIPSI_KULINER));
                GetData.setLatitude(json.getDouble(LATITUDE));
                GetData.setLongitude(json.getDouble(LONGITUDE));

                getDistance(mLat,mLong,json.getDouble(LATITUDE),json.getDouble(LONGITUDE));

                listaru.add(json.getString(IMAGE_URL_JSON_KULINER));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (distance <= 1){
                ListOfdataAdapter.add(GetData);
            }
        }

        recyclerViewAdapterKuliner = new RecyclerViewAdapterKuliner(ListOfdataAdapter, this);
        recyclerView.setAdapter(recyclerViewAdapterKuliner);
    }

    public void ParseJSonResponsePenginapan(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            Result GetDataAdapter2 = new Result();
            Result GetDataPenginapan = new Result();
            //HashMap<String, String> map = new HashMap<String, String>();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setNama(json.getString(Image_Name_JSON_KUILINER));
                GetDataPenginapan.setNama(json.getString(Image_Name_JSON_KUILINER));

                // Adding image title name in array to display on RecyclerView click event.
                ImageTitleNameArrayListForClickKuliner.add(json.getString(Image_Name_JSON_KUILINER));

                GetDataAdapter2.setGambar(json.getString(IMAGE_URL_JSON_KULINER));

                GetDataPenginapan.setGambar(json.getString(IMAGE_URL_JSON_KULINER));
                GetDataPenginapan.setAlamat(json.getString(ALAMAT_KULINER));
                GetDataPenginapan.setDetail(json.getString(DESKRIPSI_KULINER));
                GetDataPenginapan.setLatitude(json.getDouble(LATITUDE));
                GetDataPenginapan.setLongitude(json.getDouble(LONGITUDE));

                getDistance(mLat,mLong,json.getDouble(LATITUDE),json.getDouble(LONGITUDE));

                listaru.add(json.getString(IMAGE_URL_JSON_KULINER));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (distance <= 1){
                ListPenginapan.add(GetDataPenginapan);
            }
        }

        recyclerViewAdapterPenginapan = new RecyclerViewAdapterKuliner(ListPenginapan, this);
        recyclerViewPenginapan.setAdapter(recyclerViewAdapterPenginapan);
    }

    public Double getDistance(Double firstLat, Double firstLong, Double secondLat, Double secondLong){
        double phi1 = firstLat * PI_RAD;
        double phi2 = secondLat * PI_RAD;
        double lam1 = firstLong * PI_RAD;
        double lam2 = secondLong * PI_RAD;

        distance = 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));

        return distance;
    }
}
