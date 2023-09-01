package cs211.project.services;

import cs211.project.models.collections.StaffList;

public class StaffListHardCode implements Datasource<StaffList>{
    @Override
    public StaffList readData() {
        StaffList list = new StaffList();
        list.addStaff("01","win");
        list.addStaff("02","best");
        list.addStaff("03","jim");
        list.addStaff("04","ai");
        list.addStaff("05","boom");
        return list;
    }

    @Override
    public void writeData(StaffList data) {

    }
}
