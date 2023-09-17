package com.tma.english.utils;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Supplier;

@Service
public class OTPService {

    private final static Integer LENGTH = 6;

    public static String createRandomOneTimePassword() {

        Random random = new Random();
        StringBuilder oneTimePassword = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int randomNumber = random.nextInt(10);
            oneTimePassword.append(randomNumber);
        }
        return oneTimePassword.toString().trim();
    }
}
