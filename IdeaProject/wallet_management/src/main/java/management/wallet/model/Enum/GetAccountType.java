package management.wallet.model.Enum;

public class GetAccountType {
    public static AccountType getEnum(String stringEnum) {
        switch (stringEnum) {
            case "bank" -> {
                return AccountType.BANK;
            }
            case "cash" -> {
                return AccountType.CASH;
            }
            case "mobile_money" -> {
                return AccountType.MOBILE_MONEY;
            }
            default -> {
                return null;
            }
        }
    }
}
