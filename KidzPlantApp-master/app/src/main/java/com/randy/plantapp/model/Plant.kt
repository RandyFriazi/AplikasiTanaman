package com.randy.plantapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val desc: String,
    val videoUrl: String,
    val countCompleted: Int,
) : Parcelable

@Parcelize
data class SubPlant(
    val id: Int,
    val plantId: Int,
    val subPlantId: Int,
    val title: String,
    val iconUrl: String,
    val imageUrl1: String,
    val imageUrl2: String,
    val imageUrl3: String,
    val videoUrl: String,
    val audioUrl: String,
    val benefit: String,
    val isCompleted: Boolean
) : Parcelable

//val previewPlants = listOf(
//    Plant(
//        id = 1,
//        title = "Tanaman Umbi-umbian",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1103701009173323796/green_onion.png",
//        desc = "Lorem ipsum dolor sit amet consectetur. Varius tincidunt quisque est tortor semper lectus blandit nibh. Leo egestas aliquet in pellentesque faucibus.",
//    ),
//    Plant(
//        id = 2,
//        title = "Tanaman Hias",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1103701267542450246/flower.png",
//        desc = "Lorem ipsum dolor sit amet consectetur. Varius tincidunt quisque est tortor semper lectus blandit nibh. Leo egestas aliquet in pellentesque faucibus.",
//    ),
//    Plant(
//        id = 3,
//        title = "Tanaman Obat",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1103701652046889061/ginger.png",
//        desc = "Lorem ipsum dolor sit amet consectetur. Varius tincidunt quisque est tortor semper lectus blandit nibh. Leo egestas aliquet in pellentesque faucibus.",
//    ),
//)

//val previewOrnamentalPlants = listOf(
//    SubPlant(
//        id = 1,
//        title = "Bunga Matahari",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1104594660506210386/sunflower.png",
//        benefit = "1. Memperindah taman.\n2. Mengurangi Risiko Penyakit Jantung.\n3. Mengontrol Diabetes.\n4. Menjaga Kesehatan Mata",
//        imageUrl1 = "",
//        imageUrl2 = "",
//        imageUrl3 = "",
//        videoUrl = "",
//        isCompleted = false
//    ),
//    SubPlant(
//        id = 2,
//        title = "Bunga Mawar",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1104594661999382599/rose.png",
//        benefit = "1. Lorem ipsum dolor sit amet consectetur.\n2. Varius tincidunt quisque est tortor semper lectus blandit nibh.\n3. Leo egestas aliquet in pellentesque faucibus.",
//        imageUrl1 = "",
//        imageUrl2 = "",
//        imageUrl3 = "",
//        videoUrl = "",
//        isCompleted = false
//    ),
//    SubPlant(
//        id = 3,
//        title = "Kaktus",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1104594662246862888/cactus.png",
//        benefit = "1. Lorem ipsum dolor sit amet consectetur.\n2. Varius tincidunt quisque est tortor semper lectus blandit nibh.\n3. Leo egestas aliquet in pellentesque faucibus.",
//        imageUrl1 = "",
//        imageUrl2 = "",
//        imageUrl3 = "",
//        videoUrl = "",
//        isCompleted = false
//    ),
//    SubPlant(
//        id = 4,
//        title = "Bunga Lavender",
//        iconUrl = "https://cdn.discordapp.com/attachments/1052099242157674509/1104594661747732560/lavender.png",
//        benefit = "1. Lorem ipsum dolor sit amet consectetur.\n2. Varius tincidunt quisque est tortor semper lectus blandit nibh.\n3. Leo egestas aliquet in pellentesque faucibus.",
//        imageUrl1 = "",
//        imageUrl2 = "",
//        imageUrl3 = "",
//        videoUrl = "",
//        isCompleted = false
//    ),
//)
