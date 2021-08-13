package com.datasourse.repos;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;



import com.util.MongoClientFactory;
import com.util.exceptions.DataSourceException;
import org.bson.Document;
import java.util.Iterator;
import java.util.List;


public class RegistrationCatalog {

   // private final Logger logger = LogManager.getLogger(StudentDashboard.class);
    private String className;
    private int classSize;
    private List<String> students;
    private ObjectMapper mapper = new ObjectMapper();

    public RegistrationCatalog(String className, int classSize){
        this.className = className;
        this.classSize = classSize;
    }

    public RegistrationCatalog(String className, String students){
        this.className = className;
        this.students.add(students);
    }


    public RegistrationCatalog(String className){
        this.className = className;
    }

    public RegistrationCatalog save(RegistrationCatalog newUser, String className) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection(); //connect to mongoDB

            MongoDatabase classDb = mongoClient.getDatabase("classes");
            //sets db to classes. all class names and student rosters exist here
            try{
                classDb.createCollection(className); //create new collection with class name
            }catch (Exception e){
              //  logger.error(e.getMessage());
                System.out.println("Class already exists!");
            }

            return newUser; //has no actual functionality

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }


    public RegistrationCatalog delete(RegistrationCatalog newUser, String name) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");
            classDb.getCollection(name).drop(); //deletes collection with class name

            return newUser;

        } catch (Exception e) {
            //logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public void showClasses() {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");

            MongoIterable<String> list = classDb.listCollectionNames();
            //iterate through all collections in class DB and print
            for (String name : list) {
                System.out.println(name);
            }

        } catch (Exception e) {
            //logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public RegistrationCatalog showRoster(RegistrationCatalog newUser, String className) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");
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

            MongoDatabase classDb = mongoClient.getDatabase("classes");
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

    public List<String> getAllCollections(List<String> classNames){
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");

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

    public RegistrationCatalog register(RegistrationCatalog newUser, String classname) {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");
            boolean collExists = false;
            //used to check if a class already exists. does not register student if it does not
            for (final String name : classDb.listCollectionNames()) {
                if (name.equalsIgnoreCase(classname)) {
                    //if provided class name matches a collection in DB, then it exists
                    collExists = true;
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

            return newUser;

        } catch (Exception e) {
           // logger.error(e.getMessage());
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    public RegistrationCatalog withdraw(RegistrationCatalog newUser, String className){
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase classDb = mongoClient.getDatabase("classes");
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
