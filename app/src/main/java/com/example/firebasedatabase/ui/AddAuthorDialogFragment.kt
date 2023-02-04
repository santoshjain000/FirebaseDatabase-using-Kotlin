package com.example.firebasedatabase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasedatabase.R
import com.example.firebasedatabase.data.Author


class AddAuthorDialogFragment : DialogFragment() {
    private lateinit var viewModel: AuthorsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AuthorsViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_add_author_dialog, container, false)
        return view  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.author_added)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        val btn_click_me = dialog?.findViewById(R.id.button_add) as Button


        btn_click_me.setOnClickListener{
            val edit_text_name = dialog?.findViewById(R.id.edit_text_name) as EditText

            val name = edit_text_name.text.toString().trim()


           val author = Author()
            author.name = name
            viewModel.addAuthor(author)
        }




    }

}

