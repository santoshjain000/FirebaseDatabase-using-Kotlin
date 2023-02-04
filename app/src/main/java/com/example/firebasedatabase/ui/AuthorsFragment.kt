package com.example.firebasedatabase.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedatabase.R
import com.example.firebasedatabase.data.Author
import com.google.android.material.floatingactionbutton.FloatingActionButton



class AuthorsFragment : Fragment(),RecyclerViewClickListener {
    private lateinit var button: FloatingActionButton
    private lateinit var recycler: RecyclerView


    private lateinit var viewModel: AuthorsViewModel
    private val adapter = AuthorsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AuthorsViewModel::class.java)
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_authors, container, false)


        button = view.findViewById(R.id.button_add)
        recycler = view.findViewById(R.id.recycler_view_authors)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler.adapter = adapter

        adapter.listener = this

      // //********When fetch data through query based on condition*******///
        viewModel.fetchFilteredAuthors(4)


       // //********When fetch all data *********////
        // viewModel.fetchAuthors()
        // viewModel.getRealTimeUpdates()

        //For fetching authors
        viewModel.authors.observe(viewLifecycleOwner, Observer {
            adapter.setAuthors(it)
        })

        //for realtime updates
        viewModel.author.observe(viewLifecycleOwner, Observer {
            adapter.addAuthor(it)
        })


        button.setOnClickListener {
            AddAuthorDialogFragment()
                .show(childFragmentManager, "")
        }
    }

    override fun onRecycleViewItemClicked(view: View, author: Author) {
        when (view.id) {
            R.id.button_edit -> {

                EditAuthorDialogFragment(author).show(childFragmentManager, "")
            }
            R.id.button_delete -> {

                AlertDialog.Builder(requireContext()).also {
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)) { dialog, which ->

                            viewModel.deleteAuthor(author)

                    }.create().show()
                }



            }
        }
    }


}