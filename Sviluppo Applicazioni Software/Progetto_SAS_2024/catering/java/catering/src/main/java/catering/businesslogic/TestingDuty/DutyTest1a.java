package catering.businesslogic.TestingDuty;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.duty.DutySheet;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.ObservableList;

import java.util.Scanner;

public class DutyTest1a {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("TEST CLEANUP TABLES");
            PersistenceManager.resetTables();
            System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();
            System.out.print("SCEGLI ACCOUNT CON CUI FARE LOGIN: ");
            CatERing.getInstance().getUserManager().login(in.nextLine());
            User currUser = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(currUser);

            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu di " + currUser.getUserName());

            Section antipasti = CatERing.getInstance().getMenuManager().defineSection("Antipasti");
            Section primi = CatERing.getInstance().getMenuManager().defineSection("Primi");
            Section secondi = CatERing.getInstance().getMenuManager().defineSection("Secondi");

            ObservableList<Recipe> recipes = CatERing.getInstance().getRecipeManager().getRecipes();
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(0), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(1), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(2), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(6), secondi);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(7), secondi);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(3));
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(4));

            CatERing.getInstance().getMenuManager().publish();
            System.out.println("\nMENU IS PUBLIC");
            EventInfo e = CatERing.getInstance().getEventManager().getEventInfoFromName("Convegno Agile Community");
            e.AssignUser(currUser);
            EventInfo e1 = CatERing.getInstance().getEventManager().getEventInfoFromName("Compleanno di Manuela");
            e1.AssignUser(currUser);

            System.out.println("--------------------------------------------------");
            System.out.println("\nTEST CREATE DUTY SHEET");
            DutySheet ds = CatERing.getInstance().getDutyManager().createDutySheet(e,false);
            DutySheet ds1 = CatERing.getInstance().getDutyManager().createDutySheet(e1,true);
            ds.testString();

            System.out.println("--------------------------------------------------");
            System.out.println("\nTEST DELETE DUTY SHEET");
            CatERing.getInstance().getDutyManager().deleteDutySheet(ds);
            ds.testString();


        } catch (UseCaseLogicException e) {
            System.out.println(e.getErrorDetails());
        }
    }
}
