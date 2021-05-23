package com.tutorials.eu.favdish.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.application.FavDishApplication
import com.tutorials.eu.favdish.databinding.FragmentDishDetailsBinding
import com.tutorials.eu.favdish.viewmodel.FavDishViewModel
import com.tutorials.eu.favdish.viewmodel.FavDishViewModelFactory
import java.util.*


class DishDetailsFragment : Fragment() {

    lateinit var binding: FragmentDishDetailsBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        Log.i("Dish Title", args.dishDetails.title)
        Log.i("Dish type", args.dishDetails.type)
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.w("Tag", "Error loading image", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource.let {

                                Palette.from(resource!!.toBitmap()).generate { palette ->
                                    val intcolor = palette?.lightMutedSwatch?.rgb ?: 0
                                    binding.root.setBackgroundColor(intcolor)

                                }
                                binding.ivDishImage.setImageDrawable(resource)
                            }
                            return true
                        }


                    })
                    .into(binding.ivDishImage)
                binding.apply {
                    tvTitle.text = it.dishDetails.title
                    tvType.text = it.dishDetails.type.capitalize(Locale.ROOT)
                    tvCategory.text = it.dishDetails.category
                    tvIngredients.text = it.dishDetails.ingredients
                    tvCookingDirection.text = it.dishDetails.directionToCook
                    tvCookingTime.text = resources.getString(
                        R.string.lbl_estimate_cooking_time,
                        it.dishDetails.cookingTime
                    )


                    if (args.dishDetails.favoriteDish) {
                        ivFavoriteDish.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_favorite_selected
                            )
                        )
                    } else {
                        ivFavoriteDish.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_favorite_unselected
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        binding.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish
            mFavDishViewModel.update(args.dishDetails)
            if (args.dishDetails.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_fav),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_unfav),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}