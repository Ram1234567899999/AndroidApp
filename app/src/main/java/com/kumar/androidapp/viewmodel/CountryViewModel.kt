package com.kumar.androidapp.viewmodel





import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumar.androidapp.data.CountryRepository
import com.kumar.androidapp.model.Country
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {
    private val repository = CountryRepository()

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchCountries()
                .onSuccess {
                    _countries.value = it
                    _error.value = null
                }
                .onFailure {
                    _error.value = it.message ?: "Unknown error occurred"
                }
            _isLoading.value = false
        }
    }
}