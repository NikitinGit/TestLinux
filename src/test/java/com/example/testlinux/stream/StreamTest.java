package com.example.testlinux.stream;

import com.example.testlinux.interfaces.Animal;
import com.example.testlinux.interfaces.Dog;
import com.example.testlinux.stream.api.Stage1;
import org.hibernate.proxy.HibernateProxy;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
public class StreamTest {

    @Test
    public void helloStream() {
        List<Integer> sample = new ArrayList<>();
        sample.stream().forEach(System.out::println);

        List<Stage1> stage1s = List.of(new Stage1("Nikitin"), new Stage1("Ton"));

        stage1s.stream().peek(u -> {
            if(Objects.equals(u.name, "Nikitin")) {
                System.out.println("name: " + u.name);
            } else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).toList();
    }

    @Test
    public void equalsTest() {
        //Animal an = new Animal();
        // Dog dog = (Dog) an; - error
        Animal an = new Dog();
        //an.methodOfDog();  -- ERROR
        ((Dog) an).methodOfDog();
        Dog dog = (Dog) an;   // OK, реальный тип — Dog
        Object o = new Object();
        assertTrue(dog.equals(o));

//        Dog dog = new Dog();
//        Animal a = dog;              // неявный upcast
//        Animal a2 = (Animal) dog;    // явный upcast, то же самое
//        if (this == o) return true;
//        if (o == null) return false;
//        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
//        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
//        if (thisEffectiveClass != oEffectiveClass) return false;
//        Event event = (Event) o;
//        return getEventId() != null && Objects.equals(getEventId(), event.getEventId());
    }

    @Test
    public void st16_averageNumber() {
        int[] sumAndValue = Stream.of(2, 4, 6, 7).reduce(
                new int[]{0, 0},
                (acc, val) -> {
                    acc[0]++;
                    acc[1] += val;
                    return acc;
                },
                (acc, val) -> {
                    acc[0] += val[0];
                    acc[1] += val[1];
                    return acc;
                }
        );
        double average = (double)sumAndValue[1] / sumAndValue[0];
        System.out.println("average; " + average);

        var nums = List.of(2, 4, 6, 7);
        int[] acc = nums.stream().reduce(
                new int[2],
                (a, v) -> {
                    a[0] += v; // sum
                    a[1]++;    // count
                    return a;
                },
                (a, b) -> {
                    a[0] += b[0];
                    a[1] += b[1];
                    return a;
                }
        );

        double avg = (double) acc[0] / acc[1];

        System.out.println("reduce avg; " + avg);
    }
}
