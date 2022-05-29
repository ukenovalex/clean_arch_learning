package ca.ukenov.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.ukenov.shoppinglist.data.ShopRepositoryImpl
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.AddShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.EditShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.GetItemById
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = ShopRepositoryImpl

    private val editShopItem = EditShopItem(repository)
    private val getShopItemById = GetItemById(repository)
    private val addShopItem = AddShopItem(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _currentShopItem = MutableLiveData<ShopItem>()
    val currentShopItem: LiveData<ShopItem>
        get() = _currentShopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName = inputName)
        val count = parseCount(inputCount = inputCount)
        val isCorrect = validateInput(name = name, count = count)
        if (isCorrect) {
            _currentShopItem.value?.let {
                val item = it.copy(title = name, count = count)
                editShopItem.editItem(item)
                finishWork()
            }
        }
    }

    fun getShopItem(id: Int) {
        _currentShopItem.value =  getShopItemById.getById(id)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isCorrect = validateInput(
            name = name, count = count
        )
        if (isCorrect) {
            addShopItem.addItem(
                ShopItem(title = name, count = count, isActive = true)
            )
            finishWork()
        }
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String, count: Int): Boolean {
        if (name.isBlank()) {
            _errorInputName.value = true
            return false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            return false
        }
        return true
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

}