import java.util.Scanner;
import java.io.File;
import java.awt.Desktop;

public class Main {
    private static Scanner sc              = new Scanner(System.in);
    private static DocumentManager manager = new DocumentManager();
    private static UserManager userManager = new UserManager();
    private static String loggedInUser     = null;   // tracks who is logged in

    public static void main(String[] args) {
        System.out.println("==============================");
        System.out.println("  Student File Organizer v1  ");
        System.out.println("==============================");

        // show login screen first
        boolean running = true;
        while (running) {
            showLoginMenu();
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleRegister();
                case "3" -> { System.out.println("Goodbye! 👋"); running = false; }
                default  -> System.out.println("⚠️  Enter 1, 2 or 3.");
            }

            // if login success → open main menu
            if (loggedInUser != null) {
                showMainMenu();
                loggedInUser = null;   // reset after logout
            }
        }
        sc.close();
    }

    // ── LOGIN MENU ────────────────────────────────────────
    private static void showLoginMenu() {
        System.out.println("\n==============================");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Exit");
        System.out.println("==============================");
    }

    // ── REGISTER ─────────────────────────────────────────
    private static void handleRegister() {
        System.out.println("\n── Register ──");
        System.out.print("Enter username : "); String username = sc.nextLine().trim();
        System.out.print("Enter password : "); String password = sc.nextLine().trim();
        userManager.register(username, password);
    }

    // ── LOGIN ─────────────────────────────────────────────
    private static void handleLogin() {
        System.out.println("\n── Login ──");
        int attempts = 3;   // max 3 attempts
        while (attempts > 0) {
            System.out.print("Username : "); String username = sc.nextLine().trim();
            System.out.print("Password : "); String password = sc.nextLine().trim();
            String result = userManager.login(username, password);
            if (result != null) {
                loggedInUser = result;
                System.out.println("✅ Welcome, " + loggedInUser + "!");
                return;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("❌ Wrong username or password. " + attempts + " attempt(s) left.");
                } else {
                    System.out.println("❌ Too many failed attempts. Returning to menu.");
                }
            }
        }
    }

    // ── MAIN MENU LOOP ────────────────────────────────────
    private static void showMainMenu() {
        int choice;
        do {
            printMenu();
            System.out.print("Choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("⚠️  Enter a number between 1-8.");
                choice = 0;
                continue;
            }
            switch (choice) {
                case 1 -> handleAdd();
                case 2 -> manager.viewAll();
                case 3 -> handleSearch();
                case 4 -> handleUpdate();
                case 5 -> handleDelete();
                case 6 -> handleOpenFile();
                case 7 -> System.out.println("👋 Logged out successfully.");
                case 8 -> { System.out.println("Goodbye! 👋"); System.exit(0); }
                default -> System.out.println("⚠️  Enter a number between 1-8.");
            }
        } while (choice != 7 && choice != 8);
    }

    // ── MENU ─────────────────────────────────────────────
    private static void printMenu() {
        System.out.println("\n------------------------------");
        System.out.println("  Logged in as: " + loggedInUser);
        System.out.println("------------------------------");
        System.out.println("  1. Add Document");
        System.out.println("  2. View All Documents");
        System.out.println("  3. Search Document");
        System.out.println("  4. Update Document");
        System.out.println("  5. Delete Document");
        System.out.println("  6. Open File");
        System.out.println("  7. Logout");
        System.out.println("  8. Exit");
        System.out.println("------------------------------");
    }

    // ── ADD ───────────────────────────────────────────────
    private static void handleAdd() {
        System.out.println("\n── Add New Document ──");
        System.out.print("Title       : "); String title   = sc.nextLine();
        System.out.print("Subject     : "); String subject = sc.nextLine();
        System.out.print("Type (Note/PDF/Assignment/Other): "); String type = sc.nextLine();
        System.out.print("Description : "); String desc    = sc.nextLine();
        System.out.println("File Path   : paste full path or press Enter to skip");
        System.out.println("Example     : C:\\Users\\saloni\\Desktop\\notes.pdf");
        System.out.print("Path        : ");
        String filePath = sc.nextLine().trim().replace("\"", "");
        manager.addDocument(title, subject, type, desc, filePath);
    }

    // ── SEARCH ────────────────────────────────────────────
    private static void handleSearch() {
        System.out.print("\nSearch (title or subject): ");
        manager.search(sc.nextLine());
    }

    // ── UPDATE ────────────────────────────────────────────
    private static void handleUpdate() {
        System.out.println("\n── Update Document ──");
        System.out.print("Enter ID to update: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("⚠️  Invalid ID.");
            return;
        }
        if (manager.findById(id) == null) {
            System.out.println("❌ No document found with ID " + id);
            return;
        }
        System.out.print("New Title       : "); String title   = sc.nextLine();
        System.out.print("New Subject     : "); String subject = sc.nextLine();
        System.out.print("New Type        : "); String type    = sc.nextLine();
        System.out.print("New Description : "); String desc    = sc.nextLine();
        System.out.print("New File Path (or press Enter to skip): ");
        String filePath = sc.nextLine().trim().replace("\"", "");
        manager.update(id, title, subject, type, desc, filePath);
    }

    // ── DELETE ────────────────────────────────────────────
    private static void handleDelete() {
        System.out.println("\n── Delete Document ──");
        System.out.print("Enter ID to delete: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("⚠️  Invalid ID.");
            return;
        }
        System.out.print("Are you sure? (yes/no): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            manager.delete(id);
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ── OPEN FILE ─────────────────────────────────────────
    private static void handleOpenFile() {
        System.out.println("\n── Open File ──");
        System.out.print("Enter ID of document to open: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("⚠️  Invalid ID.");
            return;
        }
        Document d = manager.findById(id);
        if (d == null) {
            System.out.println("❌ No document found with ID " + id);
            return;
        }
        String filePath = d.getFilePath();
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("⚠️  No file attached to this document.");
            return;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("❌ File not found at: " + filePath);
                System.out.println("💡 Tip: C:\\Users\\saloni\\Desktop\\notes.pdf");
                return;
            }
            Desktop.getDesktop().open(file);
            System.out.println("✅ Opening: " + filePath);
        } catch (Exception e) {
            System.out.println("❌ Could not open file: " + e.getMessage());
        }
    }
}