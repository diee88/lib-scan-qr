package docotel.com.libscan.merchantqr;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
class MerchantQrTag {
    private String tag;
    private int length;
    private String value;
}
