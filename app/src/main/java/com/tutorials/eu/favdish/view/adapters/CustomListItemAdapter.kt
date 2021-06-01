package com.tutorials.eu.favdish.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutorials.eu.favdish.databinding.ItemCustomListLayoutBinding
import com.tutorials.eu.favdish.view.activities.AddUpdateDishActivity

class CustomListItemAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val selection: String, private val selecteItem: selecteItem? = null
) :
    RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListLayoutBinding =
            ItemCustomListLayoutBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = listItems[position]

        holder.tvText.text = item

        holder.itemView.setOnClickListener {

            if (activity is AddUpdateDishActivity) {
                activity.selectedListItem(item, selection)
            } else {
                selecteItem?.onSelect(position,item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: ItemCustomListLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val tvText = view.tvText
    }


}

interface selecteItem {
    fun onSelect(int: Int, string: String)
}