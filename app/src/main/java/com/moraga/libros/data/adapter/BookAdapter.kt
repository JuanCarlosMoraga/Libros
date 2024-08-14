package com.moraga.libros.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moraga.libros.R
import com.moraga.libros.data.models.Data

class BookAdapter(val lstBooks: MutableList<Data>,
                  private val onEditClick: (Data) -> Unit,
                  private val onDeleteClick: (Data) -> Unit): RecyclerView.Adapter<BookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = LayoutInflater.from(parent.context)
        return BookViewHolder(binding.inflate(R.layout.itembook, parent, false))
    }

    override fun getItemCount(): Int = lstBooks.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = lstBooks[position]
        holder.bind(item, onEditClick, onDeleteClick)
    }
}