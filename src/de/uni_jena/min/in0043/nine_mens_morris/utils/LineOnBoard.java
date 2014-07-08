package de.uni_jena.min.in0043.nine_mens_morris.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.graph.DefaultEdge;

public class LineOnBoard extends DefaultEdge {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger();
	
	public Object getOtherEnd(Object o) {
		log.entry(o);
		Object retVal = null;
		if(this.getTarget().equals(o)) {
			log.trace("We want the source");
			retVal = this.getSource();
		} else {
        	log.trace("We want the target");
        	retVal = this.getTarget();
		}
		log.exit(retVal);
		return retVal;
	}

}
