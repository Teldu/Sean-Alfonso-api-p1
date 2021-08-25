package com.services;

import com.documents.AppUser;
import com.datasourse.repos.UserRepository;

import com.dto.SheildedUser;
import com.util.exceptions.AuthenticationException;
import com.util.exceptions.InvalidRequestException;
import com.util.exceptions.ResourcePersistenceException;

import java.util.zip.DataFormatException;

public class UserService {

    private final UserRepository userRepo;
    private final RegistrationCatalog registrationCatalog;

    public UserService(UserRepository userRepo , RegistrationCatalog registrationCatalog) {

        this.userRepo = userRepo;
        this.registrationCatalog = registrationCatalog;
    }


    public SheildedUser FindUserById(String id)
    {
        return new SheildedUser(userRepo.findById(id));
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

    public void AddClass(String courseName , String addedStudent, String username) throws DataFormatException , InvalidRequestException{

        if(courseName == null || addedStudent == null || username == null || registrationCatalog.GetClassDetailsOf(courseName) == null)
        {
            throw new DataFormatException("Provided Information is Invalid");
        }

        if(registrationCatalog.GetClassDetailsOf(courseName).isOpen() == false)
        {
                throw new InvalidRequestException("Cannot Register for CLOSED Course");
        }

        userRepo.AddUserToClass(username , courseName);
        registrationCatalog.AddStudentToCourse(courseName, addedStudent);
    }

    public void DropClass(String courseName , String dropedStudent , String username) throws DataFormatException {
        if(courseName == null || dropedStudent == null || username == null)
        {
            throw new DataFormatException("Provided Information is Invalid");
        }

       userRepo.RemoveUserFromClass(username , courseName);
       registrationCatalog.RemoveStudentFromCourse(courseName, dropedStudent);
    }
    public boolean isUserValid(AppUser user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }

}
