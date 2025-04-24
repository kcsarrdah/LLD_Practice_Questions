from datetime import datetime
from abc import ABC, abstractmethod
from typing import Dict, Collection, Optional
import os

# Base class for File System Node (Composite Pattern) Trie Node
# This class represents a node in the file system, which can be either a file or a directory.
# It contains methods to manage child nodes, check if it's a file or directory, and display its structure.
# It also maintains timestamps for creation and modification.
# The class is abstract and requires subclasses to implement specific behaviors for files and directories.
# The class uses the Composite design pattern to allow for a tree structure of files and directories.
class FileSystemNode(ABC):
    def __init__(self, name: str):
        self.name = name
        self.children: Dict[str, 'FileSystemNode'] = {}
        self.created_at = datetime.now()
        self.modified_at = datetime.now()

    def add_child(self, child: 'FileSystemNode') -> None:
        self.children[child.name] = child
        self.modified_at = datetime.now()

    def get_child(self, name: str) -> Optional['FileSystemNode']:
        return self.children.get(name)

    def remove_child(self, name: str) -> bool:
        if name in self.children:
            del self.children[name]
            return True
        return False

    @abstractmethod
    def is_file(self) -> bool:
        pass

    @abstractmethod
    def display(self, depth: int) -> None:
        pass

# File class representing individual files (Leaf)
class File(FileSystemNode):
    def __init__(self, name: str):
        super().__init__(name)
        self.content = ""

    def set_content(self, content: str) -> None:
        self.content = content
        self.modified_at = datetime.now()

    def get_content(self) -> str:
        return self.content

    def is_file(self) -> bool:
        return True

    def display(self, depth: int) -> None:
        indent = " " * (depth * 2)
        print(f"{indent} {self.name}")

# Directory class representing directories (Composite)
class Directory(FileSystemNode):
    def is_file(self) -> bool:
        return False

    def display(self, depth: int) -> None:
        indent = " " * (depth * 2)
        print(f"{indent}📁 {self.name} ({len(self.children)} items)")
        for child in self.children.values():
            child.display(depth + 1)

# Main File System class implementing the trie structure
class FileSystem:
    def __init__(self):
        self.root = Directory("/")

    def create_path(self, path: str) -> bool:
        if not path.startswith("/"):
            return False

        parts = [p for p in path.split("/") if p]
        current = self.root

        for part in parts[:-1]:
            if part not in current.children:
                current.add_child(Directory(part))
            current = current.get_child(part)
            if current.is_file():
                return False

        last = parts[-1]
        if last in current.children:
            return False

        node = File(last) if "." in last else Directory(last)
        current.add_child(node)
        return True

    def delete_path(self, path: str) -> bool:
        if path == "/" or not path.startswith("/"):
            return False

        parts = [p for p in path.split("/") if p]
        parent = self.root
        for part in parts[:-1]:
            parent = parent.get_child(part)
            if not parent or parent.is_file():
                return False

        return parent.remove_child(parts[-1])

    def set_file_content(self, path: str, content: str) -> bool:
        file = self._get_node(path)
        if isinstance(file, File):
            file.set_content(content)
            return True
        return False

    def get_file_content(self, path: str) -> Optional[str]:
        file = self._get_node(path)
        if isinstance(file, File):
            return file.get_content()
        return None

    def _get_node(self, path: str) -> Optional[FileSystemNode]:
        if not path.startswith("/"):
            return None

        current = self.root
        for part in [p for p in path.split("/") if p]:
            current = current.get_child(part)
            if not current:
                return None

        return current

    def display(self) -> None:
        self.root.display(0)

# Client code to test the file system
def main():
    fs = FileSystem()

    while True:
        try:
            user_input = input("\nEnter command: ").strip()
            if not user_input:
                continue

            parts = user_input.split(maxsplit=2)
            cmd = parts[0].lower()

            if cmd == "create" and len(parts) == 2:
                fs.create_path(parts[1])

            elif cmd == "write" and len(parts) == 3:
                fs.set_file_content(parts[1], parts[2])

            elif cmd == "read" and len(parts) == 2:
                content = fs.get_file_content(parts[1])
                if content is not None:
                    print(content)
                else:
                    print("Failed")

            elif cmd == "delete" and len(parts) == 2:
                fs.delete_path(parts[1])

            elif cmd == "display":
                fs.display()

            elif cmd == "exit":
                print("Exiting...")
                break

            else:
                print(f"Usage error or unknown command '{cmd}'")

        except Exception as e:
            print(f"Error: {str(e)}")

if __name__ == "__main__":
    main()

