/**
 * 
 */
package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

	private static void gui() {
		final Frame myFrame = new Frame();
		myFrame.add(new Spielfeld());
		myFrame.setSize(800,800);
		myFrame.setVisible(true);
			
		myFrame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
				  myFrame.dispose();
			  }
		});
	}

	public static void main(String[] args) {

        ArgumentParser parser = ArgumentParsers.newArgumentParser("nine_mens_morris")
                .description("Play Nine Men's Morris")
                .version("${nine_mens_morris} v0.0.1 alpa by CJ0fail and thingol\n(c) 2014")
                .defaultHelp(true);
        parser.addArgument("--debug")
                .setDefault(true)
                .help("Log all manner of (ir)relevant things");
        parser.addArgument("-v","--version")
                .action(Arguments.version())
                .help("Show version information");

        MutuallyExclusiveGroup fGrp = parser.addMutuallyExclusiveGroup("function");

        fGrp.addArgument("-s","--server")
                .help("Start a server (not implemented)")
                .action(Arguments.storeTrue());
        fGrp.addArgument("-og","--online-game")
                .help("Play online. Requires a server. (not implemented)")
                .action(Arguments.storeTrue());
        fGrp.addArgument("-lg","--local-game")
                .help("Play locally.").setDefault(true)
                .action(Arguments.storeTrue());

        try {
            parser.parseArgs(args);
            parser.printHelp();
            System.out.println("\n\nStarting local game.");

            gui();

        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
	}
}
