#encoding: utf-8
require_relative "casilla"
require_relative "tipo_casilla"
require_relative "titulo_propiedad"
require_relative "otra_casilla"
require_relative "calle"

module ModeloQytetet
  class Tablero
    attr_reader :casillas, :carcel
    
    def initialize
      @casillas = Array.new
      inicializar
    end
    
    def inicializar
      @casillas << OtraCasilla.new(0, TipoCasilla::SALIDA)
      @casillas << Calle.new(1, TituloPropiedad.new("Calle la Rocka",  50, 10, 150, 250,250))
      @casillas << OtraCasilla.new(2, TipoCasilla::CARCEL )
      @casillas << Calle.new(3, TituloPropiedad.new("Calle el Zaguán",  50, 10, 150, 250,250))
      @casillas << OtraCasilla.new(4, TipoCasilla::SORPRESA)
      @casillas << Calle.new(5, TituloPropiedad.new("Calle la Guarida", 50, 10, 150, 250,250))
      @casillas << OtraCasilla.new(6,TipoCasilla::SORPRESA)
      @casillas << Calle.new(7, TituloPropiedad.new("Calle la Stukas",  50, 10, 150, 250,250))
      @casillas << OtraCasilla.new(8, TipoCasilla::SORPRESA)
      @casillas << Calle.new(9,TituloPropiedad.new("Calle la Mala Vida",  70, 10, 350, 350,350))
      @casillas << Casilla.new(10,100, TipoCasilla::IMPUESTO,nil)
      @casillas << Calle.new(11, TituloPropiedad.new("Calle el Refugio",  70, 10, 350, 350,350))
      @casillas << OtraCasilla.new(12, TipoCasilla::JUEZ)
      @casillas << Calle.new(13, TituloPropiedad.new("Calle el Soho",  70, 10, 350, 350,350))
      @casillas << Calle.new(14, TituloPropiedad.new("Calle el Engranaje",  70, 10, 350, 350,350))
      @casillas << Calle.new(15, TituloPropiedad.new("Calle el Brujo",  100, 20, 550, 550,550))
      @casillas << OtraCasilla.new(16, TipoCasilla::PARKING)
      @casillas << Calle.new(17, TituloPropiedad.new("Calle el Playmobil",  100, 20, 550, 550,550))
      @casillas << Calle.new(18, TituloPropiedad.new("Calle el Meneillo",  100, 20, 550, 550,550))
      @casillas << Calle.new(19, TituloPropiedad.new("Calle el Bubión",  100, 20, 550, 550,550))
    
      @carcel = @casillas[2]
    end
    
    def esCasillaCarcel(numeroCasilla)
      if numeroCasilla == @carcel.numeroCasilla
        return true
      else
        return false
      end
    end
    
    def obtenerCasillaFinal(casilla, desplazamiento)
      posicion = casilla.numeroCasilla

      for i in 0..desplazamiento-1
        posicion = posicion+1
        
        if (posicion == @casillas.size)
          posicion = 0
        end
      end
      return @casillas[posicion]
    end
    
    def obtenerCasillaNumero(numeroCasilla)
      return @casillas.at(numeroCasilla)
    end
    
    def to_s
      "\nTABLERO\n Casillas:\n" + @casillas.to_s + "\n"
    end
    
    private :inicializar
  end
end
