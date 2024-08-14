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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.moraga.libros.data.APIService
import com.moraga.libros.data.RetrofitBuilder
import com.moraga.libros.data.adapter.BookAdapter
import com.moraga.libros.data.models.Data
import com.moraga.libros.databinding.FragmentListBooksBinding

class ListBooksFragment : Fragment() {
    private lateinit var binding: FragmentListBooksBinding
    private lateinit var adapter: BookAdapter
    private val listado = mutableListOf<Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBooksBinding.inflate(inflater, container, false)

        initView()
        initRecyclerView()
        showBooks()

        return binding.root
    }

    private fun initView() {
        binding.btnAdd.setOnClickListener {
            goNewBook()
        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            searchBooks(query)
        }
    }

    private fun initRecyclerView() {
        adapter = BookAdapter(listado, this::onEditClick, this::onDeleteClick)

        binding.RvLstBooks.layoutManager = LinearLayoutManager(context)
        binding.RvLstBooks.adapter = adapter
    }

    private fun onDeleteClick(data: Data) {
        val context = requireContext()

        lifecycleScope.launch {
            try {
                val retrofit = RetrofitBuilder.getRetrofit()
                val call = retrofit.create(APIService::class.java).deleteBook("book", data.id)
                val response = call.body()
                requireActivity().runOnUiThread {
                    if (call.isSuccessful) {
                        listado.remove(data)
                        adapter.notifyDataSetChanged()
                    } else Toast.makeText(context, "Error al eliminar", Toast.LENGTH_LONG).show()
                    Toast.makeText(context, "Registro eliminado...", Toast.LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                Log.e("errojd", ex.toString())
                Toast.makeText(context, "erro ${ex.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onEditClick(data: Data) {
        val bundle = Bundle()
        bundle.putInt("book_id", data.id)
        findNavController().navigate(R.id.action_listBooksFragment_to_booksFragment, bundle)
    }

    private fun showBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit = RetrofitBuilder.getRetrofit()
            val call = retrofit.create(APIService::class.java).listBooks("books")
            val response = call.body()
            requireActivity().runOnUiThread {
                if (call.isSuccessful && response != null) {
                    val listaBooks = response.data.toMutableList()
                    listado.clear()
                    listado.addAll(listaBooks)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("listabook", "Hubo un error")
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
    }

    private fun goNewBook() {
        findNavController().navigate(R.id.action_listBooksFragment_to_booksEditFragment)
    }

    private fun searchBooks(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val retrofit = RetrofitBuilder.getRetrofit()
                val call = retrofit.create(APIService::class.java).listBooks("books", query)
                val response = call.body()
                requireActivity().runOnUiThread {
                    if (call.isSuccessful && response != null) {
                        Log.e("listabook", "ok")
                        val listaBooks = response.data.toMutableList()
                        listado.clear()
                        listado.addAll(listaBooks)
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("listabook", "Hubo un error")
                        showError()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Failure: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}