package ca.ukenov.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemDbModel(
    val title: String,
    val count: Int,
    val isActive: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
)
