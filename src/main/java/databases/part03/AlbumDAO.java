package databases.part03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databases.part02.Artist;

/**
 * Data Access Object for the Album table in the Chinook database.
 */
public class AlbumDAO {

    /**
     * The connection string used to connect to the database. You MUST use this
     * string when connecting to the database using JDBC. In the unit tests, this
     * field will be set to a different value.
     */
    private final String connectionString;

    /**
     * Creates a new AlbumDAO that uses the specified connection string to connect
     * to the database. For example: "jdbc:sqlite:data/Chinook_Sqlite.sqlite"
     *
     * @param jdbcConnection see https://www.baeldung.com/java-jdbc-url-format
     */
    public AlbumDAO(String jdbcConnection) {
        this.connectionString = jdbcConnection;
    }

    /**
     * Returns a list of all albums that have the specified artist as the artist.
     * If there are no albums for the specified artist, the list is empty.
     *
     * @param artist the artist whose albums to retrieve.
     * @return a list of all albums that have the specified artist as the artist,
     *         sorted by AlbumId in ascending order.
     */
    public List<Album> getAlbumsByArtist(Artist artist) {
        List<Album> albums = new ArrayList<>();
        if (artist == null) {
            return albums;
        }

        String sql = "SELECT AlbumId, Title, ArtistId FROM Album WHERE ArtistId = ? ORDER BY AlbumId ASC";

        try (Connection connection = DriverManager.getConnection(this.connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, artist.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long albumId = resultSet.getLong("AlbumId");
                    String title = resultSet.getString("Title");
                    long artistId = resultSet.getLong("ArtistId");

                    albums.add(new Album(albumId, title, artistId));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return albums;
    }

    /**
     * Adds the specified album to the database. Returns true if the album was
     * added successfully, false otherwise.
     *
     * @param album the album to add to the database.
     * @return true if the album was added successfully, false otherwise.
     */
    public boolean addAlbum(Album album) {
        if (album == null) {
            return false;
        }

        String sql = "INSERT INTO Album (Title, ArtistId) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(this.connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates the specified album in the database. Returns true if the album was
     * updated successfully, false otherwise.
     *
     * @param album the album to update in the database.
     * @return true if the album was updated successfully, false otherwise.
     */
    public boolean updateAlbum(Album album) {
        if (album == null) {
            return false;
        }

        String sql = "UPDATE Album SET Title = ?, ArtistId = ? WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(this.connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, album.getTitle());
            statement.setLong(2, album.getArtistId());
            statement.setLong(3, album.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes the specified album from the database. Returns true if the album was
     * deleted successfully, false otherwise.
     *
     * @param album the album to delete from the database.
     * @return true if the album was deleted successfully, false otherwise.
     */
    public boolean deleteAlbum(Album album) {
        if (album == null) {
            return false;
        }

        String sql = "DELETE FROM Album WHERE AlbumId = ?";

        try (Connection connection = DriverManager.getConnection(this.connectionString);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, album.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}