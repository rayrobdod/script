package com.rayrobdod.scriptSample


final case class State(
	val name:String,
	val flags:Map[String, Int]
)

object State {
	val empty = new State("", Map.empty)
	
	
	val SetFlag = {(s:State, name:String, value:Int) => {
		State(
			s.name,
			s.flags + ((name, value))
		)
	}}
}
