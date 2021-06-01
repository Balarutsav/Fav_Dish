package com.tutorials.eu.favdish.model.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.tutorials.eu.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishDetails(favDish: FavDish){
        favDishDao.updateFavDetails(favDish)
    }
    val favList: Flow<List<FavDish>> = favDishDao.getFavouriteDishesList()

    @WorkerThread
    suspend fun deleteFavDish(favDish: FavDish){
        favDishDao.deleteFavDishDetails(favDish)
    }

    fun getFilterList(value: String) : Flow<List<FavDish>> =
            favDishDao.getFilterDishes(value)

}