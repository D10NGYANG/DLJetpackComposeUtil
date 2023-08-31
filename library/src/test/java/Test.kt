import androidx.compose.ui.graphics.Color
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.utils.isDark
import org.junit.Test

class Test {

    @Test
    fun test() {
        println(Color.White.isDark())
        println(AppColor.Main.primary.isDark())
    }
}