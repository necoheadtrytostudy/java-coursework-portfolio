import java.sql.*;

public class GradeDatabaseDemo {
    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/student?serverTimezone=UTC"
            );

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        if (USER == null || PASSWORD == null) {
            System.out.println(
                    "Please set DB_USER and DB_PASSWORD environment variables."
            );
            return;
        }

        try (Connection connection =
                     DriverManager.getConnection(URL, USER, PASSWORD)) {

            createTable(connection);
            addGrade(connection, "2026001", "Amy", "Java", 92.0);
            addGrade(connection, "2026002", "Ben", "Java", 85.5);
            updateGrade(connection, "2026002", 88.0);
            printJavaGrades(connection);
            deleteFailingGrades(connection);

        } catch (SQLException exception) {
            System.err.println(
                    "Database operation failed: " + exception.getMessage()
            );
        }
    }

    private static void createTable(Connection connection)
            throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS grades (
                    student_id VARCHAR(20) PRIMARY KEY,
                    student_name VARCHAR(100) NOT NULL,
                    course_name VARCHAR(100) NOT NULL,
                    score DECIMAL(5, 2) NOT NULL
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static void addGrade(
            Connection connection,
            String id,
            String name,
            String course,
            double score
    ) throws SQLException {
        String sql = """
                INSERT INTO grades
                    (student_id, student_name, course_name, score)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    student_name = VALUES(student_name),
                    course_name = VALUES(course_name),
                    score = VALUES(score)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, course);
            statement.setDouble(4, score);
            statement.executeUpdate();
        }
    }

    private static void updateGrade(
            Connection connection,
            String id,
            double score
    ) throws SQLException {
        String sql = """
                UPDATE grades
                SET score = ?
                WHERE student_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setDouble(1, score);
            statement.setString(2, id);
            statement.executeUpdate();
        }
    }

    private static void printJavaGrades(Connection connection)
            throws SQLException {
        String sql = """
                SELECT student_id, student_name, score
                FROM grades
                WHERE course_name = ?
                ORDER BY score DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, "Java");

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    System.out.printf(
                            "%s | %-15s | %.2f%n",
                            results.getString("student_id"),
                            results.getString("student_name"),
                            results.getDouble("score")
                    );
                }
            }
        }
    }

    private static void deleteFailingGrades(Connection connection)
            throws SQLException {
        String sql = "DELETE FROM grades WHERE score < ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setDouble(1, 60.0);
            statement.executeUpdate();
        }
    }
}
