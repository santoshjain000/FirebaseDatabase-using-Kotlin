package com.example.firebasedatabase.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedatabase.R
import com.example.firebasedatabase.data.Author


class AuthorsAdapter : RecyclerView.Adapter<AuthorsAdapter.AuthorViewModel>() {

    private var authors = mutableListOf<Author>()
   var listener: RecyclerViewClickListener? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AuthorViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_author, parent, false)
    )


    override fun getItemCount() = authors.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AuthorViewModel, position: Int) {
        holder.textView.text = authors[position].name
        holder.text_view_city_votes.text =
            "${authors[position].city} | Votes : ${authors[position].votes}"
        holder.button_edit.setOnClickListener {
            listener?.onRecycleViewItemClicked(it, authors[position])
        }
        holder.button_delete.setOnClickListener {
            listener?.onRecycleViewItemClicked(it, authors[position])
        }
    }

    fun setAuthors(authors: List<Author>) {
        this.authors = authors as MutableList<Author>
        notifyDataSetChanged()
    }

    fun addAuthor(author: Author) {
        if (!authors.contains(author)) {
            authors.add(author)
        }
        else {
            val index = authors.indexOf(author)
            if (author.isDeleted) {
                authors.removeAt(index)
            } else {
                authors[index] = author
            }
        }

        notifyDataSetChanged()
    }

    class AuthorViewModel(val view: View) : RecyclerView.ViewHolder(view){

        val textView: TextView = view.findViewById(R.id.text_view_name)
        val button_edit: ImageButton = view.findViewById(R.id.button_edit)
        val button_delete: ImageButton = view.findViewById(R.id.button_delete)
        val text_view_city_votes: TextView = view.findViewById(R.id.text_view_city_votes)


    }


}