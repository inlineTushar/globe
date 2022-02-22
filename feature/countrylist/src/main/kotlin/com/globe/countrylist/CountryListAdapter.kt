package com.globe.countrylist

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.globe.CountryInfoView
import com.globe.data.model.CountryModel
import com.globe.extension.onClickDebounced

internal class CountryListAdapter(
    private val listener: (CountryModel) -> Unit
) : ListAdapter<CountryModel, CountryListAdapter.CountryViewHolder>(CountryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(CountryInfoView(parent.context)).apply {
            itemView.onClickDebounced {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener(getItem(bindingAdapterPosition))
                }
            }
        }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        getItem(position).apply {
            holder.itemView as CountryInfoView
            holder.itemView.renderContent(name, flag?.unicode, flag?.url, countryCode)
        }
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class CountryDiffCallback : DiffUtil.ItemCallback<CountryModel>() {
        override fun areItemsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean =
            oldItem == newItem
    }
}
