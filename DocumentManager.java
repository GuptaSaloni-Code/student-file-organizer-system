import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class DocumentManager {
    private ArrayList<Document> documents = new ArrayList<>();
    private int idCounter = 1;
    private static final String FILE_NAME = "documents.txt";

    // Constructor — loads saved data when program starts
    public DocumentManager() {
        loadFromFile();
    }

    // ADD
    public void addDocument(String title, String subject, String type, String description, String filePath) {
        Document doc = new Document(idCounter++, title, subject, type, description, filePath);
        documents.add(doc);
        saveToFile();
        System.out.println("✅ Document added! ID = " + doc.getId());
    }

    // VIEW ALL
    public void viewAll() {
        if (documents.isEmpty()) {
            System.out.println("⚠️  No documents found.");
            return;
        }
        System.out.println("\n📂 Total Documents: " + documents.size());
        for (Document d : documents) System.out.println(d);
    }

    // SEARCH
    public void search(String keyword) {
        boolean found = false;
        for (Document d : documents) {
            if (d.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                d.getSubject().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(d);
                found = true;
            }
        }
        if (!found) System.out.println("⚠️  No match found for: " + keyword);
    }

    // UPDATE
    public void update(int id, String title, String subject, String type, String description, String filePath) {
        Document d = findById(id);
        if (d == null) { System.out.println("❌ ID not found."); return; }
        d.setTitle(title);
        d.setSubject(subject);
        d.setType(type);
        d.setDescription(description);
        d.setFilePath(filePath);
        saveToFile();
        System.out.println("✅ Document updated!");
        System.out.println(d);
    }

    // DELETE
    public void delete(int id) {
        Document d = findById(id);
        if (d == null) { System.out.println("❌ ID not found."); return; }
        documents.remove(d);
        saveToFile();
        System.out.println("✅ Document \"" + d.getTitle() + "\" deleted!");
    }

    // FIND BY ID
    public Document findById(int id) {
        for (Document d : documents)
            if (d.getId() == id) return d;
        return null;
    }

    // PHASE 2 — SAVE all documents to file
    private void saveToFile() {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            for (Document d : documents) {
                writer.write(d.toFileString() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // PHASE 2 — LOAD all documents from file when program starts
    private void loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;   // first time running — no file yet

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", 6);  // split by | into 6 parts
                if (parts.length == 6) {
                    int id          = Integer.parseInt(parts[0]);
                    String title    = parts[1];
                    String subject  = parts[2];
                    String type     = parts[3];
                    String desc     = parts[4];
                    String filePath = parts[5];
                    documents.add(new Document(id, title, subject, type, desc, filePath));
                    if (id >= idCounter) idCounter = id + 1;  // keep ID counter correct
                }
            }
            reader.close();
            System.out.println("📂 " + documents.size() + " document(s) loaded from saved data.");
        } catch (Exception e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
        }
    }
}