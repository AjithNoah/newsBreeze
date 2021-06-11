package com.app.newsbreeze.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "news_table")
@Parcelize
data class News(
    var Title: String,
    var Description: String,
    var Author:String,
    var Image:String,
    var Url:String,
    var Date:String,
    var Content:String,
    var Name:String,
    var Saved:Int,
    @PrimaryKey(autoGenerate = true) val NewsID: Int = 0): Parcelable