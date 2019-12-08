#encoding: utf-8

module ModeloQytetet
  class Calle < Casilla
    def initialize(numeroCasilla, titulo)
      super(numeroCasilla,titulo.precioCompra, TipoCasilla::CALLE, titulo)
    end
    
    def asignarPropietarioJugador(jugador)
      @titulo.propietario = jugador
    end
    
    def pagarAlquiler
      @titulo.pagarAlquiler
    end
    
    def tengoPropietario
      super
    end
  end
end
