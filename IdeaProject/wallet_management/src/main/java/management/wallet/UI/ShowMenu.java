package management.wallet.UI;

import java.util.Scanner;

public class ShowMenu {
    public static void actionMenu() {
        System.out.println("""
            ---------- Wallet Management ----------
            
            1. Find all elements
            2. Find element by ID
            0. Exit
        """);
    }
    public static void modelMenu() {
        System.out.println();
        System.out.println("""
            Choose the element to find :
            
            1. Account
            2. Balance
            3. Balance History
            4. Currency Model
            5. Currency Value
            6. Transaction
            7. Transaction Category
            8. Transfer History
            0. Exit
        """);
    }
    public static int prompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice : ");
        int answer = scanner.nextInt();
        System.out.println();
        return answer;
    }
}
