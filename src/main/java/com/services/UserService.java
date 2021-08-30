package com.services;

import com.datasourse.repos.RegistrationCatalog;
import com.documents.AppUser;
import com.datasourse.repos.UserRepository;

import com.documents.ClassDetails;
import com.dto.SheildedUser;
import com.util.DateParser;
import com.util.exceptions.AuthenticationException;
import com.util.exceptions.InvalidRequestException;
import com.util.exceptions.ResourcePersistenceException;

import java.util.List;

public class UserService {

    private final UserRepository userRepo;
    private final RegistrationCatalog registrationCatalog;
    private DateParser dateParser =  new DateParser();
    public UserService(UserRepository userRepo , RegistrationCatalog registrationCatalog) {

        this.userRepo = userRepo;
        this.registrationCatalog = registrationCatalog;
    }

    public void updateCourse( String oldCourseName , ClassDetails classDetails) throws InvalidRequestException
        {
            if(classDetails == null) { throw new InvalidRequestException("Null Data"); }

            // aquiring student roster for given coursename
            List<String> registeredStudents = registrationCatalog.FindAllStudentsInCourse(oldCourseName);
                if(registeredStudents == null)
                {
                    throw new InvalidRequestException("Null Student List");
                }
            //remove class from each student on roster
            for (String studentName : registeredStudents)
            {
                AppUser foundStudent = userRepo.findUserByFirstName(studentName);
                if(foundStudent == null)
                {
                    throw new InvalidRequestException("couldnt Find Student");
                }
                //updateing course names
                userRepo.updateCourseName(oldCourseName , classDetails.getClassName() , foundStudent.getUsername());
            }


           registrationCatalog.UpdateFull( oldCourseName, classDetails);

        }


    public SheildedUser FindUserById(String id)
    {
        if(id == null)
        {
            throw new InvalidRequestException("null data");
        }

        return new SheildedUser(userRepo.findById(id));
    }

    public SheildedUser FindUserName(String username)
    {
        if(username == null)
        {
            throw new InvalidRequestException("null data");
        }
        return new SheildedUser(userRepo.findUserByUsername(username));
    }


    public void RemoveClassFromCatalog(String courseName) throws InvalidRequestException
    {
        if(courseName == null) { throw new InvalidRequestException("Null Data"); }

        // aquiring student roster for given coursename
       List<String> registeredStudents = registrationCatalog.FindAllStudentsInCourse(courseName);

        //remove class from each student on roster
        for (String studentName : registeredStudents)
        {
            // finding student in data base to get username in order to use RemoveUserFromClass method
           AppUser foundStudent = userRepo.findUserByFirstName(studentName);
           //removing givin course name from student planner
            userRepo.RemoveUserFromClass(foundStudent.getUsername() , courseName);
        }
        //removing course from data base
        registrationCatalog.RemoveClass(courseName);
    }

    public AppUser register(AppUser newUser) {

        if (!isUserValid(newUser)) {
            throw new InvalidRequestException("Invalid user data provided!");
        }

        if (userRepo.findUserByUsername(newUser.getUsername()) != null) {
            throw new ResourcePersistenceException("Provided username is already taken!");
        }

        if (userRepo.findUserByEmail(newUser.getEmail()) != null) {
            throw new ResourcePersistenceException("Provided email is already taken!");
        }

        return userRepo.save(newUser);
    }

    public AppUser registerAdmin(AppUser newUser) {

        if (!isUserValid(newUser)) {
            throw new InvalidRequestException("Invalid user data provided!");
        }

        if (userRepo.findUserByUsername(newUser.getUsername()) != null) {
            throw new ResourcePersistenceException("Provided username is already taken!");
        }

        if (userRepo.findUserByEmail(newUser.getEmail()) != null) {
            throw new ResourcePersistenceException("Provided email is already taken!");
        }

        return userRepo.saveAdmin(newUser);
    }

    //tested
    public AppUser login(String username, String password) {

        if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            throw new InvalidRequestException("Invalid user credentials provided!");
        }

        AppUser authUser = userRepo.findUserByCredentials(username, password);

        if (authUser == null) {
            throw new AuthenticationException("Invalid credentials provided!");
        }

        return authUser;

    }

    // tested
    public ClassDetails getClassDetailsOf(String courseName)
    {
        if(courseName == null || courseName.isEmpty())
        {
            throw new InvalidRequestException("Class name is null");
        }
       return registrationCatalog.GetClassDetailsOf(courseName);
    }

    //tested
    public void AddClass(String courseName , String addedStudent, String username) {

        ClassDetails classDetails = registrationCatalog.GetClassDetailsOf(courseName);

        if(courseName == null || addedStudent == null || username == null || registrationCatalog.GetClassDetailsOf(courseName) == null) { throw new InvalidRequestException("Provided Information is Invalid"); }
//        if(classDetails.isOpen() == false) {
//            throw new InvalidRequestException("Cannot Register for CLOSED Course");
//        }
        if(classDetails.getStudentsRegistered().size() >= classDetails.getClassSize()) {
            throw new InvalidRequestException("Class is full");

        }
        //if in window
        if(dateParser.htmlWindow(classDetails.getRegistrationTime(), classDetails.getRegistrationClosedTime()))
        {
            classDetails.setOpen(true);
            registrationCatalog.UpdateFull(classDetails.getClassName(), classDetails);
        }else
        {
            //else
            classDetails.setOpen(false);
            registrationCatalog.UpdateFull(classDetails.getClassName(), classDetails);
            throw new InvalidRequestException("Class is closed");
        }

        // finally
        registrationCatalog.AddStudentToCourse(courseName, addedStudent);
        userRepo.AddUserToClass(username , courseName);
    }
    //tested
    public void DropClass(String courseName , String dropedStudent , String username)  {
        if(courseName == null || dropedStudent == null || username == null)
        {
            throw new InvalidRequestException("Provided Information is Invalid");
        }

        if(registrationCatalog.GetClassDetailsOf(courseName).getStudentsRegistered().contains(dropedStudent) == false || registrationCatalog.GetClassDetailsOf(courseName).isOpen() == false)
        {
            System.out.println("Failed to Drop Course");
            throw new InvalidRequestException("Can not drop unattained class ");
        }

       userRepo.RemoveUserFromClass(username , courseName);
       registrationCatalog.RemoveStudentFromCourse(courseName, dropedStudent);
    }

    //tested
    public boolean isUserValid(AppUser user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }

}
