package com.saov.simpleslist2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper


class AdapterActivity(var contexto: Context, var lista: MutableList<Produto>?)
    : RecyclerView.Adapter<AdapterActivity.ViewHolder>() {

    private lateinit var hex:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(contexto).inflate(R.layout.activity_adapter,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       initItems(holder,position)

        holder.card.setOnClickListener{
            val cor = Color.parseColor(lista!![position].corFundo) //pegar cor de fundo direto do item exibido na tela
            hex = Integer.toHexString(cor) //converter número para hexadecimal argb
            toggleColors(holder,position)
        }
    }

    override fun getItemCount(): Int = lista!!.size

    class ViewHolder(item:View):RecyclerView.ViewHolder(item){
        val nome = itemView.findViewById<TextView>(R.id.idProduto)
        val card = itemView.findViewById<CardView>(R.id.idCardView)
    }

    class swipeToDelete(var adapter: AdapterActivity) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.adapterPosition
            adapter.deleteItem(pos) //método que deleta item da tela
        }
    }

    private fun deleteItem(pos: Int) { //método usado dentro do swuipeToDelete
        lista!!.removeAt(pos) //deleta item da lista
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos,lista!!.size)
        notifyDataSetChanged()
    }

    private fun initItems(holder: ViewHolder,position: Int) {
        holder.nome.text = lista!![position].nome //exibe o nome
        holder.nome.setBackgroundColor(Color.parseColor(lista!![position].corFundo))
        holder.nome.setTextColor(Color.parseColor(lista!![position].corTexto))
    }

    private fun toggleColors(holder: ViewHolder,position: Int) {
        if(hex == "fff0a1bf"){ //rosa
            holder.nome.setBackgroundColor(Color.parseColor("#7ff689")) //verde claro
            holder.nome.setTextColor(Color.parseColor("#31b539")) //verde escuro
            lista!![position].corFundo = "#7ff689"
            lista!![position].corTexto = "#31b539"
            hex = "ff7ff689"
        }else{
            holder.nome.setBackgroundColor(Color.parseColor("#f0a1bf")) //rosa
            holder.nome.setTextColor(Color.parseColor("#F3071c")) //vermelho
            lista!![position].corFundo = "#f0a1bf"
            lista!![position].corTexto = "#F3071c"
            hex = "fff0a1bf"
        }
    }
}


