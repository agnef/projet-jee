package fr.uga.miashs.album.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

import fr.uga.miashs.album.model.Album;
import fr.uga.miashs.album.model.Picture;

public class PictureAnnotationService extends JpaService<Long,Picture>{

//	requetes sur les images
	
	public List<Picture> queries(String requete) {
		List<String> uriPictures = query(requete); // uri de l'ontologie
		List<Picture> pictures = new ArrayList<Picture>(); // liste des images qu'on renvoie
		//requete jpa		
		javax.persistence.Query queryJPA = getEm().createQuery("SELECT p FROM Picture p");
		List<Picture> pictureList = queryJPA.getResultList(); // toutes les photos dans jpa
		
		//Comparaison des uri dans l'ontologie et des uri dans jpa
		for (int i=0; i<uriPictures.size(); i++){
			for (int j=0; j<pictureList.size(); j++){
				String uriOntologie = uriPictures.get(i).toString();
				String uriJPA = pictureList.get(j).getUri().toString();
				if(uriOntologie.equals(uriJPA)){
					pictures.add(pictureList.get(j));
				}
			}
		}		
		return pictures;
	}
	
	public List<String> query(String requete){
		Query query = null;
		if (requete.equals("query0")){
			query = query0();
		} else if (requete.equals("query1")){
			query = query1();
		} else if (requete.equals("query2")){
			query = query2();
		}
		
		List<String> uriPictures = new ArrayList<String>();
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql", query)) {		
			ResultSet results = qexec.execSelect();
			
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				RDFNode x = soln.get("s");
				System.out.println(x + " ");
				uriPictures.add(x.toString());
			}
		}
		return uriPictures;
	}
	
	public Query query0(){
		// On selectionne toutes les photos qui sont dans l'ontologie
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT ?s  WHERE {"
							+ "?s a pa:Picture .}");
		return query;
	}

	public Query query1(){
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT ?s  WHERE {"
							+ "?s a pa:Picture;"
							+ "pa:pictureDate ?date. "
							+ "FILTER ( ?date > \"2015-06-21T00:00:00\"¨¨xsd:dateTime && "
							+ "?date < \"2015-11-11\"¨¨xsd:dateTime)."
							+ "}UNION"
							+ "{?s a pa:Picture;"
							+ "pa:inYear 2015;"
							+ "pa:during pa:summer.}"
							+ "}");
		return query;
	}
	
	public Query query2(){
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT DISTINCT ?s  WHERE {"
							+ "?s a pa:Picture;"
							+ "pa:hasInside ?p ."
							+ "?p pa:isFriendOf pa:marcLebeau ;"
								+ "a pa:User ."
							+ " OPTIONAL { ?s pa:inYear ?y } "
							+ "}"
							+ "ORDER BY ?y");
		return query;
	}
	
	public Query query3(){
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT DISTINCT ?s  WHERE {"
							+ "?s a pa:Picture;"
							+ "pa:isTakenBy pa:jeanneDupont;"
							+ "pa:hasInside ?p ."
							+ "?p pa:belongsToTheSameFamily pa:jeanneDupont. "
							+ "}"
							);
		return query;
	}
	
	public Query query4(){
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT DISTINCT ?s  WHERE {"
							+ "?s a pa:Picture;"
							+ "pa:hasInside ?p."
							+ "?p a pa:Horse. "
							+ "}"
							);
		return query;
	}
	
// insertion dans ontologie
	
	public List<String> seeAnnotations(String uri){
		List<String> annotations = new ArrayList<String>();
		Query query = QueryFactory
				.create("PREFIX pa: <http://www.semanticweb.org/masterDCISS/projetAlbum#>"
						+ "SELECT ?s ?p ?o WHERE {"
							+ "?s a pa:Picture;"
							+ " .}");
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql", query)) {		
			ResultSet results = qexec.execSelect();
			
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				RDFNode x = soln.get("s"); //on recupere la photo
				Resource r = soln.getResource("p"); // une proppriete
				Literal l = soln.getLiteral("o"); // valeur de la propriete
				System.out.println(x + " ");
				annotations.add(r.toString());
				annotations.add(l.toString());
			}
		}
		// query doit obtenir toutes les propriétés et les valeurs de la propriete de l'uri passée en paramètres
		return annotations;
	}
	
	public void insertPictureOntology(String uri){		
		
		UpdateRequest request = UpdateFactory.create(
		 "INSERT DATA { "
		+ "<" + uri + ">" + "a" + "<http://www.semanticweb.org/masterDCISS/projetAlbum#Picture>"
		+ " .}");
		System.out.println("insertion dans ontologie (pictureAnnootationService)");
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	
	public void insertOntology(String uriStr, String propriete, String valeurPropriete){
		
		String subject = "<" + uriStr + ">";
		String predicate =  propriete;
		String object = valeurPropriete;
		
		
		UpdateRequest request = UpdateFactory.create(
		 "INSERT DATA {PREFIX pa:<http://www.semanticweb.org/masterDCISS/projetAlbum#> "
		+ subject + predicate + object
		+ " .}");

		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
	}
	

//	public static void main(String[] args) {
//
//		// SPARQL Update
//		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
//				+ "INSERT DATA {"
//				+ " <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> rdfs:subClassOf <http://imss.univ-grenoble-alpes.fr/ns/album#Picture> ."
//				+ " <http://imss.univ-grenoble-alpes.fr/ns/album#picture321> a <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> .}");
//
//		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
//		up.execute();
//
//	}

}
