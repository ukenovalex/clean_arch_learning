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

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val itemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {
    private val shopItemViewModel: ShopItemViewModel by activityViewModels()

    private var _binding: ShopItemFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parseParams()
        _binding = ShopItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }
    private fun launchEditMode() {
        setupViewEditShopItem()
        addErrorInputListener()

        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.editShopItem(inputName = name, inputCount = count)
        }
        onFinishActivity()
    }

    private fun launchAddMode() {
        addErrorInputListener()
        binding.saveButton.setOnClickListener {
            val name = binding.etName.text.toString()
            val count = binding.etCount.text.toString()
            shopItemViewModel.addShopItem(inputName = name, inputCount = count)
        }
        onFinishActivity()
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
        if (screenMode != MODE_ADD && screenMode != MODE_EDIT) {
            throw RuntimeException("Mode $screenMode is unknown")
        }
        if (screenMode == MODE_EDIT && itemId == ShopItem.UNDEFINED_ID) {
            throw RuntimeException("Param id is absent")
        }
    }


    companion object {
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEditItem(itemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, itemId)
        }

        fun newInstanceAddItem(): ShopItemFragment {
            return  ShopItemFragment(MODE_ADD)
        }
    }
}