package management.wallet;

import management.wallet.UI.Menu;
import management.wallet.UI.ShowMenu;

public class Main {
    public static void main(String[] args) {
        while (!Menu.toBreak) {
            ShowMenu.actionMenu();
            Menu menu = new Menu();
            menu.chooseAction(ShowMenu.prompt());
           if (!Menu.toBreak) {
               ShowMenu.modelMenu();
               menu.chooseModel(ShowMenu.prompt());
           }
        }
    }
}
