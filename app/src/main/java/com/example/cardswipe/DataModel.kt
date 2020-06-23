package com.example.cardswipe

class DataModel {
    private var name: String? = null;
    private var totalLikes: String? = null;
    private var photo = 0

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getTotalLikes(): String? {
        return totalLikes
    }

    fun setTotalLikes(likes: String) {
        totalLikes = likes
    }

    fun getPhoto(): Int {
        return photo
    }

    fun setPhoto(photo: Int) {
        this.photo = photo
    }
}