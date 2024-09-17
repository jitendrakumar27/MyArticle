package com.example.newswatchapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().reference.child("articles")

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rec)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articlesAdapter = ArticlesAdapter(listOf())
        recyclerView.adapter = articlesAdapter

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_nav)

        // Fetch articles from Realtime Database
        fetchArticles()

        // Set up item selection listener for BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle Home tab click (optional)
                    true
                }
                R.id.nav_profile -> {
                    // Handle Profile tab click, redirect to LoginActivity
                    openLoginActivity()
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchArticles() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val articlesList = mutableListOf<Article>()
                for (snapshot in dataSnapshot.children) {
                    val article = snapshot.getValue(Article::class.java)
                    article?.let { articlesList.add(it) }
                }
                // Update RecyclerView with articles data
                articlesAdapter.updateData(articlesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("MainActivity", "Error fetching articles", databaseError.toException())
            }
        })
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
