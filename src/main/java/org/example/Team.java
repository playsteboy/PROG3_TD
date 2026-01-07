package org.example;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

public class Team {
    private int id;
    private String name;
    private ContinentEnum continent;
    private List<Player> players;

    public Team(int id , String name, ContinentEnum continent , List<Player> players) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.players = players;
    }

    public Integer getPlayersCount(){
        return players.size();
    }
    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && Objects.equals(name, team.name) && continent == team.continent && Objects.equals(players, team.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, continent, players);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", continent=" + continent +
                ", players=" + players +
                '}';
    }

    public int getId() {
        return id;
    }

    public ContinentEnum getContinent() {
        return continent;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Integer getPlayersGoals(){
        Integer goals = 0;
        for(Player p : players){
            if(p.getGoal_nb()!=null){
                goals+=p.getGoal_nb();
            }
            else{
                throw new RuntimeException("a player' s goal number is unknown and it's impossible to calculate the team goal number");
            }
        }
        return goals;
    }
}