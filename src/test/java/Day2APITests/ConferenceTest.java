package Day2APITests;

import Day2.Conference;
import Day2.Location;
import Day2.Speaker;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Created by User on 2/28/2017.
 */
public class ConferenceTest {

    private static String url = "https://testbashnl.herokuapp.com";

    private static String token;

    public List<Speaker> speakers;
    public Speaker speaker;
    public Location location;
    public Conference conference;


    @Before
    public void loginAdmin(){
         token = given().
                contentType(ContentType.URLENC).
                formParam("grant_type", "password").
                formParam("username", "Mirjana").
                formParam("password", "Password01").
                when().
                post("https://testbashnl.herokuapp.com/oauth/token").
                then().extract().body().path("access_token");
    }

    public static String getToken() {
        return token;
    }

    @Test
    public void getAllConferencesTest() {

        int numOfConferences = 17;

        when().get(url + "/conferences").
        then().statusCode(200).
               body("page.totalElements", equalTo("[]"));
    }

    @Test
    public void getConferenceByIDTest(){
        String conferenceId = "58b54bb4a0f9a700043a52b7";
        when().get(url + "/conferences/" + conferenceId).
                then().statusCode(404).
                body("name", equalTo("MadaConf"));
    }

    @Test
    public void getConferenceByNameTest(){
        String conferenceName = "Basics of C++";
        when().get(url + "/conferences/" + conferenceName).
                then().statusCode(200).
                body("name", equalTo(conferenceName));
    }

    private Conference getConference(){

        List<String> tags = new ArrayList<String>();
        tags.add("c++");
        location = new Location();
        conference = new Conference();
        speaker = new Speaker();
        speakers = new ArrayList<Speaker>();

        speaker.setName("Andrei Martinescu");
        speaker.setTitle("Thinking in C++");
        speakers.add(speaker);

        location.setCity("Cluj-Napoca");
        location.setCountry("RO");
        location.setVenueName("Sigma Center");
        location.setUrl("");

        conference.setLocation(location);
        conference.setSpeakers(speakers);
        conference.setName("Basics of C++");
        conference.setStartDate("2017-03-01");
        conference.setEndDate("2017-03-03");
        conference.setC4pEndDate("2017-01-05");
        conference.setC4pStartDate("2017-01-01");
        conference.setTags(tags);
        conference.setTheme("c++");

        return conference;
    }

    @Test
    public void createConferenceTest(){

        given().
            header("Authorization","Bearer " + token).
            contentType(ContentType.JSON).
            accept(ContentType.JSON).
            body(getConference()).
        when().post(url + "/conferences").
        then().statusCode(200);
    }

    @Test
    public void loginAsAdminTest(){
        given().
        contentType(ContentType.URLENC).
                formParam("grant_type", "password").
                formParam("username", "Mirjana").
                formParam("password", "Password01").
                when().
                post("https://testbashnl.herokuapp.com/oauth/token").
                then().statusCode(200).body("access_token", notNullValue());
    }


}
