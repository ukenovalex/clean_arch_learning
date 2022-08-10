package ca.ukenov.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import ca.ukenov.shoppinglist.domain.models.ShopItem

interface ShopRepository {
    suspend fun addItem(item: ShopItem)
    suspend fun deleteItem(item: ShopItem)
    suspend fun editItem(item: ShopItem)
    suspend fun getById(id: Int): ShopItem
    fun getItemList(): LiveData<List<ShopItem>>
    suspend fun toggleIsActiveItem(item: ShopItem)
}