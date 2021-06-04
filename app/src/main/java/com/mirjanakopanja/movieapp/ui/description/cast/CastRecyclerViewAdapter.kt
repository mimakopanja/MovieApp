package com.mirjanakopanja.movieapp.ui.description.cast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mirjanakopanja.movieapp.R
import com.mirjanakopanja.movieapp.data.cast.CastCredits
import com.mirjanakopanja.movieapp.databinding.ItemCastBinding
import com.mirjanakopanja.movieapp.extensions.BASE_URL_IMG
import com.mirjanakopanja.movieapp.ui.description.DescriptionFragment
import com.squareup.picasso.Picasso
import kotlin.math.min


class CastRecyclerViewAdapter(
    private var onItemViewClickListener: DescriptionFragment.OnItemClick?
) : RecyclerView.Adapter<CastRecyclerViewAdapter.ViewHolder>() {

    var castList : CastCredits = CastCredits()
    private lateinit var binding: ItemCastBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val castImage: ImageView = binding.castImage
        val castName: TextView = binding.castName
    }

    fun updateCastAndCrew(cast: CastCredits) {
        this.castList = cast
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (position) {
            0 -> {
                binding.spacingStart.visibility = View.VISIBLE
            }
            min(20, castList.cast?.size!!) - 1 -> {
                binding.spacingEnd.visibility = View.VISIBLE
            }
            else -> {
                binding.spacingEnd.visibility = View.GONE
                binding.spacingStart.visibility = View.GONE
            }
        }
            Picasso.get()
                .load(BASE_URL_IMG.plus(castList.cast?.get(position)?.profile_path))
                .placeholder(R.drawable.ic_person_yellow)
                .fit().centerCrop()
                .into(holder.castImage)

        holder.castName.text = castList.cast?.get(position)?.name

        holder.itemView.setOnClickListener{
            onItemViewClickListener?.onItemClick(castList.cast?.get(position)?.id)
        }
}
    override fun getItemCount(): Int = min(20, castList.cast?.size!!)
}