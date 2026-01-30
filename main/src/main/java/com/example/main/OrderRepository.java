package com.example.main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class OrderRepository {

    public void saveOrder(Integer userId, String customerLogin, String receipt, double total) throws Exception {
        String sql = """
            INSERT INTO orders(user_id, customer_login, receipt, total)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (userId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, userId);
            }

            ps.setString(2, customerLogin);
            ps.setString(3, receipt);
            ps.setDouble(4, total);

            int rows = ps.executeUpdate();
            System.out.println("✅ Zapisano zamówienie. Rows=" + rows + ", login=" + customerLogin + ", total=" + total);
        }
    }
    
    public List<String> getOrdersForUser(int userId) throws Exception {
    String sql = """
        SELECT total, created_at
        FROM orders
        WHERE user_id = ?
        ORDER BY created_at DESC
    """;

    List<String> list = new ArrayList<>();

    try (var con = DB.getConnection();
         var ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);

        try (var rs = ps.executeQuery()) {
            while (rs.next()) {
                double total = rs.getDouble("total");
                Timestamp ts = rs.getTimestamp("created_at");

                String row = String.format(
                        "%.0f zł — %s",
                        total,
                        ts.toLocalDateTime()
                );

                list.add(row);
            }
        }
    }
    return list;
}

}

