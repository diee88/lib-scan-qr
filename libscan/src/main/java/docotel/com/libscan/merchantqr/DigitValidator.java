package docotel.com.libscan.merchantqr;

import org.apache.commons.lang3.CharUtils;

public class DigitValidator implements Validator<DigitValidator.Result> {

    private Result result;

    public DigitValidator(String digits, int maxDigit, boolean strict) {
        if (digits == null || digits.isEmpty()) {
            result = Result.EMPTY;
            return;
        }
        for (Character c: digits.toCharArray()){
            if (!CharUtils.isAsciiNumeric(c)) {
                result = Result.INVALID;
                return;
            }
        }
        if (strict) {
            result = digits.length() == maxDigit ? Result.VALID : Result.INVALID;
        } else {
            result = digits.length() >= maxDigit ? Result.VALID : Result.INVALID;
        }
    }

    @Override
    public Result getResult() {
        return result;
    }

    public enum Result {
        VALID, EMPTY, INVALID
    }

}
