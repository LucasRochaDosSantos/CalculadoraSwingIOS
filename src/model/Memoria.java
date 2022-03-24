package model;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	private static Memoria instancia = new Memoria();
	private String texto = "";
	private List<MemoriaObservador> observadores = new ArrayList<>();
	private String textoBuffer = "";
	private boolean substituir = false;
	private TipoComando ultimaOperacao = null;

	private enum TipoComando {
		ZERAR, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA, SINAL;
	};

	private Memoria() {

	}

	public static Memoria getInstancia() {
		return instancia;
	}

	public void adicionarObservador(MemoriaObservador o) {
		observadores.add(o);
	}

	public String getTexto() {
		return texto.isEmpty() ? "0" : texto;
	}

	public void processarComando(String comando) {
		TipoComando tipoComando = detectarTipoComando(comando);
		if (tipoComando == null) {
			return;
		} else if (tipoComando == TipoComando.ZERAR) {
			texto = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;
		} else if (tipoComando == TipoComando.SINAL && texto.contains("-")) {
			texto = texto.substring(1);
		} else if (tipoComando == TipoComando.SINAL && !texto.contains("-")) {
			texto = "-" + texto;
		} else if (tipoComando == TipoComando.NUMERO || tipoComando == TipoComando.VIRGULA) {
			texto = substituir ? comando : texto + comando;
			substituir = false;
		} else {
			substituir = true;
			texto = obterResultadoOperacao();
			textoBuffer = texto;
			ultimaOperacao = tipoComando;
		}

		observadores.forEach(o -> o.valorAlterado(getTexto()));
	}

	private String obterResultadoOperacao() {
		if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return texto;
		}
		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(texto.replace(",", "."));
		double resultado = 0;
		if (ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if (ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		}
		if (ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		} else if (ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		}
		String resultadoString = Double.toString(resultado).replace(".", ",");
		boolean inteiro = resultadoString.endsWith(",0");

		return inteiro ? resultadoString.replace(",0", "") : resultadoString;
	}

	private TipoComando detectarTipoComando(String comando) {
		if (comando.isEmpty() && comando == "0") {
			return null;
		}
		try {
			Integer.parseInt(comando);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			if ("A/C".equals(comando)) {
				return TipoComando.ZERAR;
			} else if ("/".equals(comando)) {
				return TipoComando.DIV;
			} else if ("*".equals(comando)) {
				return TipoComando.MULT;
			} else if ("+".equals(comando)) {
				return TipoComando.SOMA;
			} else if ("-".equals(comando)) {
				return TipoComando.SUB;
			} else if ("=".equals(comando)) {
				return TipoComando.IGUAL;
			} else if (",".equals(comando) && !texto.contains(",")) {
				return TipoComando.VIRGULA;
			} else if ("+/-".equals(comando)) {
				return TipoComando.SINAL;
			}

		}

		return null;
	}
}
