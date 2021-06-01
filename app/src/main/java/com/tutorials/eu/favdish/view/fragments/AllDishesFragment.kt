package com.tutorials.eu.favdish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.application.FavDishApplication
import com.tutorials.eu.favdish.databinding.DialogCustomListBinding
import com.tutorials.eu.favdish.databinding.FragmentAllDishesBinding
import com.tutorials.eu.favdish.model.entities.FavDish
import com.tutorials.eu.favdish.utils.Constants
import com.tutorials.eu.favdish.view.activities.AddUpdateDishActivity
import com.tutorials.eu.favdish.view.activities.MainActivity
import com.tutorials.eu.favdish.view.adapters.CustomListItemAdapter
import com.tutorials.eu.favdish.view.adapters.FavDishAdapter
import com.tutorials.eu.favdish.view.adapters.selecteItem
import com.tutorials.eu.favdish.viewmodel.FavDishViewModel
import com.tutorials.eu.favdish.viewmodel.FavDishViewModelFactory

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding
    lateinit var favDishAdapter: FavDishAdapter
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        favDishAdapter = FavDishAdapter(this@AllDishesFragment)
        mBinding.rvDishesList.adapter = favDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                if (it.isNotEmpty()) {

                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)
                } else {

                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
            R.id.action_filter -> {
                var list = Constants.dishTypes()
                list.add(0, "All Items")
                customItemsListDialog(
                    resources.getString(R.string.title_select_dish_type),
                    list
                    ,
                    Constants.DISH_TYPE
                )
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun customItemsListDialog(title: String, itemsList: List<String>, selection: String) {
        var mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        val adapter =
            CustomListItemAdapter(requireActivity(), itemsList, selection, object : selecteItem {
                override fun onSelect(int: Int, selectedItem: String) {
                    mCustomListDialog.dismiss()
                    if (selectedItem == Constants.ALL_ITEMS) {
                        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { favDishes: List<FavDish>? ->
                            favDishes?.let {
                                if (favDishes.isNotEmpty()) {
                                    mBinding.rvDishesList.visibility = View.VISIBLE
                                    mBinding.tvNoDishesAddedYet.visibility = View.GONE
                                    favDishAdapter.dishesList(favDishes)

                                } else {
                                    mBinding.rvDishesList.visibility = View.GONE
                                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                                }
                            }
                        }
                    } else {
                        mFavDishViewModel.getFilterList(selectedItem)
                            .observe(viewLifecycleOwner) { list: List<FavDish> ->
                                if (list.isNotEmpty()) {
                                    mBinding.rvDishesList.visibility = View.VISIBLE
                                    mBinding.tvNoDishesAddedYet.visibility = View.GONE
                                    favDishAdapter.dishesList(list)
                                } else {

                                    mBinding.rvDishesList.visibility = View.GONE
                                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE

                                }
                            }
                    }
                }

            })
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun dishDetails(dish: FavDish) {
        findNavController().navigate(
            AllDishesFragmentDirections.actionNavigationAllDishesToNavigationDishDetails(
                dish
            )
        )
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideBottomNavigation()
        }
    }

    fun deleteDish(dish: FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        builder.setMessage(resources.getString(R.string.msg_delete_dialog, dish.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialoginterface, _ ->
            mFavDishViewModel.delete(dish)
            dialoginterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialog, which ->
            {
                dialog.dismiss()
            }
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    @Override
    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).showBottomNavigation()
        }
    }


}