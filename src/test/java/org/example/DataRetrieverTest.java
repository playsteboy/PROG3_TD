package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.example.ContinentEnum.EUROPA;
import static org.example.PlayerPositionEnum.*;

public class DataRetrieverTest {
    private DataRetriever dataRetriever;
    @BeforeEach
    public void setUp(){
        dataRetriever = new DataRetriever();
    }
    @Test
    public void findTeamByIdTest()throws Exception {
        Assertions.assertEquals("Real Madrid CF",dataRetriever.findTeamById(1).getName());
        Assertions.assertEquals("Inter Miami CF",dataRetriever.findTeamById(5).getName());
    }
    @Test
    public void findPlayersTest()throws Exception {
        Assertions.assertEquals(2,dataRetriever.findPlayers(1,2).size());
        Assertions.assertEquals(0,dataRetriever.findPlayers(3,5).size());
    }
    @Test
    public void findTeamsByPlayerNameTest()throws Exception {
        Assertions.assertEquals(3,dataRetriever.findTeamsByPlayerName("an").size());
    }
    @Test
    public void  findPlayersByCriteriaTest()throws Exception {
        Assertions.assertEquals("Jude Bellingham",dataRetriever. findPlayersByCriteria("ud",MIDF,"Madrid", EUROPA,1, 10 ).getFirst().getName());
    }
    @Test
    public void createPlayersTest()throws Exception {
        Assertions.assertThrows(RuntimeException.class, ()->{
            dataRetriever.createPlayers(List.of(new Player(
                    6,"Jude Bellingham",23,STR,null
            ),new Player(
                    7,"Pedri",24,MIDF,null
            )));
        });
        Assertions.assertEquals(2,dataRetriever.createPlayers(List.of(new Player(
                6,"Vini",25,STR,null
        ),new Player(
                7,"Pedri",24,MIDF,null
        ))));
    }
    @Test
    public void  saveTeamTest()throws Exception {
        Assertions.assertEquals(new Team(1,"Real Madrid CF",EUROPA,List.of(
                new Player(1,"Thibaut Courtois",32,GK,null),
                new Player(2,"Dani Carvajal",33,DEF,null),
                new Player(3,"Jude Bellingham",21,MIDF,null),
                new Player(6,"Vini",25,STR,null))),
                dataRetriever.saveTeam(new Team(1,"Real Madrid CF",EUROPA,List.of(
                new Player(1,"Thibaut Courtois",32,GK,null),
                new Player(2,"Dani Carvajal",33,DEF,null),
                new Player(3,"Jude Bellingham",21,MIDF,null),
                new Player(6,"Vini",25,STR,null)
        ))));
        Assertions.assertEquals(new Team(2,"FC Barcelona",EUROPA,List.of()),
                dataRetriever.saveTeam(new Team(2,"FC Barcelona",EUROPA,List.of()
                )));
    }

}
