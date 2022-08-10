package ca.ukenov.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.ukenov.shoppinglist.data.ShopRepositoryImpl
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.DeleteShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.GetShopItemList
import ca.ukenov.shoppinglist.domain.usecases.shop.ToggleIsActiveShopItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopRepositoryImpl(application)

    private val getShopItemList = GetShopItemList(repository)
    private val deleteShopItem = DeleteShopItem(repository)
    private val toggleIsActiveShopItem = ToggleIsActiveShopItem(repository)

    val items = getShopItemList.getItemList()


    fun deleteShopItem(item: ShopItem) {
        viewModelScope.launch {
            deleteShopItem.deleteItem(item)
        }
    }

    fun toggleIsActive(item: ShopItem) {
        viewModelScope.launch {
            toggleIsActiveShopItem.toggleIsActiveItem(item)
        }
    }
}