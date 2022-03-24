package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Memoria;
import model.MemoriaObservador;

public class Display extends JPanel  implements MemoriaObservador{
	private JLabel label;
	
	public Display() {
	Memoria.getInstancia().adicionarObservador(this);
	setBackground(new Color(46, 49, 50));
	this.label	= new JLabel(Memoria.getInstancia().getTexto());
	label.setForeground(Color.WHITE);
	label.setFont(new Font("courier", Font.PLAIN, 24));
	
	 setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25)); // recebe 3 parametros de aliamento
	 add(label);
	 
	}
	public void valorAlterado(String novoValor) {
		this.label.setText(novoValor);
	}
}
