package com.example.createplanetapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CatalogState : ViewModel() {
    var selectedButton by mutableStateOf("")
    var selectedOption by mutableStateOf("Выберите раздел")
    var selectedSortType by mutableStateOf("По умолчанию")
    var scrollState by mutableStateOf(0)
        private set  // Запрещаем внешнее изменение, кроме как через методы

    fun updateState(
        button: String = selectedButton,
        option: String = selectedOption,
        sortType: String = selectedSortType,
        scroll: Int = scrollState
    ) {
        selectedButton = button
        selectedOption = option
        selectedSortType = sortType
        scrollState = scroll
    }
}
