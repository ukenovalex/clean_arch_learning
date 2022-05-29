package ca.ukenov.shoppinglist.presentation

import androidx.recyclerview.widget.DiffUtil
import ca.ukenov.shoppinglist.domain.models.ShopItem
import kotlin.math.log

class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItems = oldList[oldItemPosition]
        val newItems = newList[newItemPosition]
        return oldItems.id == newItems.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItems = oldList[oldItemPosition]
        val newItems = newList[newItemPosition]

        return oldItems == newItems
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItems = oldList[oldItemPosition]
        val newItems = newList[newItemPosition]

        return oldItems.id == newItems.id
    }
}