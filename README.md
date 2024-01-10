# Labyrinth Escape Game

Welcome to the Labyrinth Escape Game! This simple and entertaining game challenges you to navigate through a labyrinth, avoid the evil dragon, and reach the exit as quickly as possible.

## How to Play

1. **Objective:** Reach the top right corner of the labyrinth as fast as you can, avoiding the evil dragon.
2. **Movement:** Use the WASD keys to move the player character. Navigate left (A), right (D), up (W), or down (S).
3. **Dragon Movement:** The dragon starts at a random position and moves randomly until it reaches a wall. If the dragon gets to a neighboring field of the player, you lose!
4. **Visibility:** You can see only the neighboring fields at a distance of 3 units.
5. **Game Levels:** The game features multiple levels. Predefined levels are provided, ensuring they are playable. You can create additional levels or load them from files.
6. **Timer:** Each game has a timer that counts the elapsed time since the start of the game level.
7. **Highscore:** Record how many labyrinths you solve. If you lose, your score is saved in the highscore table.

## Menu Options

1. **Highscore Table:** View the top 10 scores achieved by players.
2. **Restart Game:** Restart the game for a fresh challenge.

## Database Setup

This game utilizes MySQL for data storage. Follow the steps below to set up the database:

1. Install MySQL on your machine.

2. Create a new database named `labyrinth_game`.

3. Run the following SQL script to create the necessary table:

    ```sql
    create database scores;
    use scores;
    drop table scores;
    create table scores(
    	name varchar(255) primary key unique not null,
    	score int not null
    );
    ```

4. Update the database connection details in the Java code (`src/persistence/Database.java`) to match your MySQL server, username, and password.

## How to Run

1. Clone the repository to your local machine.
   ```sh
   git clone https://github.com/your-username/labyrinth-escape-game.git

2. Open the project in your preferred Java development environment.

3. Run the program.

4. Enjoy navigating the labyrinth and escaping the dragon!

## Contributions

Contributions are welcome! If you have ideas for improvements or new features, feel free to fork the repository and submit a pull request.

## Credits

This Labyrinth Escape Game was created with love by Ren Koike.

Happy Escaping! 🏰🐉
