package com.example.pusherbot

class Helper {
    companion object {

        fun <T: Any> addListElement(list: List<T>, element: T): List<T> {
            val arrayList = ArrayList(list)
            arrayList.add(element)
            return arrayList.toList()
        }

    }
}