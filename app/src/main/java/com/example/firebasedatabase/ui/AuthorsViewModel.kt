package com.example.firebasedatabase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasedatabase.data.Author
import com.example.firebasedatabase.data.NODE_AUTHORS
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ChildEventListener as ChildEventListener1
import kotlin.collections.mutableListOf as mutableListOf

class AuthorsViewModel : ViewModel() {

    private val  dbAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)


    //For adding the Author
    private val _result = MutableLiveData<Exception?>()
    val result:LiveData<Exception?>
    get() = _result

    //For Fetching the authors
    private  val _authors = MutableLiveData<List<Author>>()
    val authors:LiveData<List<Author>>
        get() = _authors


    //For Fetching realtime updates on the any author
    private  val _author = MutableLiveData<Author>()
    val author:LiveData<Author>
        get() = _author



    //Function to add Author(Data)
    fun addAuthor(author: Author){
         author.id = dbAuthors.push().key

        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener{

                if(it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }


    private val childEventListener = object : ChildEventListener1 {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            author?.isDeleted = true
            _author.value = author
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

    fun getRealTimeUpdates(){
        dbAuthors.addChildEventListener(childEventListener)
    }


    //Function to fetch Author(Data)
    fun fetchAuthors(){

        dbAuthors.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val authors = mutableListOf<Author>()

                    for (authorSnapshot in snapshot.children){
                        val  author = authorSnapshot.getValue(Author::class.java)
                        author?.id = authorSnapshot.key
                        author?.let { authors.add(it) }
                    }
                    _authors.value = authors
                }
            }
        })


    }

    //Function to update Author(Data)
    fun updateAuthor(author: Author){


        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener{

                if(it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }


    //Function to delete Author(Data)
    fun deleteAuthor(author: Author){


        dbAuthors.child(author.id!!).setValue(null)
            .addOnCompleteListener{

                if(it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception
                }
            }
    }


    //Function to Query Author(Data) on different conditions
    fun fetchFilteredAuthors(index: Int) {
        val dbAuthors =
            when (index) {
                1 ->
                    //#1 SELECT * FROM Authors
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)

                2 ->
                    //#2 SELECT * FROM Authors WHERE id = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .child("-NNSG2jyGVkusHytOgxD")

                3 ->
                    //#3 SELECT * FROM Authors WHERE city = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("city")
                        .equalTo("Hyderabad")

                4 ->
                    //#4 SELECT * FROM Authors LIMIT 2
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .limitToFirst(2)

                5 ->
                    //#5 SELECT * FROM Authors WHERE votes < 500
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("votes")
                        .endAt(500.toDouble())

                6 ->
                    //#6 SELECT * FROM Artists WHERE name LIKE "A%"
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("name")
                        .startAt("A")
                        .endAt("A\uf8ff")

                7 ->//This is not possible means in firebase database we can't query with multiple conditions
                    //#7 SELECT * FROM Artists Where votes < 500 AND city = Bangalore
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                else -> FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
            }

        dbAuthors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                //For single data
//                if(snapshot.exists()){
//                    _author.value = snapshot.getValue(Author::class.java)
//                }

                //For multiple data
                if (snapshot.exists()) {
                    val authors = mutableListOf<Author>()
                    for (authorSnapshot in snapshot.children) {
                        val author = authorSnapshot.getValue(Author::class.java)
                        author?.id = authorSnapshot.key
                        author?.let { authors.add(it) }
                    }
                    _authors.value = authors
                }
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        dbAuthors.removeEventListener(childEventListener)
    }
}