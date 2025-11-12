/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.datastructuressecondproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author emir
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private Node<T> root;
    private BinarySearchTree<T> suggestions;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(T data) {
        root = insert(root, data);
    }

    private Node<T> insert(Node<T> root, T word) {
        if (root == null) {
            return new Node<T>(word);
        }

        if (word.compareTo(root.word) < 0) {
            root.left = insert(root.left, word);
        } else {
            root.right = insert(root.right, word);
        }

        return root;
    }

    /**
     * Reads words from a file and inserts them into the binary search tree.
     *
     * @param fileName The name of the file to be read.
     */
    public void readFromFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("words.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                T newWord = (T) line.trim();
                insert(newWord);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean search(T word) {
        return search(root, word);
    }

    private boolean search(Node<T> root, T word) {
        if (root == null) {
            return false;
        }

        if (word.compareTo(root.word) == 0) {
            return true;
        } else if (word.compareTo(root.word) < 0) {
            return search(root.left, word);
        } else {
            return search(root.right, word);
        }
    }

    public void update(T oldData, T newData) {
        if (root == null) {
            return;
        }
        update(root, oldData, newData);
    }

    private void update(Node<T> node, T oldWord, T newWord) {
        if (node == null) {
            return;
        }
        if (oldWord.compareTo(node.word) == 0) {
            node.word = newWord;
        } else if (oldWord.compareTo(node.word) < 0) {
            update(node.left, oldWord, newWord);
        } else {
            update(node.right, oldWord, newWord);
        }
    }

    public void delete(T data) {
        root = delete(root, data);
    }

    private Node<T> delete(Node<T> root, T data) {
        if (root == null) {
            return null;
        }

        if (data.compareTo(root.word) < 0) {
            root.left = delete(root.left, data);
        } else if (data.compareTo(root.word) > 0) {
            root.right = delete(root.right, data);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.word = minValue(root.right);
            root.right = delete(root.right, root.word);
        }

        return root;
    }

    private T minValue(Node<T> root) {
        T minv = root.word;
        while (root.left != null) {
            minv = root.left.word;
            root = root.left;
        }
        return minv;
    }

    /*Bu kısımda yanlışlıklar olabilir*/
    public BinarySearchTree<T> suggestion(String word) {
        int minDist = Integer.MAX_VALUE;
        String closestWord = "";
        suggestions = new BinarySearchTree<>();

        for (Node<T> node : getAllNodes()) {
            int dist = getLevenshteinDistance(word, node.word);
            if (dist < minDist) {
                minDist = dist;
                closestWord = (String) node.word;
                suggestions.insert((T) closestWord);
            } else if (dist == minDist) {
                closestWord = (String) node.word;
                suggestions.insert((T) closestWord);
            }
        }
        return suggestions;
    }

    public ArrayList<String> suggestCorrection(String word) {
        int minDist = Integer.MAX_VALUE;
        String closestWord = "";
        ArrayList<String> suggestions = new ArrayList<>();

        for (Node<T> node : getAllNodes()) {
            int dist = getLevenshteinDistance(word, node.word);
            if (dist < minDist) {
                minDist = dist;
                closestWord = (String) node.word;
                suggestions.add(closestWord);
            }
        }
        return suggestions;
    }


    public String auto(T prefix) {
        String prefixWord = (String) prefix;
        return auto(root, prefix);
    }

    public String auto(Node<T> node, T prefix) {
        if (node == null) {
            return null;
        }

        String prefixWord = (String) prefix;
        String word = (String) node.word;

        if (word.startsWith(prefixWord)) {
            return word;
        }

        int dist = getLevenshteinDistance(prefixWord, node.word);
        if (dist <= 1) {
            return word;
        }

        String leftResult = auto(node.left, prefix);
        String rightResult = auto(node.right, prefix);

        if (leftResult == null) {
            return rightResult;
        } else if (rightResult == null) {
            return leftResult;
        } else {
            int leftDist = getLevenshteinDistance(prefixWord, (T) leftResult);
            int rightDist = getLevenshteinDistance(prefixWord, (T) rightResult);

            if (leftDist <= rightDist) {
                return leftResult;
            } else {
                return rightResult;
            }
        }
    }

    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        getAllNodes(root, nodes);
        return nodes;
    }

    private void getAllNodes(Node node, ArrayList<Node> nodes) {
        if (node == null) {
            return;
        }
        getAllNodes(node.left, nodes);
        nodes.add(node);
        getAllNodes(node.right, nodes);
    }

    private int getLevenshteinDistance(String s1, T word2) {
        String s2 = (String) word2;
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    public int getNodeCount() {
        return getNodeCount(root);
    }

    private int getNodeCount(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            int count = 1;
            count += getNodeCount(node.left);
            count += getNodeCount(node.right);
            return count;
        }
    }
}
