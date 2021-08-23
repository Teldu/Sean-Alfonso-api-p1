package com.datasourse.repos;

import com.documents.ClassDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.util.MongoClientFactory;
import com.util.exceptions.InvalidRequestException;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.documents.AppUser;
import com.util.exceptions.DataSourceException;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class UserRepository implements CrudRepository<AppUser> {
    public final MongoClient mongoClient;
    String DatabaseName = "SchoolDatabase";
    String StudentCollectionName = "Students";
    String AdminCollectionName = "Admin";
    CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
    CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
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

    public void RemoveUserFromClass(String username, String className)
    {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registraiondb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> UserCollection = registraiondb.getCollection(StudentCollectionName);
            // find the user
            Document queryDoc = new Document("username", username);
            Document AuthDoc = UserCollection.find(queryDoc).first();

            // check is user is null
            System.out.println(AuthDoc);
            if(AuthDoc == null)
            {
                System.out.println("Failed to add course");
                return;
            }


            // remove class from students schedual
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
     * @param className
     */
    public void AddUserToClass( String username , String className )
    {

        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();

            MongoDatabase registrationdb = mongoClient.getDatabase(DatabaseName);
            MongoCollection<Document> UserCollection = registrationdb.getCollection(StudentCollectionName);
            Document queryDoc = new Document("username", username);

            // add course to student scedual
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
