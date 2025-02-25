package repository.book;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;
    public BookRepositoryMySQL(Connection connection) {
        this.connection=connection;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String author = resultSet.getString("author");
                String title = resultSet.getString("title");
                Date publicationDate = resultSet.getDate("publishedDate");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                Book book = new Book();
                book.setId(id);
                book.setAuthor(author);
                book.setTitle(title);
                book.setPublishedDate(publicationDate.toLocalDate());
                book.setStock(stock);
                book.setPrice(price);

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    @Override
    public Optional<Book> findByTitle(String title1) {
        String sql = "SELECT * FROM book WHERE title = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String author = resultSet.getString("author");
                String title = resultSet.getString("title");
                Date publishedDate = resultSet.getDate("publishedDate");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                Book book = new Book();

                book.setId(id);
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublishedDate(publishedDate.toLocalDate());
                book.setStock(stock);
                book.setPrice(price);

                return Optional.of(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Book> findById(Long id) {
        String query = "SELECT * FROM book WHERE id = ?";
        try  {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String author = resultSet.getString("author");
                String title = resultSet.getString("title");
                Date publicationDate = resultSet.getDate("publishedDate");
                int stock = resultSet.getInt("stock");
                double price = resultSet.getDouble("price");

                Book book = new Book();

                book.setId(id);
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublishedDate(publicationDate.toLocalDate());
                book.setStock(stock);
                book.setPrice(price);


                return Optional.of(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) {
        //String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book VALUES(null,?,?,?,?,?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setInt(4, book.getStock());
            preparedStatement.setDouble(5, book.getPrice());
            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted == 1;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(String title, int newStock) {
        String sql = "UPDATE book SET stock = ? WHERE title = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newStock);
            preparedStatement.setString(2, title);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void removeAll() {
        String query = "DELETE FROM book";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Book book) {
        String query="DELETE FROM book WHERE author = ? AND title = ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,book.getAuthor());
            preparedStatement.setString(2,book.getTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
