package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FootballPlayerAnalyzer {

    private final List<Player> players;

    private final static int MAX_RATING_DIFFERENCE = 3;

    /**
     * Loads the dataset from the given {@code reader}. The reader argument will not be null and a correct dataset of
     * the specified type can be read from it.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public FootballPlayerAnalyzer(Reader reader) {
        try (var bufferedReader = new BufferedReader(reader)) {
            this.players = bufferedReader.lines()
                    .skip(1)
                    .map(Player::of)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load dataset!", e);
        }
    }

    /**
     * Returns all players from the dataset in undefined order as an unmodifiable List. If the dataset is empty, returns
     * an empty List.
     *
     * @return the list of all players.
     */
    public List<Player> getAllPlayers() {
        return List.copyOf(this.players);
    }

    /**
     * Returns an unmodifiable set of all nationalities in the dataset. If the dataset is empty, returns an empty Set.
     *
     * @return the set of all nationalities
     */
    public Set<String> getAllNationalities() {
        return this.players.stream()
                .map(Player::nationality)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns the highest paid player from the provided nationality. If there are two or more players with equal
     * maximum wage, returns any of them.
     *
     * @param nationality the nationality of the player to return
     * @return the highest paid player
     * @throws IllegalArgumentException in case the provided nationality is null
     * @throws NoSuchElementException   in case there is no player with the provided nationality
     */
    public Player getHighestPaidPlayerByNationality(String nationality) {
        if(nationality == null) {
            throw new IllegalArgumentException("Nationality can not be null!");
        }

        return this.players.stream()
                .filter(e -> e.nationality().equals(nationality))
                .max(Comparator.comparingLong(Player::wageEuro))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Returns a breakdown of players by position. Note that some players can play in more than one position so they
     * should be present in more than one value Set. If no player plays in a given Position then that position should
     * not be present as a key in the map.
     *
     * @return a Map with key: a Position and value: the set of players in the dataset that can play in that Position,
     * in undefined order.
     */
    public Map<Position, Set<Player>> groupByPosition() {
        return this.players.stream()
                .flatMap(this::mapPlayerToPositionEntry)
                .collect(Collectors.toUnmodifiableMap(AbstractMap.SimpleEntry::getKey, this::newSetFromPlayerEntry, this::mergeSets));
    }

    private Stream<AbstractMap.SimpleEntry<Position, Player>> mapPlayerToPositionEntry(Player player) {
        return player.positions()
                .stream()
                .map(pos -> new AbstractMap.SimpleEntry<>(pos, player));
    }

    private Set<Player> newSetFromPlayerEntry(AbstractMap.SimpleEntry<Position, Player> playerSimpleEntry) {
        Set<Player> playerSet = new HashSet<>();
        playerSet.add(playerSimpleEntry.getValue());

        return playerSet;
    }

    private Set<Player> mergeSets(Set<Player> set1, Set<Player> set2) {
        set1.addAll(set2);

        return set1;
    }

    /**
     * Returns an Optional containing the top prospect player in the dataset that can play in the provided position and
     * that can be bought with the provided budget considering the player's value_euro. If no player can be bought with
     * the provided budget then return an empty Optional.
     * <p>
     * The player's prospect is calculated by the following formula: Prospect = (r + p) ÷ a where r is the player's
     * overall rating, p is the player's potential and a is the player's age
     *
     * @param position the position in which the player should be able to play
     * @param budget   the available budget for buying a player
     * @return an Optional containing the top prospect player
     * @throws IllegalArgumentException in case the provided position is null or the provided budget is negative
     */
    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if(position == null) {
            throw new IllegalArgumentException("Position can not be null!");
        }

        if(budget < 0) {
            throw new IllegalArgumentException("Budget can not be a negative number!");
        }

        return this.players.stream()
                .filter(p -> p.positions().contains(position))
                .filter(p -> p.valueEuro() <= budget)
                .max(Comparator.comparingDouble(this::calculatingProspectRating));
    }

    private double calculatingProspectRating(Player player) {
        return ((double) player.overallRating() + player.potential()) / player.age();
    }

    /**
     * Returns an unmodifiable set of players that are similar to the provided player. Two players are considered
     * similar if: 1. there is at least one position in which both of them can play 2. both players prefer the same foot
     * 3. their overall_rating measures differ by at most 3 (inclusive)
     * If the dataset contains the provided player, the player will be present in the returned result.
     *
     * @param player the player for whom similar players are retrieved. It may or may not be part of the dataset.
     * @return an unmodifiable set of similar players
     * @throws IllegalArgumentException if the provided player is null
     */
    public Set<Player> getSimilarPlayers(Player player) {
        if(player == null) {
            throw new IllegalArgumentException("Player can not be null!");
        }

        return this.players.stream()
                .filter(p -> canPlayWithTheSameFoot(p, player))
                .filter(p -> haveSimilarPosition(p, player))
                .filter(p -> isSimilarRating(p, player))
                .collect(Collectors.toUnmodifiableSet());
    }

    private boolean canPlayWithTheSameFoot(Player player1, Player player2) {
        return player1.preferredFoot() == player2.preferredFoot();
    }

    private boolean haveSimilarPosition(Player player1, Player player2) {
        return player1.positions().stream()
                .anyMatch(pos -> player2.positions().contains(pos));
    }

    private boolean isSimilarRating(Player player1, Player player2) {
        int rating1 = player1.overallRating();
        int rating2 = player2.overallRating();

        return (rating2 - MAX_RATING_DIFFERENCE) <= rating1 && rating1 <= (rating2 + MAX_RATING_DIFFERENCE);
    }

    /**
     * Returns an unmodifiable set of players whose full name contains the provided keyword (case-sensitive search)
     *
     * @param keyword the keyword that should be contained in player's full name
     * @return an unmodifiable set of players
     * @throws IllegalArgumentException if the provided keyword is null
     */
    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if(keyword == null) {
            throw new IllegalArgumentException("Keyword can not be null!");
        }

        return this.players.stream()
                .filter(p -> p.fullName().contains(keyword))
                .collect(Collectors.toUnmodifiableSet());
    }

}
