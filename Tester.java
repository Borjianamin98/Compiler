//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Scanner;

public class Tester {
    public Tester() {
    }

    public static void main(String[] var0) {
        int var1 = (new Scanner(System.in)).nextInt();
        int[][] var4 = new int[var1][var1];
        int var5 = -1;

        for(int var2 = 0; var2 < var1; ++var2) {
            for(int var3 = 0; var3 < var1; ++var3) {
                var4[var2][var3] = (new Scanner(System.in)).nextInt();
                if (var2 > 1 && var3 > 1) {
                    int var6 = var4[var2][var3] + var4[var2][var3 - 1] + var4[var2][var3 - 2] + var4[var2 - 1][var3 - 1] + var4[var2 - 2][var3] + var4[var2 - 2][var3 - 1] + var4[var2 - 2][var3 - 2];
                    if (var6 > var5) {
                        var5 = var6;
                    }
                }
            }
        }

        System.out.println(var5);
    }
}
