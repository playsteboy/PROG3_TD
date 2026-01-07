package org.example;

import java.io.IOException;
import java.io.UncheckedIOException;
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
                "SELECT Team.id as t_id,Team.name as t_name,Team.continent as t_continent,Player.name as p_name,Player.goal_nb as p_goal_nb ,Player.id as p_id,Player.age as p_age,Player.position as p_position FROM Team left join Player on Team.id=Player.id_team WHERE Team.id = ?"
        );
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        Team team = null;
        List<Player> players = new ArrayList<>();
        while (resultSet.next()) {
            if(team == null){
                team = new Team(
                        resultSet.getInt("t_id"),
                        resultSet.getString("t_name"),
                        ContinentEnum.valueOf(resultSet.getString("t_continent")),
                        players
                );
            }
            if (resultSet.getInt("p_id") != 0) {
                Player player = new Player(
                        resultSet.getInt("p_id"),
                        resultSet.getString("p_name"),
                        resultSet.getInt("p_age"),
                        PlayerPositionEnum.valueOf(resultSet.getString("p_position")),
                        team,
                        resultSet.getObject("p_goal_nb", Integer.class)
                );
                players.add(player);
            }
        }
            connection.close();
            return team;
    }
    public List<Player> findPlayers(int page , int size) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        int offset = size * (page - 1);
        PreparedStatement statement = connection.prepareStatement(
                "SELECT Team.id as t_id,Team.name as t_name,Team.continent as t_continent,Player.name as p_name,Player.id as p_id,Player.goal_nb as p_goal_nb ,Player.age as p_age,Player.position as p_position FROM Player left join Team on Player.id_Team=Team.id limit ? offset ?"
        );
        List<Player> players = new ArrayList<>();
        statement.setInt(1, size);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Player player = new Player(
                    resultSet.getInt("p_id"),
                    resultSet.getString("p_name"),
                    resultSet.getInt("p_age"),
                    PlayerPositionEnum.valueOf(resultSet.getString("p_position")),
                    new Team(
                            resultSet.getInt("t_id"),
                            resultSet.getString("t_name"),
                            ContinentEnum.valueOf(resultSet.getString("t_continent")),
                            new ArrayList<>()
                    ),
                    resultSet.getObject("p_goal_nb", Integer.class)
            );
            players.add(player);
        }
        connection.close();
        return players;
    }

    public List<Player>  createPlayers(List<Player>  newPlayers) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        try{
            String select = "SELECT id, name, age, position, id_team,goal_nb FROM player WHERE id = ?";
            String insert = """
        INSERT INTO player(id, name, age, position, id_team,goal_nb)
        VALUES (?, ?, ?, ?, ?,?)
        """;
            connection.setAutoCommit(false);
            PreparedStatement selectStatement = connection.prepareStatement(select);
            PreparedStatement insertStatement = connection.prepareStatement(insert);
            for (Player player : newPlayers) {
                selectStatement.setInt(1, player.getId());
                ResultSet rs = selectStatement.executeQuery();
                insertStatement.setInt(1, player.getId());
                insertStatement.setString(2, player.getName());
                insertStatement.setInt(3, player.getAge());
                insertStatement.setString(4, player.getPosition().name());
                insertStatement.setInt(5, player.getGoal_nb());
                if(player.getTeam()!=null){
                    insertStatement.setInt(5, player.getTeam().getId());
                }
                else{
                    insertStatement.setNull(5, java.sql.Types.INTEGER);
                }


                insertStatement.executeUpdate();
            }
            connection.commit();
            connection.close();
            return newPlayers;
        }catch(Exception e){
            connection.rollback();
            throw new RuntimeException(e);
        }

    }

    public  Team  saveTeam(Team  teamToSave) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        String existsSql = "SELECT id, name, continent FROM team WHERE id = ?";
        String insertTeamSql =
                "INSERT INTO team(id, name, continent) VALUES (?, ?, ?) on conflict (id) do update ";
        String updateTeamSql =
                "UPDATE team SET name = ?, continent = ? WHERE id = ?";
        String updatePlayerSql =
                "UPDATE player SET id_team = ? WHERE id = ?";
        connection.setAutoCommit(false);
        PreparedStatement selectStatement = connection.prepareStatement(existsSql);
        PreparedStatement insertStatement = connection.prepareStatement(insertTeamSql);
        PreparedStatement updateTeamStatement = connection.prepareStatement(updateTeamSql);
        PreparedStatement updatePlayerStatement = connection.prepareStatement(updatePlayerSql);
        selectStatement.setInt(1, teamToSave.getId());
        ResultSet rs = selectStatement.executeQuery();
        if (rs.next()) {
            /*
            updateTeamStatement.setString(1, teamToSave.getName());
            updateTeamStatement.setString(2, teamToSave.getContinent().toString());
            updateTeamStatement.setInt(3, teamToSave.getId());
            updateTeamStatement.executeUpdate();
            */
            connection.rollback();
        }
        else{
            insertStatement.setInt(1, teamToSave.getId());
            insertStatement.setString(2, teamToSave.getName());
            insertStatement.setString(3, teamToSave.getContinent().toString());
            insertStatement.executeUpdate();
            for(Player player : teamToSave.getPlayers()){
                updatePlayerStatement.setInt(1, teamToSave.getId());
                updatePlayerStatement.setInt(2, player.getId());
                updatePlayerStatement.executeUpdate();
            }
            connection.commit();
        }
        /*
        for(Player player : teamToSave.getPlayers()){
                updatePlayerStatement.setInt(1, teamToSave.getId());
                updatePlayerStatement.setInt(2, player.getId());
                updatePlayerStatement.executeUpdate();
        }
        connection.commit();
        */
        connection.close();
        return teamToSave;
    }

    public List<Team>  findTeamsByPlayerName(String  playerName) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT Team.id as t_id,Team.name as t_name,Team.continent as t_continent FROM Team inner join Player on Team.id=Player.id_team WHERE Player.name ilike ?");
        statement.setString(1, "%"+playerName+"%");
        ResultSet resultSet = statement.executeQuery();
        List<Team> teams = new ArrayList<>();
        while (resultSet.next()) {
            Team team = new Team(
                    resultSet.getInt("t_id"),
                    resultSet.getString("t_name"),
                    ContinentEnum.valueOf(resultSet.getString("t_continent")),
                    new ArrayList<>()
            );
            teams.add(team);
        }
        connection.close();
        return teams;
    }

    public List<Player> findPlayersByCriteria(String playerName, PlayerPositionEnum  position,  String  teamName,  ContinentEnum continent, Integer goal_nb ,  int  page,  int  size) throws SQLException {
        int offset = (page - 1) * size;
        StringBuilder sql = new StringBuilder();
        String selectStatement = "select Team.id as t_id,Team.name as t_name,Team.continent as t_continent,Player.name as p_name,Player.goal_nb as p_goal_nb ,Player.id as p_id,Player.age as p_age,Player.position as p_position  from player left join Team on Player.id_team = Team.id where 1=1";
        sql.append(selectStatement);
        int index  = 1;
        if(playerName != null){
            sql.append(" and Player.name ilike ?");
        }
        if(position != null){
            sql.append(" and Player.position ilike ?");
        }
        if(teamName != null){
            sql.append(" and Team.name ilike ?");
        }
        if(continent != null){
            sql.append(" and Team.continent ilike ?");
        }
        if(goal_nb != null){
            sql.append(" and goal_nb = ?");
        }
        sql.append( " limit ? offset ?");
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        if(playerName != null){
            statement.setString(index++, "%"+playerName+"%");
        }
        if(position != null){
            statement.setString(index++, "%"+position.toString()+"%");
        }
        if(teamName != null){
            statement.setString(index++, "%"+teamName+"%");
        }
        if(continent != null){
            statement.setString(index++, "%"+continent.toString()+"%");
        }
        if(goal_nb != null){
            statement.setInt(index++, goal_nb);
        }
        statement.setInt(index++, size);
        statement.setInt(index, offset);
        ResultSet resultSet = statement.executeQuery();
        List<Player> players = new ArrayList<>();
        while (resultSet.next()) {
            Player player = new Player(
                    resultSet.getInt("p_id"),
                    resultSet.getString("p_name"),
                    resultSet.getInt("p_age"),
                    PlayerPositionEnum.valueOf(resultSet.getString("p_position")),
                    new Team(
                            resultSet.getInt("t_id"),
                            resultSet.getString("t_name"),
                            ContinentEnum.valueOf(resultSet.getString("t_continent")),
                            new ArrayList<>()
                    ),
                    resultSet.getObject("p_goal_nb", Integer.class)
            );
            players.add(player);
        }
        connection.close();
        return players;
    }
}
