package com.hcmute.clothingstore.jwt;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

import java.security.SecureRandom;

public final class RandomUtil {
    public static final int KEY_LENGTH =50;
    public static final int CODE_LENGTH =6;
    public static final SecureRandom SECURE_RANDOM =new SecureRandom();

    public RandomUtil() {
    }

    public static String generateActivationCode() {
        return generateRandomAlphabetString();
    }

    private static String generateRandomAlphabetString() {
        return RandomStringUtils.random(KEY_LENGTH,0,0,false,true,(char[]) null, SECURE_RANDOM);
    }

    public static String generateActivationKey() {
        return generateRandomNumericString();
    }

    private static String generateRandomNumericString() {
        return RandomStringUtils.random(CODE_LENGTH,0,0,false,true,(char[]) null, SECURE_RANDOM);
    }
static {
        SECURE_RANDOM.nextBytes(new byte[64]);
}
}
