import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        MyList<Integer> list = new MyList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(0,3); // adding element to existing position
        list.add(4,4); // adding element to existing position (end of container)

        list.add(7, 7); // adding element to non-existing position

        System.out.println(list);

        System.out.println("Element at index 7 is " + list.get(7));
        System.out.println("Element at index 6 is " + list.get(6)); // getting non-existent element

        list.add(6,null); // adding null element
    }
}
