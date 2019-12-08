#encoding: utf-8

require_relative "qytetet"
require_relative "sorpresa"
require_relative "tablero"
require_relative "dado"
require_relative "jugador"

module ModeloQytetet
  class PruebaQytetet
    @@juego = Qytetet.instance
    @@nombres = Array.new
    
    def self.main
      getNombreJugadores
    end
        
    def self.mayorQueCero
      mayor_que_cero = Array.new
      @@juego.mazo.each{|x| if x.valor > 0 then mayor_que_cero << x end}
      
      return mayor_que_cero
    end
    
    def self.tipoIra
      tipo_ira = Array.new
      @@juego.mazo.each{|x| if x.tipo == TipoSorpresa::IRACASILLA then tipo_ira << x end}
      
      return tipo_ira
    end
    
    def self.tipoEspecifico(sorpresa)
      tipo_especifico = Array.new
      @@juego.mazo.each{|x| if x.tipo == sorpresa then tipo_especifico << x end}
      
      return tipo_especifico
    end
    
    def self.getNombreJugadores
      puts "Introduce el número de jugadores (mínimo 2 y máximo 4):"
      numero = gets.chomp.to_i
      
      if numero < 2 || numero > 4
        puts "Introduce el número de jugadores (mínimo 2 y máximo 4)"
      else
        introduceNombres(numero)
        muestraPantalla
      end       
    end
    
    def self.introduceNombres(n)
      while n > @@nombres.length
        puts "Introduce un nombre de un jugador:"
        s = gets.chomp.to_s
        @@nombres << s
      end    
    end
    
    def self.muestraPantalla
    end
  end
  PruebaQytetet.main
end
