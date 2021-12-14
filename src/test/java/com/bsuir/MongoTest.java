//package com.bsuir;
//
//import com.bsuir.models.Role;
//import com.bsuir.models.Status;
//import com.bsuir.models.User;
//import com.bsuir.models.UserActive;
//import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//
//@DataMongoTest
//@RunWith(SpringRunner.class)
//public class MongoTest {
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @DisplayName("given object to save"
//            + " when save object using MongoDB template"
//            + " then object is saved")
//    @Test
//    public void checkThatSaveUsesAsUpdate() {
//        User user = new User(
//                null,
//                "Egor111",
//                "123123",
//                "123@gmail.com",
//                "Egor",
//                "Kokhan",
//                Arrays.asList(Role.USER, Role.ADMIN),
//                Status.ACTIVE,
//                new HashSet<>()
//        );
//        mongoTemplate.save(user);
//        user.getUserActives().add(new UserActive(null, 25000, "USD"));
//        mongoTemplate.save(user);
//
//    }
//
//}
