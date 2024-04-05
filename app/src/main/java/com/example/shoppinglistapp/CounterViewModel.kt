package com.example.shoppinglistapp

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class CounterViewModel:ViewModel(){
    private var _count:MutableState<Int> = mutableStateOf(0)
    val count=_count

    fun increment(){
        _count.value=count.value+1
        println(count)
    }
    fun decrement(){
        _count.value=count.value-1
        println(count)
    }

}
