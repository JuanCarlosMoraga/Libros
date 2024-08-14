package com.moraga.libros

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.moraga.libros.data.APIService
import com.moraga.libros.data.RetrofitBuilder
import com.moraga.libros.data.models.Data
import com.moraga.libros.databinding.FragmentBooksBinding
import com.moraga.libros.databinding.FragmentBooksEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class BooksEditFragment : Fragment() {
    private lateinit var binding: FragmentBooksEditBinding
    private var bookId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBooksEditBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = arguments ?: return
        bookId = arguments.getInt("book_id")
        loadBook(bookId)

        binding.btnUpdate.setOnClickListener {
            updateBook()
        }
    }

    private fun updateBook() {
        CoroutineScope(Dispatchers.IO).launch {
            val book = Data(
                id = bookId,
                name = binding.tfNameEd.editText?.text?.trim().toString(),
                author = binding.tfAuthor.editText?.text?.trim().toString(),
                year_of_publication = binding.tfYearOfPublication.editText?.text?.trim().toString(),
                description = binding.tfDescriptionEd.editText?.text?.trim().toString(),
                category = binding.tfCategoryEd.editText?.text?.trim().toString(),
                image = binding.tfURLImageEd.editText?.text?.trim().toString(),
                created_at = Date().toString(),
                updated_at = Date().toString()
            )

            val retrofit = RetrofitBuilder.getRetrofit()
            val call = retrofit.create(APIService::class.java).updateBook("book", book.id, book)
            val response = call.body()
            requireActivity().runOnUiThread {
                if(call.isSuccessful && response != null){
                    Toast.makeText(context, "Registro actualizado...", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }else{
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadBook(id: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val retrofit = RetrofitBuilder.getRetrofit()
                val apiService = retrofit.create(APIService::class.java)
                val response = apiService.showBook("book", id)
                if (response.isSuccessful) {
                    val book = response.body()
                    if (book != null) {
                        Log.e("Data", "Book data received: $book")

                        // Actualiza la UI con los datos de la ciudad en el hilo principal
                        binding.tfNameEd.editText?.setText(book.name)
                        binding.tfAuthor.editText?.setText(book.author)
                        binding.tfYearOfPublication.editText?.setText(book.year_of_publication)
                        binding.tfDescriptionEd.editText?.setText(book.description)
                        binding.tfCategoryEd.editText?.setText(book.category)
                        binding.tfURLImageEd.editText?.setText(book.image)
                    } else {
                        Toast.makeText(context, "No data found for the book ", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                Log.e("Error", "Exception: ${ex.message}")
                Toast.makeText(context, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

}