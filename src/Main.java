import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.net.SocketTimeoutException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Inserisci l'URL iniziale: ");
        String seedURL = sc.nextLine();
        System.out.print("Inserisci la profondità del crawling: ");
        int maxDepth = sc.nextInt();

        Set<String> visitedLinks = new HashSet<>();
        new Crawler().crawl(seedURL, maxDepth, visitedLinks);

        System.out.println("Link trovati:");
        for (String link : visitedLinks) {
            System.out.println(link);
        }
    }
}
