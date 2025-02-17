package edu.fit.schedulo.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edu.fit.schedulo.app.objs.course.Catalog;
import edu.fit.schedulo.app.objs.course.CatalogDeserializer;
import edu.fit.schedulo.app.objs.course.Courses;
import edu.fit.schedulo.app.objs.course.CoursesDeserializer;
import edu.fit.schedulo.app.objs.event.StudentCalendar;
import edu.fit.schedulo.app.objs.event.StudentCalendarDeserializer;
import edu.fit.schedulo.app.objs.mood.MoodReports;
import edu.fit.schedulo.app.objs.mood.MoodReportsDeserializer;
import edu.fit.schedulo.app.objs.semester.Semesters;
import edu.fit.schedulo.app.objs.semester.SemestersDeserializer;

/**
 * @author Joshua Sheldon
 */
public class StudentStorage {

    /* ---------- CONSTANTS ---------- */

    public static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
    public static final int IV_LENGTH_IN_BYTES = 16;
    public static final String KEY_ALGORITHM = "AES";
    public static final int KEY_LENGTH_IN_BYTES = 32;
    public static final String STORAGE_FILE = "storage.dat";

    /* ---------- INSTANCE VARIABLES ---------- */

    private final ObjectMapper mapper;

    /* ---------- CONSTRUCTORS ---------- */

    public StudentStorage() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    /* ---------- METHODS ---------- */

    /**
     * Creates a CipherOutputStream that encrypts data written
     * to the given FileOutputStream using the given password.
     *
     * @param fos      The FileOutputStream to write to
     * @param password The password to encrypt with
     * @param ivSpec   The IV to use for encryption
     * @return A CipherOutputStream, or <code>null</code>
     * if an error occurred.
     */
    private CipherOutputStream createCOS(FileOutputStream fos, String password, IvParameterSpec ivSpec) {

        // Convert password to key for AES-256
        SecretKeySpec secretKey = this.createKey(password);

        // Create the CipherOutputStream
        Cipher cipher;

        try {
            cipher = Cipher.getInstance(CIPHER_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return new CipherOutputStream(fos, cipher);
        } catch (Exception e) {
            System.err.println("Could not create Cipher of type: \"" + CIPHER_TYPE + "\" with " +
                    "key: \"" + secretKey + "\" and IV: \"" + ivSpec + "\"");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @return A FileInputStream opened from the storage file, or
     * <code>null</code> if there is no storage file or if there
     * is an exception opening the input stream.
     */
    private FileInputStream createFIS() {

        File file = new File(STORAGE_FILE);

        if (!file.exists()) {
            // No file to read from
            return null;
        }

        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            System.err.println("Could not open FileInputStream from existing storage " +
                    "file: \"" + file.getAbsolutePath() + "\"");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @return A FileOutputStream for writing to the defined
     * output file, or <code>null</code> if an error
     * occured.
     */
    private FileOutputStream createFOS() {

        File file = new File(STORAGE_FILE);

        // Create directories containing the file
        try {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Could not create storage file parent directories!");
            e.printStackTrace();
            return null;
        }

        // Delete the file if it already exists
        if (file.exists()) {
            file.delete();
        }

        // Create the file
        try {
            file.createNewFile();
        } catch (Exception e) {
            System.err.println("Could not create storage file!");
            e.printStackTrace();
            return null;
        }

        // Create the FileOutputStream
        try {
            return new FileOutputStream(file);
        } catch (Exception e) {
            System.err.println("Could not create storage FileOutputStream!");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Creates secret key from the given password string.
     * Secret key will be <code>KEY_LENGTH_IN_BYTES</code>
     * bytes long.
     *
     * @param password The password to create the key from
     * @return The secret key
     */
    private SecretKeySpec createKey(String password) {
        return new SecretKeySpec(
                Arrays.copyOf(password.getBytes(StandardCharsets.UTF_8), KEY_LENGTH_IN_BYTES),
                KEY_ALGORITHM
        );
    }

    /**
     * Creates random IV for AES encryption.
     *
     * @return The IV
     */
    private IvParameterSpec createRandomIV() {
        byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Creates a JSON object that stores all of Schedulo's
     * persistent data, including academic calendar
     * events, course descriptions, course instances,
     * student calendar events, and mood reports.
     *
     * @return The JSON object
     */
    public ObjectNode createStorageJSON() {

        ObjectNode root = this.mapper.createObjectNode();

        root.set("Semesters", this.mapper.valueToTree(Semesters.getInstance()));
        root.set("Catalog", this.mapper.valueToTree(Catalog.getInstance()));
        root.set("Courses", this.mapper.valueToTree(Courses.getInstance()));
        root.set("StudentCalendar", this.mapper.valueToTree(StudentCalendar.getInstance()));
        root.set("MoodReports", this.mapper.valueToTree(MoodReports.getInstance()));

        return root;

    }

    /**
     * @return <code>true</code> if the storage file exists,
     * <code>false</code> otherwise.
     */
    public boolean doesStorageExist() {
        return new File(STORAGE_FILE).exists();
    }

    /**
     * Loads the app's persistent data structures from the
     * given JSON object.
     *
     * @param json The JSON object to load data from
     */
    public void loadStorageJSON(ObjectNode json) {

        // Load semesters JSON
        JsonNode semesters = json.get("Semesters");
        if (semesters != null && semesters.isArray()) {
            SemestersDeserializer.loadFromJSON(
                    (ArrayNode) semesters,
                    this.mapper
            );
        }

        // Load catalog JSON
        JsonNode catalog = json.get("Catalog");
        if (catalog != null && catalog.isArray()) {
            CatalogDeserializer.loadFromJSON((ArrayNode) catalog);
        }

        // Load courses JSON
        JsonNode courses = json.get("Courses");
        if (courses != null && courses.isObject()) {
            CoursesDeserializer.loadFromJSON((ObjectNode) courses, this.mapper);
        }

        // Load student calendar JSON
        JsonNode studentCalendar = json.get("StudentCalendar");
        if (studentCalendar != null && studentCalendar.isObject()) {
            StudentCalendarDeserializer.loadFromJSON((ObjectNode) studentCalendar, this.mapper);
        }

        // Load mood reports JSON
        JsonNode moodReports = json.get("MoodReports");
        if (moodReports != null && moodReports.isObject()) {
            MoodReportsDeserializer.loadFromJSON((ObjectNode) moodReports, this.mapper);
        }

    }

    /**
     * Read <code>IV_LENGTH_IN_BYTES</code> bytes from
     * the given FileInputStream, which should be the
     * IV for AES encryption.
     *
     * @param fis The FileInputStream to read from
     * @return The IV, or <code>null</code> if an error occurred
     */
    private byte[] readIVFromFIS(FileInputStream fis) {
        byte[] iv = new byte[IV_LENGTH_IN_BYTES];
        try {
            int read = fis.read(iv);
            if (read != iv.length) {
                throw new Exception("Did not read " + iv.length + " bytes from storage file!");
            }
            return iv;
        } catch (Exception e) {
            System.err.println("Could not read IV from storage file!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the encrypted storage file from disk, decrypts
     * it using the given password, and loads the data into
     * the app's persistent data structures.
     *
     * @param password The password to decrypt the data with
     * @return <code>true</code> if the data was successfully
     * read from disk and decrypted, <code>false</code> otherwise.
     */
    public boolean readStorageFromDisk(String password) {

        long startTime = System.currentTimeMillis();

        // Open the FileInputStream
        FileInputStream fis = createFIS();
        if (fis == null) return false;

        // Read prepended IV
        byte[] iv = readIVFromFIS(fis);
        if (iv == null) return false;

        // Set up the key and IV
        SecretKeySpec secretKey = this.createKey(password);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Initialize the cipher for decryption
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        } catch (Exception e) {
            System.err.println("Could not create Cipher of type: \"" + CIPHER_TYPE + "\" with " +
                    "key: \"" + secretKey + "\" and IV: \"" + ivSpec + "\"");
            e.printStackTrace();
            return false;
        }

        // Create a CipherInputStream to decrypt data on-the-fly
        CipherInputStream cis = new CipherInputStream(fis, cipher);

        ObjectNode json;
        try {
            json = (ObjectNode) this.mapper.readTree(cis);
        } catch (Exception e) {
            System.err.println("Could not read storage JSON from encrypted file! " +
                    "Likely means wrong password.");
            // e.printStackTrace();
            return false;
        }

        // Load data structures
        loadStorageJSON(json);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Successfully loaded data from disk in " + elapsedTime + "ms.");

        return true;

    }

    /**
     * Structures the app's persistent data into a JSON
     * object and writes it to disk, encrypted using the
     * given password.
     *
     * @param password The password to encrypt the data with
     * @return <code>true</code> if the data was successfully
     * written to disk, <code>false</code> otherwise.
     */
    public boolean writeStorageToDisk(String password) {

        // Create AES initial value
        IvParameterSpec ivSpec = this.createRandomIV();

        // Create output streams
        FileOutputStream fos = this.createFOS();
        if (fos == null) return false;

        CipherOutputStream cos = this.createCOS(fos, password, ivSpec);
        if (cos == null) return false;

        // Generate JSON and write to disk
        boolean success = false;
        ObjectNode json = this.createStorageJSON();
        try {
            // Prepend IV to the file
            fos.write(ivSpec.getIV());

            // Write encrypted JSON
            this.mapper.writeValue(cos, json);
            success = true;
        } catch (Exception e) {
            System.err.println("Could not write storage JSON to disk!");
            e.printStackTrace();
        }

        // Close the streams
        try {
            cos.close();
            fos.close();
        } catch (Exception e) {
            // Ignore
        }

        return success;

    }

}
