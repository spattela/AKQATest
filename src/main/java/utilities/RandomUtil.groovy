package utilities

/**
 * Created by alitan on 20/05/2015.
 */
class RandomUtil {

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    static String generateRandomString(int stringLength) {

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < stringLength; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    static int pickRandomNum(int startRangeInclusive, int boundExclusive) {
        int randomNum

        try {
            Random random = new Random()
            randomNum = random.nextInt(boundExclusive - startRangeInclusive) + startRangeInclusive
        } catch (IllegalArgumentException e) {
            String errorMsg = "Parameters are: startRangeInclusive = " + startRangeInclusive + ', boundExclusive = ' + boundExclusive + ' -----' + e
            throw new CTMCustomException(errorMsg)
        }
        return randomNum
    }
}
