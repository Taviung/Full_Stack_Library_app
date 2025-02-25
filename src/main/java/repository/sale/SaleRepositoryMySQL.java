package repository.sale;

import model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaleRepositoryMySQL implements SaleRepository {
    private final Connection connection;

    public SaleRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Sale> findAllSale() {
        String sql = "SELECT * FROM sale;";

        List<Sale> sales = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong("id");
                String booktitle = resultSet.getString("book_title");
                int stock = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                Date sellDate = resultSet.getDate("saleDate");
                String seller = resultSet.getString("seller");

                Sale sale = new Sale();

                sale.setId(id);
                sale.setBookTitle(booktitle);
                sale.setQuantity(stock);
                sale.setTotalPrice(price);
                sale.setSeller(seller);
                sale.setSelldate(sellDate.toLocalDate());

                sales.add(sale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }

    @Override
    public Optional<Sale> findSaleByTitle(String title1) {
        String sql = "SELECT * FROM sale WHERE book_title = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title1);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String booktitle = resultSet.getString("book_title");
                int stock = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                Date sellDate = resultSet.getDate("sellDate");
                String seller = resultSet.getString("seller");

                Sale sale = new Sale();

                sale.setId(id);
                sale.setBookTitle(booktitle);
                sale.setQuantity(stock);
                sale.setTotalPrice(price);
                sale.setSeller(seller);
                sale.setSelldate(sellDate.toLocalDate());

                return Optional.of(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean saveSale(Sale sale) {
        String newSql = "INSERT INTO sale VALUES(null,?,?,?,?,?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, sale.getBookTitle());
            preparedStatement.setInt(2, sale.getQuantity());
            preparedStatement.setDouble(3, sale.getTotalPrice());
            preparedStatement.setString(5, sale.getSeller());
            preparedStatement.setDate(4,java.sql.Date.valueOf(sale.getSelldate()));

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSale(Sale sale) {
        String newSql = "DELETE FROM sale WHERE book_title = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, sale.getBookTitle());

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void removeAllSale() {
        String sql = "DELETE FROM sale WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
