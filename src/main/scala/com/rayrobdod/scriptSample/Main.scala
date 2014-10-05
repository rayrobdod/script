package com.rayrobdod.scriptSample

import java.io.{InputStreamReader, OutputStreamWriter}
import com.codecommit.antixml.XML
import com.rayrobdod.script.consoleView.BaseScriptPrinter
import com.rayrobdod.script.parser.BaseScriptFromXml

object Main extends App {
	
	val script = {
		val url = this.getClass().getResource("/com/rayrobdod/scriptSample/intro.xml")
		val stream = url.openStream
		val scriptXml = XML.fromReader(new InputStreamReader(stream))
		val scriptScript = BaseScriptFromXml(AttrsToUseFun, scriptXml, url)
		stream.close()
		scriptScript
	}
	
	val (consoleIn, consoleOut) = {
		val c = System.console()
		(c.reader, c.writer)
	}
	
	
	
	new BaseScriptPrinter(consoleOut, consoleIn, State.SetFlag).apply(State.empty, script)
	
	
}

