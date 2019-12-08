#encoding: utf-8

require_relative "qytetet"
require_relative "controlador_qytetet"
require_relative "opcion_menu"

module VistaTextualQytetet
  class VistaTextualQytetet
    @@modelo = ModeloQytetet::Qytetet.instance
    @@controlador = ControladorQytetet::ControladorQytetet.instance
    
    def self.main
      ui = VistaTextualQytetet.new
      @@controlador.nombreJugadores = ui.obtenerNombreJugadores
      operacionElegida = 0
      casillaElegida = 0
      
      puts "\n^"
      puts "\n|"
      puts "\n| Expande el panel para ver mejor las operaciones"
      puts "\n|"
      puts "\nv"
      
      
      loop do
        operacionElegida = ui.elegirOperacion
        necesitaElegirCasilla = @@controlador.necesitaElegirCasilla(operacionElegida)
        
        if (necesitaElegirCasilla)
          casillaElegida = ui.elegirCasilla(operacionElegida)
        end
        if (@@controlador.obtenerOperacionesJuegoValidas.include?(operacionElegida))
          if (!necesitaElegirCasilla || casillaElegida >= 0)
            puts @@controlador.realizarOperacion(operacionElegida,casillaElegida)
          end
        else
          puts "\nOperación no válida\n"
        end
        
        break if 1!=1
          
      end
    end
     
    def obtenerNombreJugadores
      nombres = Array.new
      
      puts "Introduce el número de jugadores (mínimo 2 y máximo 4):"
      numero = gets.chomp.to_i
      
      if numero < 2 || numero > 4
        puts "Introduce el número de jugadores (mínimo 2 y máximo 4)"
      else
        while numero > nombres.length
          puts "Introduce un nombre de un jugador:"
          s = gets.chomp.to_s
          nombres << s
        end  
      end
      
      return nombres
    end
    
    def elegirCasilla(opcionMenu)
      valor = 0
      casillas_validas = @@controlador.obtenerCasillasValidas(opcionMenu)
    
      if casillas_validas.empty?
        valor = -1
      else
        puts "\nElige una casilla:\n"
        
        for i in 0..casillas_validas.size-1
          puts "Casilla " + casillas_validas.at(i).to_s
        end
        
        valor = gets.chomp.to_i
      end
      
      return valor
    end
    
    def elegirOperacion
      operaciones = @@controlador.obtenerOperacionesJuegoValidas
      
      for i in 0..operaciones.size-1
        mensaje = ControladorQytetet::OpcionMenu.at(operaciones.at(i))
       
        puts "Opción " + operaciones.at(i).to_s + ": " + mensaje.to_s
      end
      
      numero = gets.chomp.to_i
      
      return numero
    end
    
    VistaTextualQytetet.main
  end
end

