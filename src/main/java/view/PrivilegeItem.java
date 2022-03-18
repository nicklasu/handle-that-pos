package view;

import model.classes.Privilege;
import model.classes.PrivilegeLevel;

import java.sql.Date;
import java.time.LocalDate;

public class PrivilegeItem {
    private LocalDate startDate;
    private LocalDate endDate;
    private String privilegeLevel;
    public PrivilegeItem(LocalDate startDate, LocalDate endDate, String privilegeLevel){
        this.startDate = startDate;
        this.endDate = endDate;
        this.privilegeLevel = privilegeLevel;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(String privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    @Override
    public String toString() {
        return "" + startDate + ", " +
               endDate + ", " +
               privilegeLevel;
    }
}
