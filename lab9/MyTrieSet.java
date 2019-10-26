import java.util.*;

public class MyTrieSet implements TrieSet61B {
    private Node root;

    private static class Node {
        private boolean isKey;
        private HashMap<Character, Node> map;

        private Node(boolean b) {
            isKey = b;
            map = new HashMap<>();
        }
    }

    public MyTrieSet() {
        root = new Node(false);
    }

    @Override
    public void clear() {
        root = new Node(false);
    }

    @Override
    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        Node curr = root;
        char c;
        int n = key.length();
        for (int i=0; i!=n; i++) {
            c = key.charAt(i);
            if (!curr.map.containsKey(c)) return false;
            curr = curr.map.get(c);
        }
        return curr.isKey;
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> list = new ArrayList<>();

        Node curr = root;
        char c;
        int n = prefix.length();
        for (int i=0; i!=n; i++) {
            c = prefix.charAt(i);
            curr = curr.map.get(c);
        }
        prefixHelper(list, curr, prefix);
        return list;
    }

    private void prefixHelper(List<String> list, Node curr, String str) {
        if (curr.isKey) list.add(str);

        for (Map.Entry<Character, Node> entry : curr.map.entrySet()) {
            Character c = entry.getKey();
            Node nextNode = entry.getValue();
            prefixHelper(list, nextNode, str + c);
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        MyTrieSet t = new MyTrieSet();
        t.add("hello");
        t.add("hi");
        t.add("help");
        t.add("zebra");
        List<String> l = t.keysWithPrefix("he");
        System.out.println(l.toString());
    }
}
