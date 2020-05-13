package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }


    public static String readString() throws Exception {
        String str = "";
        try {
            str = bf.readLine();

        } catch (Exception e) {
            System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз." );
            return readString();
        }
        return str;
    }

    public static int readInt() throws Exception {
        int num = 0;
        try {
            num = Integer.parseInt(readString());
        } catch (NumberFormatException e) {
            System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз." );
            num = readInt();
        }
        return num;
    }
}

