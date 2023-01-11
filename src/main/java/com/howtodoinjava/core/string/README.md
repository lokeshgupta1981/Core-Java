# Related Tutorials

## String Methods

*   [_char charAt(int index)_](https://howtodoinjava.com/java/string/string-charat-method-example/) – Returns the character at the specified index. The specified index value should be between `'0'` to `'length() -1'` both inclusive. It throws `IndexOutOfBoundsException` if index is invalid/ out of range.
*   [_boolean equals(Object obj)_](https://howtodoinjava.com/java/string/string-equals-method/) – Compares the string with the specified string and returns **true** if both match else **false**.
*   [_boolean equalsIgnoreCase(String string)_](https://howtodoinjava.com/java/string/string-equalsignorecase-method/) – Compares same as `equals` method but in case-insensitive way.
*   [_int compareTo(String string)_](https://howtodoinjava.com/java/string/java-string-compareto-method/) – Compares the two strings lexicographically based on the Unicode value of each character in the strings. You can consider it a dictionary-based comparison. The return value is 0 if the argument string is equal to this string; a value less than 0 if this string is lexicographically less than the string argument, and a value greater than 0 if this string is lexicographically greater than the string argument.
*   [_int compareToIgnoreCase(String string)_](https://howtodoinjava.com/java/string/string-comparetoignorecase-example/) – Same as `CompareTo` method however it ignores the case during comparison.
*   [_boolean startsWith(String prefix, int offset)_](https://howtodoinjava.com/java/string/java-string-startswith-example/) – Checks whether the String has the specified prefix or not – starting from the specified offset index.
*   [_boolean startsWith(String prefix)_](https://howtodoinjava.com/java/string/java-string-startswith-example/) – Tests whether the string is having specified `prefix`, if yes then it returns `true` else `false`. The offset index value is 0 in this overloaded method.
*   [_boolean endsWith(String suffix)_](https://howtodoinjava.com/java/string/java-string-endswith-method/) – Checks whether the string ends with the specified suffix.
*   [_int hashCode()_](https://howtodoinjava.com/java/string/string-hashcode-method/) – Returns the hash code of the string.
*   [_int indexOf(int ch)_](https://howtodoinjava.com/java/string/java-string-indexof-method-example/) – Returns the index of first occurrence of the specified character argument in the string.
*   [_int indexOf(int ch, int fromIndex)_](https://howtodoinjava.com/java/string/java-string-indexof-method-example/) – Overloaded version of `indexOf(char ch)` method however it starts searching in the string from the specified `fromIndex`.
*   [_int indexOf(String str)_](https://howtodoinjava.com/java/string/java-string-indexof-method-example/) – Returns the index of first occurrence of specified substring `'str'`.
*   [_int indexOf(String str, int fromIndex)_](https://howtodoinjava.com/java/string/java-string-indexof-method-example/) – Overloaded version of `indexOf(String str)` method however it starts searching in the string from the specified `fromIndex`.
*   [_int lastIndexOf(int ch)_](https://howtodoinjava.com/java/string/string-lastindexof-method/) – Returns the last occurrence of the character `'ch'` in the string.
*   [_int lastIndexOf(int ch, int fromIndex)_](https://howtodoinjava.com/java/string/string-lastindexof-method/) – Overloaded version of `lastIndexOf(int ch)` method. It starts searching backward starting at the `fromIndex`.
*   [_int lastIndexOf(String str)_](https://howtodoinjava.com/java/string/string-lastindexof-method/) – Returns the index of last occurrence of string `'str'`. It is similar to `lastIndexOf(int ch)`.
*   [_int lastIndexOf(String str, int fromIndex)_](https://howtodoinjava.com/java/string/string-lastindexof-method/) – Overloaded version of `lastIndexOf(String str)` method. It starts searching backward starting at the `fromIndex`.
*   [_String substring(int beginIndex)_](https://howtodoinjava.com/java/string/java-string-substring-example/) – Returns the substring of the string. The substring starts with the character at the specified index.
*   [_String substring(int beginIndex, int endIndex)_](https://howtodoinjava.com/java/string/java-string-substring-example/) – Returns the substring. The substring starts with character at beginIndex and ends with the character at endIndex.
*   [_String concat(String str)_](https://howtodoinjava.com/java/string/java-string-concat-method-example/) – Concatenates the specified string argument at the end of the string.
*   [_String replace(char oldChar, char newChar)_](https://howtodoinjava.com/java/string/java-string-replace-method/) – Returns the new updated string after changing all the occurrences of oldChar with the newChar arguments.
*   [_String replace(String target, String replacement)_](https://howtodoinjava.com/java/string/java-string-replace-method/) – Returns the new updated string after changing all the occurrences of `target` with the `replacement` argument.
*   [_String replaceFirst(String regex, String replacement)_](https://howtodoinjava.com/java/string/java-string-replacefirst-example/) – Replaces the first occurrence of substring that matches the given [regular expression](https://howtodoinjava.com/java-regular-expression-tutorials/) argument with the specified replacement string.
*   [_String replaceAll(String regex, String replacement)_](https://howtodoinjava.com/java/string/java-string-replaceall-example/) – Replaces all the occurrences of substrings that matches the regular expression argument with the replacement string.
*   [_String\[\] split(String regex, int limit)_](https://howtodoinjava.com/java/string/java-string-split-example/) – Splits the string and returns the array of sub-strings that matches the given regular expression. `'limit'` is a maximum number of elements in array.
*   [_String\[\] split(String regex)_](https://howtodoinjava.com/java/string/java-string-split-example/) – Overload of previous method without any threshold limit.
*   [_boolean contains(CharSequence s)_](https://howtodoinjava.com/java/string/java-string-contains-example/) – Checks whether the string contains the specified sequence of char values. If yes then it returns `true` else `false`. It throws [NullPointerException](https://howtodoinjava.com/java/exception-handling/how-to-effectively-handle-nullpointerexception-in-java/) if argument is null.
*   [_String toUpperCase(Locale locale)_](https://howtodoinjava.com/java/string/java-string-touppercase-method/) – Converts the string to upper case string using the rules defined by specified locale.
*   [_String toUpperCase()_](https://howtodoinjava.com/java/string/java-string-touppercase-method/) – Overloaded version of previous `toUpperCase()` method with default locale.
*   [_String toLowerCase(Locale locale)_](https://howtodoinjava.com/java/string/java-string-tolowercase-method/) – Converts the string to lower case string using the rules defined by given locale.
*   [_String toLowerCase()_](https://howtodoinjava.com/java/string/java-string-tolowercase-method/) – Overloaded version of previous method with default locale.
*   [_String intern()_](https://howtodoinjava.com/java/string/java-string-intern-method-example/) – Searches the specified string in the memory pool and returns its reference if it is found. Otherwise, this method allocates creates string literal in string pool and return the reference.

## String Conversions

* [Convert Java String to int](https://howtodoinjava.com/java/string/convert-string-to-int/)
* [Convert int to String in Java](https://howtodoinjava.com/java/string/convert-int-to-string/)
* [Convert String to Long](https://howtodoinjava.com/java/string/convert-string-to-long/)
* [Convert Long to String in Java](https://howtodoinjava.com/java/string/convert-long-to-string/)
* [Convert String to Date](https://howtodoinjava.com/java/date-time/java-date-examples/)
* [Convert Date to String](https://howtodoinjava.com/java/date-time/java-date-examples/)
* [Convert String to String\[\] Example](https://howtodoinjava.com/java/array/string-to-string-array/)
* [Java 8 – Join String Array – Convert Array to String](https://howtodoinjava.com/java/array/join-string-array-example/)
* [Convert String to InputStream Example](https://howtodoinjava.com/java/io/convert-string-to-inputstream/)
* [Convert InputStream to String Example](https://howtodoinjava.com/java/io/inputstream-to-string/)
* [Java Split CSV String – Convert String to List Example](https://howtodoinjava.com/java/string/java-split-csv-string-to-list/)
* [Join CSV to String](https://howtodoinjava.com/java8/java-8-string-join-csv-example/)
* [Unescape HTML to String Example](https://howtodoinjava.com/java/string/unescape-html-to-string/)
* [Escape HTML – Encode String to HTML Example](https://howtodoinjava.com/java/string/escape-html-encode-string/)
* [Convert byte array to String](https://howtodoinjava.com/java/array/java-convert-byte-array-to-string-example/)
* [StackTrace to String conversion](https://howtodoinjava.com/java/string/convert-stacktrace-to-string/)
* [Convert float to String – Format to N decimal points](https://howtodoinjava.com/java/string/float-to-string-format-decimal-points/)

## Useful Examples

* [Reverse a String in Java using Recursion](https://howtodoinjava.com/java/string/reverse-string-using-recursion/)
* [Remove extra white spaces between words](https://howtodoinjava.com/java/string/remove-extra-whitespaces-between-words/)
* [Remove only leading spaces of a String](https://howtodoinjava.com/java/string/remove-leading-trailing-whitespaces/)
* [Remove only trailing spaces of a String](https://howtodoinjava.com/java/string/remove-leading-trailing-whitespaces/)
* [How to Reverse String in Java](https://howtodoinjava.com/java/puzzles/how-to-reverse-string-in-java/)
* [Reverse words in a string in Java](https://howtodoinjava.com/java/string/reverse-words-in-string/)
* [Reverse string in Java using recursion](https://howtodoinjava.com/java/string/reverse-string-using-recursion/)
* [How to find duplicate words in String](https://howtodoinjava.com/java/string/how-to-find-duplicate-words-in-a-string-in-java/)
* [How to find duplicate characters in a String](https://howtodoinjava.com/java/string/find-duplicate-characters/)
* [Java Sort String Characters Alphabetically](https://howtodoinjava.com/java/sort/sort-string-chars-alphabetically/)
* [Convert String to Title Case](https://howtodoinjava.com/java/string/convert-string-to-titlecase/)
* [4 ways to split a String](https://howtodoinjava.com/java/string/split-tokenize-strings/)
* [Left, right, or center align string](https://howtodoinjava.com/java/string/left-right-or-center-align-string/)
* [Read File to String](https://howtodoinjava.com/java/io/java-read-file-to-string-examples/)
* [Java 8 StringJoiner Example](https://howtodoinjava.com/java/string/java8-stringjoiner-example/)
* [Left pad a string with spaces or zeros](https://howtodoinjava.com/java/string/left-pad-string-with-spaces-zeros/)
* [Right pad a string with spaces or zeros](https://howtodoinjava.com/java/string/right-pad-string-with-spaces-zeros/)
* [Get first 4 characters of a string](https://howtodoinjava.com/java/string/get-first-4-characters/)
* [Get last 4 characters of a string](https://howtodoinjava.com/java/string/get-last-4-characters/)
* [Mask a String Except Last 4 Chars](https://howtodoinjava.com/java/string/mask-string-except-last-4-chars/)
* [Format string to (123) 456-7890 pattern](https://howtodoinjava.com/java/string/format-phone-number/)
* [Removing Last Character from String](https://howtodoinjava.com/java/string/java-string-remove-last-char/)

## FAQs

* [Always Use length() Instead of equals() to Check Empty String](https://howtodoinjava.com/java/string/always-use-length-instead-of-equals-to-check-empty-string-in-java/)
* [Why String is immutable](https://howtodoinjava.com/java/string/java-interview-question-why-strings-are-immutable/)
* [Java String Interview Questions](https://howtodoinjava.com/interview-questions/string-questions/)