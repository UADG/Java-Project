# CS211-661-Project
> Project ภาคต้น 2566

## Project detail
### รายชื่อสมาชิก
* 6510450160 กวินท์ สินธพสิริพร: วิน (Kawin Sinthopsiriporn)
* 6510450607 ปริยานุช มั่งคั่ง: อัย (Pariyanuch)
* 6510450763 ภัควัฒน์ ปานกลาง: จิม (JIMpk2w)
* 6510450917 เลิศพิพัฒน์ กาญจนเรืองโรจน์: เบส (B1gdawg0)

### วิธีการติดตั้งหรือรันโปรแกรม
* ไปที่ project211-malimy\Malimy\
* Double click run.bat เพื่อ run โปรแกรม

### การวางโครงสร้างไฟล์
```
cs211-661-project-java-best-win-ai-jim
ª   .idea
ª   .mvn
ª   .target
ª   .gitignore
ª   mvnw
ª   mvnw.cmd
ª   pom.xml
ª   README.md
ª   
ª           
+---data
ª       activity-list.csv
ª       ban-staff-list.csv
ª       ban-user.csv
ª       event-list.csv
ª       team-comment.csv
ª       team.csv
ª       user-info.csv
ª       user-joined-event.csv
ª       
+---images
ª       ...
ª       
+---src
ª   +---main
ª       +---java
ª       ª   ª   module-info.java
ª       ª   ª   
ª       ª   +---cs211
ª       ª       +---project
ª       ª           +---controllers
ª       ª           ª       AboutUsController.java
ª       ª           ª       BanAllController.java
ª       ª           ª       CommentTeamController.java
ª       ª           ª       CreateEventController.java
ª       ª           ª       CreateScheduleController.java
ª       ª           ª       CreateTeamController.java
ª       ª           ª       EditEventController.java
ª       ª           ª       EventDetailsController.java
ª       ª           ª       EventHistoryController.java
ª       ª           ª       EventItemController.java
ª       ª           ª       EventScheduleController.java
ª       ª           ª       EventsListController.java
ª       ª           ª       FinishActivityController.java
ª       ª           ª       FixScheduleController.java
ª       ª           ª       JoinedHistoryController.java
ª       ª           ª       LoginPageController.java
ª       ª           ª       ParticipantScheduleController.java
ª       ª           ª       ProfileSetPageController.java
ª       ª           ª       RegisterPageController.java
ª       ª           ª       RePassPageController.java
ª       ª           ª       TeamScheduleController.java
ª       ª           ª       UserStatusController.java
ª       ª           ª       
ª       ª           +---cs211661project
ª       ª           ª       HelloApplication.java
ª       ª           ª       
ª       ª           +---models
ª       ª           ª   ª   Account.java
ª       ª           ª   ª   Activity.java
ª       ª           ª   ª   Event.java
ª       ª           ª   ª   Staff.java
ª       ª           ª   ª   Team.java
ª       ª           ª   ª   
ª       ª           ª   +---collections
ª       ª           ª           AccountList.java
ª       ª           ª           ActivityList.java
ª       ª           ª           EventList.java
ª       ª           ª           StaffList.java
ª       ª           ª           TeamList.java
ª       ª           ª           
ª       ª           +---services
ª       ª                   AccountListDatasource.java
ª       ª                   ActivityListFileDatasource.java
ª       ª                   BanListFileDatasource.java
ª       ª                   CommentTeamListDatasource.java
ª       ª                   Datasource.java
ª       ª                   EventListFileDatasource.java
ª       ª                   FXRouter.java
ª       ª                   TeamListFileDatasource.java
ª       ª                   UserEventListFileDatasource.java
ª       ª                   
ª       +---resources
ª           +---cs211
ª           ª   +---project
ª           ª       +---views
ª           ª               about-us.fxml
ª           ª               ban-all.fxml
ª           ª               comment-activity.fxml
ª           ª               create-event.fxml
ª           ª               create-schedule.fxml
ª           ª               create-team.fxml
ª           ª               dark-theme.css
ª           ª               edit-event.fxml
ª           ª               event-details.fxml
ª           ª               event-history.fxml
ª           ª               event-item.fxml
ª           ª               event-schedule.fxml
ª           ª               events-list.fxml
ª           ª               finish-activity.fxml
ª           ª               fix-schedule.fxml
ª           ª               joined-history.fxml
ª           ª               login-page.fxml
ª           ª               participant-schedule.fxml
ª           ª               profile-setting.fxml
ª           ª               re-password.fxml
ª           ª               register-page.fxml
ª           ª               st-theme.css
ª           ª               team-schedule.fxml
ª           ª               user-status.fxml
ª           ª               
ª           +---images
ª                   ai-image.jpg
ª                   best-image.jpg
ª                   default-event.png
ª                   default-profile.png
ª                   jim-image.jpg
ª                   logo-dark-theme.png
ª                   logo-light-theme.png
ª                   win-image.jpg
```

### ตัวอย่างข้อมูลผู้ใช้ระบบ
* (Admin) Username : admin, Password : 123
* (User)  Username : best, Password : 123
* (User)  Username : win, Password : 123
* (User)  Username : ai, Password : 123
* (User)  Username : jim, Password : 123
* (User)  Username : testjoin, Password : 123
* (User)  Username : testcreate, Password : 123

### สรุปสิ่งที่พัฒนาแต่ละครั้งที่นำเสนอความก้าวหน้าของระบบ
* ครั้งที่ 1 (11/08/2023)
  * uml diagram, วางโครงสร้างโปรแกรมและ UI (ทุกคนในกลุ่ม)
  * กำหนด class พื้นฐานต่างๆ เช่น user, userList (ทุกคนในกลุ่ม)
  * ux/ui หน้าต่างหลักต่างๆ เช่น home, about_us, หน้ารวมเรื่องร้องเรียน, login, register, user_manual, news fxml (ทุกคนในกลุ่ม)
* ครั้งที่ 2 (01/09/2023)
  * สามารถ register user, staff, admin ได้, การอัพรูปภาพ, มีการเขียนไฟล์ csv ของ user, staff, admin ได้ถูกต้อง (แทน)
  * พัฒนาระบบการทำงานของเรื่องร้องเรียน หน้ารวมเรื่องเรียนและการเพิ่มเรื่องร้องเรียน (มะลิ)
  * พัฒนาระบบ login และ reset password (นัตตี้)
  * พัฒนา interface home , admin และนำ css มาใช้ (เปรม)
* ครั้งที่ 3 (22/09/2023)
  * รายละเอียดต่างๆของระบบ admin, staff, user, เรื่องร้องเรียน (แทน)
  * รายละเอียดต่างๆของระบบเรื่องร้องเรียนและ user (มะลิ)
  * สามารถใช้ระบบ login และ reset password ได้ (นัตตี้)
  * รายละเอียดระบบการทำงานของ staff และ admin เล็กน้อย (เปรม)
  * สามารถใช้งานระบบ user, report, request, request category, task category, request unban และมีการทำงานกับไฟล์ csv (ทุกคนในกลุ่ม)
  * fxml เกือบเสร็จทุกหน้าเเละสามารถเปลี่ยนหน้าได้อย่างถูกต้อง (ทุกคนในกลุ่ม)
* ครั้งที่ 4 (13/10/2023)
  * ระบบ request, report, ban, category การเพิ่มหัวข้อของเรื่องร้องเรียน, หน่วยงานของเจ้าหน้าที่ผ่านระบบและระบบทั้งหมดของ admin, ระบบข่าวและมีการทำงานกับไฟล์ csv อย่างเต็มระบบ, เก็บรายละเอียดต่างๆของระบบ (แทน)
  * การจัดเรียงในเงื่อนไขต่างๆของเรื่องร้องเรียน (มะลิ)
  * ตกแต่งความสวยงามต่างๆและเก็บรายละเอียดในระบบ (นัตตี้)
  * ระบบ staff ที่สมบูรณ์ (เปรม)
  * แก้บัคต่างๆและเก็บรายละเอียดของทุกระบบ (ทุกคนในกลุ่ม)
  * เเก้ไขทุกระบบให้สมบูรณ์ fxml สามารถใช้ได้ 100% และสามารถไปได้ทุกหน้า (ทุกคนในกลุ่ม)
  * สร้าง pdf เเละ ทำส่วนย่อยต่างๆที่เป็นส่วนเพิ่มเติมนอกเหนือจากโปรเเกรม (นัตตี้)
  * สร้าง readme, UML diagram และรายละเอียดย่อยต่างๆ (แทน)