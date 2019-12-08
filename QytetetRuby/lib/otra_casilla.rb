#encoding: utf-8
require_relative "casilla"

module ModeloQytetet
  class OtraCasilla < Casilla
    def initialize(numeroCasilla, tipo)
      super(numeroCasilla,0,tipo,nil)
    end
  end
end
