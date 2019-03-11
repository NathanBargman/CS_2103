import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class IMDBGraphImpl implements IMDBGraph{
	
	private Scanner actorFile;
	private Scanner actressFile;
	
	private Collection<ActorNode> actors = new HashSet<ActorNode>();
	private Collection<MovieNode> movies = new HashSet<MovieNode>();
	
	HashMap<String, MovieNode> movieMap = new HashMap<String, MovieNode>();
	HashMap<String, ActorNode> actorMap = new HashMap<String, ActorNode>();
	
	//consructor
	public IMDBGraphImpl(String actorFileName, String actressFileName) throws IOException {
		System.out.print("Request Received...");
		actorFile = new Scanner (new File(actorFileName), "ISO-8859-1");
		actressFile = new Scanner (new File(actressFileName), "ISO-8859-1");
		System.out.print("Parsing Actor File -> ");
		makeCollection(actorFile);
		System.out.print("Parsing Actress File -> ");
		makeCollection(actressFile);
		System.out.println("Done Parsing");

	}
	/*
	 * @param file to scanned in
	 * a helper method to parse the data into Collections
	 */
	private void makeCollection(Scanner scan) {
		while(!scan.nextLine().contains("LIST")) {
			scan.nextLine();
		}
		scan.nextLine();
		scan.nextLine();
		scan.nextLine();
		scan.nextLine();
		String name, movie, temp;
		
		System.out.print("Scanning...");

		while (scan.hasNextLine()) {
			temp = scan.nextLine();
			if(!temp.contains("\t")) {break;}
			
			name = temp.substring(0, temp.indexOf("\t"));
			ActorNode crntActor = new ActorNode(name, new HashSet<MovieNode>());
			actors.add(crntActor); actorMap.put(name, crntActor);
			
			movie = parseMovie(temp);
			if(!movie.equals("No Movie Found in String")) {
				MovieNode crntMovie = null;
				if(!movieMap.containsKey(movie)) {
					crntMovie = new MovieNode(movie, new HashSet<ActorNode>());
					movieMap.put(movie, crntMovie);
					movies.add(crntMovie);
				}else {
					crntMovie = movieMap.get(movie);
				}
				crntActor.addToNeighbors(crntMovie); 
				crntMovie.addToNeighbors(crntActor);
			}
			if(scan.hasNextLine()) {temp = scan.nextLine();}else {temp = "";}
			if(!temp.isEmpty()) {
				while(temp.contains("\t\t\t")){
					if(temp.contains(")")){
						
						movie = parseMovie(temp);
						if(!movie.equals("No Movie Found in String")) {
							MovieNode crntMovie;
							if(!movieMap.containsKey(movie)) {
								crntMovie = new MovieNode(movie, new HashSet<ActorNode>());
								movieMap.put(movie, crntMovie);
								movies.add(crntMovie);
							}else {
								crntMovie = movieMap.get(movie);
							}
							 crntActor.addToNeighbors(crntMovie); crntMovie.addToNeighbors(crntActor);
						}		
						
						temp = scan.nextLine();
					}
				}
			}
			if(crntActor.getNeighbors().isEmpty()) {
				actors.remove(crntActor);
				actorMap.remove(name);
			}	
		}
	}
	/*
	 * @param String to be manipulated
	 * @return the correct portion of the String
	 * helper method for makeCollection() to make sure the movie node is properly add in the format "Title (Year)"
	 */
	private String parseMovie(String input) {
		String output = "";
		output = input.substring(input.lastIndexOf("\t"));
		output = output.replace("\t", "");		
		if(output.contains("(TV)")){return "No Movie Found in String";}		
		output = output.substring(0, output.indexOf(")") + 1);		
		if(output.contains("\"")) {return "No Movie Found in String";}		
		return output;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see IMDBGraph#getActors()
	 */
	public Collection<? extends Node> getActors() {
		return actors;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see IMDBGraph#getMovies()
	 */
	public Collection<? extends Node> getMovies() {
		return movies;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see IMDBGraph#getMovie(java.lang.String)
	 */
	public Node getMovie(String name) {
		return movieMap.get(name);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see IMDBGraph#getActor(java.lang.String)
	 */
	public Node getActor(String name) {
		return actorMap.get(name);
	}
}
