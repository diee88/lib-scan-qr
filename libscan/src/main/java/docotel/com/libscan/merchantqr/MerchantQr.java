package docotel.com.libscan.merchantqr;

import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static docotel.com.libscan.merchantqr.CreditCardValidator.format;

public class MerchantQr {
    @Getter private String qr;
    private int currentIdx = 0;
    private boolean isValid = false;
    private List<MerchantQrTag> qrByTag = new ArrayList<>();
    @Getter private String qrVersion = "";
    @Getter private QrType type = null;
    private String visaId = "";
    private String mastercardId = "";
    private String emoneyId = "";
    private String debitId = "";
    private String merchantData = "";
    private String defaultId = "";
    @Getter private String merchantCategoryCode = "";
    @Getter private String currencyCode = "";
    @Getter private String amount = "";
    @Getter private Tip tip = Tip.PROMPT;
    @Getter private String vTip = "";
    @Getter private String fixedFee = "0";
    @Getter private String percentageFee = "";
    @Getter private String countryCode = "";
    @Getter private String merchantName = "";
    @Getter private String merchantCode = "";
    @Getter private String merchantType = "";
    @Getter private String merchantCity = "";
    @Getter private String postalCode = "";
    @Getter @Setter private String domain = "";
    @Getter @Setter private String dataBank = "";
    @Getter @Setter private String additionalMerchant = "";
    @Getter @Setter private String tipeMerchant = "";
    @Getter @Setter private String additionalDataField = "";
    @Getter @Setter private String billNumber = "";
    @Getter @Setter private String referenceId = "";
    private String crc = "";
    private boolean isAdditionalField = false;
    @Getter private String network = "";

    public MerchantQr(String qr) {
        this.qr = qr;
        crcCheck();
    }

    private MerchantQr(String qr, boolean isAdditionalField) {
        this.qr = qr;
        this.isAdditionalField = isAdditionalField;
        parseByTag();
    }

    private void crcCheck() {
        try {
            String plain = qr.substring(0, qr.length() - 4);
            String crc = qr.substring(qr.length() - 4, qr.length()).toLowerCase();
            String validCrc = CrcHelper.crc(plain).toLowerCase();
            if (validCrc.length() == 3) validCrc = "0" + validCrc;
            if (validCrc.length() == 2) validCrc = "00" + validCrc;
            if (validCrc.length() == 1) validCrc = "000" + validCrc;
            LoggerHelper.info(validCrc);
            if (validCrc.equals(crc)) parseByTag();
        } catch (Exception ignored) { }
    }

    private List<MerchantQrTag> getFields() {
        if (isAdditionalField) return qrByTag;
        else return null;
    }

    private void parseByTag() {
        try {
            if (currentIdx == qr.length()) {
                isValid = true;
                if (!isAdditionalField) readEachTag();
                return;
            }
            MerchantQrTag qrTag = new MerchantQrTag();
            qrTag.setTag(qr.substring(currentIdx, currentIdx + 2));
            if (TextUtils.isDigitsOnly(qr.substring(currentIdx + 2, currentIdx + 4)))
                qrTag.setLength(Integer.valueOf(qr.substring(currentIdx + 2, currentIdx + 4)));
            qrTag.setValue(qr.substring(currentIdx + 4, currentIdx + 4 + qrTag.getLength()));
            currentIdx = currentIdx + 4 + qrTag.getLength();
            qrByTag.add(qrTag);
            parseByTag();
        } catch (Exception e) {
            e.printStackTrace();
            isValid = false;
        }
    }

    private void readEachTag() {
        for (MerchantQrTag qrTag : qrByTag) {
            setValue(qrTag.getTag(), qrTag.getValue());
        }
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getvTip() {
        return vTip;
    }

    public void setvTip(String vTip) {
        this.vTip = vTip;
    }

    public String getVisa_id() {
        return visaId;
    }

    public String getMastercard_id() {
        return mastercardId;
    }

    public String getVisaId() {
        return format(visaId);
    }

    public String getMastercardId() {
        return format(mastercardId);
    }

    public String getEmoneyId() {
        return format(emoneyId);
    }

    public String getDebitId() {
        return format(debitId);
    }

    public String getMerchantData() {
        return merchantData;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public void setDefaultId(MerchantNetwork network) {
        switch (network) {
            case VISA:
                defaultId = visaId;
                this.network = "Visa";
                break;
            case MASTERCARD:
                defaultId = mastercardId;
                this.network = "Master";
                break;
            case EMONEY:
                defaultId = emoneyId;
                this.network = "Emoney";
                break;
            case DEBIT:
                defaultId = debitId;
                this.network = "Debit";
                break;
            case NEWQR:
                defaultId = dataBank;
                this.network = "Debit";
                break;
        }
    }

    public boolean hasVisaPan() {
        return !TextUtils.isEmpty(visaId);
    }

    public boolean hasMasterCardPan() {
        return !TextUtils.isEmpty(mastercardId);
    }

    public boolean hasEmoneyPan() {
        return !TextUtils.isEmpty(emoneyId);
    }

    public boolean hasDebitPan() {
        return !TextUtils.isEmpty(debitId);
    }

    public String getAcquiringBin() {
        return defaultId.subSequence(0, 6).toString();
    }

    public String getDefaultId() {
        return defaultId;
    }

    private void setValue(String tag, String value) {
        switch (tag) {
            case "00":
                qrVersion = value;
                break;
            case "01":
                if (value.equals("11")) type = QrType.STATIC;
                if (value.equals("12")) type = QrType.DYNAMIC;
                break;
            case "02":
                visaId = value;
                break;
            case "03":
            case "04":
//                mastercardId = LuhnHelper.getValidCC(value);
                mastercardId = value;
                break;
            case "05":
                referenceId = value;
                break;
            case "26":
                if(value.length()<=16) {
                    debitId = value;
                } else {
                    merchantData = value;
                }
                break;
            case "33":
                emoneyId = value;
                break;
            case "35":
                merchantCode = value;
                break;
            case "52":
                merchantType = value;
                break;
            /*case "52":
                merchantCategoryCode = value;
                break;*/
            case "53":
                currencyCode = value;
                break;
            case "54":
                amount = value;
                break;
            case "55":
                vTip = value;
                if (value.equals("02")) tip = Tip.FLAT;
                if (value.equals("03")) tip = Tip.PERCENTAGE;
                break;
            case "56":
                fixedFee = value;
                break;
            case "57":
                percentageFee = value;
                break;
            case "58":
                countryCode = value;
                break;
            case "59":
                merchantName = value;
                break;
            case "60":
                merchantCity = value;
                break;
            case "61":
                postalCode = value;
                break;
            case "62":
                additionalDataField = value;
                break;
            case "63":
                crc = value;
        }
        validate();
    }

    private void validate() {
        readAdditionalField();
        readEmoneyCard();
        if(!merchantData.isEmpty())readMerchantData();
//        readDebitCard();
    }

    private void readMerchantData() {
        MerchantQr additionalField = new MerchantQr(merchantData, true);
        List<MerchantQrTag> additionalTags = additionalField.getFields();
        if (additionalTags != null) {
            for (MerchantQrTag additionalTag: additionalTags) {
                switch (additionalTag.getTag()) {
                    case "00":
                        domain = additionalTag.getValue();
                        break;
                    case "01":
                        dataBank = additionalTag.getValue();
                    case "02":
                        additionalMerchant = additionalTag.getValue();
                    case "03":
                        tipeMerchant = additionalTag.getValue();
                }
            }
        }
    }

    private void readAdditionalField() {
        MerchantQr additionalField = new MerchantQr(additionalDataField, true);
        List<MerchantQrTag> additionalTags = additionalField.getFields();
        if (additionalTags != null) {
            for (MerchantQrTag additionalTag: additionalTags) {
                switch (additionalTag.getTag()) {
                    case "01":
                        billNumber = additionalTag.getValue();
                        break;
                    case "05":
                        referenceId = additionalTag.getValue();
                }
            }
        }
    }

    private void readEmoneyCard() {
        MerchantQr additionalField = new MerchantQr(emoneyId, true);
        List<MerchantQrTag> additionalTags = additionalField.getFields();
        if (additionalTags != null) {
            for (MerchantQrTag additionalTag: additionalTags) {
                switch (additionalTag.getTag()) {
                    case "01":
                        emoneyId = additionalTag.getValue();
                }
            }
        }
    }
    private void readDebitCard() {
        MerchantQr additionalField = new MerchantQr(debitId, true);
        List<MerchantQrTag> additionalTags = additionalField.getFields();
        if (additionalTags != null) {
            for (MerchantQrTag additionalTag: additionalTags) {
                switch (additionalTag.getTag()) {
                    case "01":
                        debitId = additionalTag.getValue();
                }
            }
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public enum QrType { STATIC, DYNAMIC }

    private enum Tip { PROMPT, FLAT, PERCENTAGE }

    public enum MerchantNetwork { VISA, MASTERCARD, EMONEY, DEBIT, NEWQR }
}
