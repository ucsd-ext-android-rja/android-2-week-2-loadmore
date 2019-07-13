package com.ucsdextandroid2.petfinder

import android.util.Log
import androidx.paging.PageKeyedDataSource

/**
 * Created by rjaylward on 2019-07-13
 */

class PetsDataSourceFactory(private val lat: Long?, private val lng: Long?) : androidx.paging.DataSource.Factory<Int, PetModel>() {
    override fun create(): androidx.paging.DataSource<Int, PetModel> = PetsDataSource(lat, lng)
}

class PetsDataSource(private val lat: Long?, private val lng: Long?) : PageKeyedDataSource<Int,PetModel>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PetModel>) {
        Log.d("PetsDataSource", "loadInitial ${params.requestedLoadSize}")

        DataSource.findAnimals(lat, lng, 1, pageSize(params.requestedLoadSize)) {
            val result = processResult(it)
            callback.onResult(result.data, result.previousPage, result.nextPage)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PetModel>) {
        Log.d("PetsDataSource", "loadAfter ${params.key}")

        DataSource.findAnimals(lat, lng, params.key, pageSize(params.requestedLoadSize)) {
            val result = processResult(it)
            callback.onResult(result.data, result.nextPage)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PetModel>) {
        if (params.key <= 1)
            callback.onResult(emptyList(), null)

        DataSource.findAnimals(lat, lng, params.key, pageSize(params.requestedLoadSize)) {
            val result = processResult(it)
            callback.onResult(result.data, result.previousPage)
        }
    }

    private fun pageSize(size: Int): Int {
        return if(size in 1..99) size else 10
    }

    private fun processResult(apiResult: ApiResult<AnimalsResponse>): ResultWithPaginationInfo {
        val response = when(apiResult) {
            is ApiSuccess<AnimalsResponse> -> apiResult.data
            is ApiFail<AnimalsResponse> -> null
        }

        val currentPage = response?.pagination?.currentPage
        val totalPages = response?.pagination?.totalPages ?: 0

        val listOfPets: List<PetModel> = response?.animals?.map { animal ->

            PetModel(
                id = animal.id,
                name = "${animal.name} - page $currentPage",
                breed = animal.breeds.primary,
                imageUrl = animal.photos.firstOrNull()?.large
            )
        }.orEmpty()

        val previousPage = if (currentPage != null && currentPage > 1) currentPage - 1 else null
        val nextPage = if (currentPage != null && currentPage < totalPages) currentPage + 1 else null

        Log.d("PetsDataSource", "$currentPage, $totalPages, next: $nextPage prev: $previousPage, items: ${listOfPets.size} animals: ${response?.animals?.size} | ${response?.pagination}")

        return ResultWithPaginationInfo(listOfPets, previousPage, nextPage)
    }

    class ResultWithPaginationInfo(val data: List<PetModel>, val previousPage: Int?, val nextPage: Int?)

}