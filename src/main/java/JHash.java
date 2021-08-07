public class JHash {

    public static boolean isValid(String hashed, String check) {
        return hashed.equals(check);
    }

    public static String hash(String value) {
        return value;
    }

}
