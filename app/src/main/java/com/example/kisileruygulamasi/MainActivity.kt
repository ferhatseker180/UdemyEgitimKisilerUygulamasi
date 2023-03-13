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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var kisilerListe : ArrayList<Kisiler>
    private lateinit var adapter: KisilerAdapter
    private lateinit var kdi : KisilerDaoInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "Kişiler Uygulaması"
        setSupportActionBar(toolbar)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        kdi = ApiUtils.getKisilerDaoInterface()

        tumKisiler()


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

            kdi.kisiEkle(kisi_ad,kisi_tel).enqueue(object : Callback<CRUDCevap>{
                override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {

                    tumKisiler()
                }

                override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {


                }

            })

            Toast.makeText(applicationContext,"${kisi_ad}-${kisi_tel}",Toast.LENGTH_SHORT).show()

        }
        ad.setNegativeButton("İptal") {dialogInterface,i ->

        }
        ad.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        if (query != null) {
            aramaYap(query)
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

    fun tumKisiler() {

        kdi.tumKisiler().enqueue(object : Callback<KisilerCevap>{
            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                if (response != null) {

                    val liste = response.body()!!.kisiler
                    adapter = KisilerAdapter(this@MainActivity,liste,kdi)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }

        })
    }

    fun aramaYap(aramaKelime : String) {

        kdi.kisiAra(aramaKelime).enqueue(object : Callback<KisilerCevap>{
            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                if (response != null) {

                    val liste = response.body()!!.kisiler
                    adapter = KisilerAdapter(this@MainActivity,liste,kdi)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }

        })
    }



}