#encoding: utf-8
require_relative "sorpresa"
require_relative "tipo_sorpresa"
require_relative "titulo_propiedad"


module ModeloQytetet
  class Casilla
    attr_accessor :titulo
    attr_reader :numeroCasilla, :tipo, :coste
    
    #tipo calle
    def initialize(numeroCasilla, coste,tipo, titulo)
      @numeroCasilla = numeroCasilla
      @coste = coste
      @tipo = tipo
      @titulo = titulo
    end
        
    def soyEdificable
      if (@tipo == TipoCasilla::CALLE)
        return true
      else
        return false
      end
    end
    
    def to_s
      "\n   NumeroCasilla: " + @numeroCasilla.to_s+ "\n   Tipo: " +@tipo.to_s+"\n   Coste: " + @coste.to_s+" \n   Titulo: " +@titulo.to_s 
    end 
    
    def tengoPropietario
      return @titulo.tengoPropietario
    end
    
    protected :soyEdificable, :tengoPropietario
   end
end
