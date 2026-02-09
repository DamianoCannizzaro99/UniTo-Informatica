package catering.businesslogic;

import catering.businesslogic.event.EventInfo;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.shift.ShiftTable;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.util.Arrays;
import java.sql.Date;
import java.util.Map;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            System.out.println("TEST CLEANUP TABLES");
            PersistenceManager.resetTables();
             System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();
            System.out.println("LOGIN");
            CatERing.getInstance().getUserManager().login("Lidia");
            User organizer =CatERing.getInstance().getUserManager().getCurrentUser();

            //--------------------Menu test section--------------------

            System.out.println("\nTEST CREATE MENU");
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");

            System.out.println("\nTEST DEFINE SECTION");
            Section antipasti = CatERing.getInstance().getMenuManager().defineSection("Antipasti");
            Section primi = CatERing.getInstance().getMenuManager().defineSection("Primi");
            Section secondi = CatERing.getInstance().getMenuManager().defineSection("Secondi");
            System.out.println(m.testString());

            System.out.println("\nTEST GET EVENT INFO");
            ObservableList<EventInfo> events = CatERing.getInstance().getEventManager().getEventInfo();
            for (EventInfo e: events) {
                System.out.println(e);
                for (ServiceInfo s: e.getServices()) {
                    System.out.println("\t" + s);
                }
            }
            System.out.println("");

            System.out.println("\nTEST GET RECIPES AND INSERT ITEM IN SECTION");
            ObservableList<Recipe> recipes = CatERing.getInstance().getRecipeManager().getRecipes();
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(0), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(1), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(2), antipasti);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(6), secondi);
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(7), secondi);
            System.out.println(m.testString());

            System.out.println("\nTEST INSERT FREE ITEM");
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(3));
            CatERing.getInstance().getMenuManager().insertItem(recipes.get(4));
            System.out.println(m.testString());

            System.out.println("\nTEST EDIT FEATURES");
            Map<String, Boolean> f = CatERing.getInstance().getMenuManager().getCurrentMenu().getFeatures();
            String[] fNames = f.keySet().toArray(new String[0]);
            boolean[] vals = new boolean[fNames.length];
            Arrays.fill(vals, true);
            CatERing.getInstance().getMenuManager().setAdditionalFeatures(fNames, vals);
            System.out.println(m.testString());

            System.out.println("\nTEST EDIT TITLE");
            CatERing.getInstance().getMenuManager().changeTitle("Titolo Nuovo");
            System.out.println(m.testString());

            System.out.println("\nTEST PUBLISH");
            CatERing.getInstance().getMenuManager().publish();
            System.out.println(m.testString());

            //--------------------Shift test section--------------------
            System.out.println("\nTEST CREATE SHIFT TABLES");
            EventInfo e = CatERing.getInstance().getEventManager().getEventInfoFromName("Convegno Agile Community");
            e.AssignUser(organizer);
            System.out.println(e);
            ShiftTable cst = CatERing.getInstance().getShiftManager().createCookShiftTable("c", e);
            cst.testString();
            ShiftTable sst = CatERing.getInstance().getShiftManager().createServiceShiftTable("s", e);
            sst.testString();
            System.out.println("Done.");

            System.out.println("\nTEST CREATE NEW SHIFTS");
            Time sT1 = Time.valueOf("15:30:00");
            Time st2 = Time.valueOf("16:00:00");
            Time eT1 = Time.valueOf("18:30:00");
            Time et2 = Time.valueOf("20:00:00");
            Date jd = Date.valueOf("2024-11-22");
            Date dl1 = Date.valueOf("2024-11-20");
            Date dl2 = Date.valueOf("2024-11-19");
            boolean g1 = true;
            boolean g2 = false;
            String gn = "blocco 1";
            Shift newCS = CatERing.getInstance().getShiftManager().addShiftToTable(cst,sT1,eT1,jd,dl1,g1, gn);
            System.out.println("New Cook Shift Created: \n" + newCS);
            Shift newSS = CatERing.getInstance().getShiftManager().addShiftToTable(sst,st2,et2,jd,dl2,g2, gn);
            System.out.println("New service shift created: \n" + newSS);
            CatERing.getInstance().getShiftManager().addShiftToTable(cst,st2,et2,jd,dl2,g2, gn);
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Cook Shift Table.");
            cst.testString();
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Service Shift Table.");
            sst.testString();
            System.out.println("------------------------------------------------------------------------------------------------------");
            //----------------TEST DELETE SHIFT---------------------
            System.out.println("TEST DELETE SHIFT");
            CatERing.getInstance().getShiftManager().deleteShift(newCS,cst);
            cst.testString();
            //CatERing.getInstance().getShiftManager().deleteShift(newSS,sst);

            //----------------TEST RECURRING TABLE-------------------







        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
