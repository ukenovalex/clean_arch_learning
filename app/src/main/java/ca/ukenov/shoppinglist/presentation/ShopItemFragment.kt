package ca.ukenov.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.ukenov.shoppinglist.databinding.ShopItemFragmentBinding
import ca.ukenov.shoppinglist.domain.models.ShopItem

class ShopItemFragment : Fragment() {
    private val shopItemViewModel: ShopItemViewModel by activityViewModels()

    private var _binding: ShopItemFragmentBinding? = null
    private val binding get() = _binding!!

    private var screenMode: String = MODE_UNKNOWN
    private var itemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parseParams()
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        return binding.root
    }
    private fun launchEditMode() {
        setupViewEditShopItem()
        addErrorInputListener()

        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.editShopItem(inputName = name, inputCount = count)
            onFinishActivity()
        }
    }

    private fun launchAddMode() {
        addErrorInputListener()
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.addShopItem(inputName = name, inputCount = count)
            onFinishActivity()
        }
    }

    private fun addErrorInputListener() {
        binding.etName.addTextChangedListener(ClearError {
            shopItemViewModel.resetErrorInputName()
        })
        binding.etCount.addTextChangedListener(ClearError {
            shopItemViewModel.resetErrorInputCount()
        })
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it) {
                binding.tilName.error = "Name is require field"
            } else {
                binding.tilName.error = ""
            }
        }
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it) {
                binding.tilCount.error = "Count is must be more than zero"
            } else {
                binding.tilCount.error = ""
            }
        }
    }

    private fun setupViewEditShopItem() {
        shopItemViewModel.getShopItem(itemId)
        shopItemViewModel.currentShopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.title)
            binding.etCount.setText(it.count.toString())
        }
    }

    private fun onFinishActivity() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private class ClearError(val callback: () -> Unit) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Mode $mode is unknown")
        }
        screenMode = mode
        if (mode == MODE_EDIT && !args.containsKey(SHOP_ITEM_ID)) {
            throw RuntimeException("Param id is absent")
        }
        itemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
    }


    companion object {
        private const val SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val SHOP_ITEM_ID = "EXTRA_ITEM_ID"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEditItem(itemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, itemId)
                }
            }
        }

        fun newInstanceAddItem(): ShopItemFragment {
            return  ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
    }
}