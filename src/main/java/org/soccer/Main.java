package org.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.soccer.enums.PlayerPosition;
import org.soccer.models.League;
import org.soccer.models.Match;
import org.soccer.models.Player;
import org.soccer.models.Team;
import org.soccer.repositories.LeagueRepository;
import org.soccer.repositories.MatchRepository;
import org.soccer.repositories.PlayerRepository;
import org.soccer.repositories.TeamRepository;
import org.soccer.services.LeagueService;
import org.soccer.services.MatchService;
import org.soccer.services.PlayerService;
import org.soccer.services.TeamService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static Scanner scanner;
    
    // Services
    private static PlayerService playerService;
    private static TeamService teamService;
    private static LeagueService leagueService;
    private static MatchService matchService;

    public static void main(String[] args) {
        try {
            // Initialize JPA
            initializeJPA();
            
            // Initialize services
            initializeServices();
            
            // Initialize scanner
            scanner = new Scanner(System.in);
            
            // Run main menu
            runMainMenu();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up
            if (scanner != null) {
                scanner.close();
            }
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    private static void initializeJPA() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    private static void initializeServices() {
        // Initialize repositories
        PlayerRepository playerRepository = new PlayerRepository(em);
        TeamRepository teamRepository = new TeamRepository(em);
        LeagueRepository leagueRepository = new LeagueRepository(em);
        MatchRepository matchRepository = new MatchRepository(em);
        
        // Initialize services
        playerService = new PlayerService(playerRepository);
        teamService = new TeamService(teamRepository);
        leagueService = new LeagueService(leagueRepository);
        matchService = new MatchService(matchRepository);
        
        // Add mocked Spanish league data
        addMockedSpanishLeague();
    }

    private static void runMainMenu() {
        while (true) {
            System.out.println("\n=== SOCCER LEAGUE SIMULATOR ===");
            System.out.println("1) Add League");
            System.out.println("2) Add Team");
            System.out.println("3) Add Player");
            System.out.println("4) Start League Simulation");
            System.out.println("5) View League Table");
            System.out.println("6) View All Data");
            System.out.println("7) Exit");
            System.out.print("Choose an option: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addLeague();
                    break;
                case 2:
                    addTeam();
                    break;
                case 3:
                    addPlayer();
                    break;
                case 4:
                    startLeagueSimulation();
                    break;
                case 5:
                    viewLeagueTable();
                    break;
                case 6:
                    viewAllData();
                    break;
                case 7:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addLeague() {
        System.out.println("\n=== ADD LEAGUE ===");
        System.out.print("Enter league name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("League name cannot be empty!");
            return;
        }
        
        League league = new League(name);
        leagueService.createLeague(league);
        System.out.println("League '" + name + "' created successfully!");

        // Guided setup: enforce 4-10 teams, each with 4-5 players
        System.out.println("\nNow let's add teams to '" + name + "'.");
        while (true) {
            List<Team> currentTeams = teamService.getTeamsByLeague(league.getId());
            int teamCount = currentTeams.size();
            System.out.println("Current teams: " + teamCount + " (min 4, max 10)");

            if (teamCount >= 10) {
                System.out.println("This league already has the maximum number of teams (10).");
                break;
            }

            if (teamCount < 4) {
                System.out.println("You must add at least " + (4 - teamCount) + " more team(s).");
            } else {
                System.out.print("Add another team? (y/n): ");
                String addMore = scanner.nextLine().trim();
                if (addMore.equalsIgnoreCase("n")) {
                    break;
                }
            }

            // Collect team info
            System.out.print("Enter team name: ");
            String teamName = scanner.nextLine().trim();
            if (teamName.isEmpty()) {
                System.out.println("Team name cannot be empty!");
                continue;
            }
            System.out.print("Enter coach name: ");
            String coachName = scanner.nextLine().trim();
            if (coachName.isEmpty()) {
                System.out.println("Coach name cannot be empty!");
                continue;
            }

            Team team = new Team(teamName, coachName);
            team.setLeague(league);
            teamService.createTeam(team);
            System.out.println("Team '" + teamName + "' added to '" + league.getName() + "'.");

            // Guided players: enforce 4-5 players
            while (true) {
                List<Player> players = playerService.getPlayersByTeam(team.getId());
                int count = players.size();
                System.out.println("Players in '" + team.getName() + "': " + count + " (min 4, max 5)");
                if (count >= 5) {
                    System.out.println("This team already has the maximum number of players (5).");
                    break;
                }
                if (count < 4) {
                    System.out.println("You must add at least " + (4 - count) + " more player(s).");
                } else {
                    System.out.print("Add another player? (y/n): ");
                    String morePlayers = scanner.nextLine().trim();
                    if (morePlayers.equalsIgnoreCase("n")) {
                        break;
                    }
                }

                // Player details
                System.out.print("Enter player name: ");
                String playerName = scanner.nextLine().trim();
                if (playerName.isEmpty()) {
                    System.out.println("Player name cannot be empty!");
                    continue;
                }

                System.out.println("Available positions:");
                PlayerPosition[] positions = PlayerPosition.values();
                for (int i = 0; i < positions.length; i++) {
                    System.out.println((i + 1) + ") " + positions[i]);
                }
                System.out.print("Select position (number): ");
                int positionChoice = getIntInput();
                if (positionChoice < 1 || positionChoice > positions.length) {
                    System.out.println("Invalid position selection!");
                    continue;
                }
                PlayerPosition selectedPosition = positions[positionChoice - 1];

                System.out.print("Enter shirt number: ");
                int shirtNumber = getIntInput();
                if (shirtNumber < 1 || shirtNumber > 99) {
                    System.out.println("Shirt number must be between 1 and 99!");
                    continue;
                }
                System.out.print("Enter age: ");
                int age = getIntInput();
                if (age < 16 || age > 50) {
                    System.out.println("Age must be between 16 and 50!");
                    continue;
                }

                Player player = new Player(playerName, selectedPosition, shirtNumber, age);
                player.setTeam(team);
                playerService.createPlayer(player);
                System.out.println("Player '" + playerName + "' added to '" + team.getName() + "'.");
            }
        }
        // Final validation
        int finalTeamCount = teamService.getTeamsByLeague(league.getId()).size();
        if (finalTeamCount < 4) {
            System.out.println("League created but missing required teams. Please add at least 4 teams later from the menu.");
        } else {
            System.out.println("League '" + name + "' setup complete.");
        }
    }

    private static void addTeam() {
        System.out.println("\n=== ADD TEAM ===");
        
        // Show available leagues
        List<League> leagues = leagueService.getAllLeagues();
        if (leagues.isEmpty()) {
            System.out.println("No leagues available. Please create a league first!");
            return;
        }
        
        System.out.println("Available leagues:");
        for (int i = 0; i < leagues.size(); i++) {
            System.out.println((i + 1) + ") " + leagues.get(i).getName());
        }
        
        System.out.print("Select league (number): ");
        int leagueChoice = getIntInput();
        
        if (leagueChoice < 1 || leagueChoice > leagues.size()) {
            System.out.println("Invalid league selection!");
            return;
        }
        
        League selectedLeague = leagues.get(leagueChoice - 1);
        
        // Enforce league max teams
        int existingTeams = teamService.getTeamsByLeague(selectedLeague.getId()).size();
        if (existingTeams >= 10) {
            System.out.println("This league already has the maximum number of teams (10). Cannot add more.");
            return;
        }

        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine().trim();
        
        if (teamName.isEmpty()) {
            System.out.println("Team name cannot be empty!");
            return;
        }
        
        System.out.print("Enter coach name: ");
        String coachName = scanner.nextLine().trim();
        
        if (coachName.isEmpty()) {
            System.out.println("Coach name cannot be empty!");
            return;
        }
        
        Team team = new Team(teamName, coachName);
        team.setLeague(selectedLeague);
        teamService.createTeam(team);
        System.out.println("Team '" + teamName + "' added to '" + selectedLeague.getName() + "' successfully!");

        // Optional guided player addition to satisfy 4-5 constraint
        while (true) {
            List<Player> players = playerService.getPlayersByTeam(team.getId());
            int count = players.size();
            System.out.println("Players in '" + team.getName() + "': " + count + " (min 4, max 5)");
            if (count >= 5) {
                System.out.println("This team already has the maximum number of players (5).");
                break;
            }
            System.out.print("Add a player now? (y/n): ");
            String answer = scanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("y")) {
                break;
            }

            // Player details
            System.out.print("Enter player name: ");
            String playerName = scanner.nextLine().trim();
            if (playerName.isEmpty()) {
                System.out.println("Player name cannot be empty!");
                continue;
            }
            System.out.println("Available positions:");
            PlayerPosition[] positions = PlayerPosition.values();
            for (int i = 0; i < positions.length; i++) {
                System.out.println((i + 1) + ") " + positions[i]);
            }
            System.out.print("Select position (number): ");
            int positionChoice = getIntInput();
            if (positionChoice < 1 || positionChoice > positions.length) {
                System.out.println("Invalid position selection!");
                continue;
            }
            PlayerPosition selectedPosition = positions[positionChoice - 1];
            System.out.print("Enter shirt number: ");
            int shirtNumber = getIntInput();
            if (shirtNumber < 1 || shirtNumber > 99) {
                System.out.println("Shirt number must be between 1 and 99!");
                continue;
            }
            System.out.print("Enter age: ");
            int age = getIntInput();
            if (age < 16 || age > 50) {
                System.out.println("Age must be between 16 and 50!");
                continue;
            }
            Player player = new Player(playerName, selectedPosition, shirtNumber, age);
            player.setTeam(team);
            playerService.createPlayer(player);
            System.out.println("Player '" + playerName + "' added to '" + team.getName() + "'.");
        }
    }

    private static void addPlayer() {
        System.out.println("\n=== ADD PLAYER ===");
        
        // Show available teams
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("No teams available. Please create a team first!");
            return;
        }
        
        System.out.println("Available teams:");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.println((i + 1) + ") " + team.getName() + " (" + team.getLeague().getName() + ")");
        }
        
        System.out.print("Select team (number): ");
        int teamChoice = getIntInput();
        
        if (teamChoice < 1 || teamChoice > teams.size()) {
            System.out.println("Invalid team selection!");
            return;
        }
        
        Team selectedTeam = teams.get(teamChoice - 1);
        // Enforce max 5 players per team
        int existingPlayers = playerService.getPlayersByTeam(selectedTeam.getId()).size();
        if (existingPlayers >= 5) {
            System.out.println("This team already has the maximum number of players (5). Cannot add more.");
            return;
        }
        
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Player name cannot be empty!");
            return;
        }
        
        // Show positions
        System.out.println("Available positions:");
        PlayerPosition[] positions = PlayerPosition.values();
        for (int i = 0; i < positions.length; i++) {
            System.out.println((i + 1) + ") " + positions[i]);
        }
        
        System.out.print("Select position (number): ");
        int positionChoice = getIntInput();
        
        if (positionChoice < 1 || positionChoice > positions.length) {
            System.out.println("Invalid position selection!");
            return;
        }
        
        PlayerPosition selectedPosition = positions[positionChoice - 1];
        
        System.out.print("Enter shirt number: ");
        int shirtNumber = getIntInput();
        
        if (shirtNumber < 1 || shirtNumber > 99) {
            System.out.println("Shirt number must be between 1 and 99!");
            return;
        }
        
        System.out.print("Enter age: ");
        int age = getIntInput();
        
        if (age < 16 || age > 50) {
            System.out.println("Age must be between 16 and 50!");
            return;
        }
        
        Player player = new Player(playerName, selectedPosition, shirtNumber, age);
        player.setTeam(selectedTeam);
        playerService.createPlayer(player);
        System.out.println("Player '" + playerName + "' added to '" + selectedTeam.getName() + "' successfully!");
    }

    private static void startLeagueSimulation() {
        System.out.println("\n=== START LEAGUE SIMULATION ===");
        
        // Show available leagues
        List<League> leagues = leagueService.getAllLeagues();
        if (leagues.isEmpty()) {
            System.out.println("No leagues available. Please create a league first!");
            return;
        }
        
        System.out.println("Available leagues:");
        for (int i = 0; i < leagues.size(); i++) {
            League league = leagues.get(i);
            List<Team> teams = teamService.getTeamsByLeague(league.getId());
            System.out.println((i + 1) + ") " + league.getName() + " (" + teams.size() + " teams)");
        }
        
        System.out.print("Select league to simulate (number): ");
        int leagueChoice = getIntInput();
        
        if (leagueChoice < 1 || leagueChoice > leagues.size()) {
            System.out.println("Invalid league selection!");
            return;
        }
        
        League selectedLeague = leagues.get(leagueChoice - 1);
        List<Team> teams = teamService.getTeamsByLeague(selectedLeague.getId());
        
        if (teams.size() < 2) {
            System.out.println("League needs at least 2 teams to simulate!");
            return;
        }
        
        // Validate constraints before simulation
        if (teams.size() < 4 || teams.size() > 10) {
            System.out.println("League must have between 4 and 10 teams to simulate.");
            return;
        }
        for (Team t : teams) {
            int playerCount = playerService.getPlayersByTeam(t.getId()).size();
            if (playerCount < 4 || playerCount > 5) {
                System.out.println("Team '" + t.getName() + "' must have between 4 and 5 players. Current: " + playerCount);
                return;
            }
        }

        System.out.println("\nStarting simulation for '" + selectedLeague.getName() + "' with " + teams.size() + " teams...");

        // Build single round-robin fixtures: each team plays others once
        List<List<Team[]>> fixtures = buildRoundRobinFixtures(teams);
        int totalRounds = fixtures.size();

        for (int round = 1; round <= totalRounds; round++) {
            System.out.println("\n=== ROUND " + round + " ===");
            
            // Create matches for this round from fixtures
            List<Match> roundMatches = new ArrayList<>();
            List<Team[]> pairs = fixtures.get(round - 1);
            boolean homeAwayFlip = (round % 2 == 0);
            for (Team[] pair : pairs) {
                Team homeTeam = homeAwayFlip ? pair[1] : pair[0];
                Team awayTeam = homeAwayFlip ? pair[0] : pair[1];
                Match match = new Match(homeTeam, awayTeam, selectedLeague);
                matchService.createMatch(match);
                roundMatches.add(match);
            }
            
            // Simulate each match
            for (Match match : roundMatches) {
                System.out.println("\n" + match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName());
                
                System.out.print("Press Enter to simulate this match...");
                scanner.nextLine();
                
                // Simulate match
                simulateMatch(match);
                
                // Display result
                System.out.println("Result: " + match.getHomeTeam().getName() + " " + 
                                 match.getHomeScore() + " - " + match.getAwayScore() + " " + 
                                 match.getAwayTeam().getName());
            }
            
            // Show round results
            System.out.println("\n=== ROUND " + round + " RESULTS ===");
            viewLeagueTable(selectedLeague.getId());
            
            if (round < totalRounds) {
                System.out.print("Press Enter to continue to next round, or type 'exit' to stop: ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Simulation stopped.");
                    return;
                }
            }
        }
        
        System.out.println("\n=== LEAGUE SIMULATION COMPLETED ===");
        System.out.println("Final League Table:");
        viewLeagueTable(selectedLeague.getId());
    }

    private static List<List<Team[]>> buildRoundRobinFixtures(List<Team> inputTeams) {
        List<Team> teams = new ArrayList<>(inputTeams);
        // If odd number of teams, add a bye (null)
        if (teams.size() % 2 != 0) {
            teams.add(null);
        }
        int n = teams.size();
        int rounds = n - 1;
        List<List<Team[]>> allRounds = new ArrayList<>();

        for (int r = 0; r < rounds; r++) {
            List<Team[]> pairs = new ArrayList<>();
            for (int i = 0; i < n / 2; i++) {
                Team t1 = teams.get(i);
                Team t2 = teams.get(n - 1 - i);
                if (t1 != null && t2 != null) {
                    pairs.add(new Team[] { t1, t2 });
                }
            }
            allRounds.add(pairs);
            // Rotate teams: keep first fixed, move last to index 1
            Team last = teams.remove(n - 1);
            teams.add(1, last);
        }
        return allRounds;
    }

    private static void simulateMatch(Match match) {
        // Start match
        matchService.startMatch(match.getId());
        
        // Simulate match events
        int homeGoals = 0;
        int awayGoals = 0;
        List<String> goalScorers = new ArrayList<>();
        
        // Get players for both teams
        List<Player> homePlayers = playerService.getPlayersByTeam(match.getHomeTeam().getId());
        List<Player> awayPlayers = playerService.getPlayersByTeam(match.getAwayTeam().getId());
        
        // Filter forwards and midfielders (more likely to score)
        List<Player> homeAttackers = homePlayers.stream()
            .filter(p -> p.getPosition() == PlayerPosition.FORWARD || p.getPosition() == PlayerPosition.MIDFIELDER)
            .collect(Collectors.toList());
        List<Player> awayAttackers = awayPlayers.stream()
            .filter(p -> p.getPosition() == PlayerPosition.FORWARD || p.getPosition() == PlayerPosition.MIDFIELDER)
            .collect(Collectors.toList());
        
        // Simulate 90 minutes with random events
        for (int minute = 1; minute <= 90; minute++) {
            // Random goal chance (5% per minute)
            if (ThreadLocalRandom.current().nextDouble() < 0.05) {
                // Determine which team scores (50/50 chance)
                if (ThreadLocalRandom.current().nextBoolean()) {
                    homeGoals++;
                    // Select random attacker from home team
                    Player scorer = homeAttackers.get(ThreadLocalRandom.current().nextInt(homeAttackers.size()));
                    scorer.scoreGoal(); // Update player's goal count
                    playerService.updatePlayer(scorer.getId(), scorer);
                    goalScorers.add(scorer.getName() + " (" + minute + "')");
                    System.out.println("GOAL! " + scorer.getName() + " scores for " + match.getHomeTeam().getName() + "! (" + minute + "')");
                } else {
                    awayGoals++;
                    // Select random attacker from away team
                    Player scorer = awayAttackers.get(ThreadLocalRandom.current().nextInt(awayAttackers.size()));
                    scorer.scoreGoal(); // Update player's goal count
                    playerService.updatePlayer(scorer.getId(), scorer);
                    goalScorers.add(scorer.getName() + " (" + minute + "')");
                    System.out.println("GOAL! " + scorer.getName() + " scores for " + match.getAwayTeam().getName() + "! (" + minute + "')");
                }
            }
            
            // Update match time
            match.setMatchTime(minute);
        }
        
        // Update final score
        matchService.updateMatchScore(match.getId(), homeGoals, awayGoals);
        
        // Finish match
        matchService.finishMatch(match.getId());
        
        // Award points
        if (homeGoals > awayGoals) {
            // Home team wins
            teamService.addPointsToTeam(match.getHomeTeam().getId(), 3);
            System.out.println(match.getHomeTeam().getName() + " wins! (3 points)");
        } else if (awayGoals > homeGoals) {
            // Away team wins
            teamService.addPointsToTeam(match.getAwayTeam().getId(), 3);
            System.out.println(match.getAwayTeam().getName() + " wins! (3 points)");
        } else {
            // Draw
            teamService.addPointsToTeam(match.getHomeTeam().getId(), 1);
            teamService.addPointsToTeam(match.getAwayTeam().getId(), 1);
            System.out.println("It's a draw! (1 point each)");
        }
    }

    private static void viewLeagueTable() {
        System.out.println("\n=== LEAGUE TABLES ===");
        
        List<League> leagues = leagueService.getAllLeagues();
        if (leagues.isEmpty()) {
            System.out.println("No leagues available.");
            return;
        }
        
        for (League league : leagues) {
            viewLeagueTable(league.getId());
        }
    }

    private static void viewLeagueTable(Long leagueId) {
        League league = leagueService.getLeagueById(leagueId);
        List<Team> teams = teamService.getTeamsByLeague(leagueId);
        
        // Sort teams by points (descending)
        teams.sort((t1, t2) -> Integer.compare(t2.getTotalPoints(), t1.getTotalPoints()));
        
        System.out.println("\n" + league.getName() + " - League Table");
        System.out.println("Pos | Team | Coach | Points");
        System.out.println("----|------|-------|-------");
        
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.printf("%2d  | %-15s | %-15s | %3d%n", 
                            (i + 1), 
                            team.getName(), 
                            team.getCoachName(), 
                            team.getTotalPoints());
        }
    }

    private static void viewAllData() {
        System.out.println("\n=== ALL DATA ===");
        
        // Display all leagues
        List<League> leagues = leagueService.getAllLeagues();
        System.out.println("Leagues:");
        for (League league : leagues) {
            System.out.println("  - " + league.getName() + " (ID: " + league.getId() + ")");
        }
        
        // Display all teams
        List<Team> teams = teamService.getAllTeams();
        System.out.println("\nTeams:");
        for (Team team : teams) {
            System.out.println("  - " + team.getName() + " (Coach: " + team.getCoachName() + 
                             ", League: " + team.getLeague().getName() + 
                             ", Points: " + team.getTotalPoints() + ")");
        }
        
        // Display all players
        List<Player> players = playerService.getAllPlayers();
        System.out.println("\nPlayers:");
        for (Player player : players) {
            System.out.println("  - " + player.getName() + " (" + player.getPosition() + 
                             ", Team: " + player.getTeam().getName() + 
                             ", Age: " + player.getAge() + 
                             ", Goals: " + player.getGoalsScored() + ")");
        }
        
        // Display all matches
        List<Match> matches = matchService.getAllMatches();
        System.out.println("\nMatches:");
        for (Match match : matches) {
            System.out.println("  - " + match.getHomeTeam().getName() + " " + match.getHomeScore() + 
                             " - " + match.getAwayScore() + " " + match.getAwayTeam().getName() + 
                             " (" + match.getStatus() + ")");
        }
    }

    private static void addMockedSpanishLeague() {
        // Check if Spanish league already exists
        List<League> existingLeagues = leagueService.getAllLeagues();
        for (League league : existingLeagues) {
            if (league.getName().equals("La Liga")) {
                return; // Spanish league already exists
            }
        }
        
        // Create Spanish League
        League laLiga = new League("La Liga");
        leagueService.createLeague(laLiga);
        
        // Create Spanish teams
        Team realMadrid = new Team("Real Madrid", "Carlo Ancelotti");
        Team barcelona = new Team("Barcelona", "Xavi Hernández");
        Team atleticoMadrid = new Team("Atlético Madrid", "Diego Simeone");
        Team sevilla = new Team("Sevilla", "Quique Sánchez Flores");
        
        realMadrid.setLeague(laLiga);
        barcelona.setLeague(laLiga);
        atleticoMadrid.setLeague(laLiga);
        sevilla.setLeague(laLiga);
        
        teamService.createTeam(realMadrid);
        teamService.createTeam(barcelona);
        teamService.createTeam(atleticoMadrid);
        teamService.createTeam(sevilla);
        
        // Create players for Real Madrid
        Player vinicius = new Player("Vinícius Júnior", PlayerPosition.FORWARD, 7, 23);
        Player benzema = new Player("Karim Benzema", PlayerPosition.FORWARD, 9, 36);
        Player modric = new Player("Luka Modrić", PlayerPosition.MIDFIELDER, 10, 38);
        Player courtois = new Player("Thibaut Courtois", PlayerPosition.GOALKEEPER, 1, 31);
        
        vinicius.setTeam(realMadrid);
        benzema.setTeam(realMadrid);
        modric.setTeam(realMadrid);
        courtois.setTeam(realMadrid);
        
        playerService.createPlayer(vinicius);
        playerService.createPlayer(benzema);
        playerService.createPlayer(modric);
        playerService.createPlayer(courtois);
        
        // Create players for Barcelona
        Player lewandowski = new Player("Robert Lewandowski", PlayerPosition.FORWARD, 9, 35);
        Player pedri = new Player("Pedri González", PlayerPosition.MIDFIELDER, 8, 21);
        Player gavi = new Player("Gavi Páez", PlayerPosition.MIDFIELDER, 6, 19);
        Player terStegen = new Player("Marc-André ter Stegen", PlayerPosition.GOALKEEPER, 1, 31);
        
        lewandowski.setTeam(barcelona);
        pedri.setTeam(barcelona);
        gavi.setTeam(barcelona);
        terStegen.setTeam(barcelona);
        
        playerService.createPlayer(lewandowski);
        playerService.createPlayer(pedri);
        playerService.createPlayer(gavi);
        playerService.createPlayer(terStegen);
        
        // Create players for Atlético Madrid
        Player griezmann = new Player("Antoine Griezmann", PlayerPosition.FORWARD, 7, 32);
        Player morata = new Player("Álvaro Morata", PlayerPosition.FORWARD, 19, 31);
        Player koke = new Player("Koke", PlayerPosition.MIDFIELDER, 6, 32);
        Player oblak = new Player("Jan Oblak", PlayerPosition.GOALKEEPER, 13, 30);
        
        griezmann.setTeam(atleticoMadrid);
        morata.setTeam(atleticoMadrid);
        koke.setTeam(atleticoMadrid);
        oblak.setTeam(atleticoMadrid);
        
        playerService.createPlayer(griezmann);
        playerService.createPlayer(morata);
        playerService.createPlayer(koke);
        playerService.createPlayer(oblak);
        
        // Create players for Sevilla
        Player enNesyri = new Player("Youssef En-Nesyri", PlayerPosition.FORWARD, 15, 26);
        Player rakitic = new Player("Ivan Rakitić", PlayerPosition.MIDFIELDER, 10, 35);
        Player navas = new Player("Jesús Navas", PlayerPosition.DEFENDER, 16, 38);
        Player bono = new Player("Yassine Bounou", PlayerPosition.GOALKEEPER, 13, 32);
        
        enNesyri.setTeam(sevilla);
        rakitic.setTeam(sevilla);
        navas.setTeam(sevilla);
        bono.setTeam(sevilla);
        
        playerService.createPlayer(enNesyri);
        playerService.createPlayer(rakitic);
        playerService.createPlayer(navas);
        playerService.createPlayer(bono);
    }

    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}