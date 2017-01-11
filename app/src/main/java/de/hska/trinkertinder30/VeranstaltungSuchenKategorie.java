package de.hska.trinkertinder30;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidiwertowski on 21.12.16.
 */

public class VeranstaltungSuchenKategorie extends AppCompatActivity {


    private DatabaseHelperKategorien helper;
    Contact contact = new Contact();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veranstaltungsuchenkategorie);

        final ListView listView = (ListView)findViewById(R.id.LVKatsSuchen);
        helper = new DatabaseHelperKategorien(this);
        ArrayList<String> array_kategorien = helper.getAllKategorien();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        if(array_kategorien.size() != 0) {
            adapter.addAll(array_kategorien);
            adapter.notifyDataSetChanged();
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(VeranstaltungSuchenKategorie.this, VeranstaltungSuchen.class);

                String kategoriename = (String) listView.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("kategoriename", kategoriename);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_logo);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String username = contact.getUname();
        if (username == "Gast") {
            getMenuInflater().inflate(R.menu.menured, menu);
        } else {
            getMenuInflater().inflate(R.menu.menugreen, menu);
        }

        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        String username = contact.getUname();
        MenuItem bedMenuItem = menu.findItem(R.id.MItemUser);
        bedMenuItem.setTitle(username);

        return super.onPrepareOptionsMenu(menu);

    }
    public void clickItem(MenuItem item) {
        Intent intent = new Intent(VeranstaltungSuchenKategorie.this, Profil.class);
        startActivity(intent);
    }

}
