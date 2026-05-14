public class Document {
    private int id;
    private String title;
    private String subject;
    private String type;
    private String description;
    private String filePath;

    public Document(int id, String title, String subject, String type, String description, String filePath) {
        this.id          = id;
        this.title       = title;
        this.subject     = subject;
        this.type        = type;
        this.description = description;
        this.filePath    = filePath;
    }

    // Getters
    public int    getId()          { return id; }
    public String getTitle()       { return title; }
    public String getSubject()     { return subject; }
    public String getType()        { return type; }
    public String getDescription() { return description; }
    public String getFilePath()    { return filePath; }

    // Setters
    public void setTitle(String title)             { this.title = title; }
    public void setSubject(String subject)         { this.subject = subject; }
    public void setType(String type)               { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setFilePath(String filePath)       { this.filePath = filePath; }

    // Convert to one line for saving to file
    // format: id|title|subject|type|description|filePath
    public String toFileString() {
        return id + "|" + title + "|" + subject + "|" + type + "|" + description + "|" + filePath;
    }

    @Override
    public String toString() {
        return "\n------------------------------" +
               "\nID          : " + id +
               "\nTitle       : " + title +
               "\nSubject     : " + subject +
               "\nType        : " + type +
               "\nDescription : " + description +
               "\nFile Path   : " + (filePath == null || filePath.isEmpty() ? "No file attached" : filePath) +
               "\n------------------------------";
    }
}