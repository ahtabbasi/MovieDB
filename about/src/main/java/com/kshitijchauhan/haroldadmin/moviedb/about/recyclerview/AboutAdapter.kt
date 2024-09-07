package com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemAppBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemAuthorBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.databinding.ItemTmdbBinding
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItem.AboutApp
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItem.AboutAuthor
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItem.AboutTmdb
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItemViewHolder.AboutAppViewHolder
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItemViewHolder.AboutAuthorViewHolder
import com.kshitijchauhan.haroldadmin.moviedb.about.recyclerview.AboutItemViewHolder.AboutTmdbViewHolder

private const val TYPE_ABOUT_APP = 1001
private const val TYPE_ABOUT_AUTHOR = 1002
private const val TYPE_ABOUT_TMDB = 1003
private val dataSet = listOf(AboutApp, AboutAuthor, AboutTmdb)

internal class AboutAdapter(
    private val callback: AboutAdapterCallback,
) : ListAdapter<AboutItem, AboutItemViewHolder>(AboutItemDiffCallback) {

    override fun getItemCount() = dataSet.size

    override fun getItemViewType(position: Int) = when (dataSet[position]) {
        AboutApp -> TYPE_ABOUT_APP
        AboutAuthor -> TYPE_ABOUT_AUTHOR
        AboutTmdb -> TYPE_ABOUT_TMDB
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AboutItemViewHolder {
        return when (viewType) {
            TYPE_ABOUT_APP -> {
                val binding = ItemAppBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                AboutAppViewHolder(binding, callback)
            }

            TYPE_ABOUT_AUTHOR -> {
                val binding = ItemAuthorBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                AboutAuthorViewHolder(binding, callback)
            }

            TYPE_ABOUT_TMDB -> {
                val binding = ItemTmdbBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                AboutTmdbViewHolder(binding, callback)
            }

            else -> {
                throw IllegalArgumentException("Invalid about item view type")
            }
        }
    }

    override fun onBindViewHolder(viewHolder: AboutItemViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }
}