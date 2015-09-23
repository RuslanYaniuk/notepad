package com.mynote.test.controllers;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public interface UserJsonFixtures {

    String ALL_USERS_IN_DB = "[" +
            "{\"firstName\":\"System\",\"lastName\":\"Administrator\",\"email\":\"admin@mynote.com\",\"id\":1,\"login\":\"administrator\"," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Andrew\",\"lastName\":\"Anderson\",\"email\":\"andrew.anderson@mail.com\",\"id\":2,\"login\":\"andrew95\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Bob\",\"lastName\":\"Buerlson\",\"email\":\"bob.buerlson@mail.com\",\"id\":3,\"login\":\"1bob1678\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Cindy\",\"lastName\":\"Cisco\",\"email\":\"cindy.cisco@mail.com\",\"id\":4,\"login\":\"$$cindy445\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Deniel\",\"lastName\":\"Dempto\",\"email\":\"deniel.dempto@mail.com\",\"id\":5,\"login\":\"daniel585ss\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Antony\",\"lastName\":\"Jones\",\"email\":\"antony.jones@disabled.com\",\"id\":6,\"login\":\"antony1715for$\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}]";
    
    String UPDATED_FIRST_NAME_USER_ID2 = "{\"firstName\":\"updated first Name\",\"lastName\":\"Anderson\",\"email\":\"andrew.anderson@mail.com\",\"id\":2,\"" +
            "userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";

    String UPDATED_ROLES_USER_ID2 = "{\"firstName\":\"Andrew\",\"lastName\":\"Anderson\",\"email\":\"andrew.anderson@mail.com\",\"id\":2," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";

    String ALL_USER_ROLES_IN_DB = "[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}]";

    String ALL_USERS_IN_DB_AFTER_DELETION_ID2 = "[" +
            "{\"firstName\":\"System\",\"lastName\":\"Administrator\",\"email\":\"admin@mynote.com\",\"id\":1,\"login\":\"administrator\"," +
            "\"userRoleDTOs\":[{\"id\":2,\"role\":\"ROLE_ADMIN\"},{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Bob\",\"lastName\":\"Buerlson\",\"email\":\"bob.buerlson@mail.com\",\"id\":3,\"login\":\"1bob1678\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Cindy\",\"lastName\":\"Cisco\",\"email\":\"cindy.cisco@mail.com\",\"id\":4,\"login\":\"$$cindy445\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Deniel\",\"lastName\":\"Dempto\",\"email\":\"deniel.dempto@mail.com\",\"id\":5,\"login\":\"daniel585ss\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}," +
            "{\"firstName\":\"Antony\",\"lastName\":\"Jones\",\"email\":\"antony.jones@disabled.com\",\"id\":6,\"login\":\"antony1715for$\"," +
            "\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}]";

    String USER_DTO_ID4 = "{\"firstName\":\"Cindy\",\"lastName\":\"Cisco\",\"email\":\"cindy.cisco@mail.com\",\"id\":4,\"login\":\"$$cindy445\",\"userRoleDTOs\":[{\"id\":1,\"role\":\"ROLE_USER\"}],\"registrationDateUTC\":\"2015-09-30T19:20:02\"}";
}
