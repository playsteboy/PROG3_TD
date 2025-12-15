package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    public Team findTeamById(int id) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Team.id as t_id,Team.name as t_name,Team.continent as t_continent,Player.name as p_name,Player.id as p_id,Player.age as p_age,Player.position as p_position FROM Team inner join Player on Team.id=Player.id_team WHERE Team.id = ?"
        );
        ResultSet resultSet = statement.executeQuery();
        statement.setInt(1, id);
        List<Player> players = new ArrayList<>();
        while (resultSet.next()) {
            Team team = new Team(
                    resultSet.getInt("t_id"),
                    resultSet.getString("t_name"),
                    Team.Continent.valueOf(resultSet.getString("t_continent")),
                    players
            );
            Player player = new Player(
                    resultSet.getInt("p_id"),
                    resultSet.getString("p_name"),
                    resultSet.getInt("p_age"),
                    Player.Position.valueOf(resultSet.getString("p_position")),
                    team
            );
            players.add(player);
        }
        if(resultSet.next()) {
            connection.close();
            return  new Team(
                    resultSet.getInt("t_id"),
                    resultSet.getString("t_name"),
                    Team.Continent.valueOf(resultSet.getString("t_continent")),
                    players
            );

        }
        connection.close();
        return null;
    }
}
