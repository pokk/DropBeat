/*
 * MIT License
 *
 * Copyright (c) 2019 SmashKs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package taiwan.no.one.ktx.edittext

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.diseditable() = apply {
    isFocusableInTouchMode = false
}

fun EditText.toCalendarField() = apply {
    diseditable()
    inputType = InputType.TYPE_CLASS_DATETIME
    isCursorVisible = false
}

fun EditText.toNumberType() = apply {
    inputType = InputType.TYPE_CLASS_NUMBER
}

fun EditText.toEmailType() = apply {
    inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT
}

fun EditText.toPhoneType() = apply {
    inputType = InputType.TYPE_CLASS_PHONE
}

fun EditText.enterToCloseKeyboard() = apply {
    imeOptions = EditorInfo.IME_ACTION_DONE
}
