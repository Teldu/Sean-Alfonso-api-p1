package com.datasourse.repos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.util.MongoClientFactory;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.documents.AppUser;
import com.util.exceptions.DataSourceException;

public class UserRepository implements CrudRepository<AppUser> {
    public final MongoClient mongoClient;
    String DatabaseName = "SchoolDatabase";
    String StudentCollectionName = "Students";
    String AdminCollectionName = "Admin";
    public UserRepository(MongoClient mongoClient){ this.mongoClient = mongoClient;}

    public AppUser findUserByCredentials(String username, String password, String type) {

        try {

            MongoDatabase classDatabase = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDatabase.getCollection(StudentCollectionName);
            Document queryDoc = new Document("username", username).append("password", password);
            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            AppUser authUser = mapper.readValue(authUserDoc.toJson(), AppUser.class);
            authUser.setId(authUserDoc.get("_id").toString());
            return authUser;

        } catch (JsonMappingException jme) {
            jme.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An exception occurred while mapping the document.", jme);
        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }
    }

    // TODO implement this so that we can prevent multiple users from having the same username!
    public AppUser findUserByUsername(String username) {
        return null;
    }

    // TODO implement this so that we can prevent multiple users from having the same email!
    public AppUser findUserByEmail(String email) {
        return null;
    }

    @Override
    public AppUser findById(String id) {
        try {


            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(StudentCollectionName);
            Document newUserDoc = new Document("id", id);
            Document foundUser = usersCollection.find(newUserDoc).first();

            if (foundUser == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            AppUser authUser = mapper.readValue(foundUser.toJson(), AppUser.class);

            return authUser;

        } catch (JsonProcessingException jme) {
            jme.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void RemoveUserFromClass(String name , String password , String className)
    {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> UserCollection = registraiondb.getCollection(StudentCollectionName);
            Document queryDoc = new Document("Username", name).append("password", password);
            Document AuthDoc = UserCollection.find(queryDoc).first();
            System.out.println(AuthDoc);
            if(AuthDoc == null)
            {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            AppUser AuthorizedUser = mapper.readValue(AuthDoc.toJson() , AppUser.class);
            AuthorizedUser.RemoveCourseFromList(className);

            UserCollection.findOneAndUpdate(queryDoc , new Document("$pull" , new Document("registeredClasses" , className)) );

        }
        catch (Exception e)
        {
            e.printStackTrace();

            throw new DataSourceException("Unexpected exception" , e);
        }
    }
    /**
     * Should update users courses
     * make isAdding false to drop a course
     * @param name
     * @param password
     * @param className
     */
    public void AddUserToClass(String name , String password , String className )
    {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registrationdb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> UserCollection = registrationdb.getCollection(StudentCollectionName);
            Document queryDoc = new Document("Username", name).append("password", password);
            Document AuthDoc = UserCollection.find(queryDoc).first();
            System.out.println(AuthDoc);
            if(AuthDoc == null)
            {
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            AppUser AuthorizedUser = mapper.readValue(AuthDoc.toJson() , AppUser.class);
            AuthorizedUser.AddCourseToList(className);


            UserCollection.findOneAndUpdate(queryDoc , new Document("$push" , new Document("registeredClasses" , className)));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new DataSourceException("Unexpected exception" , e);
        }
    }


    @Override
    public AppUser save(AppUser newUser) {

        try {


            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(StudentCollectionName);
            Document newUserDoc = new Document("firstName", newUser.getFirstName())
                    .append("lastName", newUser.getLastName())
                    .append("email", newUser.getEmail())
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getPassword())
                    .append("registeredClasses" , newUser.getRegisteredClasses());

            usersCollection.insertOne(newUserDoc);
            newUser.setId(newUserDoc.get("_id").toString());

            return newUser;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }

    }

    public AppUser saveAdmin(AppUser newUser) {

        try {


            MongoDatabase classDb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> usersCollection = classDb.getCollection(AdminCollectionName);
            Document newUserDoc = new Document("firstName", newUser.getFirstName())
                    .append("lastName", newUser.getLastName())
                    .append("email", newUser.getEmail())
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getPassword());

            usersCollection.insertOne(newUserDoc);
            newUser.setId(newUserDoc.get("_id").toString());

            return newUser;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
        }

    }


    @Override
    public boolean update(AppUser updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

}
