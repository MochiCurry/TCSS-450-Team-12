package edu.tacoma.uw.css.sextod.memeups;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import java.util.List;

import edu.tacoma.uw.css.sextod.memeups.database.User;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class UserTest
{
    //Test constructor

    /**
     * Test constructor
     */
    @Test
    public void testAccountConstructor() {
        assertNotNull(new User("mmuppa@uw.edu", "test1@3"));
    }

    //Test bad email
    @Test
    /**
     * Test Bad Email
     */
    public void testAccountConstructorBadEmail() {
        try {
            new User("mmuppauw.edu", "test1@3");
            fail("Account created with invalid email");
        } catch (IllegalArgumentException e) {

        }
    }

    //Test bad password

    /**
     * Test bad password
     */
    @Test
    public void testAccountConstructorBadPassword() {
        try {
            //no numbers
            new User("mmuppauw@uw.edu", "testtttttt");
            fail("Account created with invalid password");
        } catch (IllegalArgumentException e) {

        }
    }

    //Test empty email

    /**
     * Test Empty Email
     */
    @Test
    public void testUserConstructorEmptyEmail()
    {
        try {
            new User("", "testtttt$4");
            fail("Account created with invalid username");
        } catch (IllegalArgumentException e) {

        }
    }

    //Test empty password

    /**
     * Test Empty Password
     */
    @Test
    public void testAccountConstructorEmptyPassword() {
        try {
            new User("mmuppauw@uw.edu", "");
            fail("Account created with invalid password");
        } catch (IllegalArgumentException e) {

        }
    }

    //Test get email

    /**
     * Test getting email
     */
    @Test
    public void testGetEmail()
    {
        User test = new User("mmuppauw@uw.edu", "testtttt$4");
        Assert.assertEquals("mmuppauw@uw.edu", test.getEmail());
    }

    //test get password

    /**
     * Test getting password
     */
    @Test
    public void testGetPassword()
    {
        User test = new User("mmuppauw@uw.edu", "testtttt$4");
        Assert.assertEquals("testtttt$4", test.getPassword());
    }

    //Test set email

    /**
     * Test setting email
     */
    @Test
    public void testSetEmail()
    {
        User test = new User("temporary@uw.edu", "testtttt$4");
        test.setEmail("mmuppauw@uw.edu");
        Assert.assertEquals("mmuppauw@uw.edu", test.getEmail());
    }

    //test set password

    /**
     * Test setting password
     */
    @Test
    public void testSetPassword()
    {
        User test = new User("mmuppauw@uw.edu", "temppass2@1");
        test.setPassword("testtttt$4");
        Assert.assertEquals("testtttt$4", test.getPassword());
    }

    /**
     * Test parsing json with a sample json string from our app
     */
    @Test
    public void testParseJSON()
    {
        List<User> testList = null;

        String JSONString = "[{\"email\":\"example@gmail.com\",\"password\":\"password123\"},{\"email\":\"ILmarissa@gmail.com\",\"password\":\"pass123\"},{\"email\":\"jameshomeemail@gmail.com\",\"password\":\"pass123\"}]";
        try
        {
            testList = User.parseUserJSON(JSONString);
        }catch(JSONException e)
        {
            fail("JSONException");
        }

        assertNotNull(testList);
    }
}
