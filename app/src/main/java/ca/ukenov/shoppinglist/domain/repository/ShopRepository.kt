package ca.ukenov.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import ca.ukenov.shoppinglist.domain.models.ShopItem

interface ShopRepository {
    fun addItem(item: ShopItem)
    fun deleteItem(item: ShopItem)
    fun editItem(item: ShopItem)
    fun getById(id: Int): ShopItem
    fun getItemList(): LiveData<List<ShopItem>>
    fun toggleIsActiveItem(item: ShopItem)
}