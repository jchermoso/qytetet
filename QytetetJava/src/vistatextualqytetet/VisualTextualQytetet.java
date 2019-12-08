/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistatextualqytetet;

import controladorqytetet.*;
import java.util.ArrayList;
import controladorqytetet.OpcionMenu;
import java.util.Scanner;
import modeloqytetet.Qytetet;

/**
 *
 * @author jc
 */
public class VisualTextualQytetet {
    private ControladorQytetet controlador = ControladorQytetet.getInstance();
    private final Scanner in = new Scanner(System.in);
    private Qytetet modelo = Qytetet.getInstance();
        
     public ArrayList<String> obtenerNombreJugadores() {
        ArrayList<String> nombres = new ArrayList();
         
        System.out.println("Introduce el número de jugadores (mínimo 2 y máximo 4):");
        
        int n = in.nextInt();
        String saltoDeLinea = in.nextLine();
        
        if (n < 2 || n > 4) {
            System.out.println("Introduce el número de jugadores (mínimo 2 y máximo 4)");
        }
        else {
            while (nombres.size() < n) {
            System.out.println("Introduce un nombre de un jugador:");
            String s = in.nextLine();
            nombres.add(s);
            }
       
        }
        
        return nombres;
    }
    
    public int elegirCasilla(int opcionMenu) {
        int valor = 0;
        ArrayList<Integer> casillas_validas = controlador.obtenerCasillasValidas(opcionMenu);
        
        if (casillas_validas.isEmpty()) {
            valor = -1;
        }
        else {
            System.out.println("\nElige una casilla:\n");
            for (int i = 0; i < casillas_validas.size(); i++) {
                System.out.println("Casilla " + casillas_validas.get(i));
            }
            
            valor = in.nextInt();
            String saltoDeLinea = in.nextLine();
        }
        
        return valor;
    }
    
    /*public String leerValorCorrecto(ArrayList<String> valoresCorrectos) {
        throw new UnsupportedOperationException("Sin implementar");
    }*/
    
    public int elegirOperacion() {
        ArrayList<Integer> operaciones = controlador.obtenerOperacionesJuegoValidas();
                
        for (int i = 0; i < operaciones.size(); i++) {
            String mensaje = "Opción " + operaciones.get(i) + ": "+OpcionMenu.values()[operaciones.get(i)];
            System.out.println(mensaje);
        }
        
        int n = in.nextInt();
        String saltoDeLinea = in.nextLine();
        
        return n;        
    }
    
    public static void main(String args[]) {
        VisualTextualQytetet ui = new VisualTextualQytetet();   
        ui.controlador.setNombreJugadores(ui.obtenerNombreJugadores());
        int operacionElegida, casillaElegida = 0;
        boolean necesitaElegirCasilla;
        
        System.out.println("\n^"); 
        System.out.println("\n|");
        System.out.println("\n| Expande el panel para ver mejor las operaciones");
        System.out.println("\n|");
        System.out.println("\nv");  
        
        do {
            operacionElegida = ui.elegirOperacion();
            necesitaElegirCasilla = ui.controlador.necesitaElegirCasilla(operacionElegida);
            if (necesitaElegirCasilla)
                casillaElegida = ui.elegirCasilla(operacionElegida);
            if (ui.controlador.obtenerOperacionesJuegoValidas().contains(operacionElegida)) {
                if (!necesitaElegirCasilla || casillaElegida >= 0)
                    System.out.println(ui.controlador.realizarOperacion(operacionElegida,casillaElegida));
                }
                else {
                    System.out.println("\nOperación no válida\n");
                }
        } while (1 == 1);
        
        
    }
} 
