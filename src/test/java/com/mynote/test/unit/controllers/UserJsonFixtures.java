package com.mynote.test.unit.controllers;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public interface UserJsonFixtures {

    String ALL_USERS_IN_DB = "[" +
            "{\"firstName\":\"System\",\"lastName\":\"Administrator\",\"email\":\"admin@email.com\",\"id\":1,\"login\":\"admin\"," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User2 First Name\",\"lastName\":\"User2 Last Name\",\"email\":\"user2@email.com\",\"id\":2,\"login\":\"user2\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User3 First Name\",\"lastName\":\"User3 Last Name\",\"email\":\"user3@email.com\",\"id\":3,\"login\":\"user3\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User4 First Name\",\"lastName\":\"User4 Last Name\",\"email\":\"user4@email.com\",\"id\":4,\"login\":\"user4\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User5 First Name\",\"lastName\":\"User5 Last Name\",\"email\":\"user5@email.com\",\"id\":5,\"login\":\"user5\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User6 First Name\",\"lastName\":\"User6 Last Name\",\"email\":\"user6@email.com\",\"id\":6,\"login\":\"user6\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User7 First Name\",\"lastName\":\"User7 Last Name\",\"email\":\"user7@email.com\",\"id\":7,\"login\":\"user7\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}]";
    
    String UPDATED_FIRST_NAME_USER_ID2 = "{\"firstName\":\"updated first Name\",\"lastName\":\"User2 Last Name\",\"email\":\"user2@email.com\",\"id\":2," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";

    String UPDATED_ROLES_USER_ID2 = "{\"firstName\":\"User2 First Name\",\"lastName\":\"User2 Last Name\",\"email\":\"user2@email.com\",\"id\":2," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";

    String ALL_USER_ROLES_IN_DB = "[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}]";

    String ALL_USERS_IN_DB_AFTER_DELETION_ID2 = "[" +
            "{\"firstName\":\"System\",\"lastName\":\"Administrator\",\"email\":\"admin@email.com\",\"id\":1,\"login\":\"admin\"," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User3 First Name\",\"lastName\":\"User3 Last Name\",\"email\":\"user3@email.com\",\"id\":3,\"login\":\"user3\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User4 First Name\",\"lastName\":\"User4 Last Name\",\"email\":\"user4@email.com\",\"id\":4,\"login\":\"user4\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User5 First Name\",\"lastName\":\"User5 Last Name\",\"email\":\"user5@email.com\",\"id\":5,\"login\":\"user5\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User6 First Name\",\"lastName\":\"User6 Last Name\",\"email\":\"user6@email.com\",\"id\":6,\"login\":\"user6\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"User7 First Name\",\"lastName\":\"User7 Last Name\",\"email\":\"user7@email.com\",\"id\":7,\"login\":\"user7\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}]";

    String USER_DTO_ID4 = "{\"firstName\":\"User4 First Name\",\"lastName\":\"User4 Last Name\",\"email\":\"user4@email.com\",\"id\":4,\"login\":\"user4\",\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";
}
