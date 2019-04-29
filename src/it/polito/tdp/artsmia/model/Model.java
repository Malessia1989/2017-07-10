package it.polito.tdp.artsmia.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private List<ArtObject> artObject;
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	
	
	//popola la lista artObject (leggendo da db) e crea il grafo
	public void creaGrafo() {
		
		//leggi da db
		ArtsmiaDAO dao= new ArtsmiaDAO();
		this.artObject = dao.listObjects();
		
		// crea grafo pesato/sempli/nonOrientato
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
				
		//aggiungi vertici (tutti gli oggetti)
	//	for(ArtObject ao:this.artObject) {
	//		this.grafo.addVertex(ao);
	//	}
		//ha dentro ciclo for precedente
		Graphs.addAllVertices(this.grafo, this.artObject);
		
		//aggiungi archi (con il loro peso)
		
		for(ArtObject ao: this.artObject) {
			List<ArtObjectAndCount> connessi =dao.listArtObjrctAndCount(ao);
			for(ArtObjectAndCount c:connessi) {
				ArtObject dest= new ArtObject(c.getArtObjectId(), null, null, null, 0, null, null, null, null, null, 0, null, null, null, null, null);
				
				Graphs.addEdge(this.grafo, ao, dest, c.getCount());
				System.out.format("(%d,%d) peso %d\n", ao.getId(), dest.getId(), c.getCount());

			}
			
		}
		
		
		
		
		
		//versione poco efficiente
	/*	for(ArtObject aop: this.artObject) {
			for(ArtObject aoa:this.artObject) {
				if(!aop.equals(aoa) && aop.getId() < aoa.getId()) {	//escludo coppie aop,aoa per escludere loop
					int peso= exhibitionComuni(aop,aoa);
					
					
					
					if(peso!=0) {
					//	DefaultWeightedEdge e = this.grafo.addEdge(aop, aoa);
					//	grafo.setEdgeWeight(e, peso);		
						
						System.out.format("(%d,%d) peso %d\n", aop.getId(), aoa.getId(), peso);
						Graphs.addEdgeWithVertices(this.grafo, aop, aoa, peso);
					}
				}
			}
		}
		*/
	}


	private int exhibitionComuni(ArtObject aop, ArtObject aoa) {
		ArtsmiaDAO dao= new ArtsmiaDAO();
		
		int comuni= dao.contaExhibitionComuni( aop,  aoa);
		
		return comuni;
	}


	public Object getGraphNumVertices() {
		return this.grafo.vertexSet().size();
	}


	public Object getGraphEdges() {
		return this.grafo.edgeSet().size();
	}


	public boolean isObjValid(int idObj) {
		//devo cercare tra artObject se almeno 1 degli elementi appartiene
		if(this.artObject == null)	//se non ho premuto gia Analizza oggetti
			return false;
		
		for(ArtObject ao: this.artObject) {
			if(ao.getId()==idObj)
				return true;
			
		}
		return false;
	}


	public int calcolaDimensioneCC(int idObj) {
		//trova il vertice partenza
		ArtObject start= null;
		for(ArtObject ao: this.artObject) {
			if(ao.getId()==idObj) {
				start= ao;
				break;
			}
			
		}
		if(start == null)
			throw new IllegalArgumentException("Vertice "+idObj+" non esistente") ;		
		
		//fai una visita del grafo
		Set<ArtObject> visitati= new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfv = new DepthFirstIterator<>(this.grafo,start);
		while(dfv.hasNext())
			visitati.add(dfv.next());		
		
		//porta gli elementi
		
		return visitati.size();
	}



	}