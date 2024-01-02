package management.wallet.model.Enum;

public class GetCategoryGroup {
    public static CategoryGroup getEnum(String stringEnum) {
        switch (stringEnum) {
            case "expense" -> {
                return CategoryGroup.EXPENSE;
            }
            case "income" -> {
                return CategoryGroup.INCOME;
            }
            case "either" -> {
                return CategoryGroup.EITHER;
            }
            default -> {
                return null;
            }
        }
    }
}
