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

    private ObjectMapper mapper = new ObjectMapper();
    String DatabaseName = "SchoolDatabase";
    String CourseCollectionName = "Courses";
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    public RegistrationCatalog(String className, int classSize){

    }

    public RegistrationCatalog(String className, String students){

    }
    public RegistrationCatalog(){
        super();
    }

    public RegistrationCatalog(String className){

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
            System.out.println("#####################################################################");
            for (Document classDoc : listOfDocs) {
                //using the class name to get the document from the database
                Object classname = classDoc.get("className");
                Document AuthDoc = courseCollection.find(classDoc).first();
                System.out.println(" AuthDoc :" + AuthDoc + "\n");
                // mapping ducment into a ClassDetails pojo
                ClassDetails classDetails = mapper.readValue(AuthDoc.toJson() , ClassDetails.class);
                System.out.println(" ClassDetails :"+ classDetails + "\n");
                if(classDetails == null)
                {
                    System.out.println(classDetails);
                    throw new InvalidRequestException(" null");
                }
                if(classDetails == null)
                {
                    throw new InvalidRequestException("element list cannot be created : null");
                }
                // add pojo to list to then forward to servlet
                 outClassList.add(classDetails);
            }
            System.out.println("#####################################################################");

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


    public ClassDetails GetClassDetailsOf(String className)
    {

        try{
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> courseCollection = classDb.getCollection("Courses");
            Document queryDoc = new Document("className", className);
           Document authDoc = courseCollection.find(queryDoc).first();
           if(authDoc == null)
           {
               throw new InvalidRequestException("authdoc null");
           }
            ObjectMapper mapper = new ObjectMapper();
            ClassDetails targetCourse = mapper.readValue(authDoc.toJson() , ClassDetails.class);


            return targetCourse;

        }catch (Exception e)
        {

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



}
