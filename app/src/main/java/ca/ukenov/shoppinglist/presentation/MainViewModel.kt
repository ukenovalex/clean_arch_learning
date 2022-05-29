package ca.ukenov.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import ca.ukenov.shoppinglist.data.ShopRepositoryImpl
import ca.ukenov.shoppinglist.domain.models.ShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.DeleteShopItem
import ca.ukenov.shoppinglist.domain.usecases.shop.GetShopItemList
import ca.ukenov.shoppinglist.domain.usecases.shop.ToggleIsActiveShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopRepositoryImpl

    private val getShopItemList = GetShopItemList(repository)
    private val deleteShopItem = DeleteShopItem(repository)
    private val toggleIsActiveShopItem = ToggleIsActiveShopItem(repository)

    val items = getShopItemList.getItemList()


    fun deleteShopItem(item: ShopItem) {
        deleteShopItem.deleteItem(item)
    }

    fun toggleIsActive(item: ShopItem) {
        toggleIsActiveShopItem.toggleIsActiveItem(item)
    }


}