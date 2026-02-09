package catering.businesslogic.TestingDuty;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.duty.DutySheet;
import catering.businesslogic.duty.Task;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.ObservableList;

import java.util.Scanner;

public class DutyTest2b {
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

            System.out.println("--------------------------------------------------------------");

            System.out.println("\nTEST CREATE DUTY SHEET");
            DutySheet ds = CatERing.getInstance().getDutyManager().createDutySheet(e,false);
            ds.testString();

            System.out.println("--------------------------------------------------------------");
            System.out.println("\nTEST CREATE TASK");
            String tName = "Lasagna alla zucca";
            String tDesc = "Cuocere zucca al forno, preparare besciamella,\ntagliare pancetta,\nassemblare,\nin forno per 30'";
            int qty = 50;
            Task newTask = CatERing.getInstance().getDutyManager().addTask(tName,tDesc,qty);



            String tName1 = "Vitello tonnato";
            String tDesc1= "Rosolare il filetto,\npreparare salsa tonnata,\nfiletto a cottura lenta,\naffettare filetto,\nimpiattare";
            int qty1= 150;
            Task newTask1= CatERing.getInstance().getDutyManager().addTask(tName1,tDesc1,qty1);
            System.out.println("\nTASK CREATED");
            System.out.println(newTask.toString());
            System.out.println(newTask1.toString());
            System.out.println("--------------------------------------------------------------");

            System.out.println("\nTEST UPDATE TASK");
            Task taskUpdated = CatERing.getInstance().getDutyManager().updateTask(newTask1,"Tonno vitellato","Scottare tonno,\npreparare salsa al vitello,\nimpiattare",60);
            System.out.println(taskUpdated.toString());
        } catch (UseCaseLogicException e) {
            System.out.println(e.getErrorDetails());
        }
    }
}
