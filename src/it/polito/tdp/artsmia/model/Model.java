package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;

	public void popolaGrafo() {
		
		if(grafo== null) {
			grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
			ArtsmiaDAO dao=new ArtsmiaDAO();
			dao.creGrafo(grafo);
		}
		
	}

	public boolean isDigit(String objId) {
		
		return objId.matches("\\d+");
	}

	public String getElencoComponenteConnessa(String objId) {
		
		popolaGrafo();
				
		int id=Integer.parseInt(objId);
		ArtObject otemp=null;
		String risultato="";
		for(ArtObject c: grafo.vertexSet()) {
			if (c.getId()== id) {
				otemp=c;
			}
		}
		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector= new ConnectivityInspector<>(grafo);
		risultato+="Componenti connessi : " +inspector.connectedSets().size();
		
		
//		String risultato="";
//		for(ArtObject a: elenco) {
//			risultato+= a.getId()+ " "+ a.getTitle()+"\n";
//		}
		
		return risultato;
	}

}
