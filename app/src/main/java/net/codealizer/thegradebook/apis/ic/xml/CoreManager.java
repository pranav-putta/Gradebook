package net.codealizer.thegradebook.apis.ic.xml;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.codealizer.thegradebook.apis.ic.xml.classbook.Classbook;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookActivity;
import net.codealizer.thegradebook.apis.ic.xml.classbook.ClassbookManager;
import net.codealizer.thegradebook.apis.ic.xml.classbook.CourseDetail;
import net.codealizer.thegradebook.apis.ic.xml.classbook.PortalClassbook;
import net.codealizer.thegradebook.apis.ic.xml.district.DistrictInfo;
import net.codealizer.thegradebook.apis.ic.xml.student.Student;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.data.StateSuggestion;

import org.json.JSONException;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Pranav on 10/8/16.
 */

public class CoreManager {

    private static final String DISTRICT_CODE_URL = "https://mobile.infinitecampus.com/mobile/checkDistrict?districtCode=";
    public String cookies = "";
    public ClassbookManager gradebookManager;
    private Gson mapper = new Gson();
    private String districtCode = "";
    private DistrictInfo districtInfo;
    private Student studentInformation;

    public CoreManager(String districtInfoJson, String studentJson, String xml, String cookies) {
        Gson gson = new Gson();

        districtInfo = gson.fromJson(districtInfoJson, DistrictInfo.class);


        Document xmlDocument;
        try {
            xmlDocument = getDocument(xml);
            Element gradebookElement = (Element) xmlDocument.getElementsByTagName("SectionClassbooks").item(0);

            xmlDocument = getDocument(studentJson);
            Element studentElement = (Element) xmlDocument.getElementsByTagName("Student").item(0);

            studentInformation = new Student(studentElement, districtInfo, xml);

            gradebookManager = new ClassbookManager(gradebookElement, xml);

            this.cookies = cookies;


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    public CoreManager() {

    }

    /**
     * Sets district code for the core manager. Verifies the district code with
     * infinite campus servers.
     *
     * @param districtCode District Code inputted by user
     * @return false if code is invalid, true otherwise
     * @throws MalformedURLException throws exception if URL is invalid
     */
    public boolean setDistrictCode(String districtCode) throws IOException, JsonSyntaxException {
        String url = DISTRICT_CODE_URL + districtCode;
        String json = getContent(new URL(url), false);
        if (!json.isEmpty()) {
            this.districtCode = districtCode;
            districtInfo = mapper.fromJson(json, DistrictInfo.class);
        } else {
            return false;
        }


        return true;
    }

    public ArrayList<Pair<String, ClassbookActivity>> getAllActivities() {
        ArrayList<Pair<String, ClassbookActivity>> activities = new ArrayList<>();

        for (PortalClassbook classbook : gradebookManager.portalclassbooks) {
            for (Classbook semester : classbook.getClassbooks()) {
                activities.addAll(semester.getAllActivities());
            }
        }

        return activities;
    }

    /**
     * Retrieves district information for the selected student
     *
     * @return
     */
    public DistrictInfo getDistrictInfo() {
        return districtInfo;
    }

    /**
     * Uses username and password, as well as district information to try to attempt login with Infinite Campus Servers
     *
     * @param user     Username of student
     * @param pass     Password of student
     * @param distInfo District information class for student
     * @return Boolean value, if login was successful returns true, else false
     * @throws IOException In the case of a network error, throws this exception
     */
    public boolean attemptLogin(String user, String pass, DistrictInfo distInfo) throws IOException {

        URL loginURL = new URL(distInfo.getDistrictBaseURL() + "/verify.jsp?nonBrowser=true&username=" + user + "&password=" + pass + "&appName=" + distInfo.getDistrictAppName());
        String response = getContent(loginURL, true);
        if (response.trim().equalsIgnoreCase("<authentication>success</authentication>")) {
            return true;
        }

        return false;
    }

    public ArrayList<CourseDetail> getStudentClasses() {
        return gradebookManager.getStudentCourses();
    }

    /**
     * Retreieves student information for the student
     *
     * @return Class of student
     * @throws IOException
     * @throws JSONException
     */
    public Student retrieveStudentInformation() throws IOException, JSONException, ParserConfigurationException, SAXException {
        URL infoURL = new URL(getDistrictInfo().getDistrictBaseURL() + "/prism?x=portal.PortalOutline&appName=" + getDistrictInfo().getDistrictAppName());
        String xml = getContent(infoURL, false);

        Element e = (Element) getDocument(xml).getElementsByTagName("Student").item(0);

        this.studentInformation = new Student(e, districtInfo, xml);
        return this.studentInformation;
    }

    /**
     * Retrieves the gradebook and stores as a classbook manager
     *
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public ClassbookManager retrieveGradebook() throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(getDistrictInfo().getDistrictBaseURL()
                + "/prism?&x=portal.PortalClassbook-getClassbookForAllSections&mode=classbook&personID="
                + studentInformation.personID + "&structureID=" + studentInformation.calendars.get(0).schedule.get(0).id
                + "&calendarID=" + studentInformation.calendars.get(0).calendarID);
        String xml = getContent(url, false);

        Document xmlDocument = getDocument(xml);
        Element classbook = (Element) xmlDocument.getElementsByTagName("SectionClassbooks").item(0);

        gradebookManager = new ClassbookManager(classbook, xml);

        return gradebookManager;
    }

    public PortalClassbook getGradebook(CourseDetail position) {
        return gradebookManager.getClassbookForCourse(position);
    }

    public ClassbookManager reloadData(Context a) throws IOException, ParserConfigurationException, SAXException {


        return retrieveGradebook();

    }

    public ClassbookManager reloadAll(Context c) throws IOException, ParserConfigurationException, SAXException, JSONException {
        String username = SessionManager.getUsername(c);
        String password = SessionManager.getPassword(c);

        attemptLogin(username, password, districtInfo);
        retrieveStudentInformation();

        return reloadData(c);
    }

    public ArrayList<StateSuggestion> searchDistricts(String query, String state) throws IOException {
        query = query.replace(" ", "%20");

        String url = "https://mobile.infinitecampus.com/mobile/searchDistrict?query=" + query + "&state=" + state;
        URL mUrl = new URL(url);
        String json = getContent(mUrl, false);

        if (json.contains("data")) {
            JsonArray schoolData = new JsonParser().parse(json).getAsJsonObject().getAsJsonArray("data");

            ArrayList<StateSuggestion> suggestions = new ArrayList<>();

            for (JsonElement school : schoolData) {
                suggestions.add(new StateSuggestion(school.getAsJsonObject()));
            }

            return suggestions;
        } else {
            return new ArrayList<>();
        }
    }

    private String getContent(URL url, boolean altercookies) throws UnknownHostException, IOException {
        String s = "";
        try {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("Cookie", cookies); //Retain our sessoin
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input;
            while ((input = br.readLine()) != null) {
                s += input + "\n";
            }
            br.close();

            StringBuilder sb = new StringBuilder();

            // find the cookies in the response header from the first request
            List<String> cookie = con.getHeaderFields().get("Set-Cookie");
            if (cookie != null) {
                for (String cooki : cookie) {
                    if (sb.length() > 0) {
                        sb.append("; ");
                    }

                    // only want the first part of the cookie header that has the value
                    String value = cooki.split(";")[0];
                    sb.append(value);
                }
            }
            if (altercookies)
                cookies = sb.toString();
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
        }
        return s;
    }

    private Document getDocument(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();

        return doc;

    }


}