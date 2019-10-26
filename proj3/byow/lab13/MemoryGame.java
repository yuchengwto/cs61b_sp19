package byow.lab13;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private Font slenderFont;
    private Font boorishFont;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        rand = new Random(seed);
        slenderFont = new Font("Arial", Font.ITALIC, 20);
        boorishFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        int count = 0;
        int length = CHARACTERS.length;
        StringBuilder sb = new StringBuilder();
        while (count != n) {
            int index = rand.nextInt(length);
            char c = CHARACTERS[index];
            sb.append(c);
            count++;
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);

        if (!gameOver) {
            StdDraw.setFont(slenderFont);
            StdDraw.textLeft(1, height-1, "Round: " + round);
            StdDraw.text(width/2, height-1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width, height-1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        StdDraw.setFont(boorishFont);
        StdDraw.text(width/2, height/2, s);
        StdDraw.show();
    }


    public void flashSequence(String letters) {
        char[] chars = letters.toCharArray();
        for (char c: chars) {
            drawFrame(c + "");
            StdDraw.pause(1000);
            drawFrame(" ");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        int count = 0;
        StringBuilder input = new StringBuilder();
        drawFrame(input.toString());
        while (count!=n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                input.append(c);
                drawFrame(input.toString());
                count++;
            }
        }
        while (StdDraw.hasNextKeyTyped()) {
            StdDraw.nextKeyTyped();
        }
        StdDraw.pause(1000);
        return input.toString();
    }

    public void startGame() {
        round = 1;
        gameOver = false;
        playerTurn = false;

        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round + "! Good Luck!");
            StdDraw.pause(1500);

            String s = generateRandomString(round);
            flashSequence(s);

            playerTurn = true;
            String input = solicitNCharsInput(round);

            if (s.equals(input)) {
                drawFrame("Correct, well done!");
                StdDraw.pause(1000);
                round++;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
                break;
            }
        }
    }


}
