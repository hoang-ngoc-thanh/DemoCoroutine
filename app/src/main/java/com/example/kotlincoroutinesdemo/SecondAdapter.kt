package com.example.kotlincoroutinesdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlincoroutinesdemo.model.User

class SecondAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<SecondAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.run {
                findViewById<TextView>(R.id.textViewUserName).text = user.name
                findViewById<TextView>(R.id.textViewUserEmail).text = user.email
                Glide.with(context)
                    .load(user.avatar)
                    .into(findViewById(R.id.imageViewAvatar))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view, parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position])

    fun addData(list: List<User>) {
        users.addAll(list)
    }
}
