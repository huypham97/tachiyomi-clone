package com.example.tachiyomi_clone.utils

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URI
import java.net.URISyntaxException

fun getUrlWithoutDomain(orig: String): String {
    return try {
        val uri = URI(orig.replace(" ", "%20"))
        var out = uri.path
        if (uri.query != null) {
            out += "?" + uri.query
        }
        if (uri.fragment != null) {
            out += "#" + uri.fragment
        }
        out
    } catch (e: URISyntaxException) {
        orig
    }
}

fun imageOrNull(element: Element): String? {
    fun Element.hasValidAttr(attr: String): Boolean {
        val regex = Regex("""https?://.*""", RegexOption.IGNORE_CASE)
        return when {
            this.attr(attr).isNullOrBlank() -> false
            this.attr("abs:$attr").matches(regex) -> true
            else -> false
        }
    }

    return when {
        element.hasValidAttr("data-original") -> element.attr("abs:data-original")
        element.hasValidAttr("data-src") -> element.attr("abs:data-src")
        element.hasValidAttr("src") -> element.attr("abs:src")
        else -> null
    }
}

fun getAllElements(document: Document?, className: String): Elements? {
    return document?.body()?.select(className)
}

fun getSingleElement(document: Document?, className: String): Element? {
    return document?.body()?.select(className)?.first()
}

fun elementToString(element: Element?, className: String): String? {
    return element?.select(className)?.first()?.text()
}

fun nextSiblingToString(element: Element?, className: String): String? {
    return element?.select(className)?.first()?.nextElementSibling()?.text()
}

fun lastSiblingToString(element: Element?, className: String): String? {
    return element?.select(className)?.first()?.lastElementSibling()?.text()
}

fun getAttrFromHtml(element: Element?, className: String, attr: String): String? {
    return element?.select(className)?.first()?.attr(attr)
}