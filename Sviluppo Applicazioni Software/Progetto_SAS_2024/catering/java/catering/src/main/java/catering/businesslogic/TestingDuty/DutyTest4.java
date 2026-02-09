package catering.businesslogic.TestingDuty;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.shift.ShiftTable;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;

public class DutyTest4 {
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
            System.out.println("\nMENU CREATO");
            System.out.println(m.testString());

            EventInfo e = CatERing.getInstance().getEventManager().getEventInfoFromName("Convegno Agile Community");
            e.AssignUser(currUser);
            ShiftTable cst = CatERing.getInstance().getShiftManager().createCookShiftTable("c", e);
            ShiftTable sst = CatERing.getInstance().getShiftManager().createServiceShiftTable("s", e);
            System.out.println("\nSHIFTTABLE CREATE");

            Time sT1 = Time.valueOf("15:30:00");
            Time st2 = Time.valueOf("16:00:00");
            Time eT1 = Time.valueOf("18:30:00");
            Time et2 = Time.valueOf("20:00:00");
            Date jd = Date.valueOf("2024-11-22");
            Date dl1 = Date.valueOf("2024-11-20");
            Date dl2 = Date.valueOf("2024-11-19");
            boolean g1 = true;
            boolean g2 = false;
            String gn = "Brigata blu";
            Shift cookShift = CatERing.getInstance().getShiftManager().addShiftToTable(cst,sT1,eT1,jd,dl1,g1, gn);
            Shift serviceShift = CatERing.getInstance().getShiftManager().addShiftToTable(sst,st2,et2,jd,dl2,g2, gn);
            CatERing.getInstance().getShiftManager().addShiftToTable(cst,st2,et2,jd,dl2,g2, gn);
            System.out.println("\nSHIFT CREATI");

            System.out.println("-------------------------------------------------------------");
            System.out.println("\nTEST CHECK TABLE");
            ShiftTable openedShiftTable = CatERing.getInstance().getShiftManager().checkTable(cst);
            openedShiftTable.testString();

        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case: " + e.getErrorDetails());
        }
    }
}
