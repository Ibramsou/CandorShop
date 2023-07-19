
package fr.candor.shop.menu.util;

public class PagePatternUtil {

    public static int[] squarePattern(int startX, int endX, int startY, int endY) {
        int size = (endX - startX) * (endY - startY);
        int[] pattern = new int[size];
        int index = 0;
        for (int i = startY; i < endY; i++) {
            int add = 9 * i;
            for (int slot = startX; slot < endX; slot++) {
                pattern[index] = add + slot;
                index++;
            }
        }

        return pattern;
    }
}