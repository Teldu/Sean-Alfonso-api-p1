package com.services;

import com.datasourse.repos.RegistrationCatalog;
import com.documents.AppUser;
import com.datasourse.repos.UserRepository;

import com.util.DateParser;
import com.util.exceptions.AuthenticationException;
import com.util.exceptions.InvalidRequestException;
import com.util.exceptions.ResourcePersistenceException;
import org.junit.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTestSuite {

    UserService sut;


    private UserRepository mockUserRepo;
    private RegistrationCatalog mockClassRepo;
    private DateParser mockDateParser;

    @Before
    public void beforeEachTest() {

        mockDateParser = mock(DateParser.class);
        mockUserRepo = mock(UserRepository.class);
        mockClassRepo = mock(RegistrationCatalog.class);
        sut = new UserService(mockUserRepo , mockClassRepo) ;
    }

    @After
    public void afterEachTest() {
        sut = null;
    }

    @Test
    public void isUserValid_returnsTrue_givenValidUser() {

        // Arrange
        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");

        // Act
        boolean actualResult = sut.isUserValid(validUser);

        // Assert
        Assert.assertTrue("Expected user to be considered valid!", actualResult);

    }



//    @Test
//    public void register_returnsSuccessfully_whenGivenValidUser() {
//
//        // Arrange
//        AppUser expectedResult = new AppUser("valid", "valid", "valid", "valid", "valid");
//        AppUser validUser = new AppUser("valid", "valid", "valid", "valid", "valid");
//        when(mockUserRepo.save(any())).thenReturn(expectedResult);
//
//        // Act
//        AppUser actualResult = sut.register(validUser);
//
//        // Assert
//        Assert.assertEquals(expectedResult, actualResult);
//        verify(mockUserRepo, times(2)).save(any());
//
//    }

    @Test(expected = InvalidRequestException.class)
    public void register_throwsException_whenGivenInvalidUser() {

        // Arrange
        AppUser invalidUser = new AppUser(null, "", "", "", "");

        // Act
        try {
            sut.register(invalidUser);
        } finally {
            // Assert
            verify(mockUserRepo, times(0)).save(any());
        }

    }

    @Test(expected = ResourcePersistenceException.class)
    public void register_throwsException_whenGivenUserWithDuplicateUsername() {

        // Arrange
        AppUser existingUser = new AppUser("original", "original", "original", "duplicate", "original");
        AppUser duplicate = new AppUser("first", "last", "email", "duplicate", "password");
        when(mockUserRepo.findUserByUsername(duplicate.getUsername())).thenReturn(existingUser);

        // Act
        try {
            sut.register(duplicate);
        } finally {
            // Assert
            verify(mockUserRepo, times(1)).findUserByUsername(duplicate.getUsername());
            verify(mockUserRepo, times(0)).save(duplicate);
        }

    }

    @Test
    public void login_CallsfindUserByCredentials_Once_whenGivenvalidUserCredentials(){
        //Arrange
        AppUser existingUser = new AppUser("original", "original", "original", "duplicate", "original");

        when(mockUserRepo.findUserByCredentials(existingUser.getUsername(), existingUser.getPassword())).thenReturn(existingUser);

        //Act

        sut.login(existingUser.getUsername(),existingUser.getPassword());

        //Assert
        verify(mockUserRepo, times(1)).findUserByCredentials(existingUser.getUsername(), existingUser.getPassword());

    }

    @Test(expected = AuthenticationException.class)
    public void login_throwsException_whenGivenInvalidUserCredentials(){
        //Arrange
        AppUser existingUser = new AppUser("original", "original", "original", "duplicate", "original");
        AppUser duplicate = new AppUser("first", "last", "email", null, null);
        when(mockUserRepo.findUserByCredentials(duplicate.getUsername(), duplicate.getPassword())).thenReturn(null);

        //Act

        sut.login(existingUser.getUsername(),existingUser.getPassword());

        //Assert
        verify(mockUserRepo, times(0)).findUserByCredentials(duplicate.getUsername(), duplicate.getPassword());

    }

    @Test(expected = InvalidRequestException.class)
    public void login_throwsException_whenGivenInvalidCredentials(){
        //Arrange
        AppUser existingUser = new AppUser("original", "original", "original", "duplicate", "original");
        AppUser duplicate = new AppUser("first", "last", "email", null, null);


        //Act

        sut.login(null,existingUser.getPassword());
        sut.login(null,null);
        //Assert
        verify(mockUserRepo, times(0)).findUserByCredentials(duplicate.getUsername(), duplicate.getPassword());

    }

    @Test(expected = InvalidRequestException.class)
    public void AddCourse_ThrowsInvalidRequestExceptionException_ifClassisClosed(){
        //Arrange


        when(mockDateParser.htmlWindow(anyString(),anyString())).thenReturn(false);
        //Act
        sut.AddClass("class", "name" , "username");



        //Assert
       verify(mockUserRepo , times(0)).AddUserToClass("  " , " ");
       verify(mockClassRepo , times(0)).AddStudentToCourse(" "  , "");
    }

    @Test(expected = InvalidRequestException.class)
    public void AddCourse_ThrowsInvalidRequestExceptionException_ifinputsareNull(){
        //Arrange



        //Act
        sut.AddClass(null, "Jeff" , "Geof");
        sut.AddClass("null", null , "Geof");
        sut.AddClass("null", "Jeff" , null);


        //Assert
        verify(mockUserRepo , times(0)).AddUserToClass("  " , " ");
        verify(mockClassRepo , times(0)).AddStudentToCourse(" "  , "");
    }



    @Test(expected = InvalidRequestException.class)
    public void DropCourse_ThrowsInvalidRequestExceptionException_ifinputsareNull(){
        //Arrange



        //Act
        sut.DropClass(null, "Jeff" , "Geof");
        sut.DropClass("null", null , "Geof");
        sut.DropClass("null", "Jeff" , null);


        //Assert
        verify(mockUserRepo , times(0)).RemoveUserFromClass("  " , " ");
        verify(mockClassRepo , times(0)).RemoveStudentFromCourse(" "  , "");
    }

    @Test(expected = InvalidRequestException.class)
    public void getClassDetailsOf_ThrowsInvalidRequestExceptionException_ifinputNull(){
        //Arrange



        //Act
        sut.getClassDetailsOf(null);



        //Assert
        verify(mockClassRepo , times(0)).GetClassDetailsOf(null);
    }

    @Test
    public void ValidateUser_returnsfalse_ifinputNull(){
        //Arrange

        AppUser InvalideUser1 = new AppUser(null, "original", "original", "duplicate", "original");
        AppUser InvalideUser2 = new AppUser("null", null, "original", "duplicate", "original");
        AppUser InvalideUser3 = new AppUser("null", "original", null, "duplicate", "original");
        AppUser InvalideUser4 = new AppUser("null", "null", "original", null, "original");
        AppUser InvalideUser5 = new AppUser("null", "null", "original", "null", null);
        //Act
        boolean test1 = sut.isUserValid(InvalideUser1);
        boolean test2 = sut.isUserValid(InvalideUser2);
        boolean test3 = sut.isUserValid(InvalideUser3);
        boolean test4 = sut.isUserValid(InvalideUser4);
        boolean test5 = sut.isUserValid(InvalideUser5);
        //Assert
        Assert.assertFalse(test1);
        Assert.assertFalse(test2);
        Assert.assertFalse(test3);
        Assert.assertFalse(test4);
        Assert.assertFalse(test5);
    }

    @Test(expected = InvalidRequestException.class)
    public void RemoveClassFromCatalog_throwsException_whenGivenInvalidClasname(){
        //Arrange


        //Act

        sut.RemoveClassFromCatalog(null);

        //Assert
        verify(mockClassRepo, times(0)).FindAllStudentsInCourse("classname");
        verify(mockClassRepo, times(0)).RemoveClass("classname");
    }

    @Test(expected = InvalidRequestException.class)
    public void FindUserName_throwsException_whenGivenInvalidClasname(){
        //Arrange


        //Act

        sut.FindUserName(null);

        //Assert
        verify(mockUserRepo, times(0)).findUserByUsername("classname");

    }

    @Test(expected = InvalidRequestException.class)
    public void FindUserbyid_throwsException_whenGivenInvalidiput(){
        //Arrange


        //Act

        sut.FindUserById(null);

        //Assert
        verify(mockUserRepo, times(0)).findUserByUsername("classname");

    }

}
