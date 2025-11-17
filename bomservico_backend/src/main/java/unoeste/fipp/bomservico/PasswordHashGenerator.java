package unoeste.fipp.bomservico;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generate hash for admin password
        String adminPassword = "eddy123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Password: " + adminPassword);
        System.out.println("BCrypt Hash: " + adminHash);
        System.out.println();
        System.out.println("SQL Update Query:");
        System.out.println("UPDATE usuario SET usu_senha = '" + adminHash + "' WHERE usu_login = 'admin';");
        System.out.println();

        // You can add more passwords here if needed
        String[] testPasswords = {"123", "password", "test"};
        System.out.println("Additional password hashes:");
        for (String pwd : testPasswords) {
            System.out.println("Password: '" + pwd + "' -> Hash: " + encoder.encode(pwd));
        }
    }
}
