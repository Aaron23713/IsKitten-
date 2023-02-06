import java.io.*;
import java.util.Scanner;

public class IsKitten implements Serializable {
  private Node universe;
  private static String kitten = "kitten";

  private class Node implements Serializable {
    String text;
    Node yes;
    Node no;
    Node parent;

    Node(String text) {
      this.text = text;
    }


  }

  public IsKitten() {
    universe = new Node(kitten);
  }

  public void play() throws IOException {
    Scanner userInput = new Scanner(System.in);
    playHelper(universe, userInput);
  }

  private void playHelper(Node node, Scanner input) throws IOException {
    if (node.yes == null || node.no == null) {
      handleThing(node, input);

    } else {
      handleQuestion(node, input);
    }
  }

  private void handleQuestion(Node node, Scanner input) throws IOException {
    System.out.print(node.text + " ");

    if (input.nextLine().equals("y")) {
      playHelper(node.yes, input);
    } else {
      playHelper(node.no, input);
    }

  }

  private void handleThing(Node root, Scanner userIn) throws IOException {
    System.out.print("Are you thinking of " + root.text + "? ");

    if (userIn.nextLine().equals("y")) {
      System.out.println("I knew it.");
      save();
    } else {
      System.out.print("What were you thinking of? ");
      Node thinkingOf = new Node(userIn.nextLine());

      System.out.print("Enter a yes/no question that is yes for " + thinkingOf.text + " and no for " + root.text + ": ");
      ;
      Node question = new Node(userIn.nextLine());


      if (root.parent == null) {
        universe = question;
        question.yes = thinkingOf;
        thinkingOf.parent = question;
        question.no = root;
        root.parent = question;
        save();
      } else if (root.equals(root.parent.yes)) {
        root.parent.yes = question;
        question.parent = root.parent;
        question.yes = thinkingOf;
        thinkingOf.parent = question;
        question.no = root;
        root.parent = question;

        save();
      } else if (root.equals(root.parent.no)) {
        root.parent.no = question;
        question.parent = root.parent;
        question.yes = thinkingOf;
        thinkingOf.parent = question;
        question.no = root;
        root.parent = question;
        save();
      }


    }
  }


  public void save() throws IOException {
    ObjectOutputStream in = new ObjectOutputStream(new FileOutputStream("is-kitten.bin"));
    in.writeObject(this);
    in.close();
  }

  public static IsKitten load() throws ClassNotFoundException {
    try {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream("is-kitten.bin"));
      IsKitten game = (IsKitten) in.readObject();
      in.close();
      return game;
    } catch (IOException e) {
      return new IsKitten();
    }
  }

  public static void main(String[] args) throws ClassNotFoundException, IOException {
    IsKitten.load().play();
  }

}