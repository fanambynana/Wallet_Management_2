package management.wallet.model.Enum;

public class GetTransactionType {
    public static TransactionType getEnum(String stringEnum) {
        switch (stringEnum) {
            case "debit" -> {
                return TransactionType.DEBIT;
            }
            case "credit" -> {
                return TransactionType.CREDIT;
            }
            default -> {
                return null;
            }
        }
    }
}
