package com.example.kisileruygulamasi

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KisilerAdapter(private val mContext : Context,
                     private var kisilerListe:List<Kisiler>,
                     private val kdi : KisilerDaoInterface)
    : RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(tasarim : View) : RecyclerView.ViewHolder(tasarim) {

        var textViewKisiBilgi : TextView
        var imageViewMore : ImageView

        init {
            textViewKisiBilgi = tasarim.findViewById(R.id.textViewKisiBilgi)
            imageViewMore = tasarim.findViewById(R.id.imageViewMore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {

        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.kisiler_card_tasarim,parent,false)
        return CardTasarimTutucu(tasarim)

    }

    override fun getItemCount(): Int {

        return kisilerListe.size

    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {

        val kisi = kisilerListe.get(position)

        holder.textViewKisiBilgi.text = "${kisi.kisiAd}-${kisi.kisi_tel}"
        holder.imageViewMore.setOnClickListener {

            val popupMenu = PopupMenu(mContext,holder.imageViewMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->

                when(item.itemId) {
                    R.id.action_sil -> {

                        Snackbar.make(holder.textViewKisiBilgi,"${kisi.kisiAd} Silinsin mi?",Snackbar.LENGTH_SHORT)
                            .setAction("EVET") {

                                kdi.kisiSil(kisi.kisiId).enqueue(object :
                                    Callback<CRUDCevap> {
                                    override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {

                                        tumKisiler()
                                    }

                                    override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {


                                    }

                                })
                            }
                            .show()

                        true
                    }
                    R.id.action_guncelle -> {
                        alertGoster(kisi)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

    }

    fun alertGoster(kisi:Kisiler) {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        editTextAd.setText(kisi.kisiAd)
        editTextTel.setText(kisi.kisi_tel)

        val ad = AlertDialog.Builder(mContext)

        ad.setTitle("Kişi Güncelle")
        ad.setView(tasarim)
        ad.setPositiveButton("Güncelle") {dialogInterface,i ->
            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            kdi.kisiGuncelle(kisi.kisiId,kisi_ad,kisi_tel).enqueue(object :
                Callback<CRUDCevap> {
                override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {

                    tumKisiler()
                }

                override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {


                }

            })

            Toast.makeText(mContext,"${kisi_ad}-${kisi_tel}",Toast.LENGTH_SHORT).show()

        }
        ad.setNegativeButton("İptal") {dialogInterface,i ->

        }
        ad.create().show()
    }

    fun tumKisiler() {

        kdi.tumKisiler().enqueue(object : Callback<KisilerCevap>{
            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                if (response != null) {

                    kisilerListe = response.body()!!.kisiler

                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }

        })
    }

}