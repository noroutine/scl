public class AssertTest {
    public static void main(String[] args) {
        System.out.println("Hello");

        int i = 0;

        assert ++i > 0;

        System.out.println("assertions " + ((i == 0)? "disabled" : "enabled"));
    }
}
