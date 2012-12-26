import java.lang.reflect.*;
public class VoidTest {
    public static void main(String[] args) throws NoSuchMethodException {
        Method m = VoidTest.class.getMethod("main", String[].class);
        System.out.println(m.getReturnType().isAssignableFrom(void.class));
        System.out.println(void.class.isPrimitive());
        System.out.println(void.class.isAssignableFrom(void.class));
    }
}
