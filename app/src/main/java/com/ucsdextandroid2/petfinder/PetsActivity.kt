package com.ucsdextandroid2.petfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PetsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        val adapter = PetsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        val petsLiveData = LivePagedListBuilder(PetsDataSourceFactory(null, null), PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .build()
        ).build()

        petsLiveData.observe(this, Observer<PagedList<PetModel>> { notes ->
            adapter.submitList(notes)
        })

    }

    private class PetsAdapter : PagedListAdapter<PetModel, PetCardViewHolder>(noteDiffer) {

        var itemClickListener: ((PetModel) -> Unit)? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetCardViewHolder {
            return PetCardViewHolder.inflate(parent).apply {
                this.itemView.setOnClickListener {
                    val item = getItem(this.adapterPosition)
                    if(item != null)
                        itemClickListener?.invoke(item)
                }
            }
        }

        override fun onBindViewHolder(holder: PetCardViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        companion object {
            private val noteDiffer = object : DiffUtil.ItemCallback<PetModel>() {

                override fun areItemsTheSame(oldItem: PetModel, newItem: PetModel): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: PetModel, newItem: PetModel): Boolean =
                    oldItem == newItem

            }
        }
    }

    private class PetCardViewHolder private constructor(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView = itemView.findViewById(R.id.vnc_image)
        val titleView: TextView = itemView.findViewById(R.id.vnc_title)
        val textView: TextView = itemView.findViewById(R.id.vnc_text)

        companion object {
            fun inflate(parent: ViewGroup): PetCardViewHolder = PetCardViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_holder_note_card, parent, false)
            )
        }

        fun bind(note: PetModel?) {
            image.isVisible = note?.imageUrl != null
            image.loadImageUrl(note?.imageUrl)
            titleView.text = note?.name
            textView.text = note?.breed
        }

    }

}
