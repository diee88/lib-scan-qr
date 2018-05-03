package docotel.com.libscan.merchantqr;

public class CreditCardValidator extends DigitValidator {

    public CreditCardValidator(String digits, Character separator) {
        super(digits.replace(separator.toString(), ""), 16, true);
    }

    public static String format(String cardNumber) {
        cardNumber = cardNumber.replace(" ", "");
        String x = "";
        for (int i = 4; i <= cardNumber.length(); i = i + 4)
            x += cardNumber.substring(i - 4, i) + " ";
        return x;
    }

}
