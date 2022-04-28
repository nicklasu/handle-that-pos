package model.classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * Generates a unique hardware ID from the machine that is running the software
 * Modified version of https://github.com/Kqnth/Java-HWID/blob/master/HWID.java
 * @author Nicklas Sundell, Anna Raevskaia, Lassi Piispanen, Antti Taponen and
 *         Samu Luoma
 */
public class HWID {
    private HWID() {
    }

    /**
     * @return hardwareID in MD5;
     */
    public static String getHWID() {
        try {
            String toEncrypt = null;
            switch (System.getProperty("os.name")) {
                case "Windows 10":
                    toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name")
                            + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL")
                            + System.getenv("PROCESSOR_REVISION") + System.getenv("NUMBER_OF_PROCESSORS")
                            + System.getenv("SystemDrive");
                    break;
                case "Linux":
                    final String[] args = new String[] { "cat", "/etc/machine-id" };
                    final Process proc = new ProcessBuilder(args).start();
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    toEncrypt = reader.readLine();
                    break;
                case "Mac OS X":
                    final String[] args2 = new String[] { "ioreg", "-rd1", "-c",
                            "IOPlatformExpertDevice | grep -E '(UUID)'" };
                    final Process proc2 = new ProcessBuilder(args2).start();
                    final BufferedReader reader2 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
                    toEncrypt = reader2.readLine();
                    break;
                default:
                    toEncrypt = System.getenv("COMPUTERNAME") + System.getProperty("user.name")
                            + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL")
                            + System.getenv("PROCESSOR_REVISION") + System.getenv("NUMBER_OF_PROCESSORS")
                            + System.getenv("SystemDrive");
                    break;
            }
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            final StringBuilder hexString = new StringBuilder();
            final byte[] byteData = md.digest();
            for (final byte aByteData : byteData) {
                final String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (final Exception e) {
            e.printStackTrace();
            return "Error!";
        }
    }

    /**
     * @return current operating system
     */
    public static String getOperatingSystem() {
        final String os = System.getProperty("os.name");
        System.out.println("Using System Property: " + os);
        return os;
    }
}
