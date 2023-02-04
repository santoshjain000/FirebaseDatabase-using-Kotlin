package com.example.firebasedatabase.ui

import android.view.View
import com.example.firebasedatabase.data.Author

interface RecyclerViewClickListener{
    fun onRecycleViewItemClicked(view: View,author: Author)
}