import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

public class TestClass {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("BeforeSuite выполнен");
    }

    @AfterSuite
    public static void afterSuite() {
        System.out.println("AfterSuite выполнен");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("Test 1 с приоритетом 1 выполнен");
    }

    @Test(priority = 10)
    public void test10() {
        System.out.println("Test 10 с приоритетом 10 выполнен");
    }

    @Test
    public void test5() {
        System.out.println("Test 5 с приоритетом 5 выполнен");
    }

    @Test(priority = 3)
    public void test3() {
        System.out.println("Test 3 с приоритетом 3 выполнен");
    }

    public void noTest() {
        System.out.println("Этот метод не должен выполниться");
    }
}