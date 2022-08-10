package ca.ukenov.shoppinglist.presentation

import android.content.Context
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

    private lateinit var onFinishShopItemFragment: OnFinishShopItemFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFinishShopItemFragment) {
            onFinishShopItemFragment = context
        } else {
            throw RuntimeException("Activity must implement OnFinishShopItemFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parseParams()
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = shopItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
        return binding.root
    }
    private fun launchEditMode() {
        shopItemViewModel.getShopItem(itemId)
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
    }

    private fun onFinishActivity() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onFinishShopItemFragment.finishShopItemFragment()
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


    interface OnFinishShopItemFragment {
        fun finishShopItemFragment()
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