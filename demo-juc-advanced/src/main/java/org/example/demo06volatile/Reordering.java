package org.example.demo06volatile;

public class Reordering {
    public int x = 0, y = 0;
    public int a = 0, b = 0;
    public void test(int number) {
        Thread one = new Thread(() -> {
            a = 1;
            x = b;
        });

        Thread two = new Thread(() -> {
            b = 1;
            y = a;
        });
        one.start();
        two.start();
        try {
            one.join();
            two.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("%s::(%s,%s)%n", number, x ,y);
    }
    
    // (0,0)
    public static void main(String[] args) {
        int number = 0;
        while (true) {
            Reordering reordering = new Reordering();
            reordering.test(number++);
            if (reordering.x == 0 && reordering.y == 0) {
                break;
            }
        }
    }
}
