package com.example.tachiyomi_clone.ui.common

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.BottomSheetDialog
import com.example.tachiyomi_clone.common.widget.VerticalSpacingDecoration
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.databinding.DialogListIndexBinding
import com.example.tachiyomi_clone.ui.manga.ChapterAdapter
import kotlinx.coroutines.runBlocking

class ListIndexBottomSheetDialog(
    private var listChapter: List<ChapterEntity>,
    private var onSelectChapterListener: ((ChapterEntity) -> Unit)?
) :
    BottomSheetDialog<DialogListIndexBinding>() {

    companion object {
        private fun newInstance(
            listChapter: List<ChapterEntity>,
            onSelectChapterListener: ((ChapterEntity) -> Unit)?
        ) =
            ListIndexBottomSheetDialog(listChapter, onSelectChapterListener)

        @JvmStatic
        fun show(
            fragmentManager: FragmentManager,
            listChapter: List<ChapterEntity>,
            onSelectChapterListener: ((ChapterEntity) -> Unit)? = null
        ) {
            val listIndexBottomSheetDialog = newInstance(listChapter, onSelectChapterListener)
            listIndexBottomSheetDialog.show(fragmentManager, listIndexBottomSheetDialog.tag)
        }
    }

    private var chapterAdapter: ChapterAdapter = ChapterAdapter()
    private var isAscending = true

    override fun getLayoutResId(): Int {
        return R.layout.dialog_list_index
    }

    override fun initViews() {
        super.initViews()
        runBlocking {
            listChapter = listChapter.sortedBy { it.sourceOrder }
        }
        binding.tvChaptersCount.text = "Đã ra ${listChapter.size} chương"
        binding.tvChapterSortTitle.text =
            resources.getText(if (isAscending) R.string.newest else R.string.oldest)
        binding.rvChapter.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                VerticalSpacingDecoration(
                    requireContext(),
                    R.drawable.divider
                )
            )
            adapter = chapterAdapter
        }
        chapterAdapter.refreshList(listChapter)
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.btnSort.setOnClickListener {
            sortChapterList()
            binding.tvChapterSortTitle.text =
                resources.getText(if (isAscending) R.string.newest else R.string.oldest)
        }

        chapterAdapter.onSelectChapterListener = {
            onSelectChapterListener?.invoke(it)
            dismiss()
        }
    }

    private fun sortChapterList() {
        isAscending = !isAscending
        listChapter =
            if (isAscending) listChapter.sortedBy { it.sourceOrder } else listChapter.sortedByDescending { it.sourceOrder }
        chapterAdapter.refreshList(listChapter)
    }

}
