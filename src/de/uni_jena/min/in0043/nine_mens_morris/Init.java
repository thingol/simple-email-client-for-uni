/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;

/**
 * @author mariushk
 *
 */
public class Init {
	
	private static Logger log = LogManager.getLogger(Init.class.getName());
	
	private static void gui() {
<<<<<<< HEAD
<<<<<<< HEAD
		final Frame myFrame = new Frame();
		myFrame.add(new Head());
=======
=======
>>>>>>> b67653bd2ed6a3e013fed8301fcb0ddcf5708778
		log.entry("gui()");
		/*final Frame myFrame = new Frame();
		myFrame.add(new Spielfeld());
>>>>>>> b67653bd2ed6a3e013fed8301fcb0ddcf5708778
		myFrame.setSize(800,800);
		myFrame.setVisible(true);
			
		myFrame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  log.entry("windowClosing(WindowEvent we)");
				  myFrame.dispose();
				  log.exit("windowClosing(WindowEvent we)");
			  }
		});
		*/
		log.exit("gui()");
	}
	
	public static void main(String[] args) {
		log.entry("main(String[] args)");

        ArgumentParser parser = ArgumentParsers.newArgumentParser("nine_mens_morris")
                .description("Play Nine Men's Morris")
                .version("${nine_mens_morris} v0.0.1 alpa by CJ0fail and thingol\n(c) 2014")
                .defaultHelp(true);
        parser.addArgument("-nd","--no-debug")
                .action(Arguments.storeTrue())
                .setDefault(false)
                .help("Log all manner of (ir)relevant things");
        parser.addArgument("-v","--version")
                .action(Arguments.version())
                .help("Show version information");

        MutuallyExclusiveGroup fGrp = parser.addMutuallyExclusiveGroup("function");

        fGrp.addArgument("-s","--server")
                .help("Start a server (not implemented)")
                .action(Arguments.storeTrue());
        fGrp.addArgument("-n","--network-game")
                .help("Play online. Requires a server. (not implemented)")
                .action(Arguments.storeTrue());
        fGrp.addArgument("-l","--local-game")
                .help("Play locally.")
                .action(Arguments.storeTrue());

        try {
            parser.parseArgs(args);
            parser.printHelp();

            if(args.length == -1) gui();

            log.exit("main(String[] args)");
        } catch (ArgumentParserException e) {
        	log.error(e);
            parser.handleError(e);
            log.exit(false);
        }
	}
}
