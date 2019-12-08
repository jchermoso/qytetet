/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeloqytetet;

import java.util.Random;

/**
 *
 * @author Juan Carlos Hermoso Quesada
 */
public class Dado {
    private int valor;
    private static final Dado instance = new Dado();
    
    private Dado() {
        
    }
    
    
    public static Dado getInstance() {
        return instance;
    }
    
    int tirar() {
        int valor_dado = (int)(Math.random() * 6) + 1;
        
        this.valor = valor_dado;
        
        return valor_dado;
    }
    
    public int getValor() {
        return valor;
    }
}
