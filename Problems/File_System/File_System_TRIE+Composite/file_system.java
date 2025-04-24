// Base class for File System Node (Composite Pattern)
public abstract class FileSystemNode {
  // Name of the node
  private String name;
  // Map of child nodes
  private Map<String, FileSystemNode> children;
  // Timestamp for node creation
  private LocalDateTime createdAt;
  // Timestamp for the last modification
  private LocalDateTime modifiedAt;
  // Constructor to initialize the node with a name
  public FileSystemNode(String name) {
    this.name = name;
    this.children = new HashMap<>();
    this.createdAt = LocalDateTime.now();
    this.modifiedAt = LocalDateTime.now();
  }

  // Add child node
  public void addChild(String name, FileSystemNode child) {
    this.children.put(name, child);
    this.modifiedAt = LocalDateTime.now();
  }

  // Check if child exists
  public boolean hasChild(String name) {
    return this.children.containsKey(name);
  }

  // Get child node by name
  public FileSystemNode getChild(String name) {
    return this.children.get(name);
  }

  // Remove child node
  public boolean removeChild(String name) {
    if (hasChild(name)) {
      children.remove(name);
      return true;
    }
    return false;
  }

  // Abstract methods for node operations
  public abstract boolean isFile();
  public abstract void display(int depth);

  // Getters and Setters
  public String getName() {
    return name;
  }

  public Collection<FileSystemNode> getChildren() {
    return children.values();
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getModifiedAt() {
    return modifiedAt;
  }

  // Update the modification timestamp
  protected void updateModifiedTime() {
    this.modifiedAt = LocalDateTime.now();
  }
}

// File class representing individual files (Leaf)
public class File extends FileSystemNode {
  // Content of the file
  private String content;
  // File extension
  private String extension;

  // Constructor for file with name
  public File(String name) {
    super(name);
    this.extension = extractExtension(name);
  }

  // Extract file extension from name
  private String extractExtension(String name) {
    int dotIndex = name.lastIndexOf('.');
    return (dotIndex > 0) ? name.substring(dotIndex + 1) : "";
  }

  // Set content of the file
  public void setContent(String content) {
    this.content = content;
    updateModifiedTime();
  }

  // Get content of the file
  public String getContent() {
    return content;
  }

  @Override
  public boolean isFile() {
    return true;
  }

  @Override
  public void display(int depth) {
    // Example: For a file at path "/document/cwa_lld/requirements.txt" at depth 3
    // indent = "      " (6 spaces: depth 3 * 2 spaces per depth)
    // Output would be: "      📄 requirements.txt"
    // For our example, if depth is 3 (meaning this file is at the 3rd level)
    // Generate indent string of 6 spaces (3*2)
    String indent = " ".repeat(depth * 2);
    // Print the file with appropriate indentation and emoji
    // e.g., "      📄 requirements.txt"
    System.out.println(indent + "📄 " + getName());
  }
}


// Directory class representing directories (Composite)
public class Directory extends FileSystemNode {
  // Constructor for directory with name
  public Directory(String name) {
    super(name);
  }

  @Override
  public boolean isFile() {
    return false;
  }

  @Override
  public void display(int depth) {
    // Example: For a directory at path "/document/cwa_lld" at depth 2
    // indent = "    " (4 spaces: depth 2 * 2 spaces per depth)
    // Let's say it has 3 child items
    // Output would be: "    📁 cwa_lld (3 items)"
    // Then it will recursively display each child with depth 3


    // For our example, if depth is 2 (meaning this directory is at the 2nd level)
    // Generate indent string of 4 spaces (2*2)
    String indent = " ".repeat(depth * 2);

    // Print the directory name with appropriate indentation, emoji and number of children
    // e.g., "    📁 cwa_lld (3 items)"
    System.out.println(indent + "📁 " + getName() + " (" + getChildren().size() + " items)");
    // Then for each child (let's say we have "design_file_system" directory,
    // "requirements.txt" file, and "notes.md" file)
    // We recursively call display with depth+1 (3 in this case)
    // This will produce:
    //     "      📁 design_file_system (0 items)" (if empty directory)
    //     "      📄 requirements.txt"
    //     "      📄 notes.md"
    for (FileSystemNode child : getChildren()) {
      child.display(depth + 1);
    }
  }
}


// Main File System class implementing the trie structure
public class FileSystem {
  // Root directory
  private FileSystemNode root;

  // Constructor to initialize the file system with a root directory
  public FileSystem() {
    this.root = new Directory("/");
  }
  // path = "/document/cwa_lld/design_file_system"
  // Checking if path is not null, not empty, and starts with
  // Validate file path to be non-empty and properly formatted
  public boolean isValidFilePath(String path) {
    // Returns true because path meets all criteria
    return path != null && !path.isEmpty() && path.startsWith("/");
  }

  // Create a new path
  // path = "/document/cwa_lld/design_file_system"
  public boolean createPath(String path) {
    // Validate path
    // path is valid, so continue
    if (!isValidFilePath(path))
      return false;
    // Split path into components
    // pathComponents = ["", "document", "cwa_lld", "design_file_system"]
    String[] pathComponents = path.split("/");
    // Start from root
    // current = root directory "/"
    FileSystemNode current = root;
    // Traverse to the parent directory
    // We need to process: "document" and "cwa_lld" (stopping before the last component)
    for (int i = 1; i < pathComponents.length - 1; i++) {
      String component = pathComponents[i];
      if (component.isEmpty())
        continue; // Skip empty components
      // First iteration: component = "document"
      // Second iteration: component = "cwa_lld"
      if (!current.hasChild(component)) {
        // If "document" doesn't exist, create it
        // If "cwa_lld" doesn't exist, create it
        FileSystemNode newDir = new Directory(component);
        current.addChild(component, newDir);
      }
      FileSystemNode child = current.getChild(component);
      if (child.isFile()) {
        // If "document" or "cwa_lld" is a file, we cannot navigate through it
        // Return false in that case
        return false;
      }
      // Move to the next level
      // First iteration: current = "document" directory
      // Second iteration: current = "cwa_lld" directory
      current = child;
    }
    // Get the last component (file or directory name)
    // lastComponent = "design_file_system"
    String lastComponent = pathComponents[pathComponents.length - 1];
    if (lastComponent.isEmpty())
      return false;
    // Check if the component already exists
    // If "design_file_system" already exists under "cwa_lld", return false
    if (current.hasChild(lastComponent)) {
      return false;
    }
    // Create new node based on whether it's a file (has extension) or directory
    // "design_file_system" has no dot, so create as directory
    FileSystemNode newNode;
    if (lastComponent.contains(".")) {
      newNode = new File(lastComponent);
    } else {
      newNode = new Directory(lastComponent);
    }
    // Add the new node to the parent
    // Add "design_file_system" directory to "cwa_lld"
    current.addChild(lastComponent, newNode);
    return true;
  }

  // Helper method to get node at path
  // path = "/document/cwa_lld/design_file_system"
  private FileSystemNode getNode(String path) {
    // Check if path is valid
    // Path is valid, so continue
    if (!isValidFilePath(path))
      return null;
    // For root path
    // Path is not "/", so skip this
    if (path.equals("/"))
      return root;
    // Split path into components
    // pathComponents = ["", "document", "cwa_lld", "design_file_system"]
    String[] pathComponents = path.split("/");
    // Start from root
    // current = root directory "/"
    FileSystemNode current = root;
    // Traverse through the path
    // We need to process: "document", "cwa_lld", and "design_file_system"
    for (int i = 1; i < pathComponents.length; i++) {
      String component = pathComponents[i];
      if (component.isEmpty())
        continue; // Skip empty components
      // First iteration: component = "document"
      // Second iteration: component = "cwa_lld"
      // Third iteration: component = "design_file_system
      if (!current.hasChild(component)) {
        // If any component doesn't exist at its level, return null
        return null;
      }
      // Move to the next level
      // First iteration: current = "document" directory
      // Second iteration: current = "cwa_lld" directory
      // Third iteration: current = "design_file_system" directory
      current = current.getChild(component);
    }
    // Return the node found at the path
    // Returns the "design_file_system" directory node
    return current;
  }

  // Delete path
  public boolean deletePath(String path) {
    // path = "/document/cwa_lld/design_file_system"
    // Check if path is valid
    // Path is valid, so continue
    if (!isValidFilePath(path))
      return false;
    // Can't delete root
    // Path is not "/", so continue
    if (path.equals("/"))
      return false;
    // Get parent path
    // parentPath = "/document/cwa_lld"
    String parentPath = getParentPath(path);
    // Get the parent node
    // parent = "cwa_lld" directory node
    FileSystemNode parent = getNode(parentPath);
    // If parent doesn't exist or is a file, can't delete
    // Assuming parent exists and is a directory, continue
    if (parent == null || parent.isFile())
      return false;
    // Get the last component of the path
    // lastComponent = "design_file_system"
    String lastComponent = path.substring(path.lastIndexOf('/') + 1);
    // Check if the component exists
    // If "design_file_system" doesn't exist under "cwa_lld", return false
    if (!parent.hasChild(lastComponent)) {
      return false;
    }
    // Remove the child from the parent
    // Remove "design_file_system" from "cwa_lld"
    return parent.removeChild(lastComponent);
  }

  // Helper method to get parent path
  private String getParentPath(String path) {
    // path = "/document/cwa_lld/design_file_system"
    // Find the last slash
    // lastSlash = 19 (position of last slash before "design_file_system")
    int lastSlash = path.lastIndexOf('/');
    // If there's no slash or only the root slash, return root
    // lastSlash is 19, which is > 0, so continue
    if (lastSlash <= 0) {
      return "/";
    }
    // Return the substring up to the last slash
    // Returns "/document/cwa_lld"
    return path.substring(0, lastSlash);
  }

  // Display the entire file system structure
  public void display() {
    root.display(0);
  }

  // Set content for file
  public boolean setFileContent(String path, String content) {
    FileSystemNode node = getNode(path);
    if (node == null || !node.isFile())
      return false;
    File file = (File) node;
    file.setContent(content);
    return true;
  }

  // Get content from file
  public String getFileContent(String path) {
    FileSystemNode node = getNode(path);
    if (node == null || !node.isFile())
      return null;
    File file = (File) node;
    return file.getContent();
  }
}


// Client code to test the file system
public class FileSystemClient {
  public static void main(String[] args) {
    // Create a new file system instance
    FileSystem fs = new FileSystem();
    // Create a scanner to handle user input
    Scanner scanner = new Scanner(System.in);
    boolean isRunning = true; // Flag to control the program loop
    // Display instructions for the user
    System.out.println("File System Manager - Commands:");
    System.out.println("1. create <path> - Create a new path");
    System.out.println("2. write <path> <content> - Write content to a file");
    System.out.println("3. read <path> - Read content from a file");
    System.out.println("4. delete <path> - Delete a path");
    System.out.println("5. display - Show the entire file system structure");
    System.out.println("6. exit - Exit the program");
    // Main program loop to process commands
    while (isRunning) {
      System.out.print("nEnter command: ");
      String input = scanner.nextLine().trim(); // Read and trim user input
      String[] parts =
          input.split("s+", 3); // Split input into command, path, and possibly content
      if (parts.length == 0)
        continue; // Skip empty input
      String command = parts[0].toLowerCase(); // Get the command
      try {
        switch (command) {
          case "create":
            // Create a new path
            if (parts.length >= 2) {
              String path = parts[1]; // Extract path
              boolean isCreated = fs.createPath(path); // Attempt to create the path
              System.out.println(isCreated ? "Path created successfully" : "Failed to create path");
            } else {
              System.out.println("Usage: create <path>");
            }
            break;
          case "write":
            // Write content to a file
            if (parts.length >= 3) {
              String path = parts[1]; // Extract path
              String content = parts[2]; // Extract content
              boolean isWritten = fs.setFileContent(path, content); // Attempt to write content
              System.out.println(
                  isWritten ? "Content written successfully" : "Failed to write content");
            } else {
              System.out.println("Usage: write <path> <content>");
            }
            break;
          case "read":
            // Read content from a file
            if (parts.length >= 2) {
              String path = parts[1]; // Extract path
              String content = fs.getFileContent(path); // Attempt to read content
              if (content != null) {
                System.out.println("Content: " + content);
              } else {
                System.out.println("Failed to read content");
              }
            } else {
              System.out.println("Usage: read <path>");
            }
            break;
          case "delete":
            // Delete a specific path from the file system
            if (parts.length >= 2) {
              String path = parts[1]; // Extract path
              boolean isDeleted = fs.deletePath(path); // Attempt to delete the path
              System.out.println(isDeleted ? "Path deleted successfully" : "Failed to delete path");
            } else {
              System.out.println("Usage: delete <path>");
            }
            break;
          case "display":
            // Display the entire file system structure
            System.out.println("nFile System Structure:");
            fs.display();
            break;
          case "exit":
            // Exit the program
            isRunning = false;
            System.out.println("Exiting...");
            break;
          default:
            // Handle unknown commands
            System.out.println(
                "Unknown command. Available commands: create, write, read, delete, display, exit");
        }
      } catch (Exception e) {
        // Handle general exceptions
        System.out.println("Error: " + e.getMessage());
      }
    }
    // Close the scanner to release system resources
    scanner.close();
  }
}