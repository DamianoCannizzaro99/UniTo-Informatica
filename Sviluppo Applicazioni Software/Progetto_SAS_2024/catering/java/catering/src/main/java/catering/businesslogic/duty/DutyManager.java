package catering.businesslogic.duty;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.user.User;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


public class DutyManager {
    private DutySheet currentDutySheet;
    private ArrayList<DutyEventReceiver> eventReceivers;

    public DutyManager() {
        eventReceivers = new ArrayList<>();
    }


    public DutySheet createDutySheet(EventInfo ev, boolean active) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef() || !ev.isAssigned(user)) {
            throw new UseCaseLogicException("UseCaseLogicException: User is not chef or not assigned");
        }
        DutySheet ds = new DutySheet(ev, active);
        setCurrentDutySheet(ds);
        notifyDutySheetCreated(ds);
        return ds;
    }

    public void deleteDutySheet(DutySheet ds) throws UseCaseLogicException {
        currentDutySheet = null;
        notifyDutySheetDeleted(ds);
    }



    private void setCurrentDutySheet(DutySheet dutySheet) {
        this.currentDutySheet = dutySheet;
    }

    public void setActive(Boolean active){currentDutySheet.active = active;}

    public Task addTask(String name, String description, int qty ) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef())throw new UseCaseLogicException("UseCaseLogic Exception: user " +user.getUserName() + " is not a chef: ");
        Task task = currentDutySheet.addTask(name,description,qty);
        notifyTaskCreated(task);
        return task;
    }
    public Task updateTask(Task task, String upName, String upDesc,int upQty) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef())throw new UseCaseLogicException("UseCaseLogic Exception: user " +user.getUserName() + " is not a chef: ");
        Task newTask = task.updateTask(upName,upDesc,upQty);
        notifyTaskUpdated(task,newTask);
        return newTask;
    }




    public void moveTask(Task taskToMove, Task task2) throws UseCaseLogicException {
        if(taskToMove.getPosition() == task2.getPosition()) throw new UseCaseLogicException("UseCaseLogicException: Task is already at position");
        notifyTaskRearranged(taskToMove,task2);
        currentDutySheet.moveTask(taskToMove, task2);
    }

    public void assignTask(DutySheet ds, Task task, Shift[] shifts,User[] staff) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException("UseCaseLogic Exception: user is not chef");
        if(staff.length==0) throw new UseCaseLogicException("UseCaseLogic Exception: staff is empty");
        LocalDate dateNow = LocalDate.now();
        Date date = Date.valueOf(dateNow);
        for(Shift s : shifts){
            if (s.getJobDate().equals(date)){throw new UseCaseLogicException("UseCaseLogic Exception: cannot assign task if job date is today:");}
        }ds.assignTask(task,shifts,staff);
        notifyTaskAssigned(task,staff);
    }

    public void removeAssignedTask(DutySheet ds, Task task, User[]staff,Shift[] shifts) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) throw new UseCaseLogicException("UseCaseLogic Exception: user is not chef:");
        LocalDate dateNow = LocalDate.now();
        Date date = Date.valueOf(dateNow);
        for(Shift shift : shifts) { if (shift.getJobDate().equals(date)) throw new UseCaseLogicException("UseCaseLogicException: Unable to remove a task assigned for the job date: "); }
        ds.removeAssignment(task,staff);
        notifyRemoveAssignment(task,staff);
    }




    public Task setOverflow( DutySheet ds, Task task, int qty, int guests) throws UseCaseLogicException {
        if(qty <= guests) throw new UseCaseLogicException("UseCaseLogicException: guests more than quantity");
        Task olTask = ds.setOverflow(task,qty,guests);
        notifyOverflowSet(olTask);
        return olTask;
    }

    public DutySheet openDutySheet(DutySheet ds) throws UseCaseLogicException {
        if(ds == null) throw new UseCaseLogicException("UseCaseLogic Exception: Duty SHeet is null.");
        DutySheet checkedDS = ds.openDutySheetFromDB(ds);
        notifyDutySheetOpened(checkedDS);
        return checkedDS;
    }




    //------------------------NOTIFY METHODS------------------------------

    public void addEventReceiver(DutyEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    private void notifyDutySheetCreated(DutySheet ds) {
        for(DutyEventReceiver er : eventReceivers) {
            er.updateDutySheetCreated(ds);
        }
    }

    private void notifyDutySheetDeleted(DutySheet ds) {
        for(DutyEventReceiver er : eventReceivers) {
            er.updateDutySheetDeleted(ds);
        }
    }
    private void notifyTaskCreated(Task task) {
        for (DutyEventReceiver er : eventReceivers) {
            er.updateTaskCreated(task);
        }
    }

    private void notifyTaskUpdated(Task task,Task newTask) {
        for (DutyEventReceiver er : eventReceivers){
            er.updateTaskDeleted(task);
            er.updateTaskCreated(newTask);
        }
    }

    private void notifyTaskRearranged(Task taskToMove, Task task2) {
        for (DutyEventReceiver er : eventReceivers) {
            er.updateTaskRearranged(taskToMove, task2.getPosition());
            er.updateTaskRearranged(task2,taskToMove.getPosition());
        }
    }
    private void notifyOverflowSet(Task ofTask) {
        for (DutyEventReceiver er : eventReceivers) {
            er.updateOverflowSet(ofTask);
        }
    }

    private void notifyTaskAssigned(Task task, User[] staff) {
        for (DutyEventReceiver er : eventReceivers) {
            er.updateTaskAssigned(task,staff);
        }
    }

    private void notifyRemoveAssignment(Task task, User[] staff) {
        for(DutyEventReceiver er : eventReceivers) {
            er.updateAssignmentRemoved(task,staff);
        }
    }

    private void notifyDutySheetOpened(DutySheet checkedDS) {
        for (DutyEventReceiver er : eventReceivers) {
            er.updateDutySheetOpened(checkedDS);
        }
    }
}
