package com.example.newswatchapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticlesAdapter(private var articles: List<Article>) :
    RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    // ViewHolder class to hold views
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.article_title)
        val imageView: ImageView = itemView.findViewById(R.id.article_image)
        val contentTextView: TextView = itemView.findViewById(R.id.article_content)
    }

    // Inflates the item layout and creates ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false) // Replace with your layout resource name
        return ArticleViewHolder(view)
    }
    // In your repository class
    suspend fun postArticle(article: Article): Result<Unit> {
        // TODO: Implement actual posting logic here
        return Result.success(Unit)
    }


    // Binds data to the ViewHolder
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.titleTextView.text = article.title
        holder.contentTextView.text = article.content
        // Use Glide to load the image into ImageView
        Glide.with(holder.itemView.context)
            .load(article.imageUrl)
            .into(holder.imageView)
    }

    // Returns the number of items in the list
    override fun getItemCount(): Int = articles.size

    // Method to update the list of articles and refresh the RecyclerView
    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
