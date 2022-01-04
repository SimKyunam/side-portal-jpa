package com.mile.portal.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtil {

    public static List<Long> stringNotEmptyIdConvertToList(String ids) {
        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 소문자 알파벳 랜덤 생성
     *
     * @param strLength
     * @return
     */
    public static String createStringByLowerAlphabet(int strLength) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < strLength; i++) {
            stringBuilder.append((char) (random.nextInt(26) + 'a'));
        }

        return stringBuilder.toString();
    }

    /**
     * 코드 생성
     *
     * @param length
     * @return
     */
    public static String createCode(int length) {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    code.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    code.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    code.append((rnd.nextInt(10)));
                    break;
            }
        }
        return code.toString();
    }

    public static String convertToBase64(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String convertToBase64Decode(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(str));
    }
}
