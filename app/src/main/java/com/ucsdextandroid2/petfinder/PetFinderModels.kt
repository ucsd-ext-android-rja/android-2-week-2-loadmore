package com.ucsdextandroid2.petfinder

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

/**
 * Created by rjaylward on 2019-07-12
 */

data class AccessToken(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("access_token") val accessToken: String
) {
    val expiresAtMillis: Long get() = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiresIn)
}

data class AnimalsResponse (
    @SerializedName("animals") val animals : List<Animal>,
    @SerializedName("pagination") val pagination : Pagination
)

data class Animal (
    @SerializedName("id") val id : Int,
    @SerializedName("organization_id") val organizationId : String,
    @SerializedName("url") val url : String,
    @SerializedName("type") val type : String,
    @SerializedName("species") val species : String,
    @SerializedName("breeds") val breeds : Breeds,
    @SerializedName("colors") val colors : Colors,
    @SerializedName("age") val age : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("size") val size : String,
    @SerializedName("coat") val coat : String,
    @SerializedName("attributes") val attributes : Attributes,
    @SerializedName("environment") val environment : Environment,
    @SerializedName("tags") val tags : List<String>,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("photos") val photos : List<Photos>,
    @SerializedName("status") val status : String,
    @SerializedName("published_at") val published_at : String,
    @SerializedName("contact") val contact : Contact
)

data class Breeds (
    @SerializedName("primary") val primary : String,
    @SerializedName("secondary") val secondary : String,
    @SerializedName("mixed") val mixed : Boolean,
    @SerializedName("unknown") val unknown : Boolean
)

data class Photos (
    @SerializedName("small") val small : String,
    @SerializedName("medium") val medium : String,
    @SerializedName("large") val large : String,
    @SerializedName("full") val full : String
)

data class Attributes (
    @SerializedName("spayed_neutered") val spayedNeutered : Boolean,
    @SerializedName("house_trained") val houseTrained : Boolean,
    @SerializedName("declawed") val declawed : String,
    @SerializedName("special_needs") val specialNeeds : Boolean,
    @SerializedName("shots_current") val shotsCurrent : Boolean
)

data class Environment (
    @SerializedName("children") val children : Boolean,
    @SerializedName("dogs") val dogs : Boolean,
    @SerializedName("cats") val cats : Boolean
)

data class Colors (
    @SerializedName("primary") val primary : String,
    @SerializedName("secondary") val secondary : String,
    @SerializedName("tertiary") val tertiary : String
)

data class Contact (
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("address") val address : Address
)

data class Address (
    @SerializedName("address1") val address1 : String,
    @SerializedName("address2") val address2 : String,
    @SerializedName("city") val city : String,
    @SerializedName("state") val state : String,
    @SerializedName("postcode") val postcode : Int,
    @SerializedName("country") val country : String
)

data class Pagination (
    @SerializedName("count_per_page") val countPerPage : Int,
    @SerializedName("total_count") val totalCount : Int,
    @SerializedName("current_page") val currentPage : Int,
    @SerializedName("total_pages") val totalPages : Int
)