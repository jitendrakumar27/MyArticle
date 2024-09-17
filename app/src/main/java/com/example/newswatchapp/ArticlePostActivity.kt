package com.example.newswatchapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ArticlePostActivity : AppCompatActivity() {

    private lateinit var articleImage: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var etArticleHeading: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etArticleTags: EditText
    private lateinit var etVideoLink: EditText
    private lateinit var etArticleContent: EditText
    private lateinit var btnPostArticle: Button

    private lateinit var db: FirebaseFirestore // RealTime firebase data base
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data?.data != null) {
                imageUri = result.data?.data
                articleImage.setImageURI(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_post)

        // Initialize Firebase Firestore and Storage
        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        // Initialize UI components
        articleImage = findViewById(R.id.article_image)
        btnUploadImage = findViewById(R.id.btn_upload_image)
        etArticleHeading = findViewById(R.id.et_article_heading)
        spinnerCategory = findViewById(R.id.spinner_category)
        etArticleTags = findViewById(R.id.et_article_tags)
        etVideoLink = findViewById(R.id.et_video_link)
        etArticleContent = findViewById(R.id.et_article_content)
        btnPostArticle = findViewById(R.id.btn_post_article)

        // Set up button click listeners
        btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }

        btnPostArticle.setOnClickListener {
            handlePostArticle()
        }
    }

    private fun handlePostArticle() {
        // Get user input
        val heading = etArticleHeading.text.toString().trim()
        val tags = etArticleTags.text.toString().trim()
        val videoLink = etVideoLink.text.toString().trim()
        val content = etArticleContent.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()

        // Validate input
        if (heading.isEmpty() || content.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image.", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image to Firebase Storage
        val imageRef = storageRef.child("article_images/${UUID.randomUUID()}")
        imageRef.putFile(imageUri!!).addOnSuccessListener {
            // Get image download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                // Save article details to Firestore
                saveArticleToFirestore(heading, category, tags, videoLink, content, imageUrl)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to get image URL.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveArticleToFirestore(heading: String, category: String, tags: String, videoLink: String, content: String, imageUrl: String) {
        val article = hashMapOf(
            "heading" to heading,
            "category" to category,
            "tags" to tags,
            "videoLink" to videoLink,
            "content" to content,
            "imageUrl" to imageUrl
        )

        db.collection("articles").add(article)
            .addOnSuccessListener {
                Toast.makeText(this, "Article posted successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to post article.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        etArticleHeading.text.clear()
        etArticleTags.text.clear()
        etVideoLink.text.clear()
        etArticleContent.text.clear()
        articleImage.setImageDrawable(null)
        imageUri = null
        spinnerCategory.setSelection(0)
    }
}
