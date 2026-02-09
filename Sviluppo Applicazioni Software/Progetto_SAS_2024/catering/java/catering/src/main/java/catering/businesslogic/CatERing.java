package catering.businesslogic;

import catering.businesslogic.duty.DutyManager;
import catering.businesslogic.event.EventManager;
import catering.businesslogic.menu.MenuManager;
import catering.businesslogic.recipe.RecipeManager;
import catering.businesslogic.shift.ShiftManager;
import catering.businesslogic.user.UserManager;
import catering.businesslogic.persistence.DutyPersistence;
import catering.businesslogic.persistence.MenuPersistence;
import catering.businesslogic.persistence.ShiftPersistence;

public class CatERing {
    private static CatERing singleInstance;


    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private ShiftManager shiftMgr;
    private DutyManager dutyMgr;

    private MenuPersistence menuPersistence;
    private ShiftPersistence shiftPersistence;
    private DutyPersistence dutyPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        shiftMgr = new ShiftManager();
        dutyMgr = new DutyManager();
        menuPersistence = new MenuPersistence();
        shiftPersistence = new ShiftPersistence();
        dutyPersistence = new DutyPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        shiftMgr.addEventReceiver(shiftPersistence);
        dutyMgr.addEventReceiver(dutyPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public ShiftManager getShiftManager() { return shiftMgr; }

    public DutyManager getDutyManager() { return dutyMgr;}
}
