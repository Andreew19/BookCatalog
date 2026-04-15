package ui;

import model.Book;
import storage.FileStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private FileStorage storage;
    private List<Book> books;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JButton searchButton;

    public MainFrame() {
        storage = new FileStorage();
        loadData();
        initUI();
        refreshTable();
    }

    private void loadData() {
        try {
            books = storage.load();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            books = new java.util.ArrayList<>();
        }
    }

    private void saveData() {
        try {
            storage.save(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка сохранения данных: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initUI() {
        setTitle("Каталог книг");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Создание таблицы
        String[] columns = {"Название", "Автор", "Год издания", "Жанр", "ISBN"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(bookTable);

        // Панель поиска
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Поиск"));
        searchField = new JTextField(20);
        searchButton = new JButton("Найти");
        JButton resetButton = new JButton("Сброс");

        searchPanel.add(new JLabel("Название или автор:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        // Кнопки управления
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Добавить");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Добавление компонентов
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики событий
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        searchButton.addActionListener(e -> searchBooks());
        resetButton.addActionListener(e -> refreshTable());

        // Двойной клик для редактирования
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editBook();
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getYear(),
                    book.getGenre(),
                    book.getIsbn()
            });
        }
    }

    private void refreshTableWithList(List<Book> bookList) {
        tableModel.setRowCount(0);
        for (Book book : bookList) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getYear(),
                    book.getGenre(),
                    book.getIsbn()
            });
        }
    }

    private void addBook() {
        BookDialog dialog = new BookDialog(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Book newBook = dialog.getBook();
            books.add(newBook);
            refreshTable();
            saveData();
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Пожалуйста, выберите книгу для редактирования",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Book selectedBook = books.get(selectedRow);
        BookDialog dialog = new BookDialog(this, selectedBook);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Book editedBook = dialog.getBook();
            selectedBook.setTitle(editedBook.getTitle());
            selectedBook.setAuthor(editedBook.getAuthor());
            selectedBook.setYear(editedBook.getYear());
            selectedBook.setGenre(editedBook.getGenre());
            selectedBook.setIsbn(editedBook.getIsbn());
            refreshTable();
            saveData();
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Пожалуйста, выберите книгу для удаления",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить выбранную книгу?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            books.remove(selectedRow);
            refreshTable();
            saveData();
        }
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            refreshTable();
            return;
        }

        List<Book> foundBooks = new java.util.ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(searchText) ||
                    book.getAuthor().toLowerCase().contains(searchText)) {
                foundBooks.add(book);
            }
        }

        refreshTableWithList(foundBooks);

        if (foundBooks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Книги не найдены",
                    "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}