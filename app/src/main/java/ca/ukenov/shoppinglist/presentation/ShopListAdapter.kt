package ca.ukenov.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.ukenov.shoppinglist.R
import ca.ukenov.shoppinglist.databinding.ItemShopDisabledBinding
import ca.ukenov.shoppinglist.databinding.ItemShopEnabledBinding
import ca.ukenov.shoppinglist.domain.models.ShopItem


class ShopListAdapter :
    ListAdapter<ShopItem, ShopListAdapter.ShopListViewHolder>(ShopItemDiffCallback()) {

    var onLongClickListener: ((item: ShopItem) -> Unit)? = null
    var onClickListener: ((item: ShopItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val viewId = if (viewType == ENABLED) {
            R.layout.item_shop_enabled
        } else {
            R.layout.item_shop_disabled
        }
        LayoutInflater.from(parent.context).inflate(viewId, parent, false)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewId,
            parent,
            false
        )

        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val binding = holder.binding

        when (binding) {
            is ItemShopDisabledBinding -> {
                binding.tvName.text = getItem(holder.adapterPosition).title
                binding.tvCount.text = getItem(holder.adapterPosition).count.toString()
            }
            is ItemShopEnabledBinding -> {
                binding.tvName.text = getItem(holder.adapterPosition).title
                binding.tvCount.text = getItem(holder.adapterPosition).count.toString()
            }
        }
        binding.root.setOnLongClickListener {
            onLongClickListener?.invoke(getItem(holder.adapterPosition))
            true
        }
        binding.root.setOnClickListener {
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


    class ShopListViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ENABLED = 0
        const val DISABLED = 1
        const val MAX_POOL_SIZE = 5
    }
}