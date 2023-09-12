package cs211.project.services;

import cs211.project.models.Activity;
import cs211.project.models.Team;
import cs211.project.models.collections.ActivityList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;

public class ActivityListFileDatasource implements Datasource<ActivityList>{
    private String directoryName;
    private String fileName;

    public ActivityListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    // ตรวจสอบว่ามีไฟล์ให้อ่านหรือไม่ ถ้าไม่มีให้สร้างไฟล์เปล่า
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ActivityList readData() {
        ActivityList activities = new ActivityList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String activityName = data[0].trim();
                String date = data[1].trim();
                LocalTime startTimeActivity = LocalTime.parse(data[2].trim());
                LocalTime endTimeActivity = LocalTime.parse(data[3].trim());
                String teamName = data[4].trim();
                String participantName = data[5].trim();
                String status = data[6].trim();
                String eventName = data[7].trim();
                // เพิ่มข้อมูลลงใน list
                activities.addActivity(activityName, date, startTimeActivity, endTimeActivity, teamName, participantName, status, eventName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return activities;
    }

    @Override
    public void writeData(ActivityList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด
            for (Activity activity : data.getActivities()) {
                String line = activity.getActivityName() + "," + activity.getDate() + "," + activity.getStartTimeActivity()+","+activity.getEndTimeActivity()+","+activity.getTeamName()+","+activity.getParticipantName()+","+activity.getStatus()+","+activity.getEventName();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void updateTeamInActivity(Activity activity,Team team){
        ArrayList<String[]> listInfo = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");
                if(data[0].equals(activity.getActivityName())){
                    data[4] = team.getTeamName();
                }
                listInfo.add(data);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        filePath = directoryName + File.separator + fileName;
//        file = new File(filePath);
//
//        // เตรียม object ที่ใช้ในการเขียนไฟล์
//        FileOutputStream fileOutputStream = null;
//
//        try {
//            fileOutputStream = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                fileOutputStream,
//                StandardCharsets.UTF_8
//        );
//        BufferedWriter bufferWriter = new BufferedWriter(outputStreamWriter);
//
//        try {
//            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด
//            for (Activity activity : data.getActivities()) {
//                String line = activity.getActivityName() + "," + activity.getDate() + "," + activity.getStartTimeActivity()+","+activity.getEndTimeActivity()+","+activity.getTeamName()+","+activity.getParticipantName()+","+activity.getStatus()+","+activity.getEventName();
//                buffer.append(line);
//                buffer.append("\n");
//                bufferWriter.
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                buffer.flush();
//                buffer.close();
//            }
//            catch (IOException e){
//                throw new RuntimeException(e);
//            }
//        }
//
    }
//
//

}
