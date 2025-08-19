import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        //Найдите в списке целых чисел 3-е наибольшее число
        int result = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
        System.out.println("3-е наибольшее число: " + result);

        //Найдите в списке целых чисел 3-е наибольшее «уникальное» число
        result = Stream.of(5, 2, 10, 9, 4, 3, 10, 1, 13)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Something went wrong"));
        System.out.println("3-е наибольшее уникальное число: " + result);

        //Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3
        // самых старших сотрудников с должностью «Инженер», в порядке убывания возраста

        List<Employee> employees = List.of(
                new Employee("Алекс", 20, "Инженер"),
                new Employee("Иван", 25, "Инженер"),
                new Employee("Вася", 27, "Инженер"),
                new Employee("Оля", 20, "Директор"),
                new Employee("Маша", 38, "Инженер"),
                new Employee("Антон", 18, "Стажер"),
                new Employee("Павел", 58, "Главный инженер"));
        List<String> names = employees.stream()
                .filter(k -> k.position().equals("Инженер"))
                .sorted(Comparator.comparingInt(Employee::age).reversed())
                .limit(3)
                .map(Employee::name)
                .toList();
        System.out.println("Список имен 3 самых старших сотрудников с должностью Инженер: " + names);

        //Имеется список объектов типа Сотрудник (имя, возраст, должность),
        // посчитайте средний возраст сотрудников с должностью «Инженер»
        Double avgAge = employees.stream()
                .filter(k -> k.position().equals("Инженер"))
                .collect(Collectors.averagingDouble(Employee::age));
        System.out.println("Средний возраст сотрудников с должностью «Инженер: " + avgAge);

        //Найдите в списке слов самое длинное
        String longWord = Stream.of("F", "BB", "Test", "Test123", "B", "Test12")
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
        System.out.println("Самое длинное слово в списке: " + longWord);

        //Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы,
        // в которой будут хранится пары: слово - сколько раз оно встречается во входной строке

        String text = "test stream java stream max java world test test";
        Map<String, Long> wordsMap = Arrays.stream(text.split("\\s+"))
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));
        System.out.println("Пары: слово - сколько раз оно встречается во входной строке" + wordsMap);

        //Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины,
        // то должен быть сохранен алфавитный порядок
        List<String> words = List.of("test", "stream", "java", "new", "max", "bug", "worldd", "test", "big",
                "bag", "destination");
        List<String> sortedWords = words.stream()
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder())).toList();
        System.out.println("Строки из списка в порядке увеличения длины слова + алфавитный порядок" + sortedWords);

        //Имеется массив строк, в каждой из которых лежит набор из 5 слов, разделенных пробелом,
        // найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них

        String[] strings = {"в каждой из которых набор", "найдите среди всех слов самое",
                "если таких слов несколько получите", "консоль строки из списка в",
                "набором слов в нижнемнем регистре" };
        String maxWord = Arrays.stream(strings)
                .flatMap(sentence -> Arrays.stream(sentence.split("\\s+")))
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> new RuntimeException("Something went wrong"));
        System.out.println("Самое длинное слово из массива строк: " + maxWord);
    }
}