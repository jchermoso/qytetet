#encoding: utf-8
require_relative "jugador.rb"

module ModeloQytetet
  class Especulador < Jugador
    attr_accessor :fianza
    
    def initialize(jugador, fianza)
       super(jugador.nombre,jugador.saldo,jugador.encarcelado, jugador.propiedades, jugador.cartaLibertad, jugador.casillaActual)
       @fianza = fianza
    end
    
    def pagarImpuesto
      modificarSaldo(-((@casillaActual.coste)/2))
    end
    
    def convertirme(fianza)
      return self
    end
    
    def deboIrACarcel
      deboIr = false
      
      if (super == true) && (pagarFianza == false)
        deboIr = true
      end
      
      return deboIr
    end
    
    def pagarFianza
      pagada = false
      
      if tengoSaldo(@fianza)
        modificarSaldo(-@fianza)
        puts "\nTe has librado de ir a la carcel\n"
        pagada = true
      end
      
      return pagada
    end
    
    def puedoEdificarCasa(titulo) 
      puedo_edificar = false
      
      if titulo.numCasas < 8
        puedo_edificar = true
      end
      
      return puedo_edificar
    end
    
    def puedoEdificarHotel(titulo)
      puedo_edificar = false
      
      if titulo.numHoteles < 8 && titulo.numCasas >= 4
        puedo_edificar = true
      end
      
      return puedo_edificar
    end
    
    def to_s
      "Especulador{"+super.to_s + "fianza=" + @fianza.to_s + "}"
    end
    
    protected :pagarImpuesto, :initialize,  :deboIrACarcel,
               :puedoEdificarCasa, :puedoEdificarHotel
    private :pagarFianza
  end
end
