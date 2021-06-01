package com.tutorials.eu.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.tutorials.eu.favdish.application.FavDishApplication
import com.tutorials.eu.favdish.databinding.FragmentFavoriteDishesBinding
import com.tutorials.eu.favdish.model.entities.FavDish
import com.tutorials.eu.favdish.view.activities.MainActivity
import com.tutorials.eu.favdish.view.adapters.FavDishAdapter
import com.tutorials.eu.favdish.viewmodel.FavDishViewModel
import com.tutorials.eu.favdish.viewmodel.FavDishViewModelFactory


class FavoriteDishesFragment : Fragment() {

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    lateinit var mBinding: FragmentFavoriteDishesBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (requireActivity() as MainActivity).showBottomNavigation()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFavDishViewModel.favList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                val adapter = FavDishAdapter(this)
                mBinding.rvDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    for (dish in it) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoFavDishesAvailable.visibility = View.GONE
                        adapter.dishesList(it)
                        Log.w("Favorite Dish", "${dish.id} :: ${dish.title}")
                    }
                } else {
                    mBinding.rvDishesList.visibility=View.GONE
                    mBinding.tvNoFavDishesAvailable.visibility=View.VISIBLE
                    Log.w("list of fav dish ", "is empty")
                }
            }
        }
    }
    fun dishDetails(dish: FavDish) {
        findNavController().navigate(FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToNavigationDishDetails(dish))
        if (requireActivity() is MainActivity){
            (requireActivity() as MainActivity).hideBottomNavigation()
        }
    }
}