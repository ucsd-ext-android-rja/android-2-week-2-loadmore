package com.ucsdextandroid2.petfinder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PetsActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        checkLocationPermission()

    }

    private fun checkLocationPermission() {

        // Here, thisActivity is the current activity
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 5)
            }
        } else {
            // Permission has already been granted

            getLocation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            // Got last known location. In some rare situations this can be null.
            Toast.makeText(this, location?.latitude.toString(), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            7 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocation()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
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
