package com.eyehail.workoutandchill

class ExerciseModel(private var id: Int,
                    private var name: String,
                    private var image: Int,
                    private var isCompleted: Boolean,
                    private var isSelected: Boolean) {
    //getter
    fun getId(): Int {
        return id
    }
    //setter
    fun setId(id: Int) {
        this.id = id
    }
    //getter
    fun getName(): String {
        return name
    }
    //setter
    fun setName(name: String) {
        this.name = name
    }
    //getter
    fun getImage(): Int {
        return image
    }
    //setter
    fun setImage(image: Int) {
        this.image = image
    }
    fun getIsCompleted(): Boolean {
        return isCompleted
    }
    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }
    fun getIsSelected(): Boolean {
        return isSelected
    }
    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }


}
