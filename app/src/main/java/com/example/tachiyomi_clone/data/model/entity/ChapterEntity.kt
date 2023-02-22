package com.example.tachiyomi_clone.data.model.entity

import android.os.Parcel
import android.os.Parcelable

data class ChapterEntity(
    var id: Long,
    var mangaId: Long,
    var read: Boolean,
    var sourceOrder: Long,
    var url: String,
    var name: String,
    var dateUpload: Long,
    var nextChapterOrder: Long?,
    var prevChapterOrder: Long?
) : Parcelable {

    companion object {
        private const val NUMBER_PATTERN = """([0-9]+)(\.[0-9]+)?(\.?[a-z]+)?"""

        fun create() = ChapterEntity(
            id = -1,
            mangaId = -1,
            read = false,
            sourceOrder = 0,
            url = "",
            name = "",
            dateUpload = 0,
            nextChapterOrder = null,
            prevChapterOrder = null
        )

        @JvmField
        val CREATOR = object : Parcelable.Creator<ChapterEntity> {
            override fun createFromParcel(parcel: Parcel): ChapterEntity {
                return ChapterEntity(parcel)
            }

            override fun newArray(size: Int): Array<ChapterEntity?> {
                return arrayOfNulls(size)
            }
        }
    }

    private val CHAPTER_TRIM_CHARS = arrayOf(
        // Whitespace
        ' ',
        '\u0009',
        '\u000A',
        '\u000B',
        '\u000C',
        '\u000D',
        '\u0020',
        '\u0085',
        '\u00A0',
        '\u1680',
        '\u2000',
        '\u2001',
        '\u2002',
        '\u2003',
        '\u2004',
        '\u2005',
        '\u2006',
        '\u2007',
        '\u2008',
        '\u2009',
        '\u200A',
        '\u2028',
        '\u2029',
        '\u202F',
        '\u205F',
        '\u3000',

        // Separators
        '-',
        '_',
        ',',
        ':',
    ).toCharArray()

    private val unwantedWhiteSpace = Regex("""\s(?=extra|special|omake)""")

    private val unwanted = Regex("""\b(?:v|ver|vol|version|volume|season|s)[^a-z]?[0-9]+""")

    private val basic = Regex("""(?<=ch\.) *$NUMBER_PATTERN""")

    private val number = Regex(NUMBER_PATTERN)

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
    ) {
    }

    fun cleanupChapterName(chapterName: String, mangaTitle: String): String {
        return chapterName.trim().removePrefix(mangaTitle).trim(*CHAPTER_TRIM_CHARS)
    }

    fun parseChapterNumber(
        mangaTitle: String,
        chapterName: String,
        chapterNumber: Float? = null
    ): Float {
        // If chapter number is known return.
        if (chapterNumber != null && (chapterNumber == -2f || chapterNumber > -1f)) {
            return chapterNumber
        }

        // Get chapter title with lower case
        var name = chapterName.lowercase()

        // Remove manga title from chapter title.
        name = name.replace(mangaTitle.lowercase(), "").trim()

        // Remove comma's or hyphens.
        name = name.replace(',', '.').replace('-', '.')

        // Remove unwanted white spaces.
        name = unwantedWhiteSpace.replace(name, "")

        // Remove unwanted tags.
        name = unwanted.replace(name, "")

        // Check base case ch.xx
        basic.find(name)?.let { return getChapterNumberFromMatch(it) }

        // Take the first number encountered.
        number.find(name)?.let { return getChapterNumberFromMatch(it) }

        return chapterNumber ?: -1f
    }

    private fun getChapterNumberFromMatch(match: MatchResult): Float {
        return match.let {
            val initial = it.groups[1]?.value?.toFloat()!!
            val subChapterDecimal = it.groups[2]?.value
            val subChapterAlpha = it.groups[3]?.value
            val addition = checkForDecimal(subChapterDecimal, subChapterAlpha)
            initial.plus(addition)
        }
    }

    private fun checkForDecimal(decimal: String?, alpha: String?): Float {
        if (!decimal.isNullOrEmpty()) {
            return decimal.toFloat()
        }

        if (!alpha.isNullOrEmpty()) {
            if (alpha.contains("extra")) {
                return .99f
            }

            if (alpha.contains("omake")) {
                return .98f
            }

            if (alpha.contains("special")) {
                return .97f
            }

            val trimmedAlpha = alpha.trimStart('.')
            if (trimmedAlpha.length == 1) {
                return parseAlphaPostFix(trimmedAlpha[0])
            }
        }

        return .0f
    }

    private fun parseAlphaPostFix(alpha: Char): Float {
        val number = alpha.code - ('a'.code - 1)
        if (number >= 10) return 0f
        return number / 10f
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(mangaId)
        parcel.writeByte(if (read) 1 else 0)
        parcel.writeLong(sourceOrder)
        parcel.writeString(url)
        parcel.writeString(name)
        parcel.writeLong(dateUpload)
        parcel.writeLong(nextChapterOrder ?: -1)
        parcel.writeLong(prevChapterOrder ?: -1)
    }

    override fun describeContents(): Int {
        return 0
    }

}