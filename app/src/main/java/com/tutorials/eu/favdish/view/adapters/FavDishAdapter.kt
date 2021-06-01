package com.tutorials.eu.favdish.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.databinding.ItemDishLayoutBinding
import com.tutorials.eu.favdish.model.entities.FavDish
import com.tutorials.eu.favdish.utils.Constants
import com.tutorials.eu.favdish.view.activities.AddUpdateDishActivity
import com.tutorials.eu.favdish.view.fragments.AllDishesFragment
import com.tutorials.eu.favdish.view.fragments.FavoriteDishesFragment

 public class FavDishAdapter(
     private val fragment: Fragment,
) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dish = dishes[position]

        // Load the dish image in the ImageView.
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment) {
                fragment.dishDetails(dish)
            } else if (fragment is FavoriteDishesFragment) {
                fragment.dishDetails(dish)
            }
        }

        if (fragment is AllDishesFragment) {
            holder.ivMenu.visibility = View.VISIBLE
        } else if (fragment is FavoriteDishesFragment) {
            holder.ivMenu.visibility = View.GONE
        }
        holder.ivMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(fragment.context, holder.ivMenu)
            popupMenu.menuInflater.inflate(
                R.menu.more_menu, popupMenu.menu
            )
            popupMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {
                    val intent= Intent(fragment.requireActivity(),AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS,dish)
                    fragment.requireActivity().startActivity(intent)
                        Log.i("You have clicked on ","Edit options of ${dish.title}")
                }else if (it.itemId==R.id.action_delete_dish){
                    if (fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                    Log.i("You have clickxed on ","Delete options of ${dish.title}")
                }
                true
            }
            popupMenu.show()
        }

    }

    /**
     * Gets the number of items in the list
     */
   public fun openEditDish(tyoe:FavDish){

    }
    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }

    class ViewHolder(var view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        // Holds the TextView that will add each item to
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ivMenu = view.ivMenu

    }
}