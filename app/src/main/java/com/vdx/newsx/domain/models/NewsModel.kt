package com.vdx.newsx.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class NewsModel(
    @SerializedName("copyright")
    val copyright: String = "",
    @SerializedName("last_updated")
    val lastUpdated: String = "",
    @SerializedName("num_results")
    val numResults: Int = 0,
    @SerializedName("results")
    val results: List<Result> = emptyList(),
    @SerializedName("section")
    val section: String = "",
    @SerializedName("status")
    val status: String = ""
)

@Entity(tableName = "news_list")
data class Result(
    @SerializedName("abstract")
    var `abstract`: String,
    @SerializedName("byline")
    var byline: String,
    @SerializedName("created_date")
    var createdDate: String,
    @SerializedName("des_facet")
    var desFacet: List<String>,
    @SerializedName("geo_facet")
    var geoFacet: List<String>,
    @SerializedName("item_type")
    var itemType: String,
    @SerializedName("kicker")
    var kicker: String,
    @SerializedName("material_type_facet")
    var materialTypeFacet: String,
    @SerializedName("multimedia")
    var multimedia: List<Multimedia>,
    @SerializedName("org_facet")
    var orgFacet: List<String>,
    @SerializedName("per_facet")
    var perFacet: List<String>,
    @SerializedName("published_date")
    var publishedDate: String,
    @SerializedName("section")
    var section: String,
    @SerializedName("short_url")
    var shortUrl: String,
    @SerializedName("subsection")
    var subsection: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("updated_date")
    var updatedDate: String,
    @SerializedName("uri")
    var uri: String,
    @SerializedName("url")
    @PrimaryKey(autoGenerate = false)
    var url: String
) {
    constructor() : this(
         "", "", "", emptyList(), emptyList(), "", "", "", emptyList(),
        emptyList(), emptyList(), "", "", "", "", "", "", "", ""
    )
}

data class Multimedia(
    @SerializedName("caption")
    val caption: String,
    @SerializedName("copyright")
    val copyright: String,
    @SerializedName("format")
    val format: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("subtype")
    val subtype: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)