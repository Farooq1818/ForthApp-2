package com.example.forthapp_2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forthapp_2.databinding.RvItemBinding

class Rv_Adapter(var context: Context, var list: ArrayList<User>) : RecyclerView.Adapter<Rv_Adapter.ViewHolder>() {

    class ViewHolder(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleTextRv.text = list[position].Title
        holder.binding.DescTextRv.text = list[position].Descript
    }
}