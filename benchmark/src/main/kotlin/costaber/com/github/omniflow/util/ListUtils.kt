package costaber.com.github.omniflow.util

import java.util.stream.Collectors

object ListUtils {
    fun collectListString(list: MutableList<String?>): String {
        return list.stream()
            .filter { str: String? -> !str!!.isEmpty() }
            .collect(Collectors.joining(System.lineSeparator()))
    }
}
