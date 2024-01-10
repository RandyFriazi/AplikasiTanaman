package com.randy.plantapp.model

import com.randy.plantapp.R

data class OnBoardingDataClass(
    val text1: String,
    val image: Int,
    val text2: String,
)

val onBoardingItem = listOf(
    OnBoardingDataClass(
        text1 = "Selamat Datang\nTeman-teman di KidzPlant",
        image = R.drawable.ic_kidzplant,
        text2 = "Aplikasi pembelajaran anak-anak\nmengenai tanaman hias, tanaman obat dan tanaman umbi-umbian"
    ),
    OnBoardingDataClass(
        text1 = "Pilih tanaman yang\nkamu ingin pelajari",
        image = R.drawable.onboard_materi,
        text2 = "Kamu dapat memilih contoh dari\nmacam-macam tanaman yang\nkamu ingin pelajari"
    ),
    OnBoardingDataClass(
        text1 = "Putar video pembelajaran\nyang kamu inginkan",
        image = R.drawable.onboard_video,
        text2 = "Kamu dapat memutar video\npembelajaran dengan cara\nmengklik ikon play"
    ),
    OnBoardingDataClass(
        text1 = "Mari lihat progres\npembelajaran kamu",
        image = R.drawable.onboard_progressmateri,
        text2 = "Kamu dapat melihat progres\npembelajaran yang sudah kamu\npelajari sebelumnya"
    )

)
