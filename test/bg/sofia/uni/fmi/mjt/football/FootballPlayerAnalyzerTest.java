package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FootballPlayerAnalyzerTest {

    private static FootballPlayerAnalyzer footballPlayerAnalyzer;

    @BeforeAll
    static void setUp() {
        String csvData = "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot" + System.lineSeparator() +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left" + System.lineSeparator() +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right" + System.lineSeparator() +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right" + System.lineSeparator() +
                "L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right" + System.lineSeparator() +
                "K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right" + System.lineSeparator() +
                "V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right" + System.lineSeparator() +
                "K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right" + System.lineSeparator() +
                "S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right" + System.lineSeparator() +
                "M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right" + System.lineSeparator() +
                "E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right" + System.lineSeparator();

        footballPlayerAnalyzer = new FootballPlayerAnalyzer(new StringReader(csvData));
    }

    @Test
    void testGetAllPlayers() {
        List<Player> playerList = footballPlayerAnalyzer.getAllPlayers();

        assertEquals(10, playerList.size(),
                "Method getAllPlayers() should return a list with the correct size!");

        assertEquals("L. Messi", playerList.getFirst().name(),
                "Method getAllPlayers() should return correct first player!");

        assertEquals("Denmark", playerList.get(1).nationality(),
                "Method getAllPlayers() should return correct nationalities!");

        assertThrows(UnsupportedOperationException.class, () -> playerList.add(playerList.get(6)),
                "Method getAllPlayers() should return an unmodifiable list!");
    }

    @Test
    void testGetAllNationalities() {
        Set<String> nationalities = footballPlayerAnalyzer.getAllNationalities();

        assertEquals(8, nationalities.size(),
                "Method getAllNationalities() should return the correct number of nationalities!");

        assertTrue(nationalities.contains("Denmark"),
                "Method getAllNationalities() should return the correct set of nationalities!");

        assertFalse(nationalities.contains("Vatican"),
                "Method getAllNationalities() should return the correct set of nationalities!");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityIfNationalityIsNull() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality(null),
                "Method getHighestPaidPlayerByNationality() can not receive a null nationality!");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityIfNationalityIsNotPresent() {
        assertThrows(NoSuchElementException.class, () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Vatican"),
                "Method getHighestPaidPlayerByNationality() can not receive a nationality that is not present!");
    }

    @Test
    void testGetHighestPaidPlayerByNationality() {
        assertEquals("P. Pogba", footballPlayerAnalyzer.getHighestPaidPlayerByNationality("France").name(),
                "Method getHighestPaidPlayerByNationality() should return the correct player's name!");

        assertEquals("L. Insigne", footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Italy").name(),
                "Method getHighestPaidPlayerByNationality() should return the correct player's name!");
    }

    @Test
    void testGroupByPosition() {
        Map<Position, Set<Player>> groupPositions = footballPlayerAnalyzer.groupByPosition();

        assertEquals(5, groupPositions.get(Position.ST).size(),
                "Method groupByPosition() should return correct positions!");

        assertEquals(2, groupPositions.get(Position.CAM).size(),
                "Method groupByPosition() should return correct positions!");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetThrowsExceptionForInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(null, 100L),
                "Method getTopProspectPlayerForPositionInBudget() can not receive a null position!");

        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.CB, -100L),
                "Method getTopProspectPlayerForPositionInBudget() can not receive a negative budget!");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudget() {
        assertEquals("C. Eriksen", footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.CAM, 70000000L).get().name(),
                "Method getTopProspectPlayerForPositionInBudget() should return the correct player!");
    }

    @Test
    void testGetSimilarPlayersThrowsExceptionIfPlayerIsNull() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getSimilarPlayers(null),
                "Method getSimilarPlayers() can not receive a null player!");
    }

    @Test
    void testGetSimilarPlayers() {
        Player pogba = footballPlayerAnalyzer.getAllPlayers().get(2);
        Set<Player> similiar = footballPlayerAnalyzer.getSimilarPlayers(pogba);

        assertEquals(2, similiar.size(),
                "Method getSimilarPlayers() should return correct players!");

        assertTrue(similiar.stream().anyMatch(p -> p.name().equals("C. Eriksen")),
                "Method getSimilarPlayers() should return correct players!");
    }

    @Test
    void testGetPlayersByFullNameKeywordThrowsExceptionIfKeywordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getPlayersByFullNameKeyword(null),
                "Method getPlayersByFullNameKeyword() can not receive a null keyword!");
    }

    @Test
    void testGetPlayersByFullNameKeyword() {
        assertEquals(1, footballPlayerAnalyzer.getPlayersByFullNameKeyword("Sergio").size(),
                "Method getPlayersByFullNameKeyword() should return correct players!");
    }

}
