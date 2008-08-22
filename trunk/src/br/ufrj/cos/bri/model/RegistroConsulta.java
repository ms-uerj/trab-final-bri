package br.ufrj.cos.bri.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;


public class RegistroConsulta extends Registro {
	int numeroConsulta;
	String textoConsulta;
	int resultados;
	Vector<String> scores = new Vector<String>();
	Vector<Integer> registros = new Vector<Integer>();
	Vector<ScoreRanking> scoreRanking = new Vector<ScoreRanking>();
	
	public int pegaNumeroConsulta() {
		return numeroConsulta;
	}
	
	public String pegaTextConsulta() {
		return textoConsulta;
	}
	
	public int pegaResultados() {
		return resultados;
	}
	
	public Vector<String> pegaScores() {
		return scores;
	}
	
	public Vector<Integer> pegaRegistros() {
		return registros;
	}
	
	public void setNumeroConsulta(int s) {
		numeroConsulta = s;
	}
	
	public void setTextoConsulta(String s) {
		textoConsulta = s.replaceAll("\\n", "");
		textoConsulta = textoConsulta.replaceAll("'", "");
	}
	
	public void setResultados(int s) {
		resultados = s;
	}
	
	public void addScore(String score) {
		scores.add(score);
	}
	
	public void addRegistro(Integer registro) {
		registros.add(registro);
	}
	
	public void ordenaScore() {
		
		for(int i=0;i < registros.size();i++) {
			scoreRanking.add(new ScoreRanking(scores.elementAt(i), registros.elementAt(i)));
		}
		
		Collections.sort(scoreRanking);
		
	}
	
	public Vector<ScoreRanking> getScoreRanking() {
		return scoreRanking;
	}
	
	public class ScoreRanking implements Comparable<ScoreRanking> {
		public String score;
		public Integer doc;
		
		ScoreRanking(String score, Integer doc) {
			this.score = score;
			this.doc = doc;
		}
		
		public int compareTo(ScoreRanking b) {
			int a1 = Integer.parseInt(new String()+score.charAt(0));
			int a2 = Integer.parseInt(new String()+score.charAt(1));
			int a3 = Integer.parseInt(new String()+score.charAt(2));
			int a4 = Integer.parseInt(new String()+score.charAt(3));
			
			int b1 = Integer.parseInt(new String()+b.score.charAt(0));
			int b2 = Integer.parseInt(new String()+b.score.charAt(1));
			int b3 = Integer.parseInt(new String()+b.score.charAt(2));
			int b4 = Integer.parseInt(new String()+b.score.charAt(3));
			
			int sumA = a1 + a2 + a3 + a4;
			int sumB = b1 + b2 + b3 + b4;
			
			return sumB - sumA;
			
		}
	}
}
