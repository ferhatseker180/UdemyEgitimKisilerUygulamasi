package com.example.kisileruygulamasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var kisilerListe : ArrayList<Kisiler>
    private lateinit var adapter: KisilerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Kişiler Uygulaması"
        setSupportActionBar(toolbar)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        kisilerListe = ArrayList()
        val kisi1 = Kisiler(1,"Ferhat","12345678910")
        val kisi2 = Kisiler(2,"Fer","12345678")

        kisilerListe.add(kisi1)
        kisilerListe.add(kisi2)

        adapter = KisilerAdapter(this,kisilerListe)
        recyclerView.adapter = adapter


        fab.setOnClickListener {
            alertGoster()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)

        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return true
    }


    fun alertGoster() {
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        val ad = AlertDialog.Builder(this)

        ad.setTitle("Kişi Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle") {dialogInterface,i ->
            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            Toast.makeText(applicationContext,"${kisi_ad}-${kisi_tel}",Toast.LENGTH_SHORT).show()

        }
        ad.setNegativeButton("İptal") {dialogInterface,i ->

        }
        ad.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        if (query != null) {
            Log.e("Gönderilen Arama",query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText != null) {
            Log.e("Harf Girdikçe",newText)
        }
        return true

    }
}