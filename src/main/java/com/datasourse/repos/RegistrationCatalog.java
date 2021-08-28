package com.datasourse.repos;
import com.documents.ClassDetails;
import com.dto.Classdto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;


import com.util.MongoClientFactory;
import com.util.exceptions.DataSourceException;
import com.util.exceptions.InvalidRequestException;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class RegistrationCatalog implements CrudRepository<Classdto> {

   // private final Logger logger = LogManager.getLogger(StudentDashboard.class);
    private String className;
    private int classSize;
    private List<String> students;
    private ObjectMapper mapper = new ObjectMapper();
    String DatabaseName = "SchoolDatabase";
    String CourseCollectionName = "Courses";
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    public RegistrationCatalog(String className, int classSize){
        this.className = className;
        this.classSize = classSize;
    }

    public RegistrationCatalog(String className, String students){
        this.className = className;
        this.students.add(students);
    }
    public RegistrationCatalog(){
        super();
    }

    public RegistrationCatalog(String className){
        this.className = className;
    }


    @Override
    public Classdto findById(String id) {
        return null;
    }

    @Override
    public Classdto save(Classdto newResource) {
        return null;
    }

    public Classdto save(ClassDetails registerCourseRequest) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection(); //connect to mongoDB


            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
                 MongoCollection<Document> usersCollection  = classDb.getCollection(CourseCollectionName);
                Document newCDoc = new Document("classSize", registerCourseRequest.getClassSize())
                                                .append("className", registerCourseRequest.getClassName())
                                                .append("open", registerCourseRequest.isOpen())
                                                .append("registrationTime" , registerCourseRequest.getRegistrationTime())
                                                .append("registrationClosedTime" , registerCourseRequest.getRegistrationClosedTime())
                                                .append("meetingPeriod" , registerCourseRequest.getMeetingPeriod())
                                                .append("studentsRegistered" , registerCourseRequest.getStudentsRegistered());


                    usersCollection.insertOne(newCDoc);
                    return new Classdto(registerCourseRequest);

        } catch (Exception e) {
           // logger.error(e.getMessage());
            e.printStackTrace();
            throw new DataSourceException("An unexpected exception occurred.", e);
        }

    }

    @Override
    public boolean update(Classdto updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

    public boolean UpdateFull( String oldCourseName, ClassDetails registerCourseRequest)
    {

        if(registerCourseRequest == null)
        {
            throw new InvalidRequestException("Cannot Search with null resource");
        }
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<ClassDetails> courseCollection = registraiondb.getCollection("Courses", ClassDetails.class);
            Document queryDoc = new Document("className", oldCourseName);

            Document queryDoc1 =new Document("classSize", registerCourseRequest.getClassSize())
                    .append("className", registerCourseRequest.getClassName())
                    .append("open", registerCourseRequest.isOpen())
                    .append("registrationTime" , registerCourseRequest.getRegistrationTime())
                    .append("registrationClosedTime" , registerCourseRequest.getRegistrationClosedTime())
                    .append("meetingPeriod" , registerCourseRequest.getMeetingPeriod());
            if(queryDoc == null || queryDoc1 == null || courseCollection.find(queryDoc).first() == null)
            {
                System.out.println("failed to update");
                return false;

            }


             courseCollection.findOneAndUpdate(queryDoc ,new Document("$set" , new Document(queryDoc1)));

            System.out.println(courseCollection.find(queryDoc).first() );


        }catch (Exception e)
        {

        }
        return true;
    }

    /**
     *
     * @param courseName
     * @param dropedStudent
     */
    public void RemoveStudentFromCourse(String courseName , String dropedStudent) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<ClassDetails> courseCollection = registraiondb.getCollection("Courses", ClassDetails.class);
            Document queryDoc = new Document("className", courseName);
            ClassDetails targetCourse = courseCollection.find(queryDoc).first();

            if (targetCourse == null) {
                String msg = String.format("For some reason the targeted course could not be found in the db using: %s, %s\n", courseName, 4);
                throw new RuntimeException(msg);
            }

            targetCourse.RemoveStudentFromList(dropedStudent);

            courseCollection.findOneAndUpdate(queryDoc, new Document("$pull", new Document("studentsRegistered", dropedStudent)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataSourceException("Unexpected exception", e);
        }
    }

    /**
     *
     * @param courseName
     * @param addedStudent
     */
    public void AddStudentToCourse(String courseName , String addedStudent) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<ClassDetails> courseCollection = registraiondb.getCollection("Courses", ClassDetails.class);
            Document queryDoc = new Document("className", courseName);
            ClassDetails targetCourse = courseCollection.find(queryDoc).first();

            if (targetCourse == null) {
                String msg = String.format("For some reason the targeted course could not be found in the db using: %s, %s\n", courseName, 4);
                throw new RuntimeException(msg);
            }

            targetCourse.AddStudentToList(addedStudent);

            courseCollection.findOneAndUpdate(queryDoc, new Document("$push", new Document("studentsRegistered", addedStudent)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataSourceException("Unexpected exception", e);
        }
    }


    public List<String> FindAllStudentsInCourse(String courseName) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<ClassDetails> courseCollection = registraiondb.getCollection("Courses", ClassDetails.class);
            Document queryDoc = new Document("className", courseName);
            ClassDetails targetCourse = courseCollection.find(queryDoc).first();

            if (targetCourse == null) {
                String msg = String.format("For some reason the targeted course could not be found in the db using: %s, %s\n", courseName, 4);
                throw new RuntimeException(msg);
            }

           return targetCourse.getStudentsRegistered();



        } catch (Exception e) {
            e.printStackTrace();
            throw new DataSourceException("Unexpected exception", e);
        }
    }

    public void RemoveClass(String className)
    {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> UserCollection = registraiondb.getCollection("Courses");
            Document queryDoc = new Document("className", className);

            Document uthDoc = UserCollection.findOneAndDelete(queryDoc);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new DataSourceException("Possibly a connection exception" , e);
        }
    }

    public boolean UpdateClassSize(String className , int classSize)
    {
        if(className == null || classSize < 0 )
        {
            throw new InvalidRequestException("Cannot Search with null resource");
        }

        if(classSize < 10)
        {
            classSize = 24;
        }

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            Document courseDescription = new Document("className" , className);
            Document authCourseDoc = classDb.getCollection(className)
                    .findOneAndUpdate(courseDescription , new Document("classSize", classSize));

            if(authCourseDoc == null)
                return false;



        }catch (Exception e)
        {

        }
        return true;
    }

    public boolean UpdateClassStatus(String className , boolean open)
    {
        if(className == null)
        {
            throw new InvalidRequestException("Cannot Search with null resource");
        }

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            Document courseDescription = new Document("className" , className);
            Document authCourseDoc = classDb.getCollection(className)
                    .findOneAndUpdate(courseDescription , new Document("open",open));

            if(authCourseDoc == null)
                return false;



        }catch (Exception e)
        {

        }
        return true;
    }

    public boolean UpdateClassStartDate(String className , Date date)
    {
        if(className == null)
        {
            throw new InvalidRequestException("Cannot Search with null resource");
        }

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            Document courseDescription = new Document("className" , className);
            Document authCourseDoc = classDb.getCollection(className)
                    .findOneAndUpdate(courseDescription , new Document("registrationTime", date));

            if(authCourseDoc == null)
                return false;



        }catch (Exception e)
        {

        }
        return true;
    }

    public RegistrationCatalog delete(RegistrationCatalog newUser, String name) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            classDb.getCollection(name).drop(); //deletes collection with class name

            return newUser;

        } catch (Exception e) {
            //logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public List<ClassDetails> showClasses() { // TODO reWork this to work with ducements instead of collections
        // list to store mapped Classes
        List<ClassDetails> outClassList = new ArrayList<>();
        // list to store class documents
       List<Document> classDetailsList = new ArrayList<>();
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> courseCollection = classDb.getCollection("Courses");
            List<Document> listOfDocs = courseCollection.find().into(classDetailsList);

            ObjectMapper mapper = new ObjectMapper();
            //iterate through all collections in class DB and convert the documents to class detail pojos
            for (Document classDoc : listOfDocs) {
                // grab all relavant data to ClassDetail pojo
                Object classSize = classDoc.get("classSize");
                System.out.println(classSize);
                Object classname = classDoc.get("className");
                Object classStatus = classDoc.get("open");
                Object registrationTime = classDoc.get("registrationTime");
                Object meetingPeriod = classDoc.get("meetingPeriod");
                Object studentsRegistered = classDoc.get("studentsRegistered");
                // create pojo
                ClassDetails registerCourseRequest = new ClassDetails((String) classname  , (int)classSize, (boolean)classStatus ,(String) registrationTime , (String) meetingPeriod);
                registerCourseRequest.setStudentsRegistered((List<String>) studentsRegistered);

                if(registerCourseRequest == null)
                {
                    throw new InvalidRequestException("element list cannot be created : null");
                }
                // add pojo to list to then forward to servlet
                 outClassList.add(registerCourseRequest);
            }

            return outClassList;

        } catch (Exception e) {
            //logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public RegistrationCatalog showRoster(RegistrationCatalog newUser, String className) {
           // List<Document> doc = new ArrayList<>();
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(className);
            //document enables reading collection contents with .find(), i.e student names

            FindIterable<Document> iterDoc = usersCollection.find();
            Iterator it = iterDoc.iterator();
            while (it.hasNext()) {
                //iterates through class roster and only prints student names
                String stNames = it.next().toString().substring(49);
                //cuts off string until start of student name
                stNames = stNames.substring(0, stNames.length() - 2);
                //removes last two characters of string (contained two brackets at end)
                System.out.println(stNames);
                //prints out remaining string after trimming unnecessary characters (only student name remains)
            }

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
        return newUser;
    }

    public boolean currentlyRegistered(RegistrationCatalog newUser, boolean reg, String className){
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(className);

            FindIterable<Document> iterDoc = usersCollection.find();
            Iterator it = iterDoc.iterator();
            while (it.hasNext()) {
                String stNames = it.next().toString().substring(49);
                stNames = stNames.substring(0, stNames.length() - 2);
                if(stNames.equals(newUser.getClassName())){
                    reg = true;
                }
            }
            //same basic process as showRoster, but instead of printing all student names, finds if
            //provided student name matches any inside collection, and returns true if it does

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
        return reg;
    }

    public ClassDetails GetClassDetailsOf(String className)
    {

        try{
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName).withCodecRegistry(pojoCodecRegistry);
            MongoCollection<ClassDetails> courseCollection = registraiondb.getCollection("Courses", ClassDetails.class);
            Document queryDoc = new Document("className", className);
            ClassDetails targetCourse = courseCollection.find(queryDoc).first();


            return targetCourse;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllCollections(List<String> classNames){
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);

            MongoIterable<String> list = classDb.listCollectionNames();
            //adds all classes in DB to a string list.
            //used to show all classes a student is registered for in StudentDashboard
            for (String name : list) {
                classNames.add(name);
            }
            return classNames;

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }


    }

    public boolean register(RegistrationCatalog newUser, String classname) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            boolean collExists = false;
            //used to check if a class already exists. does not register student if it does not
            for (final String name : classDb.listCollectionNames()) {
                if (name.equalsIgnoreCase(classname)) {
                    //if provided class name matches a collection in DB, then it exists
                    collExists = true;
                }else{
                    return false;
                }
            }

            if(collExists){
                try{
                    //class exists: add student to class

                    MongoCollection<Document> usersCollection = classDb.getCollection(classname);
                    Document newUserDoc = new Document("Students", newUser.getClassName());
                    usersCollection.insertOne(newUserDoc);
                }catch (Exception e){
                 //   logger.error(e.getMessage());
                    System.out.println("Student already registered");
                }

            }

            return true;

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }
    public boolean register(String newUser, String classname) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            boolean collExists = false;
            //used to check if a class already exists. does not register student if it does not
            for (final String name : classDb.listCollectionNames()) {
                if (name.equalsIgnoreCase(classname)) {
                    //if provided class name matches a collection in DB, then it exists
                    collExists = true;
                }else{
                    return false;
                }
            }

            if(collExists){
                try{
                    //class exists: add student to class

                    MongoCollection<Document> usersCollection = classDb.getCollection(classname);
                    Document newUserDoc = new Document("Students", newUser);
                    usersCollection.insertOne(newUserDoc);
                }catch (Exception e){
                    //   logger.error(e.getMessage());
                    System.out.println("Student already registered");
                }

            }

            return true;

        } catch (Exception e) {
            // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public RegistrationCatalog withdraw(RegistrationCatalog newUser, String className){
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(className);
            Document newUserDoc = new Document("Students", newUser.getClassName());

            usersCollection.deleteOne(newUserDoc); //removes provided class name from DB (deletes collection)
            //nothing happens if provided class does not exist in database

            return newUser;

        } catch (Exception e) {
            //logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassSize() {
        return classSize;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public void setClassSize(int classSize) {
        this.classSize = classSize;
    }
}
