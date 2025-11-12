/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.datastructuressecondproject;

public class Node <T>{
    T word;
    Node <T> left;
    Node <T> right;
    
    public Node(T word) {
        this.word = word;
        left = null;
        right = null;
    }
    
    
}
