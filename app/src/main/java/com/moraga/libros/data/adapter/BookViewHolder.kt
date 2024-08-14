package com.moraga.libros.data.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moraga.libros.R
import com.moraga.libros.data.models.Data
import com.moraga.libros.databinding.ItembookBinding
import com.squareup.picasso.Picasso

class BookViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItembookBinding.bind(view)
    private val btnEdit = binding.btnEdit
    private val btnDelete = binding.btnDel

    fun bind(book: Data, onEditClick: (Data) -> Unit, onDeleteClick: (Data) -> Unit) {
        binding.tvName.text = book.name
        binding.tvAuthor.text = book.author
        binding.tvYear.text = book.year_of_publication
        binding.tvDescription.text = book.description
        binding.tvCategory.text = book.category
        Picasso.get()
            .load(book.image)
            .placeholder(R.drawable.baseline_flag_circle_24)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.ivImage)
        btnEdit.setOnClickListener { onEditClick(book) }
        btnDelete.setOnClickListener { onDeleteClick(book) }

    }


}