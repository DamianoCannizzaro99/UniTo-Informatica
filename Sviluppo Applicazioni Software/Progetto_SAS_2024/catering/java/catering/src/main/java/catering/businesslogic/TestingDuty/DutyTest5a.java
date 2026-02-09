package catering.businesslogic.TestingDuty;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.duty.DutySheet;
import catering.businesslogic.duty.Task;
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

public class DutyTest5a {
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

            User cook1= CatERing.getInstance().getUserManager().getUser("Antonietta");
            User cook2 = CatERing.getInstance().getUserManager().getUser("Guido");

            User[] staff = {cook1,cook2};
            Shift[] shifts = {cookShift};

            CatERing.getInstance().getDutyManager().assignTask(ds,newTask,shifts,staff);
            System.out.println("\nTASK ASSIGNED");
            System.out.println("--------------------------------------------------------------");
            System.out.println("\nTEST REMOVE TASK");
            User[] staff2 = {cook2};
            CatERing.getInstance().getDutyManager().removeAssignedTask(ds,newTask,staff2,shifts);
            System.out.println("\nTASK REMOVED");

        } catch (UseCaseLogicException e) {
            System.out.println(e.getErrorDetails());
        }
    }
}
