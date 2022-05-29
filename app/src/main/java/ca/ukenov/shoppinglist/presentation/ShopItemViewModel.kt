package ca.ukenov.shoppinglist.presentation

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

    fun editShopItem(params: ShopItemParams) {
        val name = parseName(params.inputName)
        val count = parseCount(params.inputCount)
        val isCorrect = validateInput(
            ValidateShopItemParams(name = name, count = count)
        )
        if (isCorrect) {
            editShopItem.editItem(
                ShopItem(title = name, count = count, isActive = true)
            )
        }
    }

    fun getShopItem(id: Int): ShopItem {
        return getShopItemById.getById(id)
    }

    fun addShopItem(params: ShopItemParams) {
        val name = parseName(params.inputName)
        val count = parseCount(params.inputCount)
        val isCorrect = validateInput(
            ValidateShopItemParams(name = name, count = count)
        )
        if (isCorrect) {
            addShopItem.addItem(
                ShopItem(title = name, count = count, isActive = true)
            )
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

    private fun validateInput(params: ValidateShopItemParams): Boolean {
        if (params.name.isBlank()) {
            // TODO('Show message')
            return false
        }
        if (params.count <= 0) {
            // TODO('Show message')
            return false
        }
        return true
    }
}

data class ShopItemParams(
    val inputName: String?,
    val inputCount: String?
)

private data class ValidateShopItemParams(
    val name: String,
    val count: Int
)