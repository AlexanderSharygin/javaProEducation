package runner;

import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Runner {

    public static void run(Class<?> classToRun) {
        List<Method> methods = Arrays.stream(classToRun.getDeclaredMethods()).toList();
        List<Method> methodsWithBeforeSuite = methods.stream()
                .filter(m -> m.isAnnotationPresent(BeforeSuite.class)).toList();
        List<Method> methodsWithAfterSuite = methods.stream()
                .filter(m -> m.isAnnotationPresent(AfterSuite.class)).toList();
        List<Method> testMethods = methods.stream()
                .filter(m -> m.isAnnotationPresent(Test.class)).toList();

        checkBeforeAfterAnnotations(methodsWithBeforeSuite, methodsWithAfterSuite);

        Object testClassInstance;
        try {
            testClassInstance = classToRun.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Something went wrong - check constructors for your class");
        }

        if (!methodsWithBeforeSuite.isEmpty()) {
            executeStaticMethod(methodsWithBeforeSuite.getFirst());
        }
        if (!testMethods.isEmpty()) {
            executeTests(testMethods, testClassInstance);
        }
        if (!methodsWithAfterSuite.isEmpty()) {
            executeStaticMethod(methodsWithAfterSuite.getFirst());
        }
    }

    private static void checkBeforeAfterAnnotations(List<Method> methodsWithBeforeSuite,
                                                    List<Method> methodsWithAfterSuite) {
        if (methodsWithBeforeSuite.size() > 1) {
            throw new RuntimeException("More than one @BeforeSuite method found");
        }
        if (methodsWithAfterSuite.size() > 1) {
            throw new RuntimeException("More than one @BAfterSuite method found");
        }
        methodsWithBeforeSuite.forEach(m -> {
            if (!Modifier.isStatic(m.getModifiers())) {
                throw new RuntimeException("@BeforeSuite could be used only for static methods");
            }
        });
        methodsWithAfterSuite.forEach(m -> {
            if (!Modifier.isStatic(m.getModifiers())) {
                throw new RuntimeException("@AfterSuite could be used only for static methods");
            }
        });
    }

    private static void executeStaticMethod(Method methodToRun) {
        try {
            methodToRun.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
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
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Something went wrong during Test method execution with name "
                        + method.getName());
            }
        }
    }
}