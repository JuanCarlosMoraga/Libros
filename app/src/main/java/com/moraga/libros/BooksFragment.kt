package com.moraga.libros

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.moraga.libros.data.APIService
import com.moraga.libros.data.RetrofitBuilder
import com.moraga.libros.data.models.Data
import com.moraga.libros.databinding.FragmentBooksBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBooksBinding.inflate(layoutInflater)
        initBook()
        return binding.root
    }

    private fun initBook() {
        binding.btnSave.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {
        val context = requireContext()
        CoroutineScope(Dispatchers.IO).launch {
            val book = Data(
                id = 0,
                name = binding.tfName.editText?.text?.trim().toString(),
                author = binding.tfAuthor.editText?.text?.trim().toString(),
                year_of_publication = binding.tfYearOfPublication.editText?.text?.trim().toString(),
                description = binding.tfDescription.editText?.text?.trim().toString(),
                category = binding.tfCategory.editText?.text?.trim().toString(),
                image = binding.tfURLImage.editText?.text?.trim().toString(),
                created_at = Date().toString(),
                updated_at = Date().toString()
            )
            val retrofit = RetrofitBuilder.getRetrofit()
            val call = retrofit.create(APIService::class.java).saveBook("books", book)
            val response = call.body()
            requireActivity().runOnUiThread {
                if (call.isSuccessful && response != null) {
                    Toast.makeText(context, "Registro guardado...", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "Error al guardar...", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}