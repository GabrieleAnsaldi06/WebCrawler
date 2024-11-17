import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Crawler {
    public void crawl(String seedURL, int maxDepth, Set<String> visitedLinks) {
        Queue<UrlDepthPair> queue = new LinkedList<>();
        queue.add(new UrlDepthPair(seedURL, 0));

        while (!queue.isEmpty()) {
            UrlDepthPair current = queue.poll();
            String url = current.getUrl();
            int depth = current.getDepth();

            if (depth > maxDepth || visitedLinks.contains(url)) {
                continue;
            }

            try {
                // Fetch and parse the HTML document
                Document doc = Jsoup.connect(url).timeout(10000).get();
                visitedLinks.add(url);
                System.out.println("Crawled (Depth " + depth + "): " + url);

                // Only continue if we haven't reached the maximum depth
                if (depth < maxDepth) {
                    Elements linksOnPage = doc.select("a[href]");
                    for (Element link : linksOnPage) {
                        String href = link.absUrl("href");
                        if (isValidUrl(href) && !visitedLinks.contains(href)) {
                            queue.add(new UrlDepthPair(href, depth + 1));
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
                System.err.println("Timeout della connessione per " + url);
            } catch (IOException e) {
                System.err.println("Errore di I/O per " + url + ": " + e.getMessage());
            }
        }
    }

    // Helper method to validate URLs
    private boolean isValidUrl(String url) {
        return url.startsWith("http") && !url.contains("javascript:") && !url.contains("#");
    }

    // Helper class to store URL and its depth
    private static class UrlDepthPair {
        private final String url;
        private final int depth;

        public UrlDepthPair(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }

        public String getUrl() {
            return url;
        }

        public int getDepth() {
            return depth;
        }
    }
}
