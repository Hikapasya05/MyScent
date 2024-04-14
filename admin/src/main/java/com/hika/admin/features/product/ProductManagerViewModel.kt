package com.hika.admin.features.product

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hika.data.data.repository.perfume.PerfumeRepository
import com.hika.data.model.Perfume
import com.hika.data.model.PerfumeBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductManagerViewModel(
    private val repository: PerfumeRepository
): ViewModel() {

    private val _state = MutableStateFlow(ProductManagerState())
    val state = _state.asStateFlow()

    private val _perfume = MutableStateFlow<Perfume?>(null)
    val perfume = _perfume.asStateFlow()

    private val _categoriesRaw = arrayOf("Men", "Women", "Unisex")
    private val _categories = MutableStateFlow(arrayOf<String>())
    val categories = _categories.asStateFlow()

    private val _strengthsRaw = arrayOf(
        1 to "Weak",
        2 to "Intimate",
        3 to "Moderate",
        4 to "Strong",
        5 to "Very Strong"
    )
    private val _strengths = MutableStateFlow(arrayOf<Pair<Int, String>>())
    val strengths = _strengths.asStateFlow()

    private val _availabilitiesRaw = arrayOf("Available", "Not Available")
    private val _availabilities = MutableStateFlow(arrayOf<String>())
    val availabilities = _availabilities.asStateFlow()

    private val _image = MutableStateFlow<Uri?>(null)
    val image = _image.asStateFlow()

    fun pairDropDowns() {
        _categories.value = _categoriesRaw
        _strengths.value = _strengthsRaw
        _availabilities.value = _availabilitiesRaw
    }

    fun setImage(uri: Uri) {
        _image.value = uri
    }

    fun toStrengthString(strength: Int): String {
        return _strengthsRaw.first { it.first == strength }.second
    }

    fun toAvailabilityString(isAvailable: Boolean): String {
        return _availabilitiesRaw.first { it == if (isAvailable) "Available" else "Not Available" }
    }

    fun getPerfume(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.getPerfumeDetail(id).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _perfume.value = it
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, isError = true)
            }
        }
    }

    fun postProduct(name: String, category: String, description: String, strength: String, price: Int, availability: String) {
        val body = PerfumeBody(
            name = name,
            category = category,
            description = description,
            strength = _strengths.value.first { it.second == strength }.first,
            price = price,
            isAvailable = availability == _availabilities.value.first()
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.addPerfume(body, _image.value ?: return@launch).onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, isSuccess = false)
            }
        }
    }

    fun updateProduct(
        name: String,
        category: String,
        description: String,
        strength: String,
        price: Int,
        availability: String
    ) {
        val body = PerfumeBody(
            name = name,
            category = category,
            description = description,
            strength = _strengths.value.first { it.second == strength }.first,
            price = price,
            isAvailable = availability == _availabilities.value.first()
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.updatePerfume(
                _perfume.value?.id.orEmpty(),
                body,
                _image.value,
                _perfume.value?.image.orEmpty(),
            ).onSuccess {
                _state.value = _state.value.copy(isLoading = false, isSuccess = true)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false, isSuccess = false)
            }
        }
    }

}