package com.example.mesutgungor.drawer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Haber> haberlistesi = new ArrayList<Haber>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Gündem");
        setSupportActionBar(toolbar);
        ListView lv = (ListView) findViewById(R.id.haberlistesi);
        ArrayList<Haber> haberlistesi = new ArrayList<Haber>();
        new getirHaberleri().execute("gundem");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.gundem) {
            new getirHaberleri().execute("gundem");
            toolbar.setTitle("Gündem");
        } else if (id == R.id.magazin) {
            new getirHaberleri().execute("magazin");
            toolbar.setTitle("Magazin");
        } else if (id == R.id.spor) {
            new getirHaberleri().execute("spor");
            toolbar.setTitle("Spor");

        } else if (id == R.id.ekonomi) {
            new getirHaberleri().execute("ekonomi");
            toolbar.setTitle("Ekonomi");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class getirHaberleri extends AsyncTask<String, Integer, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {
            int i=0;
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;

            //Try bloğunda hurriyet apisine bağlanıyoruz ve haberleri içeren json stringini oluşturuyoruz.
            try {
                URL url = new URL(Config.BaseUrl + "articles?apikey=" + Config.ApiKey+"&$filter=Path eq '/"+args[0]+"/'");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                Integer response=urlConnection.getResponseCode();

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    //i=i+100;
                    // sleep(10000);
                }
                urlConnection.disconnect();

            }catch( Exception e) {

                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();

        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {

                //Try Bloğunda json stringini parse ediyoruz.

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    haberlistesi.removeAll(haberlistesi);

                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if (c != null) {

                                String haberid    = c.getString("Id");
                                String habertarihi = c.getString("CreatedDate");
                                String habericerigi = c.getString("Description");
                                String haberkategorisi = c.getString("Path");
                                String haberbasligi = c.getString("Title");
                                String haberurl = c.getString("Url");
                                String habermetni ="Haber";

                                JSONArray files = c.getJSONArray("Files");
                                String haberresmiurl = "";
                                if (files.length() != 0) {
                                    JSONObject joFiles = files.getJSONObject(0);
                                    if (joFiles != null) {

                                        haberresmiurl = joFiles.getString("FileUrl");
                                    }
                                } else {

                                    haberresmiurl ="@drawable/sondakika";

                                }

                              //  Haber haberin = new Haber(haberid,haberresmiurl,  haberkategorisi,  habericerigi, haberurl, habertarihi, haberbasligi);
                                Haber haberin = new Haber( haberid,  haberresmiurl,  haberbasligi,  habertarihi,  haberkategorisi,  habericerigi,  haberurl,habermetni);

                                haberlistesi.add(haberin);
                            }
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "JSON Hatası" ,
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "JSON Bağlantı Hatası.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }


            if(haberlistesi.size()>0)
            {
                ListView lv = (ListView)findViewById(R.id.haberlistesi);
                HaberAdapter ha = new HaberAdapter(getApplicationContext(),R.layout.list_view_row_model,haberlistesi);
                lv.setAdapter(ha);


                lv.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String haberurl = haberlistesi.get(position).getHaberUrl();
                                String haberid = haberlistesi.get(position).getHaberid();
                                String haberbasligi=haberlistesi.get(position).getHaberbasligi();
                                String haberdesc = haberlistesi.get(position).getHaberinicerigi();
                                String habermetni = haberlistesi.get(position).getHaberMetni();
                                if(habermetni=="Haber") {
                                    new getirHaberIcerigi().execute(Integer.parseInt(haberid), position);
                                }else {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(haberbasligi)
                                            .setMessage(Html.fromHtml(habermetni))
                                            .setCancelable(true)
                                            .setNegativeButton("Kapat",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    })
                                            .show();


                                }



                            }
                        }
                );

                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int position, long id) {

                        Intent sharingIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = haberlistesi.get(position).getHaberinicerigi()+"\n Kaynak:"+haberlistesi.get(position).getHaberUrl();
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                haberlistesi.get(position).getHaberbasligi());

                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Paylaş"));
                        return true;

                    }
                });


            }

/*            // 5 Dakikada bir haberleri getirmek için eklenen kod.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getirHaberleri().execute();
                }
            }, 5*60*1000);*/


        }


    }

    public class Haberci
    {
        public String mHabermetni;
        public int mHaberIndexi;
    }

    public class getirHaberIcerigi extends AsyncTask<Integer, Integer, Haberci> {


        @Override
        protected Haberci doInBackground(Integer... params) {
            StringBuilder mesult = new StringBuilder();
            HttpURLConnection urlConnection = null;
            Haberci haberci = new Haberci();

            //Try bloğunda hurriyet apisine bağlanıyoruz ve haberleri içeren json stringini oluşturuyoruz.
            try {
                URL url = new URL(Config.BaseUrl + "articles/" + params[0].toString() + "?apikey=" + Config.ApiKey);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                Integer response = urlConnection.getResponseCode();

                String line;
                while ((line = reader.readLine()) != null) {
                    mesult.append(line);
                    //i=i+100;
                    // sleep(10000);
                }
                urlConnection.disconnect();

            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            haberci.mHabermetni=mesult.toString();
            haberci.mHaberIndexi=params[1];

            return haberci;

        }

        @Override
        protected void onPostExecute(Haberci haberci) {
            String habermetni="";
            if (haberci.mHabermetni!= null) {
                try {
                    JSONObject jsonobject = new JSONObject(haberci.mHabermetni);
                    if (jsonobject != null) {
                            habermetni = jsonobject.getString("Text");
                            haberlistesi.get(haberci.mHaberIndexi).setHaberMetni(habermetni);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(haberlistesi.get(haberci.mHaberIndexi).getHaberbasligi())
                                .setMessage(Html.fromHtml(habermetni))
                                .setCancelable(true)
                                .setNegativeButton("Kapat",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       dialog.cancel();
                                    }
                                })
                                .show();
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "JSON Hatası",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });


                }


            }
        }
    }

}
