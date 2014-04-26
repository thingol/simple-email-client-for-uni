/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.Frame;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.gui.Head;

/**
 * @author CJ0fail, thinol
 *
 */
public class Init {
	
	private static Logger log = LogManager.getLogger();
	
	private static void gui() {
		log.entry();

		final Frame myFrame = new Frame();
		myFrame.add(new Head());

		log.exit();
	}
	
	public static void main(String[] args) {
		log.entry();

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

            log.exit();
        } catch (ArgumentParserException e) {
        	log.error(e);
            parser.handleError(e);
            log.exit(false);
        }
	}
}
