import org.junit.jupiter.api.Test;

/**
 * @author sinre
 * @create 09 30, 2022
 * @since 1.0.0
 */
public class UrlTest {
    @Test
    public void t() {
        String s = "file:/E:/maven/design-system/target/classes/static/upload/2022/studentInfo/e7deeccc-7f5d-44f7-962f-933d4176babd.xlsx";
        s = s.split("file:/")[1];
        System.out.println(s);
    }
}
