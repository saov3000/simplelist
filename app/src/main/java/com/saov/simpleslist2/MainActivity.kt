package com.saov.simpleslist2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

/**
 * este app contem os seguintes códigos:
 * criar reconhecimento de voz e envio para a lista do recyclerview
 * arrastar item para a esquerda e retirar da lista
 * salvar lista de objetos em um arquqivo json
 * tooggle para trocar a cor do item da lista
 */

class MainActivity : AppCompatActivity() {

    lateinit var edtProduto: EditText
    lateinit var btnAdd: ImageButton
    lateinit var btnVoice: ImageButton
    lateinit var rcvLista: RecyclerView
    lateinit var fabSave: FloatingActionButton
    lateinit var fabDelete: FloatingActionButton

    var myshared = "mysharedprefs"
    lateinit var produtos: MutableList<Produto>
    var preferences: SharedPreferences? = null
    lateinit var adapterActivity: AdapterActivity
    val ID_VOICE = 100

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()

        if (myshared == null) {
            criarLista()
        } else {
          produtos = listarItens()
            adapterActivity = AdapterActivity(this, produtos)
            rcvLista.adapter = adapterActivity
        }

        btnAdd.setOnClickListener {
            produtos.add(Produto(edtProduto.text.toString(),"#F3071c","#f0a1bf")) //populando a lista de produtos
            adapterActivity.notifyDataSetChanged()
            edtProduto.setText("")
        }

        btnVoice.setOnClickListener {
            var iVoz = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            iVoz.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            iVoz.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak one item")
            startActivityForResult(iVoz, ID_VOICE)
            edtProduto.requestFocus()
        }

        fabSave.setOnClickListener {
            criarLista()
            Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
        }

        fabDelete.setOnClickListener {
            produtos!!.removeAll(produtos)
            adapterActivity.notifyDataSetChanged()
            criarLista()
        }

        afterDeleteItemAtualizeList()
    }

    private fun initComponents() {
        produtos = ArrayList()
        edtProduto = findViewById(R.id.idNomeProduto)
        btnAdd = findViewById(R.id.idBtnAdd)
        btnVoice = findViewById(R.id.idBtnVoice)
        rcvLista = findViewById(R.id.idRcv)
        fabSave = findViewById(R.id.idFabSave)
        fabDelete = findViewById(R.id.iidFabDelete)
        preferences = getSharedPreferences(myshared, MODE_PRIVATE)
        rcvLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun afterDeleteItemAtualizeList() {
        val itemTouchHelper = ItemTouchHelper(AdapterActivity.swipeToDelete(adapterActivity))
        itemTouchHelper.attachToRecyclerView(rcvLista)
    }

    private fun criarLista() {
        val editor: SharedPreferences.Editor = preferences!!.edit()
        val gson = Gson()
        var json = gson.toJson(produtos) //pega a lista de produtos e transforma em json
        editor.putString("LIST", json) // guarda o json no sharedpreferences
        editor.apply()
    }

    private fun listarItens(): MutableList<Produto> {
        var lista: MutableList<Produto> = ArrayList()
        val gson = Gson()
        val json = preferences!!.getString("LIST", lista.toString())
        var type = object : TypeToken<ArrayList<Produto>>() {}.type
        lista = gson.fromJson(json, type)
        return lista
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            var result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var itemVoice = result!!.get(0)
            produtos.add(Produto(itemVoice,"#F3071c","#f0a1bf")) //populando a lista de produtos via voz
            adapterActivity.notifyDataSetChanged()
        }
    }
}
