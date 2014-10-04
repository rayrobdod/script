package com.rayrobdod.scriptSample

import java.io.{InputStreamReader, OutputStreamWriter}
import com.codecommit.antixml.XML
import com.rayrobdod.script.consoleView.BaseScriptPrinter
import com.rayrobdod.script.parser.BaseScriptFromXml

object Main extends App {
	
	val script = {
		val stream = this.getClass().getResource("/com/rayrobdod/scriptSample/intro.xml").openStream
		val scriptXml = XML.fromReader(new InputStreamReader(stream))
		val scriptScript = BaseScriptFromXml(AttrsToUseFun, scriptXml)
		stream.close()
		scriptScript
	}
	
	val (consoleIn, consoleOut) = {
		val c = System.console()
		(c.reader, c.writer)
	}
	
	
	
	new BaseScriptPrinter(consoleOut, consoleIn, State.SetFlag).apply(State.empty, script)
	
	
}

