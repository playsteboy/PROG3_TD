package org.example;

import javax.swing.text.Position;
import java.util.Objects;

public class Player {
    private int id;
    private String name;
    private int age;
    private Position position;
    public enum Position {
        GK,
        DEF,
        MIDF,
        STR
    }
    private Team team;

    public Player(int id, String name, int age, Position position , Team team) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.team = team;
    }

    public String getTeamName(){
        return team.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && age == player.age && Objects.equals(name, player.name) && position == player.position && Objects.equals(team, player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, position, team);
    }
}
