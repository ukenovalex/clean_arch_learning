package ca.ukenov.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.ukenov.shoppinglist.R
import ca.ukenov.shoppinglist.domain.models.ShopItem



class ShopListAdapter : ListAdapter<ShopItem, ShopListAdapter.ShopListViewHolder>(ShopItemDiffCallback()) {

    var onLongClickListener: ((item: ShopItem) -> Unit)? = null
    var onClickListener: ((item: ShopItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val viewId = if (viewType == ENABLED) {
            R.layout.item_shop_enabled
        } else {
            R.layout.item_shop_disabled
        }
        val view = LayoutInflater.from(parent.context).inflate(viewId, parent, false)

        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        holder.title.text = getItem(holder.adapterPosition).title
        holder.count.text = getItem(holder.adapterPosition).count.toString()
        holder.view.setOnLongClickListener {
            onLongClickListener?.invoke(getItem(holder.adapterPosition))
            true
        }
        holder.view.setOnClickListener {
            onClickListener?.invoke(getItem(holder.adapterPosition))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isActive) {
            ENABLED
        } else {
            DISABLED
        }
    }


    class ShopListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_name)
        val count: TextView = view.findViewById(R.id.tv_count)
    }

    companion object {
        const val ENABLED = 0
        const val DISABLED = 1
        const val MAX_POOL_SIZE = 5
    }
}