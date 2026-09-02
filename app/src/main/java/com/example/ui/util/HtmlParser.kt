package com.example.ui.util

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.core.text.HtmlCompat

/**
 * Parses an HTML string (supporting tags like <i>, <b>, <em>, <strong>, <u>, <br>, <p>, etc.)
 * into a Jetpack Compose AnnotatedString with matching SpanStyles.
 */
fun parseHtmlToAnnotatedString(html: String): AnnotatedString {
    if (html.isBlank()) return AnnotatedString("")

    val spanned: Spanned = try {
        HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
    } catch (_: Exception) {
        return AnnotatedString(html)
    }

    val rawText = spanned.toString()
    val trimmedText = rawText.trimEnd('\n', '\r')
    val length = trimmedText.length

    return buildAnnotatedString {
        append(trimmedText)

        val spans = spanned.getSpans(0, length, Any::class.java)
        for (span in spans) {
            val start = spanned.getSpanStart(span).coerceIn(0, length)
            val end = spanned.getSpanEnd(span).coerceIn(0, length)
            if (start >= end) continue

            when (span) {
                is StyleSpan -> {
                    when (span.style) {
                        Typeface.BOLD -> addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start,
                            end
                        )
                        Typeface.ITALIC -> addStyle(
                            SpanStyle(fontStyle = FontStyle.Italic),
                            start,
                            end
                        )
                        Typeface.BOLD_ITALIC -> addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic),
                            start,
                            end
                        )
                    }
                }
                is UnderlineSpan -> {
                    addStyle(
                        SpanStyle(textDecoration = TextDecoration.Underline),
                        start,
                        end
                    )
                }
                is StrikethroughSpan -> {
                    addStyle(
                        SpanStyle(textDecoration = TextDecoration.LineThrough),
                        start,
                        end
                    )
                }
                is ForegroundColorSpan -> {
                    addStyle(
                        SpanStyle(color = Color(span.foregroundColor)),
                        start,
                        end
                    )
                }
                is RelativeSizeSpan -> {
                    addStyle(
                        SpanStyle(fontSize = span.sizeChange.em),
                        start,
                        end
                    )
                }
                is SuperscriptSpan -> {
                    addStyle(
                        SpanStyle(baselineShift = BaselineShift.Superscript),
                        start,
                        end
                    )
                }
                is SubscriptSpan -> {
                    addStyle(
                        SpanStyle(baselineShift = BaselineShift.Subscript),
                        start,
                        end
                    )
                }
            }
        }
    }
}
