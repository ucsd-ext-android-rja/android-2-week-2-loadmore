package com.ucsdextandroid2.petfinder

import android.app.Application

/**
 * Created by rjaylward on 2019-07-12
 */

class PetFinderApp: Application() {

    override fun onCreate() {
        super.onCreate()

        AccessTokenCache.init(this)
    }

}