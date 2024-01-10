package com.randy.plantapp.utils

object StringUtil {

    fun filterText(text: String): String {
        return text.replace("1.", "Pertama")
            .replace("2.", "Kedua")
            .replace("3.", "Ketiga")
            .replace("4.", "Keempat")
            .replace("5.", "Kelima")
            .replace("6.", "Keenam")
            .replace("7.", "Ketujuh")
            .replace("8.", "Kedelapan")
            .replace("9.", "Kesembilan")
            .replace("10.", "Kesepuluh")
    }

}
