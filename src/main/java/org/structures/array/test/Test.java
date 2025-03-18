package org.structures.array.test;

import org.structures.array.structure.Array;

/**
 *
 * @author martin
 */
public class Test {
    public static void main(String[] args) {
        Array<String> queue = new Array<>(100);
        for (int i = 0; i < 110; i++) {
            queue.add("xd");
        }

        queue.removeIf(s->s.contains("xd"));
        System.out.println(queue.size());
    }
}
