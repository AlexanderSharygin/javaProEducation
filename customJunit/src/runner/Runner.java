package runner;

import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Runner {

    private static Method beforeSuiteMethod;
    private static Method afterSuiteMethod;
    private static final List<Method> testMethods = new ArrayList<>();

    public static void run(Class<?> classToRun) {
        Method[] methods = classToRun.getDeclaredMethods();
        Object testClassInstance;
        try {
            testClassInstance = classToRun.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Something went wrong - check constructors for your class");
        }

        parseMethodAnnotations(methods);

        if (beforeSuiteMethod != null) {
            executeStaticMethod(beforeSuiteMethod);
        }

        if (!testMethods.isEmpty()) {
            executeTests(testMethods, testClassInstance);
        }

        if (afterSuiteMethod != null) {
            executeStaticMethod(afterSuiteMethod);
        }
    }

    private static void parseMethodAnnotations(Method[] methods) {
        int beforeSuiteCount = 0;
        int afterSuiteCount = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteCount > 0) {
                    throw new RuntimeException("More than one @BeforeSuite method found");
                }
                beforeSuiteMethod = method;
                beforeSuiteCount++;
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException("@BeforeSuite could be used only for static methods");
                }
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteCount > 0) {
                    throw new RuntimeException("More than one @AfterSuite method found");
                }
                afterSuiteMethod = method;
                afterSuiteCount++;
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException("@AfterSuite could be used only for static methods");
                }
            }
            if (method.isAnnotationPresent(Test.class)) {
                if (method.getAnnotation(Test.class).priority() > 10 || method.getAnnotation(Test.class).priority() < 1) {
                    throw new RuntimeException("Method " + method.getName() + " has invalid priority. " +
                            "Priority should be from 1 to 10.");
                }
                testMethods.add(method);
            }
        }
    }

    private static void executeStaticMethod(Method methodToRun) {
        try {
            methodToRun.invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Something went wrong during BeforeSuite/AfterSuite method execution with " +
                    "name " + methodToRun.getName());

        }
    }

    private static void executeTests(List<Method> methods, Object testInstance) {
        List<Method> sortedMethods = methods.stream()
                .sorted(Comparator.comparingInt(m -> -m.getAnnotation(Test.class).priority())).toList();
        for (Method method : sortedMethods) {
            if (method.getAnnotation(Test.class).priority() > 10) {
                System.out.println("Метод " + method.getName() + " пропущен так как имеет приоритет "
                        + method.getAnnotation(Test.class).priority() + ". Приоритет выше максимального (10)");
                continue;
            }
            if (method.getAnnotation(Test.class).priority() < 1) {
                System.out.println("Метод " + method.getName() + " пропущен так как имеет приоритет "
                        + method.getAnnotation(Test.class).priority() + ". Приоритет ниже минимального (1)");
                continue;
            }
            try {
                method.invoke(testInstance);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Something went wrong during Test method execution with name "
                        + method.getName());
            }
        }
    }
}