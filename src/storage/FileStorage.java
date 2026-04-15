package storage;
import model.Book;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements Storage<Book> {
    private static final String FILE_NAME = "books.txt";

    @Override
    public List<Book> load() throws Exception {
        List<Book> books = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String title = parts[0];
                    String author = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    String genre = parts[3];
                    String isbn = parts[4];
                    books.add(new Book(title, author, year, genre, isbn));
                }
            }
        }
        return books;
    }

    @Override
    public void save(List<Book> books) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        }
    }
}