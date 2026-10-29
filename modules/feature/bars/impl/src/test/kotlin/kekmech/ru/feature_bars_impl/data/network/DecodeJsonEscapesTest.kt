package kekmech.ru.feature_bars_impl.data.network

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DecodeJsonEscapesTest : StringSpec({

    "unicode escapes of the server message are decoded" {
        decodeJsonEscapes("""Сообщение отправлено в 'MAX'""") shouldBe "Сообщение отправлено в 'MAX'"
    }

    "escaped Cyrillic letters are decoded" {
        decodeJsonEscapes("""Код""") shouldBe "Код"
    }

    "common escapes are decoded" {
        decodeJsonEscapes("""a\nb\"c\\d\/e""") shouldBe "a\nb\"c\\d/e"
    }

    "plain text stays as it is" {
        decodeJsonEscapes("Сообщение отправлено в 'MAX'") shouldBe "Сообщение отправлено в 'MAX'"
    }
})
