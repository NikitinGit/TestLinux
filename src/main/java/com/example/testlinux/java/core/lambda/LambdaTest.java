package com.example.testlinux.java.core.lambda;

import java.util.List;
import java.util.function.*;
import java.util.stream.Stream;

public class LambdaTest {

    static int b = 2;
    int c = 3;
    public static void main (String[] test) {
        functionInterfaces();

        Operationable op = Integer::sum;
        int a = op.getIntNumber(3, 5);
        System.out.println("a; " + a);

        op = Integer::divideUnsigned;
        int test2 = op.getIntNumber(15, 2);
        System.out.println("test2; " + test2);

        op = (b, c) -> b * c;
        int test1 = op.getIntNumber(3, 5);
        System.out.println("test1; " + test1);

        //AbstractClassMethod acm = LambdaTest::testAbstract; - не работает
        op = StaticMethod::test1;
        System.out.println("op = StaticMethod::test1; op.getIntNumber(3, 5); " + op.getIntNumber(3, 5));

        StaticMethod sm = new StaticMethod();
        op = sm::test2;
        System.out.println("op = sm::test2; op.getIntNumber(3, 5); " + op.getIntNumber(3, 5));

        GenericInterface<String> gin1 = (b, c) -> b + c;
        System.out.println("GenericInterface<String> gin1; op.getIntNumber(3, 5); " + gin1.test("3bign", "5"));
        GenericInterface<Double> gin2 = (b, c) -> b - c;
        System.out.println("GenericInterface<String> gin2; op.getIntNumber(3, 5); " + gin2.test(28.75d, 0.5d));

        GenericInterface<StaticMethod> gin3 = (b, c) -> b;
        GenericInterface<StaticMethod> gin4 = (b, c) -> new StaticMethod();

        ConstructorInterface ct = ConstructorTest::new;
        ct.test(125);

        LambdaTest lt = new LambdaTest();
        op = (x, y) -> {
            b++;
            b += ++lt.c;
            x += b;
            y *= x;

            return y;
        };

        System.out.println("op.getIntNumber(5, 2); " + op.getIntNumber(5, 2));

    }

    static void functionInterfaces() {
        task1_predicate();
        task2_binaryOperator();
        task3_unaryOperator();
        task4_function();
        task5_functionMethodRef();
        task6_biFunction();
        task7_consumer();
        task8_supplier();
        task9_predicateAnd();
        task10_functionAndThen();
        task11_binaryOperatorMethodRef();
        task12_consumerAndThen();
        streamWithLambdas();
    }

    // ЗАДАЧА 1: Predicate<Integer> — напиши лямбду которая возвращает true если x % 3 == 1
    static void task1_predicate() {
        Predicate<Integer> testInt = x -> x % 3 == 1;
        System.out.println("task1_predicate test2; " + testInt.test(2)); // false
        System.out.println("task1_predicate test4: " + testInt.test(4)); // true
    }

    // ЗАДАЧА 2: BinaryOperator<Integer> — напиши лямбду умножения двух чисел
    static void task2_binaryOperator() {
        BinaryOperator<Integer> multiply = (x, y) -> x * y;
        System.out.println("task2_binaryOperator multiply.apply(3, 5): " + multiply.apply(3, 5)); // 15
    }

    // ЗАДАЧА 3: UnaryOperator<Integer> — напиши лямбду возведения в квадрат
    static void task3_unaryOperator() {
        UnaryOperator<Integer> square = x -> x * x;
        System.out.println("task3_unaryOperator square.apply(5): " + square.apply(5)); // 25
    }

    // ЗАДАЧА 4: Function<Function1, Function2> — напиши лямбду которая создаёт новый Function2
    // с именем f1.getName() + " from lambda nikitin"
    static void task4_function() {
        Function1 f1 = new Function1("new Function1");
        Function<Function1, Function2> f1ToF2 = x -> new Function2(x.getName() + " from lambda nikitin");
        System.out.println("task4_function f2.getName: " + f1ToF2.apply(f1).getName());
    }

    // ЗАДАЧА 5: перепиши Function<Function1, Function2> через ссылку на метод вместо лямбды
    static void task5_functionMethodRef() {
        Function1 f1 = new Function1("new Function1");
        Function<Function1, Function2> f1ToF2 = Function1::getF2;
        System.out.println("task5_functionMethodRef f1ToF2 method ref: " + f1ToF2.apply(f1).getName());
    }

    // ЗАДАЧА 6: BiFunction<Integer, Function1, Function2> — напиши лямбду которая создаёт Function2
    // с именем y.getName() + ": " + (x * 2)
    static void task6_biFunction() {
        Function1 f1 = new Function1("new Function1");
        BiFunction<Integer, Function1, Function2> intWithF1ToF2 = (x, y) ->
                new Function2(y.getName() + ": " + (x * 2));
        System.out.println("task6_biFunction f3: " + intWithF1ToF2.apply(25, f1).getName());
    }

    // ЗАДАЧА 7: Consumer<Function2> — напиши лямбду которая меняет name у f2 на "consumerF2"
    static void task7_consumer() {
        Function2 f2 = new Function2("initial");
        Consumer<Function2> consumerF2 = x -> x.setName("consumerF2");
        consumerF2.accept(f2);
        System.out.println("task7_consumer f2.getName: " + f2.getName()); // consumerF2
    }

    // ЗАДАЧА 8: Supplier<Integer> — напиши лямбду которая возвращает случайное число от 1 до 10
    static void task8_supplier() {
        Supplier<Integer> randomValue = () -> (int)(Math.random() * 10) + 1;
        System.out.println("task8_supplier случайное значение: " + randomValue.get());
    }

    // ЗАДАЧА 9: Predicate.and() — напиши andTest: true если строка не пустая И длиннее 3 символов
    static void task9_predicateAnd() {
        Predicate<String> notEmpty = s -> !s.isEmpty();
        Predicate<String> longerThan3 = s -> s.length() > 3;
        Predicate<String> andTest = notEmpty.and(longerThan3);
        System.out.println("task9_predicateAnd andTest(\"\"):     " + andTest.test(""));      // false
        System.out.println("task9_predicateAnd andTest(\"ab\"):   " + andTest.test("ab"));    // false
        System.out.println("task9_predicateAnd andTest(\"abcd\"): " + andTest.test("abcd"));  // true
    }

    // ЗАДАЧА 10: Function.andThen() — напиши цепочку: Function1 -> Function2 -> String (getName)
    static void task10_functionAndThen() {
        Function1 f1 = new Function1("new Function1");
        Function<Function1, String> f1ToName = ((Function<Function1, Function2>) Function1::getF2)
                .andThen(Function2::getName);
        System.out.println("task10_functionAndThen f1ToName: " + f1ToName.apply(f1));
    }

    // ЗАДАЧА 11: BinaryOperator — напиши sum и max через ссылку на статический метод Integer (не лямбду)
    static void task11_binaryOperatorMethodRef() {
        BinaryOperator<Integer> sum = Integer::sum;
        BinaryOperator<Integer> max = Integer::max;
        System.out.println("task11_binaryOperatorMethodRef sum(3,5): " + sum.apply(3, 5)); // 8
        System.out.println("task11_binaryOperatorMethodRef max(3,5): " + max.apply(3, 5)); // 5
    }

    // ЗАДАЧА 12: Consumer.andThen() — объедини два Consumer:
    // первый меняет name на "first", второй добавляет " + second"
    static void task12_consumerAndThen() {
        Function2 f2 = new Function2("initial");
        Consumer<Function2> first  = x -> x.setName("first");
        Consumer<Function2> second = x -> x.setName(x.getName() + " + second");
        first.andThen(second).accept(f2);
        System.out.println("task12_consumerAndThen andThen consumer: " + f2.getName()); // first + second
    }

    // ===== лямбды в Stream API =====
    static void streamWithLambdas() {
        List<Function1> f1List = List.of(
                new Function1("Alice"),
                new Function1("Bob"),
                new Function1("Charlie"),
                new Function1("Al")
        );

        // Predicate → .filter()
        // оставить только те у кого name длиннее 3 символов
        Predicate<Function1> longName = f -> f.getName().length() > 3;
        f1List.stream()
                .filter(longName)
                .map(Function1::getName)
                .forEach(s -> System.out.println("streamWithLambdas filter: " + s));

        // Function → .map() — преобразование Function1 в Function2
        Function<Function1, Function2> toF2 = Function1::getF2;
        List<Function2> f2List = f1List.stream()
                .map(toF2)
                .toList();
        f2List.forEach(f -> System.out.println("streamWithLambdas map toF2: " + f.getName()));

        // UnaryOperator → .map() — преобразование строки саму в себя (uppercase)
        UnaryOperator<String> toUpper = String::toUpperCase;
        f1List.stream()
                .map(Function1::getName)
                .map(toUpper)
                .forEach(s -> System.out.println("streamWithLambdas toUpper: " + s));

        // BinaryOperator → .reduce() — склеить все имена через ", "
        BinaryOperator<String> joinNames = (a, b) -> a + ", " + b;
        String joined = f1List.stream()
                .map(Function1::getName)
                .reduce(joinNames)
                .orElse("");
        System.out.println("streamWithLambdas reduce: " + joined);

        // Consumer → .forEach() — напечатать каждый элемент
        Consumer<Function2> printF2 = f -> System.out.println("streamWithLambdas forEach consumer: " + f.getName());
        f2List.stream().forEach(printF2);

        // Supplier → Stream.generate() — бесконечный стрим, берём первые 3
        Supplier<Function1> supplier = () -> new Function1("generated");
        Stream.generate(supplier)
                .limit(3)
                .map(Function1::getName)
                .forEach(s -> System.out.println("streamWithLambdas generate: " + s));

        // Predicate.and() → .filter() с двумя условиями
        Predicate<Function1> startsWithA = f -> f.getName().startsWith("A");
        f1List.stream()
                .filter(longName.and(startsWithA))
                .map(Function1::getName)
                .forEach(s -> System.out.println("streamWithLambdas filter and: " + s)); // только Alice

        // Function.andThen() → .map() с цепочкой: Function1 -> Function2 -> String
        Function<Function1, String> f1ToName = toF2.andThen(Function2::getName);
        f1List.stream()
                .map(f1ToName)
                .forEach(s -> System.out.println("streamWithLambdas andThen: " + s));

        // BiFunction → нет прямого аналога в стриме, но можно через map с захватом переменной
        String prefix = "hello";
        f1List.stream()
                .map(f -> new Function2(prefix + " " + f.getName()))
                .map(Function2::getName)
                .forEach(s -> System.out.println("streamWithLambdas biFunction via map: " + s));
    }

    public ConstructorTest testConstruct(int i) {
        i += 25;
        return new ConstructorTest(i);
    }

    public static int testAbstract(int x, int y) {
        return x + y;
    }

    static class StaticMethod {

        static int test1(int x, int y) {
            return x + 2 * y;
        }

        int test2(int x, int y) {
            return x * 2 + y * 2;
        }
    }
}

