package com.rayrobdod.scriptSample

import java.io.{InputStreamReader, OutputStreamWriter}
import com.codecommit.antixml.XML
import com.rayrobdod.script.consoleView.{
		AggregateScriptPrinter, BaseScriptPrinter
}
import com.rayrobdod.script.parser.{
		BaseScriptFromXml, AggregateScriptFromXml
}

object Main extends App {
	
	val script = {
		val scriptFromXml = new AggregateScriptFromXml(BaseScriptFromXml, SampleScriptFromXml)
		
		val url = this.getClass().getResource("/com/rayrobdod/scriptSample/intro.xml")
		val stream = url.openStream
		val scriptXml = XML.fromReader(new InputStreamReader(stream))
		val scriptScript = scriptFromXml(AttrsToUseFun, scriptXml, url, scriptFromXml)
		stream.close()
		scriptScript
	}
	
	val (consoleIn, consoleOut) = {
		val c = System.console()
		(c.reader, c.writer)
	}
	
	
	val sp = new AggregateScriptPrinter(
			new BaseScriptPrinter(State.SetFlag),
			SampleScriptPrinter
	)
	
	sp.apply(consoleOut, consoleIn, sp, State.empty, script)
	
	
}

