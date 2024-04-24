import javax.swing.*;
import java.util.Random;

public class Bet extends Server{




    public String flipCoin() {
        Random rand = new Random();
        int outcome = rand.nextInt(2);

        if(outcome == 1) {
            return "HEADS";
        }
        else{
            return "TAILS";
        }
    }

    public  String rollDice() {
        Random rand = new Random();
        int outcome = rand.nextInt(6);
        if (outcome == 0) {
            return "ONE";
        }
        else if (outcome == 1) {
            return "TWO";
        }
        else if (outcome == 2) {
            return "THREE";
        }
        else if (outcome == 3) {
            return "FOUR";
        }
        else if (outcome == 4) {
            return "FIVE";
        }
        else if (outcome == 5) {
            return "SIX";
        }
        else {
            System.out.println("ERROR: couldnt flip coin");
            return null;
        }
    }


}
