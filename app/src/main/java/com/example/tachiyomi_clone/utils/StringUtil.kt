import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern

fun toKhongDau(str: String?): String {
    try {
        val temp = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("").lowercase(Locale.getDefault())
            .replace(" ".toRegex(), "-").replace("đ".toRegex(), "d")
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return ""
}