import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyListTest {
    @Test // all constructors, get(), contains()
    void instanceCreation() {
        // initialize empty
        assertThatCode(() -> new MyList<Integer>()).doesNotThrowAnyException();

        // initialize with capacity
        assertThatCode(() -> new MyList<Integer>(5)).doesNotThrowAnyException();

        // initialize with collection
        ArrayList<Integer> classicList = new ArrayList<>(Arrays.asList(0,1,2));
        assertThatCode(() -> new MyList<Integer>(classicList)).doesNotThrowAnyException();
        MyList<Integer> list = new MyList<Integer>(classicList);
        assertThat(list.contains(1)).isTrue();
        assertThat(list.get(2).get()).isEqualTo(2);
    }

    @Test // add(T), get(int)
    void basicAddGet() {
        MyList<Integer> list = new MyList<>();

        assertThatCode(()->list.add(1)).doesNotThrowAnyException();
        assertThat( list.get(0).get() == 1).isTrue();
    }

    @Test // add(T)
    void addNullElement() {
        MyList<Integer> list = new MyList<>();

        assertThrows(UnsupportedOperationException.class, ()->list.add(null));
    }

    @Test // add(T), get(int)
    void outOfBoundsAdd() {
        /*
            Adding element to index, that regular ArrayList would consider out of bounds
         */
        MyList<Integer> list = new MyList<>();

        assertThatCode(()->list.add(1)).doesNotThrowAnyException();
        assertThat( list.get(0).get() == 1).isTrue();
        assertThatCode(()->list.add(5,5)).doesNotThrowAnyException();
        assertThat(list.get(5).get() == 5).isTrue();
        assertThat(list.get(4));
    }

    @Test
    void ensureCapacity() {
        MyList<Integer> list = new MyList<>();
        assertThatCode(()->list.ensureCapacity(10 + 1)).doesNotThrowAnyException();
        assertThat(list.get(10).isEmpty()).isTrue();
    }

    @Test // add(int, E)
    void outOfBoundsGet() {
        /*
            Should throw an OutOfBounds exception if accessing element beyond size()
            and return Optional.empty if accessing element within size()
         */
        MyList<Integer> list = new MyList<>();
        list.add(5,5);

        assertThat(list.get(4)).isEqualTo(Optional.empty());
        assertThrows(IndexOutOfBoundsException.class, ()->list.get(10));
    }

    @Test // addAll(T), addAll(int,T)
    void addAll() {
        MyList<Integer> list = new MyList<>();
        ArrayList<Integer> classicList = new ArrayList<>(Arrays.asList(0,1,2));

        // adding elements to end of empty container
        assertThatCode(()->list.addAll(classicList)).doesNotThrowAnyException();

        // checking that all elements inserted correctly
        assertThat(list.get(0)).isEqualTo(Optional.of(0));
        assertThat(list.get(1)).isEqualTo(Optional.of(1));
        assertThat(list.get(2)).isEqualTo(Optional.of(2));

        // adding elements at position 5 (to indexes 5, 6, 7)
        assertThatCode(()->list.addAll(5,classicList)).doesNotThrowAnyException();

        // checking that elements before and after inserted chunks are indeed empty
        assertThat(list.get(3)).isEqualTo(Optional.empty());
        assertThat(list.get(4)).isEqualTo(Optional.empty());
        // checking that elements inserted correctly
        assertThat(list.get(5)).isEqualTo(Optional.of(0));
        assertThat(list.get(6)).isEqualTo(Optional.of(1));
        assertThat(list.get(7)).isEqualTo(Optional.of(2));
        // checking that there is no excessive elements after
        assertThrows(IndexOutOfBoundsException.class, ()->list.get(8));
    }

    @Test // remove(int), addAll(Collection<T>)
    void remove() {
        MyList<Integer> list = new MyList<>();
        ArrayList<Integer> classicList = new ArrayList<>(Arrays.asList(0,1,2));
        assertThatCode(()->list.addAll(classicList)).doesNotThrowAnyException();

        assertThat(list.get(0)).isEqualTo(Optional.of(0));
        assertThatCode(()->list.remove(0)).doesNotThrowAnyException();
        assertThat(list.get(0)).isEqualTo(Optional.of(1));
        list.remove(1);
        list.remove(0);
        assertThrows(IndexOutOfBoundsException.class, ()->list.get(0));
    }

    @Test // constructor(Collection<T>), size(), addAll(int, add(E), remove(int)
    void size() {
        // Initial fill size
        MyList<Integer> listEmpty = new MyList<>();
        // []
        assertThat(listEmpty.size()).isEqualTo(0);
        assertThat(listEmpty.sizeTotal()).isEqualTo(0);

        // Size when initializing with Collection
        ArrayList<Integer> classicList = new ArrayList<>(Arrays.asList(0,1,2));
        MyList<Integer> list = new MyList<>(classicList);
        // [0,1,2]
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.sizeTotal()).isEqualTo(3);

        // size after clear()
        list.clear();
        // []
        assertThat(list.size()).isEqualTo(0);
        assertThat(list.sizeTotal()).isEqualTo(0);

        // Size after addAll(Collection<T>)
        assertThatCode(()->list.addAll(classicList)).doesNotThrowAnyException();
        // [0,1,2]
        assertThat(list.size()).isEqualTo(3);
        assertThat(list.sizeTotal()).isEqualTo(3);

        // Size after remote addAll(int,Collection<T>)
        assertThatCode(()->list.addAll(5,classicList)).doesNotThrowAnyException();
        // [0,1,2,Optional.empty,Optional.empty,0,1,2]
        assertThat(list.size()).isEqualTo(6);
        assertThat(list.sizeTotal()).isEqualTo(8);

        // Size after remove(int) non-empty element
        list.remove(0);
        // [1,2,Optional.empty,Optional.empty,0,1,2]
        assertThat(list.size()).isEqualTo(5);
        assertThat(list.sizeTotal()).isEqualTo(7);

        // Size after remove(int) empty element
        list.remove(3);
        // [1,2,Optional.empty,0,1,2]
        assertThat(list.size()).isEqualTo(5);
        assertThat(list.sizeTotal()).isEqualTo(6);

        // Size after adding element
        list.add(5,3);
        // [1,2,Optional.empty,0,1,2,3]
        assertThat(list.size()).isEqualTo(6);
        assertThat(list.sizeTotal()).isEqualTo(7);

        // Size after altering empty to non-empty element
        list.set(2,3);
        // [1,2,3,0,1,2,3]
        assertThat(list.size()).isEqualTo(7);
        assertThat(list.sizeTotal()).isEqualTo(7);

        // Size after altering non-empty element
        list.set(3,4);
        // [1,2,3,4,1,2,3]
        assertThat(list.size()).isEqualTo(7);
        assertThat(list.sizeTotal()).isEqualTo(7);
    }

    @Test // constructor(Collection<T>), clear(), get(int), addAll(Collection<T>)
    void clear() {
        MyList<Integer> list = new MyList<>();
        ArrayList<Integer> classicList = new ArrayList<>(Arrays.asList(0,1,2));
        assertThatCode(()->list.addAll(classicList)).doesNotThrowAnyException();
        // checking that collection inserted
        assertThat(list.get(0)).isEqualTo(Optional.of(0));
        // checking clear
        assertThatCode(()->list.clear()).doesNotThrowAnyException();
        // checking if collection has cleared
        //assertThat()
    }

    @Test
    void indexOf() {
        MyList<Integer> list = new MyList<>(Arrays.asList(0,1,2));
        list.add(5,5);
        // [0, 1, 2, Optional.empty, Optional.empty, 5]
        assertThat(list.indexOf(null)).isEqualTo(-1);
        assertThat(list.indexOf(0)).isEqualTo(0);
        assertThat(list.indexOf(5)).isEqualTo(5);
        assertThat(list.indexOf(6)).isEqualTo(-1);
        assertThat(list.indexOf(Optional.empty())).isEqualTo(3);
    }

    @Test
    void isEmptyOnInit() {
        // Initial fill size
        MyList<Integer> listEmpty = new MyList<>();
        // []
        assertThat(listEmpty.isEmpty()).isTrue();

        MyList<Integer> list = new MyList<>(Arrays.asList(0, 1, 2));
        // [0,1,2]
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void isEmptyOnClear() {
        MyList<Integer> list = new MyList<>(Arrays.asList(0, 1, 2));
        assertThat(list.isEmpty()).isFalse();
        list.clear();
        // []
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void isEmptyAfterAdd() {
        MyList<Integer> list = new MyList<>();
        assertThat(list.isEmpty()).isTrue();
        list.add(2,2);
        // [Optional.empty,Optional.empty,2]
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void isEmptyAfterRemoveEmptyElement() {
        MyList<Integer> list = new MyList<>();
        list.add(2,2);
        // [Optional.empty,Optional.empty,2]
        assertThat(list.isEmpty()).isFalse();
        // Size after remove(int) non-empty element
        list.remove(2);
        // [Optional.empty,Optional.empty]
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void isEmptyAfterRemoveNonEmptyElement() {
        MyList<Integer> list = new MyList<>(Arrays.asList(0, 1, 2));
        // [0,1,2]
        assertThat(list.isEmpty()).isFalse();
        // Size after remove(int) non-empty element
        list.remove(1);
        // [0,2]
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    void isEmptyAfterAlterEmptyToNonEmpty() {
        MyList<Integer> list = new MyList<>();
        assertThat(list.isEmpty()).isTrue();
        list.add(2,2);
        // [Optional.empty,Optional.empty,2]
        list.remove(2);
        // [Optional.empty,Optional.empty]
        assertThat(list.isEmpty()).isTrue();
        list.set(1,1);
        // [Optional.empty,1]
        assertThat(list.isEmpty()).isFalse();
    }
}