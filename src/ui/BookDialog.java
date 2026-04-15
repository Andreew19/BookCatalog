package ui;
import model.Book;
import javax.swing.*;
import java.awt.*;

public class BookDialog extends JDialog {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField yearField;
    private JTextField genreField;
    private JTextField isbnField;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean confirmed = false;
    private Book book;

    public BookDialog(JFrame parent, Book bookToEdit) {
        super(parent, bookToEdit == null ? "Добавить книгу" : "Редактировать книгу", true);
        this.book = bookToEdit != null ? copyBook(bookToEdit) : new Book();

        initUI();

        if (bookToEdit != null) {
            loadBookData();
        }

        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Панель ввода
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Поля ввода
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Название:*"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Автор:*"), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(20);
        inputPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Год издания:*"), gbc);
        gbc.gridx = 1;
        yearField = new JTextField(20);
        inputPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Жанр:*"), gbc);
        gbc.gridx = 1;
        genreField = new JTextField(20);
        inputPanel.add(genreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("ISBN:*"), gbc);
        gbc.gridx = 1;
        isbnField = new JTextField(20);
        inputPanel.add(isbnField, gbc);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Сохранить");
        cancelButton = new JButton("Отмена");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики
        saveButton.addActionListener(e -> saveBook());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadBookData() {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        yearField.setText(String.valueOf(book.getYear()));
        genreField.setText(book.getGenre());
        isbnField.setText(book.getIsbn());
    }

    private void saveBook() {
        // Валидация
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String yearStr = yearField.getText().trim();
        String genre = genreField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название книги", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите автора книги", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (yearStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите год издания", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректный год", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите жанр книги", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите ISBN книги", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Сохранение
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        book.setGenre(genre);
        book.setIsbn(isbn);

        confirmed = true;
        dispose();
    }

    private Book copyBook(Book original) {
        return new Book(
                original.getTitle(),
                original.getAuthor(),
                original.getYear(),
                original.getGenre(),
                original.getIsbn()
        );
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Book getBook() {
        return book;
    }
}