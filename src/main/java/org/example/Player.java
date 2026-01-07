package org.example;

import javax.swing.text.Position;
import java.util.Objects;

public class Player {
    private int id;
    private String name;
    private int age;
    private PlayerPositionEnum position;
    private Team team;
    private Integer goal_nb;

    public Player(int id, String name, int age, PlayerPositionEnum position , Team team, Integer goal_nb) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.team = team;
        this.goal_nb = goal_nb;
    }

    public String getTeamName(){
        return team.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && age == player.age && Objects.equals(name, player.name) && position == player.position && Objects.equals(team, player.team) && Objects.equals(goal_nb, player.goal_nb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, position, team, goal_nb);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position=" + position +
                ", goal_nb=" + goal_nb +
                '}';
    }

    public Integer getGoal_nb() {
        return goal_nb;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public PlayerPositionEnum getPosition() {
        return position;
    }

    public Team getTeam() {
        return team;
    }
}
