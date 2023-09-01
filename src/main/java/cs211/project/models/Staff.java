package cs211.project.models;

import java.util.ArrayList;

public class Staff{
    private String id;
    private String name;
    public Staff(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public boolean isId(String id){
        return this.getId().equals(id);
    }

    @Override
    public String toString() {
        return name+" : "+id;
    }
}
